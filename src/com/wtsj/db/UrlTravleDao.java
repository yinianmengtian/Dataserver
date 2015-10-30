package com.wtsj.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;








import org.apache.log4j.Logger;

import com.wtsj.model.URLTravelModel;
import com.wtsj.model.UrlModel;
import com.wtsj.utils.XmlUtils;

public class UrlTravleDao {
	
	private static Logger log=Logger.getLogger(UrlTravleDao.class);
	
	private SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
	
	private String sql="select id,url,count,time from urlsort where time in (select MAX(time) from urlsort)";
	/**
	 * 根据浏览id读取浏览记录
	 * select id , url , time , mac from urllist where id&gt;? and id&lt;=? ORDER BY id
	 * */
	public List<URLTravelModel> getUrlHisByID(Long start,Long end,String sql){
		
		List<URLTravelModel> list=new ArrayList<URLTravelModel>();
		
		sql=sql.replace("#", start+"");
		sql=sql.replace("*", end+"");
		
		Connection conn=DataBaseConnect.getConnection();
		Statement statement=null;
		ResultSet rs=null;
		
		try {
			
			statement=conn.createStatement();
			rs=statement.executeQuery(sql);
			
			while(rs.next()){
				URLTravelModel uModel=new URLTravelModel();
				
				uModel.setId(rs.getLong(1));
				uModel.setUrlName(rs.getString(2));
				uModel.setTime(format.format(rs.getTimestamp(3)));
				uModel.setMac(rs.getString(4));
				
				list.add(uModel);
				
				log.debug(rs.getInt(1)+"  "+rs.getString(2));
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("数据库连接："+e.getMessage());
		}finally{
			try {
				statement.close();
				rs.close();
				DataBaseConnect.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error("数据库连接："+e.getMessage());
//				e.printStackTrace();
			}
		}
		
		
		return list;
		
	}
	
	/**
	 * 根据浏览id读取浏览记录
	 * select id,url,count,time from urlsort where time in (select MAX(time) from urlsort);
	 * */
	public List<UrlModel> getNowUelSort(){
		
		List<UrlModel> list=new ArrayList<UrlModel>();
		
		Connection conn=DataBaseConnect.getConnection();
		Statement statement=null;
		ResultSet rs=null;
		
		try {
			
			statement=conn.createStatement();
			rs=statement.executeQuery(sql);
			
			while(rs.next()){
				UrlModel uModel=new UrlModel();
				
				uModel.setHwid(rs.getString(1));
				uModel.setUrl(rs.getString(2));
				uModel.setRank(rs.getInt(3));
				uModel.setMac("");
				
				list.add(uModel);
				
				log.debug(rs.getInt(1)+"  "+rs.getString(2));
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("数据库连接："+e.getMessage());
		}finally{
			try {
				statement.close();
				rs.close();
				DataBaseConnect.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error("数据库连接："+e.getMessage());
			}
		}
		
		
		return list;
		
	}
	
	
	public static void main(String [] args){
		UrlTravleDao dao=new UrlTravleDao();
		String sql=XmlUtils.readXml().get(0);
		sql=sql.replace("#", "0");
		sql=sql.replace("*", "10");
		System.out.println(sql);
		dao.getUrlHisByID(0L, 10L, sql);
	}

}
