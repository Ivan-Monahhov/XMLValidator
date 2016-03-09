package com.company;

import com.company.Validators.DTD;
import com.company.Validators.Default;
import com.company.Validators.XSD;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class Main
{
    public static void main(String[] args)
    {
        Config config=null;
        String dtd=null;
        String xsd=null;
        String xml=null;
        String count=null;
        if(args.length==0){printHelp();}
        try
        {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "-xml":
                        xml = args[i + 1];
                        break;
                    case "-xsd":
                        xsd = args[i + 1];
                        break;
                    case "-dtd":
                        dtd = args[i + 1];
                        break;
                    case "-count":
                        count = args[i + 1];
                        break;
                    case "-?":
                        printHelp();
                        break;
                    case "-config":
                        regenConfig();
                        break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e)
        {
            printHelp();
        }
        if(xml==null){printHelp();}
        config = readConfig();
        boolean valid = true;
        String message = "";
        if(dtd==null&&xsd==null)
        {
            try {
                valid = new Default().Validate(xml);
                message = valid?"Valid for internal declaration \n":" Invalid for internal declaration \n";
            } catch (IOException e) {
                message = "File not found or locked";
            }
        }
        if (xsd!=null)
        {
            try {
                valid = new XSD().Validate(xml,xsd);
                message = valid?"Valid for XSD \n":" Invalid for XSD \n";
            } catch (SAXException e) {
                message = "Invalid schema";
            } catch (IOException e) {
                message = "File not found";
            }
        }
        if(dtd!=null)
        {
            valid = new DTD().Validate(xml,dtd);
            message = message + (valid?"Valid for DTD \n":" Invalid for DTD \n");
        }
        int elementCount= -1;
        if(count!=null)
        {
            try {
                elementCount = new Counter().countElements(xml,count);
            } catch (IOException e) {
                message = "File not found";
            } catch (SAXException e) {
                message = "Cant build DOM model";
            } catch (ParserConfigurationException e) {
                message = "Internal problem";
            }
        }
        if(config.isSystemOut())
        {
            System.out.println(message);
            if(count!=null)
            {
                System.out.println("Element '"+count+ "' occurs "+elementCount);
            }
        }
        if(config.isFileReport()) {
            try (PrintWriter out = new PrintWriter(config.getReportSuffix()+xml+config.getReportAppendix()))
            {
                out.println(message);
                if(count!=null)
                {
                    out.println("Element '"+count+ "' occurs "+elementCount);
                }
            }catch (FileNotFoundException e){}

        }
        if (config.isSystemOut())
        {
            if(count!=null)
            {
                System.exit(elementCount);
            }else if(valid==true){System.exit(0);}
            else if (valid==false){System.exit(1);}
        }
        // write your code here
    }

    private static Config readConfig()
    {
        try {
            File file = new File(Config.PATH);
            JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Config config = (Config) jaxbUnmarshaller.unmarshal(file);
            return config;
        }catch (JAXBException e)
        {
            System.out.println("Missing or Invalid Config.xml");
            System.out.println("regenerate with -config");
            System.exit(-1);
        }
        return null;
    }

    private static void regenConfig()
    {
        Config config = new Config();
        try
        {
            File file = new File(Config.PATH);
            JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(config, file);
        }catch (Exception e)
        {}
        System.exit(0);
    }

    private static void printHelp() {
        System.out.println(" Available args");
        System.out.println("'-xml filename.xml' - to set xml file for validation");
        System.out.println(" Optional flags");
        System.out.println("'-xsd filename.xsd' - to set external xsd to validate against");
        System.out.println("'-dtd filename.dtd' - to set external dtd to validate against");
        System.out.println("'-count elementTag' - to get that element count in xml");
        System.out.println("'-config' - to create a default config file");
        System.exit(0);
    }
}
