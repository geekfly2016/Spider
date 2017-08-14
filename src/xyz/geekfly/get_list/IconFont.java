package xyz.geekfly.get_list;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;






import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils; 

/**
 * 
 * 类名称：IconFont   
 * 类描述：   获取阿里巴巴
 * 创建人：geekfly
 * 创建时间：2017年8月14日 下午7:48:16      
 * @version  V1.0
 *
 */
public class IconFont {

	public static void main(String[] args) throws IOException {
		String url = "http://www.iconfont.cn/api/icon/search.json";
		String keyword = "java"; //查询条件
		Integer page_number = 1; //起始页数
		
		while(true){
			String json = Post(url, page_number);
			
			Map<String, Object> data = JsonUtil.JsonToMap(json);
			
			//System.out.println(data); // 不了解此处返回数据结构的，可打印数据
			
			/*返回数据类似以下格式
			 * {
			 * 		"code":200
			 * 		"data:{	
			 * 				"counts":103,
			 * 				"icons":{
			 * 							["svg:"", "path_attributes":"fill=\"\""],
			 * 							[]
			 * 						}	
			 * 			}
			 * }
			 */
			if(data.isEmpty() || Integer.parseInt(data.get("code").toString()) != 200){
				System.out.println("获取第" + page_number + "页数据出错！！");
				break;
			}
			//上述结构的一个data键所对应的值
			Map<String, Object> data_key = (Map<String, Object>) data.get("data");
			//data中的icons键所对应的值
			List<Map<String, Object>> icons = (List<Map<String, Object>>) data_key.get("icons"); 
			
			if(icons.isEmpty()){
				System.out.println("结束，共计：" + data_key.get("count")); //{data={"count":103,"icons":[]}, code=200}
				break;
			}else{
				for(Map<String, Object> icon: icons){
					//可以在此处进行数据解析
					//System.out.println(icon); //可根据icon.get("")方式获取各个值
				}
				System.out.println("第" + page_number + "页,数据:" + icons.size() + "条数据");
				
				page_number++; //继续下一页
			}
		}
	}
	
	public static String Post(String url, Integer page_number) {
		try{
			HttpClient httpClient = new DefaultHttpClient();

			HttpPost post = new HttpPost(url);
			//以下参数因不同网站安全监测机制不同，所要求带的参数也不尽相同
			post.setHeader("Content-Type", "application/json;charset=UTF-8");
			post.setHeader("Host", "www.iconfont.cn");
			post.setHeader("Cookie", "UM_distinctid=15d5d87f853427-0bbd70005d8bf3-701238-100200-15d5d87f85423; EGG_SESS=0cDWugkqvoRMq2Hsgtv1BN9l80ylmhSnOklzQc-RoRH1BVeD186UKSTw-W8z_nr764S_zotydwBV1W5fLwDZdM122vm702nRb3wIC7G9M7oWWhL4XCgQbGpssIVXWYqv4eAdpljcryYY5M1yS6Shdm6EYkVrdggnz0g4-4wFej8RxXBY8XJ0PT7cx5wm6JTUeWl0TyPxbQkb4syjpPVWGIsfbnDOd_fhghrAq8qADPRwLWfmj3upXsUysrmEbyfIixRoIurpv2XIodRYZwxQ_iIFqqePYdahOsE1-Z5eQA9D6q0LF5ENjDU8w0ggU9gIHX96aqEIL5fr0UiLdQ9ipOyT-S9A19iAdIoeeFTyyK8Rd4gRbf-tT4NxVnD3Isk9FmNgu4ksLUCNAS_RoxTRDNXykJGdWQtxptHyffF43D-dEhbcVIJPdveLjNCBml1DRAeQ555FgUJ6B4TLpZjCpZnGAETHwgltbIgnl9akXhTkLgt83advBHJYghe_SeiyAbN0vm5vKUNIVlUE8zxeOmwoiGTVR5sgf8ya_n7d935Lalh2LTN4UH56Xgq7TUUxyxeXJM-rqmSM_Dia2X1staaGYMBK36dQMJ4FtFC0WG3nUJSr7BmIfpB6pqtxDOxRA-vrd4tlsr3pYGyJRY39nGQUFaMQH0xOU0WaVHQjhmpBF07GBWz_hDXrxired94U9lFAOUsFnD9v8Rt7U5hOgZGT3CrqxD9Z_s4qMs3PDiij6LJ-v-ckI6zxkWBGmtuV_BzHoJrO2pwH661VAi8ScuDImTPpnlTR3MCfQFdeqrOYjf2oKe5mvxyXTggdCICxzDC35zl9YIqLoKMQ2X4wNyNHjn3b4Q3mJx3xDeA8rZ9vyFW-deP17t2xPlgOA-uSWBeztRyQJKWk7tMjUnbaQRv0hAbTe7Jw8RQL_ZU69X9ryqVczqK3BJe87HUYWMagJ99x5ZeTFIiOu5v1f6n9APPHEfHlEwSy_-yzmXwpq-0f9h87ivitoqbGEj-rQUHjo5S1QYY8xTqJdK8heI_J30Dp2mFacjCIG2tTbyR9FhXxC5-45YpC_Cphd1Au1F82uoMQN4ln3PaQbmfUKyf3Fts18EjLrOj8iAe0k8WxTvHRSqZ-5JPSCcQb-d9Wu4yMirhYaNS5lljFEd4Nane-oUez1d5_Bp--5Wbb3f0W3YM=; ctoken=bL67K02mQl16IlJizxgeicon-font; CNZZDATA1260547936=1613607967-1500510123-null%7C1502708127; cna=kYTgEcv0DhQCAXWeAlQMPFxT; u=872092; isg=ApqaMHwIlwRPyhtJ1ozzCQgl60B2DzsZOF-Q-qQTEi3xFztRjF_jtekhk9Vw");
			post.setHeader("Referer","http://www.iconfont.cn/search/index?q=java&page=2");
			post.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
	        
			List<NameValuePair> params = new ArrayList<NameValuePair>();  
			//变化参数
			params.add(new BasicNameValuePair("page", String.valueOf(page_number))); //传入页码
	    	params.add(new BasicNameValuePair("q", "java")); //检索关键字
	    	//固定参数
	    	params.add(new BasicNameValuePair("cityEname", "henan")); 
			params.add(new BasicNameValuePair("sortType", "updated_at"));
			params.add(new BasicNameValuePair("pageSize", "54")); //每页条数 54
			params.add(new BasicNameValuePair("t", String.valueOf(new Date().getTime()))); //当前时间戳 1502713082550
			params.add(new BasicNameValuePair("ctoken", "bL67K02mQl16IlJizxgeicon-font"));
			
			post.setEntity(new UrlEncodedFormEntity(params,Consts.UTF_8));  
	        HttpResponse response = httpClient.execute(post);
	        int statusCode = response.getStatusLine().getStatusCode();
	        if(statusCode != 200){
	        	System.out.print(statusCode);
	        	return null;
	        }
	        HttpEntity entity = response.getEntity();
	        String result = null;
	        if (entity != null) {
	            result = EntityUtils.toString(entity, "utf-8");
	        }
	        EntityUtils.consume(entity);
	        return result;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
