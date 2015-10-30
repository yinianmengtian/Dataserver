package com.wtsj.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

public class PropertyUtils {
	
	/**
	 * 获取最后抽取时间
	 * 
	 * @return
	 */
	public static String readProps(String path) {
		
//		InputStream inStream = TaopaiUtil.class
//				.getResourceAsStream("/memory.properties");
		
		File gFile=new File(path);
		
		InputStream inStream = null;
		Properties props = new Properties();
		String END_ID = "";
		try {
			
			inStream = new FileInputStream(gFile);
			props.load(inStream);
			END_ID = props.getProperty("END_ID");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
				if(END_ID==null || END_ID.length()==0){
					END_ID=0+"";
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return END_ID;
	}

	/**
	 * 写入分析最后ID
	 * */
	public static Boolean writeProps(String endId,String path) {
		
		File gFile=new File(path);
		
		Properties prop = new Properties();
		boolean flag = false;
		OutputStream out = null;

		try {
			out = new FileOutputStream(gFile);
			prop.setProperty("END_ID", endId);
			prop.store(out, "下一次查询起点的位置");
			flag = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flag;

	}
	
	/**
	 * 获取文件的路径方便读取配置文件
	 * */
	public static String getPath(){
		String path=PropertyUtils.class.getProtectionDomain().getCodeSource().getLocation().toString();
		try {
			path=URLDecoder.decode(path,"UTF-8");
			String[] s=path.split("/");
			path=path.replace(s[s.length-1], "");
			path=path.replace("file:/", "");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return path;
	}

}
