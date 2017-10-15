package xyz.geekfly.csdn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Login {

	public static void main(String[] args) {
		//1. 获取登录所需的信息
		Map<String, String> login_info = getLoginInfo();
		
		//2. 模拟登录获取Cookie
		String cookie = getCookie(login_info);
		//3. 携带Cookie访问目标页(换成自己的)
		String target_url = "http://blog.csdn.net/tmaskboy";
		
		//携带登录获取的Cookie
		String result = getData(target_url, cookie);
		Document document = Jsoup.parse(result);
		System.out.println("携带Cookie:" + document.select(".navigator").text());
		
		//未携带Cookie
		String result1 = getData(target_url, "");
		Document document1 = Jsoup.parse(result1);
		System.out.println("未携带Cookie:" + document1.select(".navigator").text());
	}
	
	public static Map<String, String> getLoginInfo(){
		String login_url = "https://passport.csdn.net/account/login?ref=toolbar";
		String login_host = "https://passport.csdn.net"; //表单中获取的登录地址不带域名，需自行添加，拼接域名需注意‘/’，不要多加或漏加
		Map<String, String> return_data = new HashMap<String, String>();
		
		try {
			Document document = Jsoup.connect(login_url) 
					.userAgent("ozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36") 
					.get();
			//获取登录的表单
			Elements element = document.select("#fm1");
//			System.out.println(element);
			
			//获取登录所需要的一些参数
			return_data.put("lt", element.select("input[name=lt]").attr("value"));//登录流水号
			return_data.put("execution", element.select("input[name=execution]").attr("value"));
			return_data.put("_eventId", element.select("input[name=_eventId]").attr("value"));
			
			//获取点击登录的请求地址
			return_data.put("action", login_host + element.attr("action"));
		}catch (IOException e) {
			e.printStackTrace();
		}
		return return_data;
	}
	
	public static String getCookie(Map<String, String> data){
		String cookie = "";
		try{
			HttpClient httpClient = new DefaultHttpClient();

			HttpPost post = new HttpPost(data.get("action"));
			/* 模拟登录所需要的参数,有些网站登录时会检测，
			    如果存在基本上为必须，但CSDN发现并没有检测，故没有添加
			    在Chrome开发者平台中可查看，粘贴过来即可
			 */
//			post.setHeader("Host", "passport.csdn.net");
//			post.setHeader("Referer","https://passport.csdn.net/account/login?ref=toolbar");
//			post.setHeader("Origin", "https://passport.csdn.net");
//			post.setHeader("Content-Type","application/x-www-form-urlencoded");
	        
			List<NameValuePair> params = new ArrayList<NameValuePair>();  
			
	    	//参数
	    	params.add(new BasicNameValuePair("lt", data.get("lt"))); 
			params.add(new BasicNameValuePair("execution", data.get("execution")));
			params.add(new BasicNameValuePair("_eventId", data.get("_eventId"))); 
			
			//用户名和密码（*号），替换为自己的
			params.add(new BasicNameValuePair("username", "*****")); 
			params.add(new BasicNameValuePair("password", "*****"));
			
			post.setEntity(new UrlEncodedFormEntity(params,Consts.UTF_8));  
	        HttpResponse response = httpClient.execute(post);
	        int statusCode = response.getStatusLine().getStatusCode();
	        if(statusCode != 200){
	        	System.out.print(statusCode);
	        }
	        Header[] map =  response.getAllHeaders();
	        
	        //如需查看所有的响应头，取消以下注释即可  
	        /*System.out.println("显示响应Header信息\n");
	        for (Header entry : map)
	        {
	            System.out.println("Key : " + entry.getName() + " ,Value : " + entry.getValue());
	        }*/
	        
	        //对于登录，主要为获取响应头中的Cookie信息，拼接Cookie
	        for (Header entry : map)
	        {
	            if(entry.getName().contains("Set-Cookie")){
	            	cookie += entry.getValue() + ";";
	            }
	        }
	        System.out.println("Cookie信息:" + cookie);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return cookie;
	}
	
	public static String getData(String target_url, String cookie){
		String result = null;
		try{
			HttpClient httpClient = new DefaultHttpClient();

			HttpGet httpGet = new HttpGet(target_url);
			
			//传入Cookie信息
			httpGet.setHeader("Cookie", cookie);
			
	        HttpResponse response = httpClient.execute(httpGet);
	        int statusCode = response.getStatusLine().getStatusCode();
	        if(statusCode != 200){
	        	System.out.print(statusCode);
	        }
	        HttpEntity entity = response.getEntity();
	        
	        if (entity != null) {
	            result = EntityUtils.toString(entity, "utf-8");
	        }
	        
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
