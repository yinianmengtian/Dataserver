package com.wtsj.hadoop.job;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.MultiTableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableInputFormatBase;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Base64;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;

import com.wtsj.hadoop.map.UrlTableCountMap;
import com.wtsj.hadoop.reduce.UrlCountTableReduce;
import com.wtsj.hadoop.utils.HbaseTbleName;

/**
 * @author zxh 从habse中的网页访问历史表中获取数据，
 * */
public class UrlCountTableJob {

	private Job job;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmssms");

	/**
	 * 从hbase中读取网站浏览记录做统计，求出每一个网页的访问次数
	 * */
	public boolean runUrlCountJob(Configuration conf, Scan scan)
			throws ClassNotFoundException, IOException, InterruptedException {

//		一般mp统计的数据都是一次性，非重复利用的数据所以不进行缓存
		scan.setCacheBlocks(true);
		
		
		
		 HBaseConfiguration.merge(conf, HBaseConfiguration.create(conf));
		conf.set(TableOutputFormat.OUTPUT_TABLE, HbaseTbleName.URLCOUNT);
//		conf.set("mapred.map.tasks", "2");
//		设置map的中间结果进行压缩
		conf.set("mapred.compress.map.output", "true");
		conf.set("mapred.map.output.compression.codec", "org.apache.hadoop.io.compress.DefaultCodec");
//		设置map写入磁盘时的shuffle内存
		conf.set("io.sort.mb","200");
		
//		设置java的运行内存，启动task管理的子进程时的内存设置
		conf.set("mapred.child.java.opts", "-Xmx1024m");
		

		conf.set("time", simpleDateFormat.format(new Date()));
		conf.set("URLCOUNT", HbaseTbleName.URLCOUNT);
		
		conf.set("hbase.nameserver.address", "master");
		

		/**
		 ，跟hbase存储机制有关，hbase本地存储机制，即hbase会尽量把hdfs上的数据文件和rs上对应
		 region存在一台机器上，即当前的map的数据肯定在当前的机器上有本地数据，不需要网络传输。如果
		 启用备用任务机制，则新创建备用任务时，很有可能新建备用任务的机器上没有所需要的数据的备份，如此
		 便需要网络传输数据，增加了网络的开销，其效率会大大的降低，还不如不开启备用任务，就让原来的map
		 任务正常做呢。当然，即便关闭了此机制，如果创建map失败（比如由于oom异常）仍然会在其他节点上重
		 新创建此map任务
		 */
		conf.setBoolean("mapred.map.tasks.speculative.execution", false);
		
		

		// conf.set("mapred.jar",
		// "hdfs://192.168.1.71:9000/xiaohui/jar/UrlCountjob.jar");
		// 进行连接访问计数
		job = new Job(conf, "URL Count "
				+ HbaseTbleName.format.format(new Date()));


		job.setNumReduceTasks(HbaseTbleName.numReduceTasks);
		
//		job.setCombinerClass(LongSumReducer.class);

		job.setInputFormatClass(TableInputFormat.class);
		
		job.setOutputFormatClass(TableOutputFormat.class);
//		job.setOutputKeyClass(Text.class);
//		job.setOutputValueClass(ImmutableBytesWritable.class);

		TableMapReduceUtil.initTableMapperJob(HbaseTbleName.UrlTravel, scan,
				UrlTableCountMap.class, Text.class, LongWritable.class, job);

		TableMapReduceUtil.initTableReducerJob(HbaseTbleName.URLCOUNT, UrlCountTableReduce.class, job);
		
		
		job.setJarByClass(UrlCountTableJob.class);

		TableMapReduceUtil.initCredentials(job);
		
		return job.waitForCompletion(true);
	}
	
//	private  String convertScanToString(Scan scan) throws IOException {  
//        ByteArrayOutputStream out = new ByteArrayOutputStream();  
//        DataOutputStream dos = new DataOutputStream(out);  
//        scan.write(dos);  
//        return Base64.encodeBytes(out.toByteArray());  
//    } 

	

}
