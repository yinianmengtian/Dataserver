package com.wtsj.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlUtils {

	/**
	 * xml中字符需要转义字符 &(逻辑与) &amp; <(小于) &lt; >(大于) &gt; "(双引号) &quot; '(单引号)
	 * &apos; [/size]
	 * */
	public static List<String> readXml() {

		List<String> list = new ArrayList<String>();

//		URL url = XmlUtils.class.getResource("Querysql.xml");
		System.out.println("加载xml====conf/Querysql.xml" );
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File("conf/Querysql.xml"));

			Element rootElement = document.getDocumentElement();

			NodeList nodeList = rootElement.getElementsByTagName("sql");

			/**
			 * 把sql语句存入List数组中
			 * */
			for (int i = 0; i < nodeList.getLength(); i++) {

				Element element = (Element) rootElement.getElementsByTagName(
						"sql").item(i);
				list.add(element.getChildNodes().item(0).getNodeValue());

				// System.out.println(element.getChildNodes().item(0).getNodeValue()+" "+element.getAttribute("ip"));
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;

	}


	
	public static void main(String[] args) {

		XmlUtils.readXml();

		PropertyUtils.writeProps(100+"","conf/memory.properties");
		System.out.println(PropertyUtils.readProps("conf/memory.properties"));
	}

}
