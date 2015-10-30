package com.wtsj.thread;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.wtsj.hbase.UrlTravelHbase;
import com.wtsj.model.UrlModel;
import com.wtsj.request.RequestData;
import com.wtsj.utils.PropertyUtils;
import com.wtsj.utils.RquestParamUtils;

//@Deprecated 
public class QueryUrlTravelTask implements Runnable {

	private Logger log = Logger.getLogger(QueryUrlTravelTask.class);

	String sql;
	String ip;

	String port;

	public static Long QuerySize = 1000L;

	// UrlTravleDao urlTravleDao = new UrlTravleDao();

	RequestData requestData = new RequestData();

	public static Long startId = 1L;

	public static String path = "";

	public QueryUrlTravelTask() {

	}

	public QueryUrlTravelTask(String sql) {
		this.sql = sql;

		// 初始化hbase表连接
		UrlTravelHbase.initHtableConnect();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public static Long getStartId() {
		return startId;
	}

	public static void setStartId(Long startId) {
		QueryUrlTravelTask.startId = startId;
	}

	/*
	 * @Override public void run() { // TODO Auto-generated method stub while
	 * (true) {
	 * 
	 * if (urlTravleDao == null) { urlTravleDao = new UrlTravleDao(); }
	 * System.out.println("*************************************");
	 * System.out.println("开始位置："+startId);
	 * 
	 * 
	 * 
	 * List<URLTravelModel> list = urlTravleDao.getUrlHisByID(startId,
	 * QuerySize, sql); UrlTravelHbase urlTravelHbase = new UrlTravelHbase();
	 * 
	 * UrlTravelHbase.endID=startId; startId =
	 * urlTravelHbase.writeDatasToHbase(list); // endId = startId + QuerySize;
	 * PropertyUtils.writeProps(startId + "", "conf/memory.properties");
	 * 
	 * try { Thread.sleep(2000L); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } } }
	 */

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {

			if (requestData == null) {
				requestData = new RequestData();
			}
			System.out.println("*************************************");
			System.out.println("开始位置：" + startId);

			String rand = "" + (int) (1 + Math.random() * (99999 - 1 + 1));
			String md5 = RquestParamUtils.md5Check(rand);

			List<UrlModel> list = null;
			try {

				list = requestData.requestUrlDate(rand, md5, QuerySize + "",
						startId + "");

			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.error(e1.getMessage());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				log.error(e1.getMessage());
			}

			if (list != null && list.size() > 0) {
				UrlTravelHbase urlTravelHbase = new UrlTravelHbase();

				UrlTravelHbase.endID = startId;
				System.out.println("写入条数：" + list.size());
				startId = urlTravelHbase.writeDatasToHbase(list);
				// endId = startId + QuerySize;
				PropertyUtils.writeProps(startId + "", path
						+ "conf/memory.properties");
				System.out.println("开始位置：" + startId);
			}
			try {
				Thread.sleep(30 * 1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}
	}

}
