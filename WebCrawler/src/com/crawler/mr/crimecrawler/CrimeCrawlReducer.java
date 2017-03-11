package com.crawler.mr.crimecrawler;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CrimeCrawlReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
	@Override
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		System.out.println("============== Crime Crawler : REDUCER => START ==================");
		int totalCrimeSeverityForACity = 0;
		for(IntWritable crimeseverity:values){
			totalCrimeSeverityForACity = totalCrimeSeverityForACity + crimeseverity.get();
		}
		System.out.println("Writing Key - "+key+". Value - "+new IntWritable(totalCrimeSeverityForACity));
		context.write(key, new IntWritable(totalCrimeSeverityForACity));
		System.out.println("============== Crime Crawler : REDUCER => END ==================");
	}
}
