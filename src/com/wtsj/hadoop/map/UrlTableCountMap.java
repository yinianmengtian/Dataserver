package com.wtsj.hadoop.map;

import java.io.IOException;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.Set;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class UrlTableCountMap extends TableMapper<Text, LongWritable> {

	public final static LongWritable oneword = new LongWritable(1L);
	public final static byte[] fimaly="URL".getBytes();

	

	@Override
	protected void map(ImmutableBytesWritable key, Result value, Context context)
			throws IOException, InterruptedException {

		NavigableMap<byte[], byte[]> MAP = value.getFamilyMap(fimaly);
		Set<byte[]> set = MAP.keySet();
		Iterator<byte[]> it = set.iterator();

		while (it.hasNext()) {
			byte[] b = it.next();
			// System.out.println(new String(b)+"   "+new String(MAP.get(b)));
			String s[] = new String(MAP.get(b)).split("\\*");

			context.write(new Text(s[1]), oneword);
		}

	}

}
