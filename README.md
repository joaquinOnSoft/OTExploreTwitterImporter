# OpenText Explore Twitter Importer

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

## Configuration file: twitter-importer.properties

Configuration file that specifies the filter to apply to the Twitter stream

It supports theses properties:

 - languages - Specifies the tweets language of the stream (You can specify more than one separated by commas)
 - keywords - Specifies keywords to track (You can specify more than one separated by commas). 
 - verbose - Verbose mode. Possible values: true (messages are shown in the console) or false (messages are NOT shown in the console)
 - host - Solr server URL. Default value: http://localhost:8983

This **twitter-importer.properties** file shows an example to listen to tweets about Madrid City Hall in Spanish: 
 
```
languages=es
keywords=Ayuntamiento de Madrid,Ayto Madrid,@MADRID
verbose=true
host=http://localhost:8983
```

