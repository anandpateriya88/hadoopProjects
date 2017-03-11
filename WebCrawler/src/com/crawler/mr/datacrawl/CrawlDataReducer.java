package com.crawler.mr.datacrawl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CrawlDataReducer extends Reducer<Text, Text, Text, Text> {

     private Path[] cacheFile;
     Map<String, Integer> crimeIndexMap = new HashMap<String, Integer>();
     
     @Override
     protected void setup(Context context){
    	 try{
    		cacheFile = DistributedCache.getLocalCacheFiles(context.getConfiguration());
    	 }catch(Exception e){
    		 System.out.println("Exception Occured in Reading file from Distributed Cache. "+e);
    	 }
     }
     
     
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		System.out.println("=====================CrawlDataReducer : Reduce START ===============");
		
		String name = "";
		String weekYear = "";
		String rentOrSale = "";
		Double price = 0.0;
		Integer floorArea = 0;
		Double perSqFootRate = 0.0;
		Double avg;
		String city = "";
		Integer crimeIndex = 1;
		int valuesSize = 0;
		String state = "";
		String cityStateCommaSeperated="";
		String reducerOutputValue;
		Double psfRateAvg=0.0, psfRateForCurrentAd = 0.0;
		
		createCrimeMapFromFile();
		for (Text propDtlsValueInput : values) {
			try {
				String propDetailsInputArr[] = propDtlsValueInput.toString().split("#");

				// String floorAreaStr =
				if (null != propDetailsInputArr) {
					if (propDetailsInputArr.length > 0) {

						if (propDetailsInputArr[0].trim() != "")
							rentOrSale = propDetailsInputArr[0].trim();

						if (propDetailsInputArr.length > 1) {
							if (propDetailsInputArr[1].trim() != "")
								floorArea = Integer.parseInt(propDetailsInputArr[1].trim());

							if (propDetailsInputArr.length > 2) {
								if (propDetailsInputArr[2] != ""){
									price = Double.parseDouble(propDetailsInputArr[2]);
									
									if (perSqFootRate != 0.0) {
										psfRateAvg = perSqFootRate/valuesSize;
										psfRateForCurrentAd = price/floorArea;
										if(psfRateForCurrentAd+100 < psfRateAvg || psfRateForCurrentAd-100 > psfRateAvg){
											continue;
										}
										perSqFootRate = perSqFootRate + (price / floorArea);
										
									}else{
										perSqFootRate = perSqFootRate + (price / floorArea);
										if(rentOrSale.equals("RENT")){
											if(perSqFootRate < 0.1 || perSqFootRate > 30){
												perSqFootRate = 0.0;
											}
										}else
											if(perSqFootRate < 100 || perSqFootRate > 10000){
												perSqFootRate = 0.0;
											}
										}
									}
								if (propDetailsInputArr.length > 3) {
									name = propDetailsInputArr[3];
									if (propDetailsInputArr.length > 4) {
										city = propDetailsInputArr[4];
										crimeIndex = crimeIndexForCurrentCity(city);
										if (propDetailsInputArr.length > 5) {
											state = propDetailsInputArr[5];
											if (propDetailsInputArr.length > 6) {
												weekYear = propDetailsInputArr[6];
												if (propDetailsInputArr.length > 7) {
													cityStateCommaSeperated = propDetailsInputArr[7];
												}
											}
										}
									}
								}
							}
							}
						}
					}
					valuesSize++;
				}catch (Exception e) {
					System.out.println("Exception Occured in Redurer =====>>" + e);
					}
			} 
		
		if (0 != valuesSize && perSqFootRate != 0) {
			avg = perSqFootRate / valuesSize;
			reducerOutputValue = rentOrSale + "#" + avg.toString() + "#" + city +"#"+state+ "#" + name 
					+ "#" + weekYear+"#"+cityStateCommaSeperated+"#"+(null != crimeIndex ? crimeIndex.toString() : "1");

			/*
			 * PrintWriter writer = new
			 * PrintWriter("G:\\crawlingAll\\localOutput\\propertyReducedOutput.txt"
			 * , "UTF-8"); writer.println(key +"==>"+ reducerOutputValue);
			 * writer.close();
			 */

			context.write(new Text(""), new Text(reducerOutputValue));
			System.out.println("=====================CrawlDataReducer : Reduce Writing Key Value to Context. Key =>"+key+"==Value "+reducerOutputValue);
		}
		System.out.println("=====================CrawlDataReducer : Reduce END ===============");
	}
	
	public void createCrimeMapFromFile(){
		FileInputStream fileStream=null;
		Integer cityCrimeIndex= 0;
		String city = "";
		String splittedArray[] = {};
		try{
			fileStream = new FileInputStream(cacheFile[0].toString());
			BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
	          
            System.out.println("Reading File line by line using BufferedReader");
          
            String line = reader.readLine();
            while(line != null){
            	splittedArray = line.split("\\t");
            	System.out.println("Splited Arraqy le");
            	if(null != splittedArray && splittedArray.length > 1){
            		city = splittedArray[0];
            		if(null != splittedArray[1] && !splittedArray[1].isEmpty()){
            			cityCrimeIndex = Integer.parseInt(splittedArray[1]);
            			crimeIndexMap.put(city, cityCrimeIndex);
            			System.out.println("Inserted on Entry in the Map - Key = "+city+" Value = "+cityCrimeIndex);
            		}
            	}
            	line = reader.readLine();
            }           
			
			
			fileStream.close();
		}catch(Exception e){
			System.out.println("Property Crawl Reducer: Error in Reading File from Cache. = >"+ e);
		}
		System.out.println("Map Creation from Crime city and Index file Complete.");
		
	}

	public Integer crimeIndexForCurrentCity(String city){
		Integer crimeIndex = 0;
		if(null != city && !city.isEmpty()){
			crimeIndex = this.crimeIndexMap.get(city.toUpperCase());
		}
		
		return crimeIndex;
	}
}
