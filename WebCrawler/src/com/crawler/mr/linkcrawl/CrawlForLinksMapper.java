package com.crawler.mr.linkcrawl;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.crawler.services.Spider;
import com.crawler.services.SpiderLeg;

public class CrawlForLinksMapper extends Mapper<Object, Text, Text, Text> {

	private static final int MAX_PAGES_TO_SEARCH = 30;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String url = value.toString().trim();
		Spider spider = new Spider();
		String newLineSeperatedLstOfUrls = "Initialized\\nNoLinkAdded";
		System.out.println("position Mapper 1");

		while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
			System.out.println("position 1");
			String currentUrl;
			SpiderLeg leg = new SpiderLeg();
			if (this.pagesToVisit.isEmpty()) {
				currentUrl = url;
				this.pagesVisited.add(url+"#VISITED");
			} else {
				currentUrl = this.nextUrl();
			}
			System.out.println("position 2");
			leg.crawl(currentUrl);
			System.out.println("position 3");
			this.pagesToVisit.addAll(leg.getLinks());
			System.out.println("position 7");
			if(this.pagesToVisit.size() > 30){
				newLineSeperatedLstOfUrls  = StringUtils.join(new LinkedHashSet<String>(leg.getLinks()), "\n");
				System.out.println("All properties list creation Done . No of Urls added -"
						+ newLineSeperatedLstOfUrls .split(",").length);
				context.write(new Text(url), new Text(newLineSeperatedLstOfUrls));
			}
		}
		newLineSeperatedLstOfUrls = StringUtils.join(new LinkedHashSet<String>(this.pagesVisited), ",");
		context.write(new Text(url), new Text(newLineSeperatedLstOfUrls));
		// newLineSeperatedLstOfUrls = spider.getAllUrls(url);
		// List<PropertyDetails> allPropoertiesLst =
		// spider.getAllPropertiesDtls(url);
		
		// --Only one input will be there and need to collect all the urls
		// related to that input.
		// -- keyOp = input
		// ---keyOp = List<String>
	}

	private String nextUrl() {
		String nextUrl;
		do {
			nextUrl = this.pagesToVisit.remove(0);
		} while (this.pagesVisited.contains(nextUrl));
		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}
}
