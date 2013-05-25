package com.neodem.starman;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Text;

public class XomHelper {

	public static Element makeElement(String name) {
		return new Element(name);
	}

	public static void addAttribute(Element e, String name, String value) {
		e.addAttribute(new Attribute(name, value));
	}

	public static void addChildElement(Element e, String name) {
		e.appendChild(makeElement(name));
	}

	public static void addChildElement(Element root, String name, String value) {
		Element e = new Element(name);
		e.appendChild(value);
		root.appendChild(e);
	}

	public static void addChildHtmlElement(Element root, String name, String value) {
		Element e = new Element(name);
		Text t = new Text(value);
		e.appendChild(t);
		root.appendChild(e);
	}

}
