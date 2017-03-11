package com.crawler.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.arch.domainobjects.PropertyDetails;

public class Spider {

	private static final int MAX_PAGES_TO_SEARCH = 1;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();

	private String nextUrl() {
		String nextUrl;
		do {
			nextUrl = this.pagesToVisit.remove(0);
		} while (this.pagesVisited.contains(nextUrl));
		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}

	public List<PropertyDetails> getAllPropertiesDtls(String url, int depthStart, int depthEnd) {
		
		SpiderLeg leg = new SpiderLeg();
		List<PropertyDetails> tempPropdtls = leg.getPropertyDetails(url, depthStart, depthEnd);
		
		System.out.println(String.format("== Properties List Creation END == Properties Found --->"+ tempPropdtls.size()));
		return tempPropdtls;
	}
	
	
	public List<String> getAllCrimeRatingDtls(String url, int depthStart, int depthEnd){
		SpiderLeg leg = new SpiderLeg();
		List<String> tempCrimedtls = leg.getCrimeRatingDtls(url, depthStart, depthEnd);
		
		System.out.println(String.format("== Properties List Creation END == Properties Found --->"+ tempCrimedtls.size()));
		return tempCrimedtls;
	}
	public String getAllUrls(String url) {
		String commaSeperatedLstOfUrls="";
		while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
			System.out.println("position 1");
			String currentUrl;
			SpiderLeg leg = new SpiderLeg();
			if (this.pagesToVisit.isEmpty()) {
				currentUrl = url;
				this.pagesVisited.add(url);
			} else {
				currentUrl = this.nextUrl();
			}
			System.out.println("position 2");
			leg.crawl(currentUrl);
			System.out.println("position 3");
			this.pagesToVisit.addAll(leg.getLinks());
			System.out.println("position 7");
		}
		System.out.println("Combiner : URLS added - "+this.pagesToVisit.size());
		commaSeperatedLstOfUrls = StringUtils.join(new LinkedHashSet<String>(this.pagesToVisit), "\n");
		return commaSeperatedLstOfUrls;
	}
}
