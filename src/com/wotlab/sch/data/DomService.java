package com.wotlab.sch.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

public class DomService {

	public static ArrayList<VideoResources> parseVideozone(String str) {

		ArrayList<VideoResources> list = new ArrayList<VideoResources>();
		Log.d("jy", "domService:" + str);
		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document doc = builder.parse(new ByteArrayInputStream(str
					.getBytes()));

			Element root = doc.getDocumentElement();

			NodeList list_0 = root.getElementsByTagName("videos");

			Log.d("jy", "video list size = " + list_0.getLength());
			for (int i = 0; i < list_0.getLength(); i++) {
				Element element = (Element) list_0.item(i);
				VideoResources videoresource = new VideoResources();
				videoresource.setmName(element
						.getElementsByTagName("english-name").item(0)
						.getFirstChild().getNodeValue());
				videoresource.setX(element.getElementsByTagName("x").item(0)
						.getFirstChild().getNodeValue());
				videoresource.setY(element.getElementsByTagName("y").item(0)
						.getFirstChild().getNodeValue());
				videoresource.setmAddress(element
						.getElementsByTagName("address").item(1)
						.getFirstChild().getNodeValue());

				Log.d("jy", videoresource.getmName() + videoresource.getX()
						+ videoresource.getY() + videoresource.getmAddress());

				NodeList list_group = element.getElementsByTagName("group");
				NodeList list_id = element.getElementsByTagName("id");
				Set<String> groups = new HashSet<String>();
				for (int j = 0; j < list_group.getLength(); j++) {
					groups.add(list_id.item(j + 1).getFirstChild()
							.getNodeValue());
				}
				videoresource.setmGroups(groups);
				list.add(videoresource);

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static Response parseResponse(String str){
		Response r = new Response();
		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document doc = builder.parse(new ByteArrayInputStream(str
					.getBytes()));

			Element root = doc.getDocumentElement();

			NodeList list_0 = root.getElementsByTagName("msg");
			r.msg = list_0.item(0).getFirstChild().getNodeValue();
			NodeList list_1 = root.getElementsByTagName("data");
			r.data = list_1.item(0).getFirstChild().getNodeValue();


		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return r;
	}

}
