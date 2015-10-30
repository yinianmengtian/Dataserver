package com.wtsj.model;

import java.io.Serializable;

public class SqlModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2248396927423948835L;
	private String ip;
	private String sql;
	private String port;
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	

}
