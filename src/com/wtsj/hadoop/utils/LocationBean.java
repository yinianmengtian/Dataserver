package com.wtsj.hadoop.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

public class LocationBean implements Writable, DBWritable {
	
	
//	网路链接
    private String urlName;

//    访问次数
    private int count;

//    统计时间
    private Date time;

    @Override
	public void write(PreparedStatement statement) throws SQLException {
		int index = 1;  
        statement.setString(index++, this.getUrlName());  
        statement.setInt(index++, this.getCount());  
        System.out.println(new Timestamp(this.getTime().getTime()));
        statement.setTimestamp(index++, new Timestamp(this.getTime().getTime()));  
//        Timestamp timestamp=new 
//        statement.setTimestamp(index++, x);
	}

	@Override
	public void readFields(ResultSet resultSet) throws SQLException {
	     this.urlName = resultSet.getString(1);
	     this.count = resultSet.getInt(2);
	     System.out.println(new Date(resultSet.getTimestamp(3).getTime()));
	     this.time = new Date(resultSet.getTimestamp(3).getTime());
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		
		
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	

	
}