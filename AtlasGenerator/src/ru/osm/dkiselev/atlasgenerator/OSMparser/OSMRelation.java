package ru.osm.dkiselev.atlasgenerator.OSMparser;


import org.xml.sax.Attributes;

public class OSMRelation extends AbstractXMLNode {

	public OSMRelation(Attributes attributes) {
		// todo
	}

	public void parseChild(String nodeType, Attributes attributes) {
		if (nodeType.equals("tag")) {
			addTag(attributes.getValue("k"), attributes.getValue("v"));
		}
		else if (nodeType.equals("member")) {
			String ref = attributes.getValue("ref");
			if (ref != null) {
				//
			}
		}
	}

}
