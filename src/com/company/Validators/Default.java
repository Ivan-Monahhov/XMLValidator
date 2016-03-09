package com.company.Validators;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by Ivan on 3/9/2016.
 */
// Default will validate against any Constraint inside the document
public class Default
{
    // IOException for file not found and etc
    public boolean Validate(String xml) throws IOException
    {
        try
        {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setValidating(true);
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            // Validation will throw an SAXException on builder.parse(xml) line
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
                }
                @Override
                public void error(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
                }
                @Override
                public void fatalError(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
                }
            });
            Document doc = builder.parse(xml);
        }catch (org.xml.sax.SAXException e)
        {
            return false;
        }
        catch (ParserConfigurationException ex)
        {
            // Something is wrong with DocumentBuilderFactory
            System.exit(-1);

        }
        return true;
    }
}
