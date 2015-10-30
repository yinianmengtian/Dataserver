package com.wtsj.hbase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.wtsj.hadoop.job.UrlCountTableJob;
import com.wtsj.hadoop.utils.HbaseTbleName;
import com.wtsj.model.UrlModel;
import com.wtsj.request.RequestData;
import com.wtsj.utils.RquestParamUtils;
import com.wtsj.utils.UralTravelUtils;

public class UrlTravelHbase {

	private static Logger log = Logger.getLogger(UrlTravelHbase.class);

	private static byte[] FAMILY = "URL".getBytes();
	private static HTablePool UrlTablePool;
	public static Long endID = 0L;

	// 写入hbase的表名
	public static String UrlTravel = "UrlTravel";
	
	static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmssms");

	static {
		System.out.println("****************初始化hadoop、hbase组件*************");

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				System.out.println("执行url访问统计");
				// TODO Auto-generated method stub
				UrlCountTableJob urlCountTableJob = new UrlCountTableJob();
				HbaseTbleName.initConfiguration();
				boolean f = false;

				try {

					// 提交mapreduce任务统计url访问量
					f = urlCountTableJob.runUrlCountJob(HbaseTbleName.conf,
							new Scan());

					// f=true;
					if (f) {
						
						// 把最新结果提交到网站上
						RequestData rData = new RequestData();
						
						String time=HbaseTbleName.conf.get("time");
						
						HbaseTbleName.conf.clear();
						String rand = ""
								+ (int) (1 + Math.random() * (99999 - 1 + 1));
						String md5 = RquestParamUtils.md5Check(rand);

						List<UrlModel> list = new ArrayList<UrlModel>();
//						UrlTravleDao urlTravleDao = new UrlTravleDao();
//
//						list = urlTravleDao.getNowUelSort();
						
						list =readDatasFromURLCOUNT(time);
						rData.uploadData(list, rand, md5);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error(e.getMessage());
				} finally {

				}
				System.out.println("执行url访问统计结果为： "+f);
			}
		}, 500L, 30*60 * 1000);
		// timer.
		initHtableConnect();

		System.out.println("************************初始化完毕*****************");
	}

	/**
	 * 初始化Htable连接如果每一在添加数据是才初始化table连接会增加系统负担
	 * 
	 * 
	 * 
	 * */
	public static void initHtableConnect() {

		Configuration conf = HBaseConfiguration.create();

		conf.set("hbase.zookeeper.quorum",
				"192.168.1.71,192.168.1.72,192.167.1.73");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("mapred.job.tracker", "192.168.1.71:9001");

		UrlTablePool = new HTablePool(conf, 10);
	}

	/**
	 * 写入Hbase UrlTravel表数据
	 * */
	@SuppressWarnings("static-access")
	public Long writeDatasToHbase(List<UrlModel> datas) {

		Long endID = 0L;

		List<Put> list = new ArrayList<Put>();

		if (datas != null) {

			for (UrlModel uModel : datas) {

				String rows = UralTravelUtils.toTimeFirstString(uModel);
				String values = UralTravelUtils.toTimeFirstString(uModel);
				byte[] row = null;

				if (rows != null) {

					row = rows.getBytes();
				}

				if (values != null) {

					byte[] value = values.getBytes();
					Put put = new Put(row);

					put.add(FAMILY, row, value);
					list.add(put);
				}
				Long ends = 0L;
				try {
					ends = Long.parseLong(uModel.getId());
				} catch (Exception e) {
					// TODO: handle exception
					// ends=0L;
				}
				if (endID < ends) {
					endID = ends;
				}
				// System.out.println("uModel.getId()" +uModel.getHwid()+
				// "  "+endID);

			}
		}

		try {

			HTableInterface urlTable = UrlTablePool.getTable("UrlTravel");

			urlTable.put(list);
			urlTable.flushCommits();
			if (datas != null && datas.size() > 0)
				this.endID = endID;
			System.out.println("最终插入条数：" + list.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("插入失败");
		}
		// this.endID=0L;
		return this.endID;

	}
	
	
	/**
	 * 读取Hbase URLCOUNT表数据
	 * */
	@SuppressWarnings("static-access")
	public static List<UrlModel> readDatasFromURLCOUNT(String date) {
		
		List<UrlModel> list=new ArrayList<UrlModel>();
		
		Scan scan=new Scan();
		
//		date=simpleDateFormat.format(new Date(Long.parseLong(date)));
		
		RowFilter rowFilter=new RowFilter(CompareOp.EQUAL, new SubstringComparator(date));
		
		scan.setFilter(rowFilter);
		
		HTableInterface URLCOUNT = UrlTablePool.getTable(HbaseTbleName.URLCOUNT);
		
		try {
			ResultScanner scanner=URLCOUNT.getScanner(scan);
			Result rs=scanner.next();
			while(rs!=null && rs.size()>0){
				
				UrlModel urlModel=UralTravelUtils.rowToURLTravelModel(Bytes.toString(rs.getRow()));
				
				if(urlModel!=null && urlModel.getRank()>=500){
					list.add(urlModel);
				}
				
				rs=scanner.next();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
//			return list;
		}
		
		return list;

	}


	public static void main(String[] args) {
		Configuration conf = HBaseConfiguration.create();

		conf.set("hbase.zookeeper.quorum",
				"192.168.1.71,192.168.1.72,192.167.1.73");

		conf.set("hbase.zookeeper.property.clientPort", "2181");

		conf.set("mapred.job.tracker", "192.168.1.71:9001");

		try {
			HTable hTable = new HTable(conf, "test");

			// 插入数据
			Put put = new Put("asd".getBytes());
			put.add("URL".getBytes(), "asd".getBytes(), "abs".getBytes());
			hTable.put(put);
			hTable.flushCommits();

			Scan scan = new Scan();
			// scan.setStartRow("b".getBytes());
			ResultScanner scanner = hTable.getScanner(scan);
			Iterator<Result> rsIt = scanner.iterator();
			while (rsIt.hasNext()) {
				Result re = rsIt.next();

				System.out.println(new String(re.value()));
				List<KeyValue> list = re.getColumn("URL".getBytes(),
						"d".getBytes());
				System.out.println("*******");
				for (KeyValue keyValue : list) {
					System.out.println(new String(keyValue.getValue()));
				}
				System.out.println("*******");
				// System.out.println(new String(re.value()));

			}

			// 在Hbase中建表建表
			// HBaseAdmin admin=new HBaseAdmin(conf);
			// HTableDescriptor des=new HTableDescriptor("UrlTravel");
			// des.addFamily(new HColumnDescriptor(FAMILY));
			// admin.createTable(des);
			// admin.close();
			
			
			List<UrlModel> list=readDatasFromURLCOUNT("201510160954515451");
			
			System.out.println("################################################");
			
			for(UrlModel urlModel :list){
				System.out.println(urlModel);
				
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
