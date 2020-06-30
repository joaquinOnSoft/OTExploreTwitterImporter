package com.opentext.explore.importer.twitter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.util.FileUtil;

public class TwitterImporterLauncher {
	
	protected static final Logger log = LogManager.getLogger(TwitterImporterLauncher.class);
		
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
			
			if (cmd.hasOption("config")) {
				String configFilePath = cmd.getOptionValue("config");

				if(FileUtil.isFile(configFilePath)) {
					file = new FileInputStream(configFilePath);
					prop.load(file);
					
					TwitterImporter importer = new TwitterImporter(prop);
					importer.start();
				}
			}

		}
		catch (IOException e) {
			log.error(e.getMessage());
			System.err.println(e.getMessage());
		}
		catch (ParseException e) {
			log.error(e.getMessage());			
			
			formatter.printHelp("java -jar file.jar --config/-c 'config file path'", options);

			System.exit(-1);	
		}
		finally {
			if (file != null) {
				try {
					file.close();
				} 
				catch (IOException e2) {
					log.error(e2.getMessage());
					System.err.println(e2.getMessage());
					System.exit(-1);
				}
			}
		}
	}
}
