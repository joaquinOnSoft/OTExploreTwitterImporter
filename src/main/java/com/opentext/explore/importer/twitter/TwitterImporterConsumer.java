package com.opentext.explore.importer.twitter;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;

import twitter4j.Status;

public class TwitterImporterConsumer implements Runnable{
	private final BlockingQueue<Status> sharedQueue;
	private Properties prop;
	
    public TwitterImporterConsumer (BlockingQueue<Status> sharedQueue, Properties prop) {
        this.sharedQueue = sharedQueue;
        this.prop = prop;	    
    }
	
	@Override
	public void run() {
		while(true){
			try {
				Status twitt = sharedQueue.take();
				manageRequest(twitt);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void manageRequest (Status twitt) {
		System.out.print(Thread.currentThread().getId() + " " +
				Thread.currentThread().getName() + " " +
				twitt.getText());
	}
}
