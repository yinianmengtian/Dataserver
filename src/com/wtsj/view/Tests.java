package com.wtsj.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.wtsj.utils.HttpClientUtils;

public class Tests {

	public  void main(String args) {

		int i = 0;
		while (true) {
			HttpClient httpClient = new DefaultHttpClient();

			// http://localhost:8080/Index/api/geturl?rand=1234&sign=1111111&list=[{"mac":"2015-09-23 10:35:04.0","hwid":9,"url":"http://www.cncrk.com/downinfo/42292.html"}]

			HttpPost httpPost = new HttpPost("http://ssplog.test.bswifi.com/m/imp?ts=1444635797167&data=T9R2slCbaysc0H0xp4kdg3ChS0KDTh-C_g6YBFHkaDGAfdl94P3lark8yn6xj3wa-3sI9fQqpoTjTs6-Fz_91U13lYm3A48SJgSnj8R0O03YkLQMydOSGA**");

			try {

				// 建立一个NameValuePair数组，用于存储欲传送的参数
				List<NameValuePair> params = new ArrayList<NameValuePair>();

				// 添加参数
				// params.add(new BasicNameValuePair("rand", "1234"));
				// params.add(new BasicNameValuePair("sign",
				// "1C5EA16B8313E6A08EA447E6C7DBCAB1"));

				httpPost.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//				httpPost.addHeader("Accept-Encoding","gzip, deflate, sdch");
				httpPost.addHeader("Accept-Language","en,zh-CN;q=0.8,zh;q=0.6");
				httpPost.addHeader("Cache-Control","max-age=0");
				httpPost.addHeader("Connection","keep-alive");
//				httpPost.addHeader("Host:ssplog.test.bswifi.com
//				httpPost.addHeader("If-Modified-Since:Wed, 07 Oct 2015 09:44:34 GMT
//				httpPost.addHeader("If-None-Match","W/\"864-1444211074000\"");
//				httpPost.addHeader("Upgrade-Insecure-Requests","1");
				httpPost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36");
				
				
				// 设置编码
				httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				HttpResponse response = httpClient.execute(httpPost);

				System.out.println(response.getStatusLine().getStatusCode());

				// 获取返回内容
				if (response.getStatusLine().getStatusCode() == 200) {
					if (response.getEntity() != null) {
						String s = EntityUtils.toString(response.getEntity());
						System.out.println(s);
//						HttpClientUtils.StringToList(s);

					}
					// 关闭流
					EntityUtils.consume(response.getEntity());

					// 关闭连接
					httpClient.getConnectionManager().shutdown();
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}

	}
	
	public static void main(String [] args){
		
		File file=new File("..");
		System.out.println(System.getProperty("user.dir") );
		System.out.println( System.getProperty("java.class.path")); 
		System.out.println( System.getProperty(Class.class.getName())); 
		try {
			System.out.println(file.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
