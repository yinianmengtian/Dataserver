package com.wtsj.hadoop.reduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

/**
 * @author zxh reduce阶段统计
 * 
 */

/**
 * @author Administrator
 * 
 */
/**
 * @author Administrator
 *
 */
public class UrlCountTableReduce extends
		TableReducer<Text, LongWritable, ImmutableBytesWritable> {

	private String date = "";
	private HTable table=null;
	
	private final static List<String> s =new ArrayList<>();

	@Override
	protected void reduce(Text key, Iterable<LongWritable> values,
			Context context) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		String Text = key.toString();
		Iterator<LongWritable> it = values.iterator();
		Long l = 0L;
		while (it.hasNext()) {
			it.next();
			l = l + 1L;
		}
		
		if (l >= 500L) {
			
//			Put put = new Put((date + "*" + l + "*" + Text).getBytes());
//			put.add(Bytes.toBytes("COUNT"), Bytes.toBytes(Text),
//					Bytes.toBytes(new String(Bytes.toBytes((l + "")), "UTF-8")));
//
//			table.put(put);
			
			Put put = new Put((date + "*" + l + "*" + Text).getBytes());
			put.add(Bytes.toBytes("COUNT"), Bytes.toBytes(Text),
					Bytes.toBytes(new String(Bytes.toBytes((l + "")), "UTF-8")));
			
			context.write(null, put);
			
		}else{
		}

	}

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		super.setup(context);
		// 写入时间统一s =new ArrayList<>();
//		s.add("http://boscdn.bpc.baidu.com/v1/holmes-moplus/mp-cdn.html");
//		s.add("http://cpro.baidustatic.com/cpro/ui/html/appDetect.html");
//		s.add("http://cache.xixik.com.cn/1/shenzhen/");
//		s.add("http://cdn.tanx.com/t/acookie/acbeacon2.html" );
		
		date = context.getConfiguration().get("time");
		
//		table=new HTable(context.getConfiguration(), context.getConfiguration().get("URLCOUNT"));
	}

	@Override
	protected void cleanup(Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
//		table.flushCommits();
//		table.close();
		
		super.cleanup(context);
	}

	
}