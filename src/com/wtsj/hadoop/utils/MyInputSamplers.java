package com.wtsj.hadoop.utils;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;
import org.apache.hadoop.util.ReflectionUtils;


public class MyInputSamplers<k, v> extends
		InputSampler<k, v> {

	public MyInputSamplers(Configuration conf) {
		super(conf);
		// TODO Auto-generated constructor stub
	}

	public static <k, v> void writePartitionFile(Job job, Sampler<k, v> sampler)
			throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = job.getConfiguration();
		final InputFormat inf = ReflectionUtils.newInstance(
				job.getInputFormatClass(), conf);
		int numPartitions = job.getNumReduceTasks();
		
		k[] samples = (k[]) sampler.getSample(inf, job);
		
		RawComparator<k> comparator = (RawComparator<k>) job
				.getSortComparator();
		Arrays.sort(samples, comparator);
		Path dst = new Path(TotalOrderPartitioner.getPartitionFile(conf));
		FileSystem fs = dst.getFileSystem(conf);
		if (fs.exists(dst)) {
			fs.delete(dst, false);
		}
		SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf, dst,
				job.getMapOutputKeyClass(), NullWritable.class);
		
		NullWritable nullValue = NullWritable.get();
		float stepSize = samples.length / (float) numPartitions;
		int last = -1;
		for (int i = 1; i < numPartitions; ++i) {
			int k = Math.round(stepSize * i);
			while (last >= k
					&& comparator.compare(samples[last], samples[k]) == 0) {
				++k;
			}
			
			ImmutableBytesWritable im= (ImmutableBytesWritable) samples[k];
			
			
			
			String[] keys=(new String(im.get())).split("\\*");
			System.out.println("取样："+new String(im.get()));
//			writer.append(samples[k], nullValue);
			writer.append(new MyK2(Long.parseLong(keys[1]), ""), nullValue);
			last = k;
		}
		writer.close();
	}
}
