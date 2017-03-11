package com.crawler.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.arch.domainobjects.City;
import com.arch.domainobjects.Country;
import com.arch.domainobjects.KeywordDictionaryCrime;
import com.arch.domainobjects.PropertyDetails;
import com.arch.domainobjects.State;
import com.google.gson.Gson;

public class SpiderLeg {

	private List<String> links = new LinkedList<String>(); // Just a list of
															// URLs
	private Document htmlDocument; // This is our web page, or in other words,
	private Integer depth;
	private String regExp_Digits = "[\\d,-/]+";
	private Country countryStateCity;
	private KeywordDictionaryCrime crimeWordsDictionary = new KeywordDictionaryCrime();
									// our document
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36";
	//private static final String USER_AGENT = "Googlebot/2.1 (+http://www.googlebot.com/bot.html)";
	// private static final String USER_AGENT =
	// "Googlebot/2.1 (+http://www.googlebot.com/bot.html)";

	// Give it a URL and it makes an HTTP request for a web page
	public void crawl(String url) {
		try {
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			connection.timeout(500000);// 500 seconds
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;
			System.out.println("Received web page at " + url);
			Elements linksOnPage = htmlDocument.select("a[href]");
			System.out.println("Found (" + linksOnPage.size() + ") links");
			for (Element link : linksOnPage) {
				this.links.add(link.absUrl("href"));
			}
			System.out.println("position 8");
		} catch (IOException ioe) {
			// We were not successful in our HTTP request
			System.out.println("Error in out HTTP request " + ioe);
		}
	}

	// Tries to find a word on the page
	public List<PropertyDetails> getPropertyDetails(String url, int depthStart, int depthEnd) {

		System.out.println("===============SpiderLeg : getPropertyDetails STARTED=============");
		List<PropertyDetails> propertyDetailsLst = new ArrayList<PropertyDetails>();
		try {
			
			String rentOrSale = "";
			if(url.toUpperCase().contains("SALE")){
				rentOrSale = "Sale";
			}else if(url.toUpperCase().contains("RENT")){
				rentOrSale = "Rent";
			}
			if (url.contains("trovit")) {
				propertyDetailsLst = getDetailsFromTrovit(rentOrSale,url, depthStart, depthEnd);
			} else if (url.contains("thinkproperty")) {
				propertyDetailsLst = getDetailsFromThinkProperty(rentOrSale, url, depthStart, depthEnd);
			} else if (url.contains("propwall")) {
				propertyDetailsLst = getDetailsFromPropwall(rentOrSale, url, depthStart, depthEnd);
			} else if (url.contains("propertyguru")) {
				propertyDetailsLst = getDetailsFromPropertyGuru(rentOrSale, url, depthStart, depthEnd);
			} else if (url.contains("mitula")) {
				if(url.toUpperCase().contains("/APARTMENT/")){
					rentOrSale = "Sale";
				}
				propertyDetailsLst = getDetailsFromMitula(rentOrSale, url, depthStart, depthEnd);
			}

			System.out.println("Properties Found on page < " + this.htmlDocument.baseUri() + " >"
					+ propertyDetailsLst.size());
		} catch (Exception e) {
			System.out.println("SpiderLeg : getPropertyDetails Exception Occured - " + e);
		}
		System.out.println("===============SpiderLeg : getPropertyDetails END=============");
		return propertyDetailsLst;
	}

	public List<PropertyDetails> getDetailsFromTrovit(String rentOrSale, String url, int depthStart, int depthEnd) {
		System.out.println("============SpiderLeg : getDetailsFromTrovit STARTED============");
		List<PropertyDetails> propertyDetailsLst = new ArrayList<PropertyDetails>();
		Integer mandatoryParamsCount = 0;
		Integer depthTemp; 
		for(depthTemp = depthStart; depthTemp <= depthEnd; depthTemp++){
		try{
			String pagesOnUrl = url.replace("#", depthTemp.toString());
			Connection connection = Jsoup.connect(pagesOnUrl).userAgent(USER_AGENT);
			connection.timeout(10000);// 10 seconds
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;
			Element elementImp = this.htmlDocument.body().getElementById("wrapper_listing");
			if (null != elementImp) {
				Elements itemInfoElems = elementImp.getElementsByAttributeValue("class", "listing item js-item");
				if (null != itemInfoElems && !itemInfoElems.isEmpty()) {
					for (Element temp : itemInfoElems) {
						PropertyDetails propDetailsTemp = new PropertyDetails();
						Elements itemTempAll = null;

						propDetailsTemp.setRentOrSale(rentOrSale);
						// -- getting property Name
						itemTempAll = temp.getElementsByTag("a");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setName(tempElement.text());
						}
						// -- getting property Desc
						itemTempAll = temp.getElementsByAttributeValue("itemprop", "description");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setDescription(tempElement.text());
						}

						itemTempAll = temp.getElementsByAttributeValue("class", "room");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setRooms(tempElement.text());
						}

						itemTempAll = temp.getElementsByAttributeValue("class", "price");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							if(null != tempElement.text()){
							//propDetailsTemp.setRentalPrice(tempElement.text());
							String price = tempElement.text().replaceAll("[,\\D]+","");
							if(!price.isEmpty()){
								mandatoryParamsCount++;
								if("RENT".equals(rentOrSale.toUpperCase())){
									propDetailsTemp.setRentalPrice(price);
								}else if("SALE".equals(rentOrSale.toUpperCase()) || "BUY".equals(rentOrSale.toUpperCase())){
									propDetailsTemp.setSalePrice(price);
								}
							}
							}
						}

						itemTempAll = temp.getElementsByAttributeValue("class", "no-price");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							propDetailsTemp.setRentalPrice("On Request");
						}

						itemTempAll = temp.getElementsByAttributeValue("class", "floorArea");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							String floorArea = tempElement.text();
							if(null != floorArea && !floorArea.isEmpty()){
								String floorAreaDigits = floorArea.replaceAll("[,\\D]+","");
								if(!floorAreaDigits.isEmpty()){
									mandatoryParamsCount++;
									propDetailsTemp.setFloorArea(floorArea);
								}
							}
						}

