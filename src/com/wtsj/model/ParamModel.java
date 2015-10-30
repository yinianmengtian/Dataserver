package com.wtsj.model;

import java.io.Serializable;


/**
 * rand=1234&sign=1111111&list={}
 * */
public class ParamModel implements Serializable{

	private static final long serialVersionUID = -8763601756510680731L;
	
//	随机数
	private String rand;
	
//	参数md5签名
	private String sign;
	
//	url访问信息字符串
	private String list;

	public String getRand() {
		return rand;
	}

	public void setRand(String rand) {
		this.rand = rand;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}
	
	
	
	

}
