package com.csw.assignment.iservice.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.csw.assignment.iservice.XMLJSONConverterI;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class XMLJSONConverterImpl  implements XMLJSONConverterI {
	Document doc;
	public void convertJSONtoXML(File jsonInput, File xmlOutput) throws Exception {
		JsonElement js = JsonParser.parseReader(new FileReader(jsonInput));
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		this.doc = dBuilder.newDocument();
		this.generateXmlDoc(js, null, null);
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(xmlOutput);
        transformer.transform(source, result);
	}
	private void generateXmlDoc(JsonElement js, Element parent, String property) {
		if (js.isJsonObject()) {
			generateObject(js, parent, property);
		} else if (js.isJsonArray()) {
			generateArray(js, parent, property);
		} else if (js.isJsonPrimitive()) {
			JsonPrimitive jp = js.getAsJsonPrimitive();
			if (jp.isBoolean()) {
				generatePrimitive(parent, "boolean", property, jp.getAsBoolean()? "true":"false");
			} else if (jp.isNumber()) {
				generatePrimitive(parent, "number", property, jp.getAsNumber().toString());
			} else if (jp.isJsonNull()) {
				generatePrimitive(parent, "null", property, null);
			} else if (jp.isString()) {
				generatePrimitive(parent, "string", property, jp.getAsString());
			}
		} else if (js.isJsonNull()) {
			generatePrimitive(parent, "null", property, null);
		}
	}
	private void generateObject(JsonElement js, Element parent, String property) {
		Element el = doc.createElement("object");
		JsonObject jo = js.getAsJsonObject();
		if (property != null) {
			Attr attr = doc.createAttribute("name");
	        attr.setValue(property);
	        el.setAttributeNode(attr);
		}
		for(Entry<String, JsonElement> e :jo.entrySet()) {
			generateXmlDoc(e.getValue(), el, e.getKey());
		}

		if (parent == null) {
			doc.appendChild(el);
		} else {
			parent.appendChild(el);
		}
	}
	private void generateArray(JsonElement js, Element parent, String property) {
		Element el = doc.createElement("array");
		if (property != null) {
			Attr attr = doc.createAttribute("name");
	        attr.setValue(property);
	        el.setAttributeNode(attr);
		}
		JsonArray ja = js.getAsJsonArray();
		for (JsonElement je: ja) {
			generateXmlDoc(je, el, null);
		}
		if (parent == null) {
			doc.appendChild(el);
		} else {
			parent.appendChild(el);
		}
	}
	private void generatePrimitive(Element parent, String nodeName, String property, String value) {
		Element el = doc.createElement(nodeName);
		if (property != null) {
			Attr attr = doc.createAttribute("name");
	        attr.setValue(property);
	        el.setAttributeNode(attr);
		}
		if (value != null) {
			el.appendChild(doc.createTextNode(value));
		}
		parent.appendChild(el);
	}

}
