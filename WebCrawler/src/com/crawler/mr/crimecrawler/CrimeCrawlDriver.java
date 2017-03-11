package com.crawler.mr.crimecrawler;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CrimeCrawlDriver {
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.printf("Usage: StubDriver <input dir> <output dir>\n");
			System.exit(-1);
		}
		JobConf conf = new JobConf();
		Job job = new Job(conf, "PropertyWebsitesCrawlAndAnalysis");
		job.setJarByClass(CrimeCrawlDriver.class);
		job.setMapperClass(CrimeCrawlMapper.class);
		job.setReducerClass(CrimeCrawlReducer.class);
		long milliSeconds = 1000*60*30; 
		conf.setLong("mapreduce.task.timeout", milliSeconds);
	/*	job.setNumReduceTasks(0);
		conf.setNumReduceTasks(0);*/
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		//job.setCombinerClass(CrawlReducer.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		System.out.println("++++++++============== Crime CRAWL STARTING =============+++++++++");	
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		boolean result = job.waitForCompletion(true);
		System.exit(result ? 0 : 1);
	}
}
