package com.wtsj.utils;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wtsj.model.ParamModel;
import com.wtsj.model.UrlModel;

/**
 * 处理验证请求参数
 * */
public class RquestParamUtils {
	private static final String Md5key = "ackeyUHB6a7d8aabwscdxevfrTGBZAQ";

	/**
	 * @param req
	 * <br>
	 *            获取参数创建，参数对象 ,从requset获取的参数用rand=1234&sign=1111111&list={}
	 * */
	public static ParamModel paramMtoParamModel(HttpServletRequest req) {

		ParamModel paramModel = null;

		String rand = req.getParameter("rand");
		String sign = req.getParameter("sign");
		String list = req.getParameter("list");

		if (rand != null && (sign!=null && sign.equals(md5Check(rand)))) {

			paramModel = new ParamModel();

			paramModel.setRand(rand);
			paramModel.setSign(sign);
			paramModel.setList(list);

		}

		

		return paramModel;

	}
	
	
	/**
	 * MD5校验
	 * @param plainText
	 * @return 32位密文
	 */

	public static String md5Check(String plainText) {

		String re_md5 = new String();
		try {

			MessageDigest md = MessageDigest.getInstance("MD5");
			
			String mk=plainText+Md5key;
			mk=URLEncoder.encode(mk);
			
			md.update((mk).getBytes());

			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");

			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];

				if (i < 0)
					i += 256;

				if (i < 16)
					buf.append("0");

				buf.append(Integer.toHexString(i));
			}
			re_md5 = buf.toString();
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

		}
		System.out.println("校验值： "+re_md5.toUpperCase());
		return re_md5;
	}
	


	/**
	 * 解析list参数 list参数以json方式发送： { "code": 0, "result":{ [ "placenum":"11111",
	 * "hwid":"asdf13", "url":"http://baidu.com", "mac":"aabbccddee", "rank":1,
	 * 
	 * ], [ "placenum":"11111", "hwid":"asdf13", "url":"http://baidu.com",
	 * "mac":"aabbccddee", "rank":1, ] },
	 * "returnSign":"DDDDDASDFASDFASDFASDF41SAD341324" }
	 * 
	 * 
	 * "{\"code\": 0, \"result\":" + "[{
	 * \"placenum\":\"11111\", " +
	 * "\"hwid\":\"asdf13\",\"url\":\"http//baidu.com\", " +
	 * "\"mac\":\"aabbccddee\", \"rank\":1,}," +
	 * " { \"placenum\":\"11111\", \"hwid\":\"asdf13\", " +
	 * "\"url\":\"http://baidu.com\"," +
	 * "\"mac\":\"aabbccddee\", \"rank\":1,  }]," +
	 * "\"returnSign\":\"DDDDDASDFASDFASDFASDF41SAD341324\" }"
	 * 
	 * */
	public static List<UrlModel> parsingList(String listString) {

		List<UrlModel> list = new ArrayList<UrlModel>();

		try {
			System.out.println(listString);
			JSONObject jObject = new JSONObject(listString);
			
//			获取url访问的json串
			JSONArray jArray=jObject.getJSONArray("result");
			
			for(int i=0;i<jArray.length();i++){
				
				UrlModel urlModel=new UrlModel();
				if(jArray.getJSONObject(i).has("hwid"))
					urlModel.setHwid(jArray.getJSONObject(i).getString("hwid"));
				
				if(jArray.getJSONObject(i).has("url"))
					urlModel.setUrl(jArray.getJSONObject(i).getString("url"));
				if(jArray.getJSONObject(i).has("placenum"))
					urlModel.setPlacenum(jArray.getJSONObject(i).getString("placenum"));
				
				if(jArray.getJSONObject(i).has("mac"))
					urlModel.setMac(jArray.getJSONObject(i).getString("mac"));
				
				if(jArray.getJSONObject(i).has("rank"))
					urlModel.setRank(jArray.getJSONObject(i).getInt("rank"));
				
				list.add(urlModel);
				
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return list;

	}

	/**
	 * 校验失败返回字符串
	 * */
	public static JSONObject failJson(){
		JSONObject jsonObject=new JSONObject();
		
		try {
			jsonObject.put("code", "1001");
			jsonObject.put("error", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * 执行操作成功返回字符串
	 * */
	public static JSONObject sueccJson(String returnSign){
		JSONObject jsonObject=new JSONObject();
		try {
			
			jsonObject.put("code", "0");
			jsonObject.put("error", "");
			jsonObject.put("returnSign", returnSign);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	
	/**
	 * 把网站访问量做成分会Json字符串的格式返回
	 * */
	public static JSONObject sueccJson(String returnSign,List<UrlModel> list){
		JSONObject jsonObject=new JSONObject();
		try {
			
			
			jsonObject.put("code", "0");
			JSONArray jArray=new JSONArray();
			for(UrlModel urlModel : list){
				JSONObject jObject2=new JSONObject();
				/*
				 placenum":"11111",
				"hwid":"asdf13",
				"url":"http://baidu.com",
				"mac":"aabbccddee",
				 */
				
				jObject2.put("placenum", urlModel.getPlacenum());
				jObject2.put("hwid", urlModel.getRank());
				jObject2.put("url", urlModel.getUrl());
				jObject2.put("mac", urlModel.getMac());
				
				jArray.put(jObject2);
				
			}
			jsonObject.put("list", jArray);
			jsonObject.put("returnSign", returnSign);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	
	public static void main(String[] args) {
		
	}
}
