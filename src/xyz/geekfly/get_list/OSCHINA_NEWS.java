package xyz.geekfly.get_list;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * 类名称：OSCHINA_NEWS   
 * 类描述：开源中国新闻列表翻页加载   
 * 创建人：geekfly
 * 创建时间：2017年7月25日 下午8:32:30      
 * @version  V1.0
 *
 */
public class OSCHINA_NEWS {
	
	public static void main(String[] args) throws IOException {
		
		for(int page_number=1;;page_number++){
			int data_rows = getPageData(page_number);
			System.out.println("当前执行：" + page_number + "页，数据数：" + data_rows);
			//返回数据为空时，结束循环
			if(data_rows == 0){
				break;
			}
		}
	}
	
	public static int getPageData(int page_number) throws IOException{
		String page_url = "https://www.oschina.net/action/ajax/get_more_news_list?newsType=&p=" + page_number;
		
		Document document = Jsoup.connect(page_url) 
				.userAgent("ozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36") 
				.post(); 
		
		//获取样例 69条 共7页
		Elements items = document.select("div[class=item box]");  
		forEachData(items);
		return items.size();
	}
	
	public static void forEachData(Elements items){
		String host = "https://www.oschina.net";
		for(Element item: items){
			//过滤广告
			if(!item.attr("data-tracepid").isEmpty()){
				continue;
			}
			// 标题
			String title = item.select("a").first().text();
			
			//标题地址
			String title_href = item.select("a").first().attr("href");
			if(!title_href.startsWith("https://")){
				title_href = host + title_href;
			}
			//描述
			String desc = item.select("div[class=sc sc-text text-gradient wrap summary]").text();
			
			//作者头像
			String author_image = item.select("img[class=avatar]").attr("src");
			//String author_image = item.select("img").first().attr("src");
			
//			System.out.println(item);
			Element mr = item.select(".from .mr").get(0);
			//作者
			String author = mr.select("a").text();
			// 从span[class=mr]中移除a标签，输出的即为发布时间
			mr.select("a").remove();
			String published = mr.text();
			
			String number = item.select(".from .mr").last().text();
			System.out.println("\t"+ title);
			
		}
	}
}
