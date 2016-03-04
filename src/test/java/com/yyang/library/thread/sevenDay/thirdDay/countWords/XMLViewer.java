package com.yyang.library.thread.sevenDay.thirdDay.countWords;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;


public class XMLViewer {
	
public static void main(String[] args) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
	XMLEventReader reader;
	String fileName = "/Users/young_mac/Documents/workbench/enwiki.xml";
	reader = XMLInputFactory.newInstance().createXMLEventReader(new FileInputStream(fileName));

	try {
		XMLEvent event;
		while(true) {
			event = reader.nextEvent();
			if(event.isStartElement()) {
				String name = event.asStartElement().getName().getLocalPart();
				System.out.println(String.format("<%s>", name));
			} else if (event.isEndElement()){
				String name = event.asEndElement().getName().getLocalPart();
				System.out.println(String.format("</%s>", name));
			} else if(event.isStartDocument()){
			} else {
				System.out.println(event.asCharacters().getData());
			}
			
		}
		
	} catch (Exception e) {
		throw new NoSuchElementException();
	}
}

}
