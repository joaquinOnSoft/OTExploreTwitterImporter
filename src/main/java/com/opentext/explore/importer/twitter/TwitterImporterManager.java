package com.opentext.explore.importer.twitter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.opentext.explore.util.FileUtil;

import twitter4j.Status;

public class TwitterImporterManager {

	public static void main(String[] args) {
		Properties prop = new Properties();
		InputStream file = null;

		Options options = new Options();

		Option actionConfig = new Option("c", "config", true, "Define config file path");
		actionConfig.setRequired(true);
		options.addOption(actionConfig);
		

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);

			if (args[0].equals("--config") || args[0].equals("-c")) {
				String configFilePath = cmd.getOptionValue("config");

				if(FileUtil.isFile(configFilePath)) {
					file = new FileInputStream(configFilePath);
					prop.load(file);

					importTwitts(prop);
				}
			}

		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("java -jar file.jar --config/-c 'config file path'", options);

			System.exit(-1);	
		}
		finally {
			if (file != null) {
				try {
					file.close();
				} 
				catch (IOException e2) {
					System.out.println(e2.getMessage());
					System.exit(-1);
				}
			}
		}

	}


	private static void importTwitts(Properties prop) {
		// Creating shared object 
		BlockingQueue<Status> sharedQueue = new LinkedBlockingQueue<Status>();

		// Creating Producer and Consumer Thread  
		Thread prodThread = new Thread(new TwitterImporterProducer(sharedQueue, prop));

		// Producer thread START 
		prodThread.start();

		int numThreads = 1;
		for(int i = 0; i < numThreads; i++){ 
			Thread consThread = new Thread(new TwitterImporterConsumer(sharedQueue, prop)); 
			// Consumer thread START 
			consThread.start(); }
		}

	}
