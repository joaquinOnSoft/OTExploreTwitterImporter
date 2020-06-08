package com.opentext.explore.importer.twitter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.opentext.explore.util.DateUtil;

import twitter4j.Status;

/**
 * Transform a Twitter Status in to a XML.
 * The XML format fit the requirements of a "Micro Media" content 
 * in Solr for OpenText Explore.
 * 
 *  The format looks like this:
 * <pre>
 * 	<?xml version="1.0" encoding="UTF-8"?>
 * 	<add>
 *	    <doc>
 *       	<field name="reference_id">1257656312529186816</field>
 *       	<field name="interaction_id">1257656312529186816</field>
 *       	<field name="title">TweetID: 1257656312529186816</field>
 *       	<field name="author_name">Haig Kartounian</field>
 *       	<field name="ID">1257656312529186816</field>
 *       	<field name="type">Micro Media</field>
 *       	<field name="published_date">2020-05-05T09:00:05Z</field>
 *       	<field name="date_time">2020-05-05T09:00:05Z</field>
 *       	<field name="content">The pathway to 100% #cleanenergy continues in CA. @SCE signed agreements for 770 MW of energy storage procurement. A big step forward demonstrating that #California is continuing to invest in clean energy and grow green jobs.</field>
 *       	<field name="en_content">The pathway to 100% #cleanenergy continues in CA. @SCE signed agreements for 770 MW of energy storage procurement. A big step forward demonstrating that #California is continuing to invest in clean energy and grow green jobs.</field>
 *       	<field name="profile_img">https://pbs.twimg.com/profile_images/602996855547408385/u1YYB6-Z.jpg</field>
 *    	</doc>
 * 	</add>
 * </pre>
 * @author Joaquín Garzón
 */
public class TwitterTransformer {

	private static Document statusToDoc(List<Status> statuses) {
		Document doc = null;
		
		if(statuses != null && statuses.size() > 0) {
			
			doc=new Document();
			//Root Element
			Element root=new Element("add");
			
			for (Status status : statuses) {
				Element eDoc = new Element("doc");
							
				eDoc.addContent(createElementField("reference_id", status.getId()));
				eDoc.addContent(createElementField("interaction_id", status.getId()));
				eDoc.addContent(createElementField("title", status.getText()));
				eDoc.addContent(createElementField("author_name", status.getUser().getName()));
				eDoc.addContent(createElementField("ID", status.getId()));
				eDoc.addContent(createElementField("type", "Twitter"));	
				eDoc.addContent(createElementField("published_date", status.getCreatedAt()));
				eDoc.addContent(createElementField("date_time", status.getCreatedAt()));
				eDoc.addContent(createElementField("content", status.getText()));				
				eDoc.addContent(createElementField(status.getLang() + "_content", status.getText()));				
				eDoc.addContent(createElementField("profile_img", status.getUser().getBiggerProfileImageURLHttps()));				
				eDoc.addContent(createElementField("followers", status.getUser().getFollowersCount()));				
				eDoc.addContent(createElementField("following", status.getUser().getFriendsCount()));
				eDoc.addContent(createElementField("favorite_count", status.getFavoriteCount()));	
				eDoc.addContent(createElementField("retweet_count", status.getRetweetCount()));
				eDoc.addContent(createElementField("latitude", status.getGeoLocation().getLatitude()));
				eDoc.addContent(createElementField("longitude", status.getGeoLocation().getLongitude()));
				
				root.addContent(eDoc);
			}
			
			doc.addContent(root);
		}
		
		return doc;
	}
	
	/**
	 * Generate a XML file using the given Twitter status 
	 * @param status - Twitter status
	 * @param fileName - File name of the XML that will be generated
	 * @return path of the XML file created
	 * @throws IOException
	 */	
	public static String statusToXMLFile(Status status, String fileName) throws IOException {
		List<Status> statuses = new LinkedList<Status>();
		statuses.add(status);
		return statusesToXMLFile(statuses, fileName);
	}
	
	/**
	 * Generate a XML file using the given Twitter statuses 
	 * @param statuses - List of Twitter statuses
	 * @param fileName - File name of the XML that will be generated
	 * @return Absolute path of the XML file created
	 * @throws IOException
	 */
	public static String statusesToXMLFile(List<Status> statuses, String fileName) throws IOException {
		String xmlPath = null;
		Document doc = statusToDoc(statuses);

		if(doc != null) {
			//Create the XML
			XMLOutputter outter=new XMLOutputter();
			outter.setFormat(Format.getPrettyFormat());
			
			File xmlFile = new File(fileName);
			xmlPath = xmlFile.getAbsolutePath();
			FileWriter fWriter = new FileWriter(xmlFile, StandardCharsets.UTF_8);
			
			outter.output(doc, fWriter);
			
			fWriter.close();
		}		
		
		return xmlPath;		
	}
	
	/**
	 * Generate a XML string using the given Twitter statuses
	 * SEE: How to create XML file with specific structure in Java 
	 * https://stackoverflow.com/questions/23520208/how-to-create-xml-file-with-specific-structure-in-java
	 * @param statuses
	 * @return
	 */
	public static String statusToString(List<Status> statuses) {
		String xml = null;
		Document doc = statusToDoc(statuses);

		if(doc != null) {
			//Create the XML
			XMLOutputter outter=new XMLOutputter();
			outter.setFormat(Format.getPrettyFormat());			
			xml = outter.outputString(doc);
		}
		
		return xml;
	}
	
	private static Element createElementField(String name, Date content) {
		return createElementField(name, DateUtil.dateToUTC(content));
	}

	private static Element createElementField(String name, long content) {
		return createElementField(name, Long.toString(content));
	}
	
	private static Element createElementField(String name, int content) {
		return createElementField(name, Integer.toString(content));
	}

	private static Element createElementField(String name, double content) {
		return createElementField(name, Double.toString(content));
	}	
	
	private static Element createElementField(String name, String content) {
		Element elementField = new Element("field");
		elementField.setAttribute("name", name);
		elementField.addContent(content);
		return elementField;
	}
}
