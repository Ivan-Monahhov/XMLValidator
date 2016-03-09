package com.company.Validators;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

/**
 * Created by Ivan on 3/9/2016.
 */
// For USE with External XSD
// http://docs.oracle.com/javase/1.5.0/docs/api/javax/xml/validation/package-summary.html - source , dont use DOMSource
public class XSD
{
    // IOException is thrown for non existent files , SAXException is for invalid schema
    public boolean Validate (String xml , String xsd) throws IOException,SAXException
    {
        StreamSource document = new StreamSource(new File(xml));
        // this is the only XMLConstraint for which SchemaFactory exists by default
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source schemaFile = new StreamSource(new File(xsd));
        Schema schema = factory.newSchema(schemaFile);
        Validator validator = schema.newValidator();
        try
        {
            validator.validate(document);
        } catch (SAXException e)
        {
            return false;
        }
        return true;
    }
}
