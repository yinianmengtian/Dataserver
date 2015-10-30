package com.wtsj.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wtsj.model.UrlModel;

public class HttpClientUtils {

	/**
	 * 
	 * */
	private static Map<String, HttpClient> map = new HashMap<>();
	

	public static HttpClient getHttpClientInsstance(String s) {

		if (map.get(s) == null) {
			HttpClient httpClient = new DefaultHttpClient();
			
			
//			httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_LINGER, value) ; 

//			设置连接超时时间，防止进程一直占用资源
			httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);  
			httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000); 

			map.put(s, httpClient);
			return map.get(s);
		} else if (s == null || s.equals("")) {
			
			return null;
		}

		return map.get(s);
	}


	/**
	 * 把json字符串转化成list的形式 "list": { [ "placenum":"11111",
	 * "hwid":"asdf13","url":"http://baidu.com", "mac":"aabbccddee", ], [
	 * "placenum":"11111","hwid":"asdf13", "url":"http://baidu.com",
	 * "mac":"aabbccddee", ] },
	 * 
	 * 
	 * */
	@SuppressWarnings("finally")
	public static List<UrlModel> StringToList(String result) {

		List<UrlModel> list = new ArrayList<UrlModel>();

		if (result != null && result.length() > 0) {

			JSONObject rjsn = null;
			try {

				rjsn = new JSONObject(result);

				// 获取浏览数据成功
				if (rjsn.has("code") && "0".equals(rjsn.getString("code"))) {

					JSONArray rejarry = rjsn.getJSONArray("list");

					for (int i = 0; i < rejarry.length(); i++) {

						UrlModel uModel = new UrlModel();

						if (rejarry.getJSONObject(i).has("hwid"))
							uModel.setHwid(rejarry.getJSONObject(i).getString(
									"hwid"));
						else
							uModel.setHwid("");

						if (rejarry.getJSONObject(i).has("placenum"))
							uModel.setPlacenum(rejarry.getJSONObject(i)
									.getString("placenum"));
						else
							uModel.setPlacenum("");

						if (rejarry.getJSONObject(i).has("mac"))
							uModel.setMac(rejarry.getJSONObject(i).getString(
									"mac"));
						else
							uModel.setMac("");

						if (rejarry.getJSONObject(i).has("url"))
							uModel.setUrl(rejarry.getJSONObject(i).getString(
									"url"));
						else
							uModel.setUrl("");
						
						if (rejarry.getJSONObject(i).has("id"))
							uModel.setId(rejarry.getJSONObject(i).getString(
									"id"));
						else
							uModel.setId("");

//						 System.out.println(uModel.toString());
						list.add(uModel);
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				return list;
			}

		}

		return list;

	}

	/**
	 * 把网站访问量list列表转换成josn字符串<br>
	 * 
	 * { [ "placenum":"11111", "hwid":"asdf13", "url":"http://baidu.com",
	 * "mac":"aabbccddee", "rank":1,
	 * 
	 * ], [ "placenum":"11111", "hwid":"asdf13", "url":"http://baidu.com",
	 * "mac":"aabbccddee", "rank":1, ] },
	 * 
	 * */
	@SuppressWarnings("finally")
	public static JSONArray ListToJson(List<UrlModel> list) {

		JSONArray randJArray = new JSONArray();

		if (list != null) {

			for (UrlModel urlModel : list) {

				JSONObject jsObject = new JSONObject();

				try {

					jsObject.put("url", urlModel.getUrl());
					jsObject.put("rank", urlModel.getRank());
//					jsObject.put("hwid", urlModel.getHwid());
//					jsObject.put("placenum", urlModel.getPlacenum());
//					jsObject.put("placenum", "");
//					jsObject.put("mac", urlModel.getMac());

					randJArray.put(jsObject);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					System.out.println(e.getMessage());
				} 

			}
		}
//		System.out.println(randJArray.toString());
		return randJArray;

	}

	/**
	 * 根据结果判断上传失败还是成功
	 * */
	@SuppressWarnings("finally")
	public static boolean requestIsSucc(String result) {

		boolean f = false;

		if (result != null && result.length() > 0) {

			JSONObject rjsn = null;
			try {

				rjsn = new JSONObject(result);

				// 获取浏览数据成功
				if (rjsn.has("code") && "0".equals(rjsn.getString("code"))) {

					if ("0".equals(rjsn.getString("code"))) {

						f = true;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				return f;
			}
		}
		return f;

	}

}
