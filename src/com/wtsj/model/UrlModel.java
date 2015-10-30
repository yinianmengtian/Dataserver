package com.wtsj.model;

import java.io.Serializable;

/**
 * "placenum":"11111",
	"hwid":"asdf13",
	"url":"http://baidu.com",
	"mac":"aabbccddee",

 * */
public class UrlModel implements Serializable{

	private static final long serialVersionUID = -8090240230913121654L;

	//	参数md5签名
	private String placenum;
	
//	被查询的ip mac地址
	private String hwid;
	
//	行业类型
	private String url;
	
//	消费水平
	private String mac;
	
//	排名
	private int rank;
	
//	id
	private String id;

	public String getPlacenum() {
		return placenum;
	}

	public void setPlacenum(String placenum) {
		this.placenum = placenum;
	}

	public String getHwid() {
		return hwid;
	}

	public void setHwid(String hwid) {
		this.hwid = hwid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String toString(){
		return "placenum="+placenum+"/thwid="+hwid+"/turl"+url+"/tmac"+mac+"\trank"+rank+"\tid"+id;
	}
	
}
