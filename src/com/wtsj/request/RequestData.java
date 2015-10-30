package com.wtsj.request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.velocity.texen.util.PropertiesUtil;
import org.json.JSONException;
import org.json.JSONObject;

import com.wtsj.model.UrlModel;
import com.wtsj.utils.HttpClientUtils;
import com.wtsj.utils.RquestParamUtils;

public class RequestData {

	/**
	 * 请求网站访问数据，把请求到的结果转化成 List
	 * */
	public List<UrlModel> requestUrlDate(String rand, String md5, String num,
			String start) throws ClientProtocolException, IOException {

		List<UrlModel> list = null;

		HttpPost httpPost = new HttpPost(
				"http://tongji.bswifi.com/index/api/geturl");

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("rand", rand));		
		params.add(new BasicNameValuePair("sign", md5));
		params.add(new BasicNameValuePair("num", num));
		params.add(new BasicNameValuePair("start", start));
		
//		System.out.println("rand =="+rand +"   md5  "+ md5);
		
		httpPost.setHeader("Connection", "close");
		httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

		HttpResponse response = HttpClientUtils.getHttpClientInsstance("requestUrlDate")
				.execute(httpPost);

		if (response.getStatusLine().getStatusCode() == 200) {
			if (response.getEntity() != null) {


				String result = EntityUtils.toString(response.getEntity());

//				System.out.println("result==  " + result);

				list = HttpClientUtils.StringToList(result);

			}
			
		}
		// 关闭流
		EntityUtils.consume(response.getEntity());
		
		return list;
	}

	/**
	 * 请求url连接数据，上传统计结果到url连接
	 * http://tongji.bswifi.com/index/api/geturl?rand=1234&sign=1111111&list={}
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws JSONException
	 * */
	public boolean uploadData(List<UrlModel> urlList, String rand, String md5)
			throws ClientProtocolException, IOException, JSONException {

		boolean f = false;

		HttpPost httpPost = new HttpPost(
				"http://tongji.bswifi.com/index/api/sendurl");

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		String results=HttpClientUtils.ListToJson(urlList).toString();



		params.add(new BasicNameValuePair("rand", rand));
		md5=RquestParamUtils.md5Check(results+rand);
		params.add(new BasicNameValuePair("sign", md5));


		params.add(new BasicNameValuePair("list",results));

		httpPost.setHeader("Connection", "close");
		httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

		HttpResponse response = HttpClientUtils.getHttpClientInsstance("uploadData")
				.execute(httpPost);

		System.out.println("最新url访问统计==  " + urlList.size());
//		System.out.println("最新url访问统计==  " +results);
		if (response.getStatusLine().getStatusCode() == 200) {
			if (response.getEntity() != null) {


				String result = EntityUtils.toString(response.getEntity());

				System.out.println("最新url访问统计==  " +result);

				f = HttpClientUtils.requestIsSucc(result);

			}

		}
		// 关闭流
		EntityUtils.consume(response.getEntity());

		return f;

	}

}
