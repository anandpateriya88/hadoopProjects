package com.crawler.mr.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import com.crawler.mr.crimecrawler.CrimeCrawlMapper;
import com.crawler.mr.crimecrawler.CrimeCrawlReducer;
import com.crawler.mr.datacrawl.CrawlDataMapper;
import com.crawler.mr.datacrawl.CrawlDataReducer;

public class DataCrawlerTest {


	  MapDriver<Object, Text, Text, Text> mapDriver;
	  ReduceDriver<Text, Text, Text, Text> reduceDriver;
	  MapReduceDriver<Object, Text, Text, Text, Text, Text> mapReduceDriver;
	  
	  MapDriver<Object, Text, Text, IntWritable> mapDriverCrime;
	  ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriverCrime;
	  MapReduceDriver<Object, Text, Text, Text, Text, Text> mapReduceDriverCrime;
	  
	  @Before
	  public void setUp() {
		CrawlDataMapper mapper = new CrawlDataMapper();
		CrawlDataReducer reducer = new CrawlDataReducer();
		  
		CrimeCrawlMapper mapperCrime = new CrimeCrawlMapper();
		CrimeCrawlReducer reducerCrime = new CrimeCrawlReducer(); 		
	    mapDriver = MapDriver.newMapDriver(mapper);
	    reduceDriver = ReduceDriver.newReduceDriver(reducer);
	    mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
	    mapDriverCrime = MapDriver.newMapDriver(mapperCrime);
	    reduceDriverCrime = ReduceDriver.newReduceDriver(reducerCrime);
	  }
	 
	  @Test
	  public void testMapper() {
	    mapDriver.withInput(new Text(), new Text("http://homes.mitula.my/homes/apartment/#::1::1"));
	   // mapDriver.withOutput(new Text("6"), new Text(""));
	    mapDriver.addOutput(new Text("PERLINGAPARTMENTSALE02-20-2016"), new Text("SALE#1238#3#PERLING APARTMENT#Johor Bahru#Johor#02-20-2016#Johor Bahru, Johor"));
	    mapDriver.runTest();
	  }
	 
	  @Test
	  public void testReducer() {
	    List<Text> values = new ArrayList<Text>();
	    values.add(new Text("960#365000#MENARA IMPIAN RM #Level 5, Menara Impian, Jalan 2/2, Taman Tun Abdul Razak, Ampang 0123155076, 68000 Kuala Lumpur#1#2016"));
	    //values.add(new IntWritable(1));
	    reduceDriver.withInput(new Text("MENARA IMPIAN RM 365SALE12016"), values);
//	    reduceDriver.withOutput(new Text("6"), new IntWritable(2));
	    reduceDriver.runTest();
	  }
	   
	  @Test
	  public void testMapReduce() {
	    mapReduceDriver.withInput(new Text(), new Text("http://homes.trovit.my/for-sale-apartment/#::1"));
	    List<Text> values = new ArrayList<Text>();
	    values.add(new Text("60#365000#MENARA IMPIAN RM #Level 5, Menara Impian, Jalan 2/2, Taman Tun Abdul Razak, Ampang 0123155076, 68000 Kuala Lumpur#1#2016"));
	    mapReduceDriver.withOutput(new Text("MENARA IMPIAN RM 365SALE12016"), new Text("60#365000#MENARA IMPIAN RM #Level 5, Menara Impian, Jalan 2/2, Taman Tun Abdul Razak, Ampang 0123155076, 68000 Kuala Lumpur#1#2016"));
	    mapReduceDriver.runTest();
	  }
	  
	  
	  @Test
	  public void testMapperCrime() {
		  mapDriverCrime.withInput(new Text(), new Text("http://www.thesundaily.my/archive/201502?page=#::41::45"));
		  mapDriverCrime.withOutput(new Text("6"), new IntWritable(1));
		  mapDriverCrime.runTest();
	  }
	
	  @Test
	  public void testReducerCrime() {
	    List<IntWritable> values = new ArrayList<IntWritable>();
	    values.add(new IntWritable(20));
	    values.add(new IntWritable(40));
	    reduceDriverCrime.withInput(new Text("IPOH"), values);
//	    reduceDriver.withOutput(new Text("6"), new IntWritable(2));
	    reduceDriverCrime.runTest();
	  }
}
