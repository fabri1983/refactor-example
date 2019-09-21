package org.fabri1983.refactorexample.joblogger.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import javax.xml.parsers.ParserConfigurationException;

import org.fabri1983.refactorexample.joblogger.category.AllLoggersCategoryTest;
import org.fabri1983.refactorexample.joblogger.category.SupportingCategoryTest;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.SAXException;

@Category({ SupportingCategoryTest.class, AllLoggersCategoryTest.class })
public class XmlDomUtilTest {

	@Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	@Test
	public void whenParsingIncompleteLogXmlFile_thenContentIsRetrievedCorrectly() 
			throws IOException, ParserConfigurationException, SAXException {
		
		// given: expected message
		String expectedMessage = "xml dom util incomplete path";
		
		// given: incomplete xml log structure
		String incompleteLogXml = "<?xml version=\"1.0\" encoding=\"windows-1252\" standalone=\"no\"?>" + 
				"<!DOCTYPE log SYSTEM \"logger.dtd\">" + 
				"<log>" + 
				"<record>" + 
				"  <message>" + expectedMessage + "</message>" + 
				"</record>";
		
		// given: a file which will contain an xml content
		File tempFile = temporaryFolder.newFile("some_file.xml");
		try (BufferedWriter writer = Files.newBufferedWriter(tempFile.toPath(), Charset.forName("UTF8"), StandardOpenOption.APPEND)) {
		    writer.write(incompleteLogXml);
		}
		
		// when: parsing xml to extract message
		String message = XmlDomUtil.getMessagesFromLogXmlFile(tempFile).get(0);
		
		// then: message is the expected
		Assert.assertEquals(expectedMessage, message);
	}
	
	@Test
	public void whenParsingCompleteLogXmlFile_thenContentIsRetrievedCorrectly() 
			throws IOException, ParserConfigurationException, SAXException {
		
		// given: expected message
		String expectedMessage = "xml dom util incomplete path";
		
		// given: incomplete xml log structure
		String completeLogXml = "<?xml version=\"1.0\" encoding=\"windows-1252\" standalone=\"no\"?>" + 
				"<!DOCTYPE log SYSTEM \"logger.dtd\">" + 
				"<log>" + 
				"<record>" + 
				"  <message>" + expectedMessage + "</message>" + 
				"</record>" + 
				"</log>";
		
		// given: a file which will contain an xml content
		File tempFile = temporaryFolder.newFile("some_file.xml");
		try (BufferedWriter writer = Files.newBufferedWriter(tempFile.toPath(), Charset.forName("UTF8"), StandardOpenOption.APPEND)) {
		    writer.write(completeLogXml);
		}
		
		// when: parsing xml to extract message
		String message = XmlDomUtil.getMessagesFromLogXmlFile(tempFile).get(0);
		
		// then: message is the expected
		Assert.assertEquals(expectedMessage, message);
	}
	
}
