package ru.osm.dkiselev.atlasgenerator.OSMparser;

import org.xml.sax.Attributes;

import java.util.HashMap;
import java.util.Map;

abstract public class AbstractXMLNode {

	protected long id;
	protected String nodeType;
	protected Map<String, String> tags = new HashMap<>();

	public abstract void parseChild(String name, Attributes attributes);

	public static AbstractXMLNode getNew(String nodeType, Attributes attributes) throws AbstractNodeException {
		AbstractXMLNode node;
		if (nodeType.equals("node")) {
			node = new OSMNode(attributes);
		}
		else if (nodeType.equals("way")) {
			node = new OSMWay(attributes);
		}
		else if (nodeType.equals("relation")) {
			node = new OSMRelation(attributes);
		}
		else {
			throw new AbstractNodeException("Wrong node type");
		}
		node.setNodeType(nodeType);

		return node;
	}

	public long getId() {
		return id;
	}

	public String getNodeType() {
		return nodeType;
	}

	public String getTag(String key) {
		String value = null;
		if (tags.containsKey(key)) {
			value = tags.get(key);
		}
		return value;
	}

	protected void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	protected void addTag(String k, String v) {
		tags.put(k, v);
	}

}

