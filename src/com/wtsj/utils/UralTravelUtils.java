package com.wtsj.utils;

import java.util.Date;
import java.util.UUID;

import com.wtsj.model.URLTravelModel;
import com.wtsj.model.UrlModel;

/**
 * @author zxh<br>
 * 用于转换把浏览记录转化成对应的字符串
 * */
public class UralTravelUtils {
	
	/**
	 * 
	 * /**
 * "placenum":"11111",
	"hwid":"asdf13",
	"url":"http://baidu.com",
	"mac":"aabbccddee",
	 * 拼接字符串顺序 ： placenum+"*"+链接+"*"+mac+"*+hwid"
	 * */
	public static String toTimeFirstString(UrlModel uModel){
		StringBuffer sb=new StringBuffer();
		
		
		if(isLegalString(uModel.getPlacenum())){
			sb.append(uModel.getPlacenum()+"*");
		}else{
			sb.append("####"+"*");
		}
		
		if(isLegalString(uModel.getUrl())){
			sb.append(uModel.getUrl()+"*");
		}else{
			return null;
		}
		
		if(isLegalString(uModel.getMac())){
			sb.append(uModel.getMac()+"*");
		}else{
			sb.append("####"+"*");
		}
		
		if(isLegalString(uModel.getHwid()+"")){
			sb.append(uModel.getHwid()+"*");
		}else{
			sb.append("####");
		}
		
		if(isLegalString(uModel.getHwid()+"")){
			
			sb.append((new Date().getTime())+UUID.randomUUID().toString()+"");
		}else{
			sb.append("####");
		}
		
//		System.out.println(sb.toString());
		return sb.toString();
		
	}
	
	
	
	/**
	 * 拼接字符串顺序 ： 链接+"*"+placenum+"*"+mac+"*+hwid"
	 * */
	public static String toURLFirstString(UrlModel uModel){
		StringBuffer sb=new StringBuffer();
		
		
		if(isLegalString(uModel.getUrl())){
			sb.append(uModel.getUrl()+"*");
		}else{
			return null;
		}
		
		
		if(isLegalString(uModel.getPlacenum())){
			sb.append(uModel.getPlacenum()+"*");
		}else{
			sb.append("####"+"*");
		}
		
	
		if(isLegalString(uModel.getMac())){
			sb.append(uModel.getMac()+"*");
		}else{
			sb.append("####"+"*");
		}
		
		if(isLegalString(uModel.getHwid()+"")){
			sb.append(uModel.getHwid()+"");
		}else{
			sb.append("####");
		}
		
		return sb.toString();
		
	}
	
	/**
	 * 把读取的rowkey转化为URLTravelModel
	 * */
	public static UrlModel rowToURLTravelModel(String row){
		
		UrlModel uModel=null;
		String[] rows=row.split("\\*");
		
		if(rows.length>=3){
			
			uModel=new UrlModel();
			
			uModel.setHwid("");
			uModel.setRank(Integer.parseInt(rows[1]));
			uModel.setUrl(rows[2]);
			uModel.setId("");
			uModel.setMac("");
			uModel.setPlacenum("");
		}
		
		
		return uModel;
		
	}
	
	/**
	 * 工具类：判断字符串是否为null或长度小于等于0，如果为null或长度小于等于0则返回false
	 * */
	private static boolean isLegalString(String s){
		if(s!=null && s.length()>0){
			return true;
		}else{
			return false;
		}
	}

}
