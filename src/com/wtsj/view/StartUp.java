package com.wtsj.view;

import org.apache.log4j.Logger;

import com.wtsj.thread.QueryUrlTravelTask;
import com.wtsj.utils.PropertyUtils;

public class StartUp {
	
	private static  Logger log=Logger.getLogger(StartUp.class);
	
	@SuppressWarnings("static-access")
	public static void main(String [] args){
		
		log.info("启动服务");
//		读取sql语句
//		List<String> sqlList=XmlUtils.readXml();
		
		QueryUrlTravelTask queryUrlTravelTask=new QueryUrlTravelTask();
		
		String path="/"+PropertyUtils.getPath();
		QueryUrlTravelTask.path=path;
		System.out.println("jar存放路径："+path);
		queryUrlTravelTask.setStartId(Long.parseLong(PropertyUtils.readProps(path+"conf/memory.properties")));
		new Thread(queryUrlTravelTask).start();
		
//		启动mapreduc任务
		 try {
			Class.forName("com.wtsj.hbase.UrlTravelHbase");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
//			e.printStackTrace();
		}  
		
	}

}