						itemTempAll = temp.getElementsByAttributeValue("class", "bath");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setBathrooms(tempElement.text());
						}

						itemTempAll = temp.getElementsByTag("h5");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setAddress(tempElement.text());
						}
						itemTempAll = temp.getElementsByAttributeValue("class", "date");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							tempElement.getClass();
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						    Date date = new Date();
						    Calendar postedDateCal = Calendar.getInstance();
						    postedDateCal.setTime(date);
						    postedDateCal.add(Calendar.DATE, -depthTemp/10); 
							propDetailsTemp.setPostedDate(dateFormat.format(postedDateCal.getTime()));
						}

						if(mandatoryParamsCount >= 2){
							propertyDetailsLst.add(propDetailsTemp);
						}
						//-- Using temp find the div containing image and then find the url of the image.
						/*String name = getName(propDetailsTemp);
						itemTempAll = temp.getElementsByTag("img");
						if(null != itemTempAll && !itemTempAll.isEmpty()){
							String absUrl = itemTempAll.get(0).absUrl("src");
							if(null != absUrl && !absUrl.isEmpty())
							downloadPhotos(absUrl, name);
						}*/
					}
					System.out.println("SpiderLeg: getDetailsFromThinkProperty : Min Count - "+depthStart+", Max Count - "+depthEnd+", Current Count - "+depthTemp);
					System.out.println("SpiderLeg: getDetailsFromTrovit : URL -"+pagesOnUrl);
					System.out.println("SpiderLeg: getDetailsFromTrovit : Advertisement List Size - "+propertyDetailsLst.size());
				}
			}
		}catch(Exception e){
			System.out.println("An Exception occured while getting response from url --->"+url+"===>"+e);
		}
		}
		System.out.println("SpiderLeg : getDetailsFromTrovit END ");
		System.out.println("============SpiderLeg : getDetailsFromTrovit Property List size =>"+propertyDetailsLst.size());
		return propertyDetailsLst;
	}

	public List<PropertyDetails> getDetailsFromThinkProperty(String rentOrSale, String url, int depthStart, int depthEnd) {
		System.out.println("=====================SpiderLeg : getDetailsFromThinkProperty START ===============");
		List<PropertyDetails> propertyDetailsLst = new ArrayList<PropertyDetails>();
		Integer mandatoryParamsCount = 0;
		Integer depthTemp; 

		for(depthTemp = depthStart; depthTemp <= depthEnd; depthTemp++){
		try{
			String pagesOnUrl = url.replace("#", depthTemp.toString());
			Connection connection = Jsoup.connect(pagesOnUrl).userAgent(USER_AGENT);
			connection.timeout(30000);// 30 seconds
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;
			Element elementImp = this.htmlDocument.body().getElementById("search_listings");

		if (null != elementImp) {
				Elements itemInfoElems = elementImp.getElementsByAttributeValue("class", "premium_listing");
				if(null == itemInfoElems || (null != itemInfoElems && itemInfoElems.isEmpty())){
					itemInfoElems = elementImp.getElementsByAttributeValue("class", "normal_listing");
				}
				
				if (null != itemInfoElems && !itemInfoElems.isEmpty()) {
					for (Element temp : itemInfoElems) {
						PropertyDetails propDetailsTemp = new PropertyDetails();
						Elements itemTempAll = null;

						propDetailsTemp.setRentOrSale(rentOrSale);
						// -- getting property Name
						itemTempAll = temp.getElementsByTag("a");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							String address = tempElement.attr("title");
							propDetailsTemp.setName(tempElement.text());
							propDetailsTemp.setAddress(address);
						}
						// -- getting property Desc
						itemTempAll = temp.getElementsByAttributeValue("class", "details");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setDescription(tempElement.text());
							String details = tempElement.text();
							if(details != null && !details.isEmpty()){
								String detailsArray[] = details.split(":"); 
									if(null != detailsArray){
									if(detailsArray.length >2){
										String postedDateWithAgent = detailsArray[2];
										propDetailsTemp.setPostedDate(postedDateWithAgent.substring(1, 12));
									}
									if(detailsArray.length >1){
										String floorArea = detailsArray[1].trim().replaceAll("[,\\D]+","");
										if(null != floorArea && !floorArea.isEmpty()){
											mandatoryParamsCount++;
											propDetailsTemp.setFloorArea(floorArea);	
										}
									}
								}
								
							}
						}

						//-- Optimizing performance by removing unused items.
						/*itemTempAll = temp.getElementsByAttributeValue("class", "prop-icon divbedrooms");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setRooms(tempElement.text());
						}*/

						itemTempAll = temp.getElementsByAttributeValue("class", "price");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							if(null != tempElement.text()){
								String price = tempElement.text().replaceAll("[,\\D]+","");
								if(!price.isEmpty()){
									mandatoryParamsCount++;
									if("RENT".equals(rentOrSale.toUpperCase())){
										propDetailsTemp.setRentalPrice(price);
									}else if("SALE".equals(rentOrSale.toUpperCase()) || "BUY".equals(rentOrSale.toUpperCase())){
										propDetailsTemp.setSalePrice(price);
									}
								}
							}
						}

						//-- Optimizing performance by removing unused items.
						/*itemTempAll = temp.getElementsByAttributeValue("class", "prop-icon divbathrooms");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setBathrooms(tempElement.text());
						}*/
						
						
						
						if(mandatoryParamsCount >= 2){
							propertyDetailsLst.add(propDetailsTemp);
						}

						//-- Using temp find the div containing image and then find the url of the image.
						/*String name = getName(propDetailsTemp);
						itemTempAll = temp.getElementsByAttributeValue("class", "photos");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							Elements imageElement = tempElement.getElementsByTag("img");
							if(null != imageElement && !imageElement.isEmpty()){
								String absUrl = imageElement.get(0).attr("src");
								if(null != absUrl && !absUrl.isEmpty())
								downloadPhotos(absUrl, name);
							}
						}*/
					}
					System.out.println("SpiderLeg: getDetailsFromThinkProperty : Min Count - "+depthStart+", Max Count - "+depthEnd+", Current Count - "+depthTemp);
					System.out.println("SpiderLeg: getDetailsFromThinkProperty : URL - "+pagesOnUrl);
					System.out.println("SpiderLeg: getDetailsFromThinkProperty : Advertisement List Size - "+propertyDetailsLst.size());
				}				
		}
		}catch(Exception e){
			System.out.println("An Exception occured while getting response from url --->"+url+"===>"+e);
		}
		}
		System.out.println("SpiderLeg : getDetailsFromThinkProperty || END || Property List size || "+propertyDetailsLst.size()+ " ||");
		return propertyDetailsLst;
	}
	
	
	public List<PropertyDetails> getDetailsFromPropertyGuru(String rentOrSale, String url, int depthStart, int depthEnd) {
		System.out.println("=====================SpiderLeg : getDetailsFromPropertyGuru START ===============");
		List<PropertyDetails> propertyDetailsLst = new ArrayList<PropertyDetails>();
		Integer mandatoryParamsCount = 0;		
		Integer depthTemp; 
		for(depthTemp = depthStart; depthTemp <= depthEnd; depthTemp++){
		try{
			String pagesOnUrl = url.replace("#", depthTemp.toString());
			Connection connection = Jsoup.connect(pagesOnUrl).userAgent(USER_AGENT);
			connection.timeout(10000);// 10 seconds
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;
		Elements elementImp = this.htmlDocument.body().getElementsByAttributeValue("class", "main-content");
		if (null != elementImp.get(0)) {
			Elements itemInfoElems = elementImp.get(0).getElementsByAttributeValue("class", "listing-item featured-listing");
			Elements infoElements2 = elementImp.get(0).getElementsByAttributeValue("class", "listing-item");
			itemInfoElems.addAll(infoElements2);
				
				if (null != itemInfoElems && !itemInfoElems.isEmpty()) {
					for (Element temp : itemInfoElems) {
						PropertyDetails propDetailsTemp = new PropertyDetails();
						Elements itemTempAll = null;

						propDetailsTemp.setRentOrSale(rentOrSale);
						// -- getting property Address
						itemTempAll = temp.getElementsByAttributeValue("itemprop", "streetAddress");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setAddress(tempElement.text());
						}
						// -- getting property Name
						itemTempAll = temp.getElementsByAttributeValue("itemprop", "name");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setName(tempElement.text());
						}

						itemTempAll = temp.getElementsByAttributeValue("class", "lst-rooms");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setRooms(tempElement.text());
						}

						itemTempAll = temp.getElementsByAttributeValue("class", "listing-price");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							if(null != tempElement.text()){
								String price = tempElement.text().replaceAll("[,\\D]+","");
								if(!price.isEmpty()){
									mandatoryParamsCount++;
									if("RENT".equals(rentOrSale.toUpperCase())){
										propDetailsTemp.setRentalPrice(price);
									}else if("SALE".equals(rentOrSale.toUpperCase()) || "BUY".equals(rentOrSale.toUpperCase())){
										propDetailsTemp.setSalePrice(price);
									}
								}
							}
						}

						itemTempAll = temp.getElementsByAttributeValue("class", "mid-sep");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setBathrooms(tempElement.text());
						}
						
						
						itemTempAll = temp.getElementsByAttributeValue("class", "listing-marketed");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							tempElement.getClass();
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						    Date date = new Date();
						    Calendar postedDateCal = Calendar.getInstance();
						    postedDateCal.setTime(date);
						    postedDateCal.add(Calendar.DATE, -depthTemp/14); 
							propDetailsTemp.setPostedDate(dateFormat.format(postedDateCal.getTime()));
						}
						
						itemTempAll = temp.getElementsByAttributeValue("class", "lst-sizes");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							String floorArea = tempElement.text();
							if(null != floorArea && !floorArea.isEmpty()){
								String floorAreaDigits = floorArea.replaceAll("[,\\D]+","");
								if(!floorAreaDigits.isEmpty()){
									mandatoryParamsCount++;
									propDetailsTemp.setFloorArea(floorAreaDigits);
								}
							}
						}
						// -- getting property Desc
						itemTempAll = temp.getElementsByAttributeValue("class", "lst-details");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setDescription(tempElement.text());
						}
						
						if(mandatoryParamsCount >= 2){
							propertyDetailsLst.add(propDetailsTemp);
						}					

						//-- Using temp find the div containing image and then find the url of the image.
						/*String name = getName(propDetailsTemp);
						itemTempAll = temp.getElementsByTag("img");
						if(null != itemTempAll && !itemTempAll.isEmpty()){
							String absUrl = itemTempAll.get(0).attr("data-original");
							if(null != absUrl && !absUrl.isEmpty())
							downloadPhotos(absUrl, name);
						}*/
					}
					System.out.println("SpiderLeg: getDetailsFromPropertyGuru : Min Count - "+depthStart+", Max Count - "+depthEnd+", Current Count - "+depthTemp);
					System.out.println("SpiderLeg: getDetailsFromPropertyGuru : URL -"+pagesOnUrl);
					System.out.println("SpiderLeg: getDetailsFromPropertyGuru : Advertisement List Size - "+propertyDetailsLst.size());
					
				}
				
		}
		}catch(Exception e){
			System.out.println("An Exception occured while getting response from url --->"+url+"===>"+e);
		}
	    }
		System.out.println("SpiderLeg : getDetailsFromPropertyGuru Property List size =>"+propertyDetailsLst.size());
		System.out.println("=====================SpiderLeg : getDetailsFromPropertyGuru END ===============");
		return propertyDetailsLst;
	}
	
	
	
	public List<PropertyDetails> getDetailsFromPropwall(String rentOrSale, String url, int depthStart, int depthEnd) {
		System.out.println("=====================SpiderLeg : getDetailsFromPropwall START ===============");
		List<PropertyDetails> propertyDetailsLst = new ArrayList<PropertyDetails>();
		Integer mandatoryParamsCount = 0;
		Integer depthTemp; 
		for(depthTemp = depthStart; depthTemp <= depthEnd; depthTemp++){
		try{
			String pagesOnUrl = url.replace("#", depthTemp.toString());
			Connection connection = Jsoup.connect(pagesOnUrl).userAgent(USER_AGENT);
			connection.timeout(10000);// 10 seconds
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;
		Element elementImp = this.htmlDocument.body().getElementById("list-content");
		if (null != elementImp) {
			Elements itemInfoElems = elementImp.getElementsByAttributeValue("class", "media");
				
				if (null != itemInfoElems && !itemInfoElems.isEmpty()) {
					for (Element temp : itemInfoElems) {
						PropertyDetails propDetailsTemp = new PropertyDetails();
						Elements itemTempAll = null;

						propDetailsTemp.setRentOrSale(rentOrSale);
						
						// -- getting property Name
						itemTempAll = temp.getElementsByTag("a");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setName(tempElement.text());
							propDetailsTemp.setAddress(tempElement.text());
						}

						itemTempAll = temp.getElementsByAttributeValue("class", "pull-right clearfix");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setRooms(tempElement.text());
						}

						itemTempAll = temp.getElementsByAttributeValue("class", "listing-price");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							if(null != tempElement.text()){
								String price = tempElement.text().replaceAll("[,\\D]+","");
								if(!price.isEmpty()){
									mandatoryParamsCount++;
									if("RENT".equals(rentOrSale.toUpperCase())){
										propDetailsTemp.setRentalPrice(price);
									}else if("SALE".equals(rentOrSale.toUpperCase()) || "BUY".equals(rentOrSale.toUpperCase())){
										propDetailsTemp.setSalePrice(price);
									}
								}
							}
						}

						// -- getting property Desc
						itemTempAll = temp.getElementsByTag("p");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							
							if(itemTempAll.size() > 0){
								Element tempElement = itemTempAll.get(0);
								String[] tempDetailsDate = tempElement.text().split(" ");
								propDetailsTemp.setPostedDate(tempDetailsDate[tempDetailsDate.length-1]);
							}
							
							
							if(itemTempAll.size() > 2){
								Element tempElement = itemTempAll.get(2);
								String[] tempDetailsArea = tempElement.text().split("#");
								propDetailsTemp.setDescription(tempDetailsArea[0]);
								
								String floorArea = tempDetailsArea[1];
								if(null != floorArea && !floorArea.isEmpty()){
									String floorAreaDigit = floorArea.trim().replaceAll("[,\\D]+","");
									if(!floorAreaDigit.isEmpty()){
										mandatoryParamsCount++;
										propDetailsTemp.setFloorArea(floorAreaDigit);
									}
								}
							}
							if(itemTempAll.size() > 3){
								Element tempElement = itemTempAll.get(3);
								if(null != tempElement.text()){
									//propDetailsTemp.setRentalPrice(tempElement.text());
									if("RENT".equals(rentOrSale.toUpperCase())){
										propDetailsTemp.setRentalPrice(tempElement.text().replaceAll("[,\\D]+",""));
									}else if("SALE".equals(rentOrSale.toUpperCase()) || "BUY".equals(rentOrSale.toUpperCase())){
										propDetailsTemp.setSalePrice(tempElement.text().replaceAll("[,\\D]+",""));
									}
								}
							}
							
						}
				
						if(mandatoryParamsCount >= 2){
							propertyDetailsLst.add(propDetailsTemp);
						}	
						
						//-- Using temp find the div containing image and then find the url of the image.
						/*String name = getName(propDetailsTemp);
						itemTempAll = temp.getElementsByTag("img");
						if(null != itemTempAll && !itemTempAll.isEmpty()){
							String absUrl = itemTempAll.get(0).attr("src");
							if(null != absUrl && !absUrl.isEmpty())
							downloadPhotos(absUrl, name);
						}*/
					}
					System.out.println("SpiderLeg: getDetailsFromPropwall : Min Count - "+depthStart+", Max Count - "+depthEnd+", Current Count - "+depthTemp);
					System.out.println("SpiderLeg: getDetailsFromPropwall : URL -"+pagesOnUrl);
					System.out.println("SpiderLeg: getDetailsFromPropwall : Advertisement List Size - "+propertyDetailsLst.size());
				}
				
		  }
		}catch(Exception e){
			System.out.println("An Exception occured while getting response from url --->"+url+"===>"+e);
		}
		}
		System.out.println("SpiderLeg : getDetailsFromPropwall Property List size =>"+propertyDetailsLst.size());
		System.out.println("=====================SpiderLeg : getDetailsFromPropwall END ===============");
		return propertyDetailsLst;
	}
	
	public List<PropertyDetails> getDetailsFromMitula(String rentOrSale, String url, int depthStart, int depthEnd) {
		System.out.println("=====================SpiderLeg : getDetailsFromMitula START ===============");
		List<PropertyDetails> propertyDetailsLst = new ArrayList<PropertyDetails>();
		Integer mandatoryParamsCount = 0;
		Integer depthTemp; 
		for(depthTemp = depthStart; depthTemp <= depthEnd; depthTemp++){
		try{
			String pagesOnUrl = url.replace("#", depthTemp.toString());
			Connection connection = Jsoup.connect(pagesOnUrl).userAgent(USER_AGENT);
			connection.timeout(10000);// 10 seconds
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;
			Elements elementImp = this.htmlDocument.body().getElementsByAttributeValue("class", "main");
			if (null != elementImp.get(0)) {
			Elements itemInfoElems = elementImp.get(0).getElementsByAttributeValue("class", "listing listing_ad");
				
				if (null != itemInfoElems && !itemInfoElems.isEmpty()) {
					for (Element temp : itemInfoElems) {
						PropertyDetails propDetailsTemp = new PropertyDetails();
						Elements itemTempAll = null;

						propDetailsTemp.setRentOrSale(rentOrSale);
						
						// -- getting property Name
						itemTempAll = temp.getElementsByAttributeValue("itemprop", "name");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setName(tempElement.text());
						}

						itemTempAll = temp.getElementsByAttributeValue("class", "subtitle");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							String city = tempElement.getElementsByTag("h5").text();
							propDetailsTemp.setCity(city);
							propDetailsTemp.setAddress(city);
							
							String floorRoomsBathRooms = tempElement.getElementsByTag("em").text();
							if(null != floorRoomsBathRooms && floorRoomsBathRooms != ""){
								String floorArea = floorRoomsBathRooms.substring(0, 12);
								if(null != floorArea && !floorArea.isEmpty()){
									String floorAreaDigit = floorArea.replaceAll("[,\\D]+","");
									if(!floorAreaDigit.isEmpty()){
										mandatoryParamsCount++;
										propDetailsTemp.setFloorArea(floorAreaDigit);
									}
								}
							}
						}

						itemTempAll = temp.getElementsByAttributeValue("class", "date_client");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							tempElement.getClass();
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						    Date date = new Date();
						    Calendar postedDateCal = Calendar.getInstance();
						    postedDateCal.setTime(date);
						    postedDateCal.add(Calendar.DATE, -depthTemp/10); 
							propDetailsTemp.setPostedDate(dateFormat.format(postedDateCal.getTime()));
						}
						
						itemTempAll = temp.getElementsByTag("a");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(1);
							if(null != tempElement.text()){
								String price = tempElement.text().replaceAll("[,\\D]+","");
								if(!price.isEmpty()){
									mandatoryParamsCount++;
									if("RENT".equals(rentOrSale.toUpperCase())){
										propDetailsTemp.setRentalPrice(price);
									}else if("SALE".equals(rentOrSale.toUpperCase()) || "BUY".equals(rentOrSale.toUpperCase())){
										propDetailsTemp.setSalePrice(price);
									}
								}
							}
						}
						// -- getting property Name
						itemTempAll = temp.getElementsByAttributeValue("itemprop", "description");
						if (null != itemTempAll && !itemTempAll.isEmpty()) {
							Element tempElement = itemTempAll.get(0);
							propDetailsTemp.setDescription(tempElement.text());
						}

						if(mandatoryParamsCount >= 2){
							propertyDetailsLst.add(propDetailsTemp);
						}
						//-- Using temp find the div containing image and then find the url of the image.
						/*String name = getName(propDetailsTemp);
						itemTempAll = temp.getElementsByTag("img");
						if(null != itemTempAll && !itemTempAll.isEmpty()){
							String absUrl = itemTempAll.get(0).attr("src");
							if(null != absUrl && !absUrl.isEmpty())
							downloadPhotos(absUrl, name);
						}*/
					}
					System.out.println("SpiderLeg: getPropertyDetailsFromMitula : Min Count - "+depthStart+", Max Count - "+depthEnd+", Current Count - "+depthTemp);
					System.out.println("SpiderLeg: getPropertyDetailsFromMitula : URL -"+pagesOnUrl);
					System.out.println("SpiderLeg: getPropertyDetailsFromMitula : Advertisement List Size - "+propertyDetailsLst.size());
				}
				
		  }
		}catch(Exception e){
			System.out.println("An Exception occured while getting response from url --->"+url+"===>"+e);
		}
		}
		System.out.println("SpiderLeg : getDetailsFromMitula Property List size =>"+propertyDetailsLst.size());
		System.out.println("=====================SpiderLeg : getDetailsFromMitula END ===============");
	return propertyDetailsLst;
	}
	
	// Returns a list of all the URLs on the page
	public List<String> getLinks() {
		return this.links;
	}


	public String testsiteForData(String url){
		Elements elementImp = null;
		String element = "NOT FOUND";
		String elementText = "NOT FOUND";
		try{
		Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
		connection.timeout(10000);// 10 seconds
		Document htmlDocument = connection.get();
		this.htmlDocument = htmlDocument;
		/*elementImp = this.htmlDocument.body().getElementsByAttributeValue("class", "main-content");
		element = elementImp.get(0).toString();
		System.out.println("element Found - "+ elementImp.get(0).toString());*/
		
		Element elementTest = this.htmlDocument.body().getElementById("search_listings");
		Elements roomElement = elementTest.getElementsByAttributeValue("class", "details");
		//temp.getElementsByAttributeValue("class", "details");
		elementText = roomElement.get(0).text();
		}catch(Exception e){
			
		}
		//return element;
		return elementText;
	}

	public String downloadPhotos(String downloadUrl, String imageName){
		String status="";
		// Image img = new Image();
				URL imgUrl = null;
				try {
					if(!downloadUrl.isEmpty()){
						imgUrl = new URL(downloadUrl);
						//File imgLocation = new File("G:\\Hadoop_2015\\Crawler_images\\"+imageName+".jpg");
						/*	File directoryForImages = new File("/home/hadoop/propertyImages");
					if(!directoryForImages.exists()){
						if(directoryForImages.mkdir()){
							File imgLocation = new File("/home/hadoop/propertyImages/"+imageName+".jpg");
							FileUtils.copyURLToFile(imgUrl, imgLocation);
						}else{
							File imgLocation = new File("/home/hadoop/"+imageName+".jpg");
							FileUtils.copyURLToFile(imgUrl, imgLocation);
						}
					}*/
						File imgLocation = new File("/home/hadoop/propertyImages/"+imageName+".jpg");
						FileUtils.copyURLToFile(imgUrl, imgLocation);
					}
					
				} catch (Exception e) {
					System.out.println("---Exception Occured While downloading image. Image URl ==>" + imgUrl);
					System.out.println("---Exception Occured  ==>" + e);
				}
		
		return status;
	}
	
	public String getName(PropertyDetails propObj) {
		String completeName = "";
		if(null != propObj.getName() && !propObj.getName().isEmpty()){
			completeName = propObj.getName().replaceAll("[\\d][\\d][\\d]+","");
		}
		String nameArray[] = completeName.split(",");
		String projectName = null;
		if (null != nameArray && nameArray.length > 0 && !nameArray[0].isEmpty()) {
			projectName = nameArray[0].replaceAll(regExp_Digits, "").toUpperCase();
		}
		
		return projectName;
	}
	
	
	//===== CRAWLING CRIME DETAILS SECTION START===========
	
	
	public List<String> getCrimeRatingDtls(String url, int depthStart, int depthEnd) {	
		List<String> crimeDtlsLst = new ArrayList<String>();
		setCountry();
		KeywordDictionaryCrime.populateCrimeKeywordMapAndList();
		if (url.contains("thesundaily")) {
			crimeDtlsLst = getDetailsFromTheSunDaily(url, depthStart, depthEnd);
		}
		
		return crimeDtlsLst;
	}
	
	public List<String> getDetailsFromTheSunDaily(String url, int depthStart, int depthEnd) {	
		List<String> cityCrimeSeverityLst = new ArrayList<String>();
		String pagesOnUrl = "";
		Integer depthTemp; 
		for(depthTemp = depthStart; depthTemp <= depthEnd; depthTemp++){
			try{
				pagesOnUrl = url.replace("#", depthTemp.toString());
				Connection connection = Jsoup.connect(pagesOnUrl).userAgent(USER_AGENT);
				connection.timeout(2*1000*60);// 2 mins
				Document htmlDocument = connection.get();
				this.htmlDocument = htmlDocument;
				
				Elements elementImp = this.htmlDocument.body().getElementsByAttributeValue("class", "view-content");
				if (null != elementImp.get(0)) {
					Elements itemInfoElems = elementImp.get(0).getElementsByAttributeValueContaining("class","views-row");
						
						if (null != itemInfoElems && !itemInfoElems.isEmpty()) {
							for (Element temp : itemInfoElems) {
								Elements itemTempAll = null;
								String city="";
								String newsHeading = "";
								itemTempAll = temp.getElementsByAttributeValue("class", "node-title");
								if (null != itemTempAll && !itemTempAll.isEmpty()) {
									Element tempElement = itemTempAll.get(0);
									//-- Got news heading.
									newsHeading = tempElement.text();
								}
								
								//-- Get City from the NEws
								itemTempAll = temp.getElementsByAttributeValue("class", "content");
								
								if (null != itemTempAll && !itemTempAll.isEmpty()) {
									Element tempElement = itemTempAll.get(0);
									//-- Got news content.
									String newsContent = tempElement.text();

									//--->News available are from the world so need to check if the news is from malaysia by checking the 
									// city written in the begining of each news.
									//--- if city is a city of Malaysia then call method for finding severity of crime.
									Elements cityRaw = tempElement.getElementsByTag("strong");
									if(null != cityRaw && !cityRaw.isEmpty() && !cityRaw.get(0).text().isEmpty()){
										String cityRefined = cityRaw.get(0).text().replaceAll(":", "").trim();
										if(checkMalaysianCity(cityRefined)){
											// -- Calling method for finding Crime severity (from 1 to 10 (Most Severe))
											// -- Creating city#crimeSeverity list to be used as Mapper Key value pair.
											cityCrimeSeverityLst.add(cityRefined
													+"#"+calculateCrimeSeverity(newsHeading+" "+newsContent));
										}
									
									}
									
					
								}
								
							}
						}
				}
				
				

			}catch(Exception e){
				System.out.println("---Exception Occured While getting News from TheSunDaily ==>" + pagesOnUrl);
				System.out.println("---Exception  ==>");
				e.printStackTrace();
			}
		}
		return cityCrimeSeverityLst;
	}

	public Boolean checkMalaysianCity(String cityFromNews){
		City cityFromNewsObj = new City(cityFromNews);
		if(null != cityFromNews && !cityFromNews.isEmpty()){
			List<State> states = this.countryStateCity.getStates();
			for(State stateTemp: states){
				List<City> cities = stateTemp.getCities();
				if(cities.contains(cityFromNewsObj)){
					return true;
				}
			}
			}
		return false;
	}
	
	public void setCountry(){
		System.out.println("CrimeDataMapper : setCountry STARTED");
		String countryObj = "{'states':[{'id':'KL','name':'Kuala Lumpur','cities':[{'city':'Ampang'},{'city':'Kuala Lumpur'},{'city':'Ampang Hilir'},{'city':'Bandar Damai Perdana'},{'city':'Bandar Menjalara'},{'city':'Bandar Sri Damansara'},{'city':'Bandar Tasik Selatan'},{'city':'Bangsar'},{'city':'Bangsar South'},{'city':'Batu'},{'city':'Brickfields'},{'city':'Bukit Bintang'},{'city':'Bukit Jalil'},{'city':'Bukit Ledang'},{'city':'Bukit Tunku'},{'city':'Chan Sow Lin'},{'city':'Cheras'},{'city':'city Centre'},{'city':'Country Heights Damansara'},{'city':'Damansara'},{'city':'Damansara Heights'},{'city':'Desa Pandan'},{'city':'Desa Park city'},{'city':'Desa Petaling'},{'city':'Federal Hill'},{'city':'Gombak'},{'city':'Jalan Ipoh'},{'city':'Jalan Klang Lama'},{'city':'Jalan Kuching'},{'city':'Jalan Sultan Ismail'},{'city':'Jinjang'},{'city':'Kenny Hills'},{'city':'Kepong'},{'city':'Keramat'},{'city':'KL city'},{'city':'KL Sentral'},{'city':'KLCC'},{'city':'Kuchai Lama'},{'city':'Mid Valley city'},{'city':'Mont Kiara'},{'city':'Old Klang Road'},{'city':'OUG'},{'city':'Pandan Indah'},{'city':'Pandan Jaya'},{'city':'Pandan Perdana'},{'city':'Pantai'},{'city':'Pekan Batu'},{'city':'Puchong'},{'city':'Salak Selatan'},{'city':'Segambut'},{'city':'Sentul'},{'city':'Seputeh'},{'city':'Serdang'},{'city':'Setapak'},{'city':'Setiawangsa'},{'city':'Solaris Dutamas'},{'city':'Solaris Mont Kiara'},{'city':'Sri Hartamas'},{'city':'Sri Petaling'},{'city':'Sunway SPK'},{'city':'Sungai Besi'},{'city':'Sungai Penchala'},{'city':'Taman Desa'},{'city':'Taman Duta'},{'city':'Taman Melawati'},{'city':'Taman Tun Dr Ismail'},{'city':'Titiwangsa'},{'city':'Wangsa Maju'}]},{'id':'JO','name':'Johor','cities':[{'city':'Ayer Baloi'},{'city':'Ayer Hitam'},{'city':'Bakri'},{'city':'Batu Anam'},{'city':'Batu Pahat'},{'city':'Bekok'},{'city':'Benut'},{'city':'Bukit Gambir'},{'city':'Bukit Pasir'},{'city':'Chaah'},{'city':'Endau'},{'city':'Gelang Patah'},{'city':'Gerisek'},{'city':'Gugusan Taib Andak'},{'city':'Horizon Hills'},{'city':'Jementah'},{'city':'Johor Bahru'},{'city':'Kahang'},{'city':'Kampung Kenangan Tun Dr Ismail'},{'city':'Kluang'},{'city':'Kota Tinggi'},{'city':'Kukup'},{'city':'Kulai'},{'city':'Labis'},{'city':'Layang Layang'},{'city':'Masai'},{'city':'Medini'},{'city':'Mersing'},{'city':'Muar'},{'city':'Nusajaya'},{'city':'Pagoh'},{'city':'Paloh'},{'city':'Panchor'},{'city':'Parit Jawa'},{'city':'Parit Raja'},{'city':'Parit Sulong'},{'city':'Pasir Gudang'},{'city':'Pekan Nanas'},{'city':'Pengerang'},{'city':'Permas Jaya'},{'city':'Plentong'},{'city':'Pontian'},{'city':'Puteri Harbour'},{'city':'Rengam'},{'city':'Rengit'},{'city':'Segamat'},{'city':'Semerah'},{'city':'Senai'},{'city':'Senggarang'},{'city':'Senibong'},{'city':'Seri Gadang'},{'city':'Setia Indah'},{'city':'Setia Tropika'},{'city':'Simpang Rengam'},{'city':'Skudai'},{'city':'Sungai Mati'},{'city':'Tampoi'},{'city':'Tangkak'},{'city':'Ulu Tiram'},{'city':'Yong Peng'}]},{'id':'KE','name':'Kedah','cities':[{'city':'Alor Setar'},{'city':'Ayer Hitam'},{'city':'Baling'},{'city':'Bandar Baharu'},{'city':'Bedong'},{'city':'Bukit Kayu Hitam'},{'city':'Guar Chempedak'},{'city':'Gurun'},{'city':'Jitra'},{'city':'Karangan'},{'city':'Kepala Batas'},{'city':'Kodiang'},{'city':'Kota Sarang Semut'},{'city':'Kuala Kedah'},{'city':'Kuala Ketil'},{'city':'Kuala Muda'},{'city':'Kuala Nerang'},{'city':'Kubang Pasu'},{'city':'Kulim'},{'city':'Kupang'},{'city':'Langgar'},{'city':'Langkawi'},{'city':'Lunas'},{'city':'Merbok'},{'city':'Padang Serai'},{'city':'Padang Terap'},{'city':'Pendang'},{'city':'Pokok Sena'},{'city':'Pulau Langkawi'},{'city':'Serdang'},{'city':'Sik'},{'city':'Simpang Empat'},{'city':'Sungai Petani'},{'city':'Univesity Utara'},{'city':'Yan'}]},{'id':'KT','name':'Kelantan','cities':[{'city':'Ayer Lanas'},{'city':'Bachok'},{'city':'Cherang Ruku'},{'city':'Dabong'},{'city':'Gua Musang'},{'city':'Jeli'},{'city':'Kem Desa Pahwalan'},{'city':'Ketereh'},{'city':'Kota Bharu'},{'city':'Kuala Balah'},{'city':'Kuala Kerai'},{'city':'Machang'},{'city':'Melor'},{'city':'Pasir Mas'},{'city':'Pasir Puteh'},{'city':'Pulai Chondong'},{'city':'Rantau Panjang'},{'city':'Selising'},{'city':'Tanah Merah'},{'city':'Tawang'},{'city':'Temangan'},{'city':'Tumpat'},{'city':'Wakaf Baru'}]},{'id':'MA','name':'Melaka','cities':[{'city':'Alor Gajah'},{'city':'Asahan'},{'city':'Ayer Keroh'},{'city':'Bandar Hilir'},{'city':'Batu Berendam'},{'city':'Bemban'},{'city':'Bukit Beruang'},{'city':'Durian Tunggal'},{'city':'Jasin'},{'city':'Kuala Linggi'},{'city':'Kuala Sungai Baru'},{'city':'Lubok China'},{'city':'Masjid Tanah'},{'city':'Melaka Tengah'},{'city':'Merlimau'},{'city':'Selandar'},{'city':'Sungai Rambai'},{'city':'Sungai Udang'},{'city':'Tanjong Kling'},{'city':'Ujong Pasir'}]},{'id':'NS','name':'Negeri Sembilan','cities':[{'city':'Bahau'},{'city':'Bandar Baru Serting'},{'city':'Batang Melaka'},{'city':'Batu Kikir'},{'city':'Gemas'},{'city':'Gemencheh'},{'city':'Jelebu'},{'city':'Jempol'},{'city':'Johol'},{'city':'Juasseh'},{'city':'Kota'},{'city':'Kuala Klawang'},{'city':'Kuala Pilah'},{'city':'Labu'},{'city':'Lenggeng'},{'city':'Linggi'},{'city':'Mantin'},{'city':'Nilai'},{'city':'Pasir Panjang'},{'city':'Pedas'},{'city':'Port Dickson'},{'city':'Rantau'},{'city':'Rembau'},{'city':'Rompin'},{'city':'Senawang'},{'city':'Seremban'},{'city':'Siliau'},{'city':'Simpang Durian'},{'city':'Simpang Pertang'},{'city':'Sri Menanti'},{'city':'Sri Rusa'},{'city':'Tampin'},{'city':'Tanjong Ipoh'}]},{'id':'OT','name':'Other','cities':[]},{'id':'PA','name':'Pahang','cities':[{'city':'Balok'},{'city':'Bandar Pusat Jengka'},{'city':'Bandar Tun Abdul Razak'},{'city':'Benta'},{'city':'Bentong'},{'city':'Bera'},{'city':'Brinchang'},{'city':'Bukit Fraser'},{'city':'Cameron Highlands'},{'city':'Chenor'},{'city':'Daerah Rompin'},{'city':'Damak'},{'city':'Dong'},{'city':'Genting Highlands'},{'city':'Jerantut'},{'city':'Karak'},{'city':'Kuala Lipis'},{'city':'Kuala Rompin'},{'city':'Kuantan'},{'city':'Lanchang'},{'city':'Lurah Bilut'},{'city':'Maran'},{'city':'Mengkarak'},{'city':'Mentakab'},{'city':'Muadzam Shah'},{'city':'Padang Tengku'},{'city':'Pekan'},{'city':'Raub'},{'city':'Ringlet'},{'city':'Rompin'},{'city':'Sega'},{'city':'Sungai Koyan'},{'city':'Sungai Lembing'},{'city':'Sungai Ruan'},{'city':'Tanah Rata'},{'city':'Temerloh'},{'city':'Triang'}]},{'id':'PE','name':'Penang','cities':[{'city':'Air Tawar'},{'city':'Alma'},{'city':'Ayer Itam'},{'city':'Bagan Ajam'},{'city':'Bagan Jermal'},{'city':'Bagan Lallang'},{'city':'Balik Pulau'},{'city':'Bandar Perda'},{'city':'Batu Ferringhi'},{'city':'Batu Kawan'},{'city':'Batu Maung'},{'city':'Batu Uban'},{'city':'Bayan Baru'},{'city':'Bayan Lepas'},{'city':'Berapit'},{'city':'Bertam'},{'city':'Bukit Dumbar'},{'city':'Bukit Jambul'},{'city':'Bukit Mertajam'},{'city':'Bukit Minyak'},{'city':'Bukit Tambun'},{'city':'Bukit Tengah'},{'city':'Butterworth'},{'city':'Gelugor'},{'city':'Georgetown'},{'city':'Gertak Sangul'},{'city':'Greenlane'},{'city':'Jawi'},{'city':'Jelutong'},{'city':'Juru'},{'city':'Kepala Batas'},{'city':'Kubang Semang'},{'city':'Mak Mandin'},{'city':'Minden Heights'},{'city':'Nibong Tebal'},{'city':'Pauh Jaya'},{'city':'Paya Terubong'},{'city':'Penaga'},{'city':'Penang Hill'},{'city':'Penanti'},{'city':'Perai'},{'city':'Permatang Kuching'},{'city':'Permatang Pauh'},{'city':'Permatang Tinggi'},{'city':'Persiaran Gurney'},{'city':'Prai'},{'city':'Pulau Betong'},{'city':'Pulau Tikus'},{'city':'Raja Uda'},{'city':'Relau'},{'city':'Scotland'},{'city':'Seberang Jaya'},{'city':'Seberang Perai'},{'city':'Serdang'},{'city':'Simpang Ampat'},{'city':'Sungai Ara'},{'city':'Sungai Bakap'},{'city':'Sungai Dua'},{'city':'Sungai Jawi'},{'city':'Sungai Nibong'},{'city':'Sungai Pinang'},{'city':'Tanjong Tokong'},{'city':'Tanjung Bungah'},{'city':'Tasek Gelugor'},{'city':'Teluk Bahang'},{'city':'Teluk Kumbar'},{'city':'USM'},{'city':'Valdor'}]},{'id':'PK','name':'Perak','cities':[{'city':'Ayer Tawar'},{'city':'Bagan Datoh'},{'city':'Bagan Serai'},{'city':'Batu Gajah'},{'city':'Batu Kurau'},{'city':'Behrang Stesen'},{'city':'Beruas'},{'city':'Bidor'},{'city':'Bota'},{'city':'Changkat Jering'},{'city':'Changkat Keruing'},{'city':'Chemor'},{'city':'Chenderiang'},{'city':'Chenderong Balai'},{'city':'Chikus'},{'city':'Enggor'},{'city':'Gerik'},{'city':'Gopeng'},{'city':'Hutan Melintang'},{'city':'Intan'},{'city':'Ipoh'},{'city':'Jeram'},{'city':'Kampar'},{'city':'Kampong Gajah'},{'city':'Kampong Kepayang'},{'city':'Kamunting'},{'city':'Kuala Kangsar'},{'city':'Kuala Kurau'},{'city':'Kuala Sepetang'},{'city':'Lahat'},{'city':'Lambor Kanan'},{'city':'Langkap'},{'city':'Lenggong'},{'city':'Lumut'},{'city':'Malim Nawar'},{'city':'Mambang Diawan'},{'city':'Manong'},{'city':'Matang'},{'city':'Menglembu'},{'city':'Padang Rengas'},{'city':'Pangkor'},{'city':'Pantai Remis'},{'city':'Parit'},{'city':'Parit Buntar'},{'city':'Pengkalan Hulu'},{'city':'Pusing'},{'city':'Rantau Panjang'},{'city':'Sauk'},{'city':'Selama'},{'city':'Selekoh'},{'city':'Selinsing'},{'city':'Semanggol'},{'city':'Seri Manjong'},{'city':'Simpang'},{'city':'Sitiawan'},{'city':'Slim River'},{'city':'Sungai Siput'},{'city':'Sungai Sumun'},{'city':'Sungkai'},{'city':'Taiping'},{'city':'Tanjong Piandang'},{'city':'Tanjong Rambutan'},{'city':'Tanjong Tualang'},{'city':'Tanjung Malim'},{'city':'Tapah'},{'city':'Teluk Intan'},{'city':'Temoh'},{'city':'TLDM Lumut'},{'city':'Trolak'},{'city':'Trong'},{'city':'Tronoh'},{'city':'Ulu Bernam'},{'city':'Ulu Kinta'}]},{'id':'PL','name':'Perlis','cities':[{'city':'Arau'},{'city':'Kaki Bukit'},{'city':'Kangar'},{'city':'Kuala Perlis'},{'city':'Padang Besar'},{'city':'Pauh'},{'city':'Simpang Ampat'}]},{'id':'PJ','name':'Putrajaya','cities':[{'city':'Cyberjaya'},{'city':'Putrajaya'}]},{'id':'SA','name':'Sabah','cities':[{'city':'Beaufort'},{'city':'Beluran'},{'city':'Bongawan'},{'city':'Keningau'},{'city':'Kota Belud'},{'city':'Kota Kinabalu'},{'city':'Kota Kinabatangan'},{'city':'Kota Marudu'},{'city':'Kuala Penyu'},{'city':'Kudat'},{'city':'Kunak'},{'city':'Lahad Datu'},{'city':'Likas'},{'city':'Membakut'},{'city':'Menumbok'},{'city':'Nabawan'},{'city':'Pamol'},{'city':'Papar'},{'city':'Penampang'},{'city':'Pitas'},{'city':'Pulatan'},{'city':'Ranau'},{'city':'Sandakan'},{'city':'Semporna'},{'city':'Sipitang'},{'city':'Tambunan'},{'city':'Tamparuli'},{'city':'Tawau'},{'city':'Tenom'},{'city':'Tuaran'}]},{'id':'SW','name':'Sarawak','cities':[{'city':'Asajaya'},{'city':'Balingian'},{'city':'Baram'},{'city':'Bau'},{'city':'Bekenu'},{'city':'Belaga'},{'city':'Belawai'},{'city':'Betong'},{'city':'Bintagor'},{'city':'Bintulu'},{'city':'Dalat'},{'city':'Daro'},{'city':'Debak'},{'city':'Engkilili'},{'city':'Julau'},{'city':'Kabong'},{'city':'Kanowit'},{'city':'Kapit'},{'city':'Kota Samarahan'},{'city':'Kuching'},{'city':'Lawas'},{'city':'Limbang'},{'city':'Lingga'},{'city':'Long Lama'},{'city':'Lubok Antu'},{'city':'Lundu'},{'city':'Lutong'},{'city':'Maradong'},{'city':'Marudi'},{'city':'Matu'},{'city':'Miri'},{'city':'Mukah'},{'city':'Nanga Medamit'},{'city':'Niah'},{'city':'Pusa'},{'city':'Roban'},{'city':'Saratok'},{'city':'Sarikei'},{'city':'Sebauh'},{'city':'Sebuyau'},{'city':'Serian'},{'city':'Sibu'},{'city':'Simunjan'},{'city':'Song'},{'city':'Spaoh'},{'city':'Sri Aman'},{'city':'Sundar'},{'city':'Tanjung Kidurong'},{'city':'Tatau'}]},{'id':'SE','name':'Selangor','cities':[{'city':'Alam Impian'},{'city':'Aman Perdana'},{'city':'Ambang Botanic'},{'city':'Ampang'},{'city':'Ara Damansara'},{'city':'Balakong'},{'city':'Bandar Botanic'},{'city':'Bandar Bukit Raja'},{'city':'Bandar Bukit Tinggi'},{'city':'Bandar Kinrara'},{'city':'Bandar Puncak Alam'},{'city':'Bandar Puteri Klang'},{'city':'Bandar Puteri Puchong'},{'city':'Bandar Saujana Putra'},{'city':'Bandar Sri Damansara'},{'city':'Bandar Sungai Long'},{'city':'Bandar Sunway'},{'city':'Bandar Utama'},{'city':'Bangi'},{'city':'Banting'},{'city':'Batang Berjuntai'},{'city':'Batang Kali'},{'city':'Batu Arang'},{'city':'Batu Caves'},{'city':'Beranang'},{'city':'Bukit Jelutong'},{'city':'Bukit Rahman Putra'},{'city':'Bukit Rotan'},{'city':'Bukit Subang'},{'city':'Cheras'},{'city':'Country Heights'},{'city':'Cyberjaya'},{'city':'Damansara Damai'},{'city':'Damansara Intan'},{'city':'Damansara Jaya'},{'city':'Damansara Kim'},{'city':'Damansara Perdana'},{'city':'Damansara Utama'},{'city':'Denai Alam'},{'city':'Dengkil'},{'city':'Glenmarie'},{'city':'Gombak'},{'city':'Hulu Langat'},{'city':'Hulu Selangor'},{'city':'Jade Hills'},{'city':'Jenjarom'},{'city':'Kajang'},{'city':'Kapar'},{'city':'Kayu Ara'},{'city':'Kelana Jaya'},{'city':'Kerling'},{'city':'Klang'},{'city':'Kota Damansara'},{'city':'Kota Emerald'},{'city':'Kota Kemuning'},{'city':'Kuala Kubu Baru'},{'city':'Kuala Langat'},{'city':'Kuala Selangor'},{'city':'Kuang'},{'city':'Mutiara Damansara'},{'city':'Nilai'},{'city':'Petaling Jaya'},{'city':'Port Klang'},{'city':'Puchong'},{'city':'Puchong South'},{'city':'Pulau  Indah ( Pulau Lumut)'},{'city':'Pulau Carey '},{'city':'Pulau Ketam'},{'city':'Puncak Jalil'},{'city':'Putra Heights'},{'city':'Rasa'},{'city':'Rawang'},{'city':'Sabak Bernam'},{'city':'Saujana'},{'city':'Sekinchan'},{'city':'Selayang'},{'city':'Semenyih'},{'city':'Sepang'},{'city':'Serdang'},{'city':'Serendah'},{'city':'Seri Kembangan'},{'city':'Setia Alam'},{'city':'Setia Eco Park'},{'city':'Shah Alam'},{'city':'SierraMas'},{'city':'SS2'},{'city':'Subang Bestari'},{'city':'Subang Heights'},{'city':'Subang Jaya'},{'city':'Sungai Ayer Tawar'},{'city':'Sungai Besar'},{'city':'Sungai Buloh'},{'city':'Sungai Pelek'},{'city':'Taman Melawati'},{'city':'Taman TTDI Jaya'},{'city':'Tanjong Karang'},{'city':'Tanjong Sepat'},{'city':'Telok Panglima Garang'},{'city':'Tropicana'},{'city':'Ulu Klang'},{'city':'USJ'},{'city':'USJ Heights'},{'city':'Valencia'}]},{'id':'TR','name':'Terengganu','cities':[{'city':'Besut'},{'city':'Dungun'},{'city':'Hulu Terengganu'},{'city':'Kemaman'},{'city':'Kuala Terengganu'},{'city':'Marang'},{'city':'Setiu'},{'city':'Kerteh'}]}]}";
		
		Gson gson = new Gson();
		Country obj = gson.fromJson(countryObj, Country.class);
		this.countryStateCity = obj;
		System.out.println("CrimeDataMapper : setCountry Coutnry List STATE Size=>"+this.countryStateCity.getStates().size());
		System.out.println("CrimeDataMapper : setCountry END");
	}
	
	public int calculateCrimeSeverity(String newsContent){
		int crimeWeight = 0;
		if(null != newsContent && !newsContent.isEmpty()){
			List<String> crimeKeywordsList = KeywordDictionaryCrime.getCrimeKeyWordsList();
			Map<String, Integer> crimeKeywordsMap = KeywordDictionaryCrime.getCrimeKeywordsMap();
			for(String crimeKeyword : crimeKeywordsList){
				//System.out.println("News Content - "+newsContent +". ==== Keyword - "+crimeKeyword);
				if(getKeywordCombinationExist(newsContent, crimeKeyword)){
					if(crimeKeywordsMap.get(crimeKeyword) > crimeWeight){
						crimeWeight = crimeKeywordsMap.get(crimeKeyword);
					}
				}
			}
		}
		return crimeWeight;
	}
	
	
