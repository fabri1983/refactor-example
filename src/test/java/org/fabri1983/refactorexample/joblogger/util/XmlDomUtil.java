package org.fabri1983.refactorexample.joblogger.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlDomUtil {

	public static String getFirstMessageFromXmlFile(File file) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		
		// disable validation of dtd since we don't have it in our class path
		factory.setValidating(false);
		factory.setFeature("http://xml.org/sax/features/namespaces", false);
		factory.setFeature("http://xml.org/sax/features/validation", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		
		// we need to close the root <log> element since the file is still open at this point in time
		String fileContent = new String(Files.readAllBytes(file.toPath()), Charset.forName("UTF8")).trim();
		if (!fileContent.endsWith("</log>")) {
			fileContent += "</log>";
		}
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new InputSource(new StringReader(fileContent)));
		
		NodeList nList = document.getElementsByTagName("message");
		return nList.item(0).getTextContent();
	}
	
}
