package net.cloudfeeds.test;

import java.io.IOException;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;





public class FeedTest {

	public static void main(String[] args) throws IOException {
		XMLOutputter outputer = new XMLOutputter();
		Element element = new Element("root");
		element.addContent(new Element("html").addContent(new CDATA("<content>nanan</content>")));
		outputer.output(new Document(element),System.out);
	}
}
