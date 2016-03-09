package com.company.Validators;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.EventReaderDelegate;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by Ivan on 3/9/2016.
 */
// For use with external DTD file
// The reason why we cant validate using Validator API - https://docs.oracle.com/javase/7/docs/api/javax/xml/validation/SchemaFactory.html
public class DTD
{
    public boolean Validate(String xml , String dtd)
    {
        try
        {
            Reader r = removeDTD(xml);
            StringWriter transformed = injectNewDTD(dtd, r);
            validateNewXML(transformed);
            return true;
        } catch (ParserConfigurationException e) {
            return false;
        } catch (TransformerException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (SAXException e) {
            return false;
        } catch (XMLStreamException e) {
            return false;
        }
    }

    private void validateNewXML(StringWriter transformed) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setValidating(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        // Validation will throw an SAXException on builder.parse(xml) line
        builder.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(org.xml.sax.SAXParseException exception) throws SAXException {
            }
            @Override
            public void error(org.xml.sax.SAXParseException exception) throws SAXException {
            }
            @Override
            public void fatalError(org.xml.sax.SAXParseException exception) throws SAXException {
            }
        });
        StringReader res = new StringReader(transformed.getBuffer().toString());
        Document doc = builder.parse(new InputSource(res));
    }

    private StringWriter injectNewDTD(String dtd, Reader r) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance().newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtd);
        StringWriter transformed = new StringWriter();
        transformer.transform(new StreamSource(r), new StreamResult(transformed));
        return transformed;
    }

    private Reader removeDTD(String xml) throws XMLStreamException {
        XMLInputFactory inFactory = XMLInputFactory.newInstance();
        XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
        XMLEventReader reader = inFactory.createXMLEventReader(new StreamSource(xml));
        reader = new DTDRemover(reader);
        StringWriter sw = new StringWriter();
        XMLEventWriter writer = outFactory.createXMLEventWriter(sw);
        writer.add(reader);
        writer.flush();
        return new StringReader(sw.getBuffer().toString());
    }

    // If there is DTD declared inside a document , it impossible to override it
    // simplified version of http://stackoverflow.com/questions/1096365/validate-an-xml-file-against-local-dtd-file-with-java
    public class DTDRemover extends EventReaderDelegate
    {
        public DTDRemover(XMLEventReader reader)
        {
            super(reader);
        }
        @Override
        public XMLEvent nextEvent() throws XMLStreamException
        {
            XMLEvent evt = super.nextEvent();
            if(evt.getEventType() == XMLEvent.DTD)
            {
                return super.nextEvent();
            }
            return evt;
        }
    }
}

