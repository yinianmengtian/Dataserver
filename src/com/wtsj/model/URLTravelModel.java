package com.wtsj.model;

import java.io.Serializable;

/**
 * @author zxh<br>
 * 浏览记录model
 * */
public class URLTravelModel implements Serializable{

	
	private static final long serialVersionUID = -5680618431400315655L;
	
//	url连接
	private String urlName;
	
//	Id号
	private Long id;
	
//	时间
	private String time;
	
//	Mac地址
	private String mac;

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public String toString(){
		return "id="+id+"/turlName="+urlName+"/ttime"+time+"/tmac"+mac;
	}

}
