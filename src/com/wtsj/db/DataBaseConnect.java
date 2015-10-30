package com.wtsj.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DataBaseConnect {
	
	private static Logger log=Logger.getLogger(DataBaseConnect.class);
	
	private static String driver="com.mysql.jdbc.Driver";
	private static String url="jdbc:mysql://localhost:3306/urldb?user=root&password=123456";
	
	/**
	 * 获取mysql连接
	 * */
	public static Connection getConnection(){
		Connection connection=null;
		try {
			Class.forName(driver);
			connection=DriverManager.getConnection(url);
			return connection;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			log.error("连接数据库失败："+e.getMessage());
			return null;
		}		
	}
	
	/**
	 * 关闭连接
	 * */
	public static void closeConnection(Connection connection){
		if(connection!=null){
			try {
				if(!connection.isClosed()){
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	

}
