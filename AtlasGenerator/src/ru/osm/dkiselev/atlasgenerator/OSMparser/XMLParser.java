package ru.osm.dkiselev.atlasgenerator.OSMparser;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.osm.dkiselev.atlasgenerator.configs.Config;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLParser extends DefaultHandler {

	private AbstractXMLNode xmlNode;

	private List<AbstractXMLNode> xmlNodes = new ArrayList<>();

	public void parse() throws ParserConfigurationException, SAXException, IOException {

		String file = Config.get("Parser", "osm-file");

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(file, this);
	}

	@Override
	public void startElement(String ns, String localName, String elName, Attributes attributes) throws SAXException {
		if (elName.equals("node") || elName.equals("way") || elName.equals("relation")) {
			try {
				xmlNode = AbstractXMLNode.getNew(elName, attributes);
				xmlNodes.add(xmlNode);
			} catch (AbstractNodeException ignored) {}
		} else {
			if (xmlNode != null) {
				xmlNode.parseChild(elName, attributes);
			}
		}
	}

	@Override
	public void endElement(String ns, String localName, String elName) {
		if (xmlNode != null && xmlNode.getNodeType().equals(elName)) {
			xmlNode = null;
		}
	}

}