public static Boolean getKeywordCombinationExist(String newsContent, String keywordRaw){
		
		
		Boolean finalKeywordExistFlag = false;
		String splitWithAndArr[] = {};
		String wordRegExp = "[\\w\\s]+";
		List<String> finalSplitedWordList = new ArrayList<String>(); // final keyword list without conditions.
		Boolean keywordExistsArr[] = new Boolean[20];
		
		if(null != newsContent && !newsContent.isEmpty()){
			//-- Find conditions symbols (And - &, OR - |) using regular expression for NOT WORD and NOT DIGIT.
			String conditionSymbols = keywordRaw.replaceAll(wordRegExp, "");
			char conditionsSymbolCharArr[] = conditionSymbols.toCharArray();

			//-- Split with AND (=&) and then split ==> Split again each element obtained with OR(=!)
			splitWithAndArr = keywordRaw.split("&");
			if(null != splitWithAndArr && splitWithAndArr.length > 0){
				for(String tempSplittedWithAnd : splitWithAndArr){
					finalSplitedWordList.addAll(Arrays.asList(tempSplittedWithAnd.split("!")));
				}
			}

			//-- Find contains for each keyword and store true/false values in a Boolean List
			int tempCount=0;
			for(String splittedWord : finalSplitedWordList){
				keywordExistsArr[tempCount] = newsContent.toUpperCase().contains(splittedWord);
				tempCount++;
			}

			//-- Final calculation --
			int i = 0;
			for(i = 0; i < conditionsSymbolCharArr.length ; i++){
				if(conditionsSymbolCharArr[i] == '!'){
					Boolean temp = keywordExistsArr[i] || keywordExistsArr[i+1];
					keywordExistsArr[i+1] = temp; 
				}else if(conditionsSymbolCharArr[i] == '&'){
					Boolean temp = keywordExistsArr[i] && keywordExistsArr[i+1];
					keywordExistsArr[i+1] = temp;
				}
			}
			finalKeywordExistFlag = keywordExistsArr[i]; 
		}

		return finalKeywordExistFlag;
	}
	
	
	
}
