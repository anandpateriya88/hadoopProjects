package com.crawler.mr.datacrawl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CrawlDataDriver {
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.printf("Usage: StubDriver <input dir> <output dir>\n");
			System.exit(-1);
		}
		
		Configuration conf = new Configuration();
		long milliSeconds = 1000*60*28; // 28 mins 
		conf.setLong("mapreduce.task.timeout", milliSeconds);
		Job job = new Job(conf, "PropertyWebsitesCrawlAndAnalysis");
		job.setJarByClass(CrawlDataDriver.class);
		job.setMapperClass(CrawlDataMapper.class);
		job.setReducerClass(CrawlDataReducer.class);
		Path crimeIndexFilePath = new Path("s3://hadoopwebcrawler1/outputCrimeComplete/mergedCrimeReport.txt");
		DistributedCache.addCacheFile(crimeIndexFilePath.toUri(), job.getConfiguration());
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		System.out.println("MAp reduce Timeout - "+conf.get("mapreduce.task.timeout"));
		System.out.println("++++++++============== DATA CRAWL STARTING =============+++++++++");	
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		boolean result = job.waitForCompletion(true);
		System.exit(result ? 0 : 1);
	}
}
