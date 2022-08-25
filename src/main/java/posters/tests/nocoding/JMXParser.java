package posters.tests.nocoding;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import org.apache.jmeter.exceptions.IllegalUserActionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.ArrayList;


public class JMXParser {



//    public JMXParser() throws ParserConfigurationException, IOException, SAXException {
//
//    }

    public static ArrayList<String> parseProperty(String expression) throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
        File xmlFile = new File("src/test/resources/check_sfra.jmx");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        XPath xPath = XPathFactory.newInstance().newXPath();

//        String expression = "//HTTPSamplerProxy/stringProp[@name='HTTPSampler.path']/text()";
        NodeList matches = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

        ArrayList<String> propsToStrings = new ArrayList<>();
        for (int i=0; i < matches.getLength();i++) {
            propsToStrings.add(i,matches.item(i).getTextContent());
        }

        return propsToStrings;
        }

    public static void replaceSelected(String file, String replaced, String replacement) throws IOException, ConfigurationException {

        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties()
                                .setFileName(file));
        Configuration config = builder.getConfiguration();
        config.setProperty(replaced, replacement);
        builder.save();
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, IllegalUserActionException, ConfigurationException {

       // NodeList usernames = parseProperty("//AuthManager/collectionProp/elementProp/stringProp[@name='Authorization.username']/text()");

//        System.out.print(parseProperty("//AuthManager/collectionProp/elementProp/stringProp[@name='Authorization.username']/text()"));
//        System.out.print(parseProperty("//AuthManager/collectionProp/elementProp/stringProp[@name='Authorization.password']/text()"));
//        System.out.print(parseProperty("//AuthManager/collectionProp/elementProp/stringProp[@name='Authorization.url']/text()"));
//
        String baseUrl = parseProperty("//Arguments/collectionProp/elementProp[@name='BASE_URL_1']/stringProp[@name='Argument.value']/text()").toString();
        String credentialUsername = parseProperty("//AuthManager/collectionProp/elementProp/stringProp[@name='Authorization.username']/text()").toString();
        String credentialPsswrd = parseProperty("//AuthManager/collectionProp/elementProp/stringProp[@name='Authorization.password']/text()").toString();
        String host = parseProperty("//AuthManager/collectionProp/elementProp/stringProp[@name='Authorization.url']/text()").toString();
        String lang = parseProperty("//HTTPSamplerProxy/collectionProp/elementProp[@name='lang']/stringProp[@name='Argument.value']/text()").toString();
        ArrayList<String> links = parseProperty("//HTTPSamplerProxy/stringProp[@name='HTTPSampler.path']/text()");
        String homepage="";
        for (String link : links){
            if (link.contains("home"))
                homepage = link;

        }
        System.out.println(homepage);
//        if (parseProperty("//HTTPSamplerProxy/stringProp[@name='HTTPSampler.path']/text()").toString().contains("home")) {
//            homepage = parseProperty("//HTTPSamplerProxy/stringProp[@name='HTTPSampler.path']/text()").toString();
//        }

        //System.out.println(new File(".").getAbsolutePath());


       replaceSelected("./config/project.properties", "host", host);
    }
}


