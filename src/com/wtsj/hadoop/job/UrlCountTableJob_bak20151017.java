package com.wtsj.hadoop.job;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.wtsj.hadoop.map.UrlTableCountMap;
import com.wtsj.hadoop.reduce.UrlCountTableReduce;
import com.wtsj.hadoop.utils.HbaseTbleName;

/**
 * @author zxh 从habse中的网页访问历史表中获取数据，
 * */
public class UrlCountTableJob_bak20151017 {

	private Job job;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmssms");

	/**
	 * 从hbase中读取网站浏览记录做统计，求出每一个网页的访问次数
	 * */
	public boolean runUrlCountJob(Configuration conf, Scan scan)
			throws ClassNotFoundException, IOException, InterruptedException {

		conf.set(TableOutputFormat.OUTPUT_TABLE, HbaseTbleName.URLCOUNTTMP);
//		conf.set("mapred.map.tasks", "2");
//		设置map的中间结果进行压缩
		conf.set("mapred.compress.map.output", "true");
		conf.set("mapred.map.output.compression.codec", "org.apache.hadoop.io.compress.DefaultCodec");

		conf.set("time", simpleDateFormat.format(new Date()));
		conf.set("URLCOUNT", HbaseTbleName.URLCOUNT);

		// conf.set("mapred.jar",
		// "hdfs://192.168.1.71:9000/xiaohui/jar/UrlCountjob.jar");
		// 进行连接访问计数
		job = new Job(conf, "URL Count "
				+ HbaseTbleName.format.format(new Date()));

//		job.setReducerClass(UrlCountTableReduce.class);

		job.setNumReduceTasks(HbaseTbleName.numReduceTasks);

		job.setInputFormatClass(TableInputFormat.class);
		
//		job.setOutputFormatClass(TableOutputFormat.class);
//		job.setOutputKeyClass(Text.class);
//		job.setOutputValueClass(ImmutableBytesWritable.class);

		TableMapReduceUtil.initTableMapperJob(HbaseTbleName.UrlTravel, scan,
				UrlTableCountMap.class, Text.class, LongWritable.class, job);

		TableMapReduceUtil.initTableReducerJob(HbaseTbleName.URLCOUNTTMP, UrlCountTableReduce.class, job);
		
		job.setJarByClass(UrlCountTableJob_bak20151017.class);

		return job.waitForCompletion(true);

	}

}
