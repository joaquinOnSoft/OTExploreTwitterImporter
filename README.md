# OpenText Explore Twitter Importer

This command-line application listen to a Twitter Stream applying the filters, defined in twitter4j.properties, to select the tweets of our interest.

These tweets are inserted into the Solr Server used by **OpenText Explore**. 

Once the tweets are available in **OpenText Explore** you can create your owns dashboards to analyze the information listened.


> [OpenText™ Explore](https://www.opentext.com/products-and-solutions/products/customer-experience-management/contact-center-workforce-optimization/opentext-explore) is a business discovery solution that allows business and call center professionals to view cross-channel interactions collectively for a comprehensive picture of customer behaviors and relationships. 

## Twitter stream API

There are a number of properties available for configuring Twitter4J (required to listen the Twitter stream). You can specify properties via **twitter4j.properties** file as follows : 

### via twitter4j.properties
Standard properties file named **"twitter4j.properties"**. Place it to either the current directory, root of the CLASSPATH directory.

```
oauth.consumerKey=*********************
oauth.consumerSecret=******************************************
oauth.accessToken=**************************************************
oauth.accessTokenSecret=******************************************
```

> SEE: [Twitter4J: Generic properties](http://twitter4j.org/en/configuration.html)

> NOTE: You need a [Twitter Developer](https://developer.twitter.com/en) account to get access to the Twitter Stream.

## Configuration file: twitter-importer.properties

Configuration file that specifies the filter to apply to the Twitter stream

It supports theses properties:

 - languages - Specifies the tweets language of the stream (You can specify more than one separated by commas)
 - keywords - Specifies keywords to track (You can specify more than one separated by commas). 
 - follow - Twitter account names separated by commas (Don't include the @), e.g. madrid to follow @madrid
 - verbose - Verbose mode. Possible values: true (messages are shown in the console) or false (messages are NOT shown in the console)
 - host - Solr server URL. Default value: http://localhost:8983

This **twitter-importer.properties** file shows an example to listen to tweets about Madrid City Hall in Spanish: 
 
```
languages=es
keywords=Ayuntamiento de Madrid,Ayto Madrid,@MADRID
verbose=true
host=http://localhost:8983
follow=madrid,lineaMadrid
```

## Command line execution 

This utility is distributed as a runnable .jar file.

These are the accepted parameters:

usage: java -jar OTExploreTwitterImporter-20.2.jar
 * -c, --config						Define config file path

### Example of invocation

```
$ java -jar OTExploreTwitterImporter-20.2.jar --config "C:\ProgramFiles\OTExploreTwitterImporter\twitter-importer.properties"
```
