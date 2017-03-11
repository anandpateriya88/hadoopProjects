package com.crawler.mr.crimecrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.crawler.services.Spider;

public class CrimeCrawlMapper extends Mapper<Object, Text, Text, IntWritable> {
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		
		System.out.println("============== Crime Crawler : Map => START ==================");
		String url = value.toString().trim();
		Spider spider = new Spider();
		String cityCrimeSeveritytemp[] = {};
		List<String> cityCrimeSeverityLst = new ArrayList<String>();
		String urlAndDepthArray[] = url.split("::");
		if (null != urlAndDepthArray && urlAndDepthArray.length > 1) {
			int depthStart = Integer.parseInt(urlAndDepthArray[1]);
			int depthEnd = Integer.parseInt(urlAndDepthArray[2]);
			System.out.println("CrawlDataMapper : Map START =>"+depthStart+", END =>"+depthEnd);
			
			cityCrimeSeverityLst = spider.getAllCrimeRatingDtls(urlAndDepthArray[0], depthStart, depthEnd);
			if(!cityCrimeSeverityLst.isEmpty()){
				for(String cityCrimeSeverity : cityCrimeSeverityLst){
					if(null != cityCrimeSeverity && !cityCrimeSeverity.isEmpty())
					cityCrimeSeveritytemp = cityCrimeSeverity.split("#");
					if(null != cityCrimeSeveritytemp && cityCrimeSeveritytemp.length > 1){
						
						if(null != cityCrimeSeveritytemp[0] && !cityCrimeSeveritytemp[0].isEmpty() && Integer.parseInt(cityCrimeSeveritytemp[1]) > 0){
							System.out.println("Crime Crawl Mapper : Map Writing key, value to context. KEY(City) =>"
									+cityCrimeSeveritytemp[0]+"==VALUE(CrimeSeverity for a perticular News) =>"
										+Integer.parseInt(cityCrimeSeveritytemp[1]));
							context.write(new Text(cityCrimeSeveritytemp[0]), new IntWritable(Integer.parseInt(cityCrimeSeveritytemp[1])));
						}
					}
				}
			}
		}
		System.out.println("============== Crime Crawler : Map => END ==================");
	}
}
