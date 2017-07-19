package xyz.geekfly.oschina;

import java.io.IOException;

import javax.rmi.CORBA.Tie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class News {

	public static void main(String[] args) throws IOException {
		String url = "https://www.oschina.net/news"; 
		Document document = Jsoup.connect(url) 
		.userAgent("Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0") 
		.get(); 
		
		Elements items = document.select("#all-news .item");
		System.out.println(items.size());
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
			
			Element mr = item.select(".from .mr").get(0);
			//作者
			String author = mr.select("a").text();
			// 从span[class=mr]中移除a标签，输出的即为发布时间
			mr.select("a").remove();
			String published = mr.text();
			
			String number = item.select(".from .mr").last().text();
			System.out.println(number);
			
		}

	}

}
