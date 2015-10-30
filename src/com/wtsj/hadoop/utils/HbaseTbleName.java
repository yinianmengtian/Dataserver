package com.wtsj.hadoop.utils;

import java.text.SimpleDateFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;

public class HbaseTbleName {

	/* 连接访问历史表，存放每一个访问的记录 */
	public static String UrlTravel = "UrlTravel";

	/* 连接访问统计表，每一个连接的访问次数 */
	public static String URLCOUNT = "URLCOUNT";
	
	/* 连接访问统计表暂存表，每一个连接的访问次数 */
	public static String URLCOUNTTMP = "URLCOUNTTMP";

	public static HTablePool hTablePool;

	public static Configuration conf;
	
	/*reducer的数量*/
	public static int numReduceTasks=3;
	
	/*时间格式化*/
	public static SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 初始化hbase连接
	 * */
	public static void initTablePool(Configuration conf) {
		
		hTablePool = new HTablePool(conf, 10);
		
	}

	/**
	 * 初始化Configuration文件
	 * */
	public static void initConfiguration() {

		// 配置远程提交mapreduce
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum",
				"192.168.1.71,192.168.1.72,192.167.1.73");

		conf.set("hbase.zookeeper.property.clientPort", "2181");

		conf.set("mapred.job.tracker", "192.168.1.71:9001");

	}

	/**
	 * 获取一个htable连接
	 * */
	public static HTableInterface getHtableByName(String htableName) {
		
		return hTablePool.getTable(htableName);
		
	}

}
