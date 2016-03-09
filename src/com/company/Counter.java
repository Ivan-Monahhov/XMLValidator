package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by Ivan on 3/9/2016.
 */
public class Counter
{
    public int countElements(String xml , String tag) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(xml);
        NodeList list = doc.getElementsByTagName(tag);
        return list.getLength();

    }
}
