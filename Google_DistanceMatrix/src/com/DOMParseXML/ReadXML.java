package com.DOMParseXML;

import java.io.File;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ReadXML {

    private float distance;

    public String DOMParseXML(String xml){
        /*try {
            File inputFile = new File(XML_FILENAME);
            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();


            System.out.println(doc.getDocumentElement().getElementsByTagName("origin_address").item(0).getTextContent());
            System.out.println(doc.getDocumentElement().getElementsByTagName("destination_address").item(0).getTextContent());
            NodeList nodeList = doc.getElementsByTagName("row");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.println(element.getAttribute("element"));
                    System.out.println(element.getElementsByTagName("text").item(0).getTextContent());
                    float distance = Integer.parseInt(element.getElementsByTagName("distance").item(0).getChildNodes().item(1).getLastChild().getTextContent().toString(), 10);
                    System.out.println(distance / 1000 + "Km");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(new InputSource(new StringReader(xml)));
            doc.getDocumentElement().normalize();


            System.out.println(doc.getDocumentElement().getElementsByTagName("origin_address").item(0).getTextContent());
            System.out.println(doc.getDocumentElement().getElementsByTagName("destination_address").item(0).getTextContent());
            NodeList nodeList = doc.getElementsByTagName("row");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.println(element.getElementsByTagName("text").item(0).getTextContent());
                    distance = Integer.parseInt(element.getElementsByTagName("distance").item(0).getChildNodes().item(1).getLastChild().getTextContent().toString(), 10);
                    System.out.println(distance / 1000 + "Km");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }
}