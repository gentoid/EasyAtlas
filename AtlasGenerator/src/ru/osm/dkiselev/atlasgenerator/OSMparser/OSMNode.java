package ru.osm.dkiselev.atlasgenerator.OSMparser;


import org.xml.sax.Attributes;

public class OSMNode extends AbstractXMLNode {

	private float lat;
	private float lon;

	public OSMNode(Attributes attributes)/* throws AbstractNodeException*/ {
		String idStr  = attributes.getValue("id");
		String latStr = attributes.getValue("lat");
		String lonStr = attributes.getValue("lon");

//		if (idStr == null || latStr == null || lonStr == null) {
//			throw new AbstractNodeException("Incorrect attributes");
//		}

		id  = Long.parseLong(idStr);
		lat = Float.parseFloat(latStr);
		lon = Float.parseFloat(lonStr);
	}

	public float getLat() {
		return lat;
	}

	public float getLon() {
		return lon;
	}

	public void parseChild(String name, Attributes attributes) {
		if (name.equals("tag")) {
			addTag(attributes.getValue("k"), attributes.getValue("v"));
		}
	}
}
