package com.opentext.explore.importer.twitter;

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import twitter4j.Status;

/**
 * Transform a Twitter Status in to a XML.
 * The XML format fit the requirements of a Micro Media content 
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
 * @author joaquin
 */
public class TwitterTransformer {
	
	/**
	 * SEE: How to create XML file with specific structure in Java 
	 * https://stackoverflow.com/questions/23520208/how-to-create-xml-file-with-specific-structure-in-java
	 * @param statuses
	 * @return
	 */
	public static String statusToString(List<Status> statuses) {
		String xml = null;
		
		if(statuses != null && statuses.size() > 0) {
			
			Document doc=new Document();
			//Root Element
			Element root=new Element("add");
			
			for (Status status : statuses) {
				Element eDoc = new Element("doc");
				
				Element eId = new Element("field");
				eId.setAttribute("name", "ID");
				eId.addContent(Long.toString(status.getId()));
				
				Element eContent = new Element("field");
				eContent.setAttribute("name", "content");
				eContent.addContent(status.getText());
				
				eDoc.addContent(eId);
				eDoc.addContent(eContent);
				root.addContent(eDoc);
			}
			
			//Define root element like root
			doc.setRootElement(root);
			//Create the XML
			XMLOutputter outter=new XMLOutputter();
			outter.setFormat(Format.getPrettyFormat());			
			xml = outter.outputString(doc);
		}
		
		return xml;
	}
}
