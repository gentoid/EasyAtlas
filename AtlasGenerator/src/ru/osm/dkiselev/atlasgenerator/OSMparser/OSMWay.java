package ru.osm.dkiselev.atlasgenerator.OSMparser;


import org.xml.sax.Attributes;

import java.util.List;

public class OSMWay extends AbstractXMLNode {

	private List<OSMNode> nodes;

	public OSMWay(Attributes attributes) {
		id = Long.parseLong(attributes.getValue("id"));
	}

	public void parseChild(String name, Attributes attributes) {
		if (name.equals("tag")) {
			addTag(attributes.getValue("k"), attributes.getValue("v"));
		}
		else if (name.equals("nd")) {
			addNode(Long.parseLong(attributes.getValue("ref")));
		}
	}

	public List<OSMNode> getNodes() {
		return nodes;
	}

	private void addNode(long nodeId) {
		// todo: implement this
//		nodes.add();
	}

}
