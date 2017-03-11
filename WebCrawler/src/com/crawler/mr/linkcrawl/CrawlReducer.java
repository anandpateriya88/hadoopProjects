package com.crawler.mr.linkcrawl;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.crawler.services.Spider;

public class CrawlReducer extends Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		System.out.println("position Combiner 1");

		for (Text iw : values) {
			System.out.println("position Combiner 2 ==>>"+iw);
			System.out.println("<<========================>>");
			System.out.println("<<========================>>");
			String newLineSeperatedLstOfUrls = "Initialized\\nNoLinkAdded";
			String inputNewLineSeperated = iw.toString();
			String[] commaSeperatedArray = inputNewLineSeperated.split("\n");
			System.out.println("Combiner --> No of urls ->"+ commaSeperatedArray.length);
			for (String url : commaSeperatedArray) {
				if (!url.endsWith("#VISITED")) {
					Spider spider = new Spider();
					System.out.println("position Combiner 1");
					newLineSeperatedLstOfUrls = spider.getAllUrls(url);
					context.write(new Text(""), new Text(newLineSeperatedLstOfUrls));
					System.out.println("Combiner : urls crawled for --> "+url);
				} else {
					// directly write the values to context.
					context.write(new Text(""), iw);
				}
			}
		}
	}
}
