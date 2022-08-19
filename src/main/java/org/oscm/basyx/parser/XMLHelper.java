package org.oscm.basyx.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** @author goebel */
public class XMLHelper {

  public static String getAttributeValue(Node node, String name, String defaultValue) {
    Node attr = node.getAttributes().getNamedItem(name);
    if (attr != null) return attr.getNodeValue();
    return defaultValue;
  }

  public static Document convertToDocument(String string)
      throws SAXException, ParserConfigurationException, IOException {

    DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();

    dfactory.setValidating(false);
    dfactory.setIgnoringElementContentWhitespace(true);
    dfactory.setNamespaceAware(true);
    DocumentBuilder builder = dfactory.newDocumentBuilder();

    return builder.parse(new InputSource(new StringReader(string)));
  }

  public static List<Node> getChildrenByTag(Node parent, String tagName) {
    NodeList childs = parent.getChildNodes();
    List<Node> children = new ArrayList<>();
    for (int j = 0; j < childs.getLength(); j++) {
      if (tagName.equals(childs.item(j).getNodeName())) {
        children.add(childs.item(j));
      }
    }

    return children;
  }

  public static List<Element> getElementsByTag(Node node, String tagName) {
    NodeList nl = node.getOwnerDocument().getElementsByTagName(tagName);
    List<Element> result = new ArrayList<>();
    for (int i = 0; i < nl.getLength(); i++) {
      result.add((Element) nl.item(i));
    }
    return result;
  }

  public static Optional<Node> getChildById(Node parent, String tagName, String id) {
    List<Node> children = getChildrenByTag(parent, tagName);
    for (Node child : children) {
      String anId = getAttributeValue(child, "id", "");
      if (anId.equalsIgnoreCase(id)) {
        return Optional.of(child);
      }
    }
    return Optional.empty();
  }

  public static String toString(Document doc, boolean stripBlanks) {
    try {
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      // DOMSource domSource = new DOMSource(doc);

      transformer.setOutputProperty(OutputKeys.METHOD, "xml");

      try {
        transformer.setOutputProperty("http://www.oracle.com/xml/is-standalone", "yes");
      } catch (IllegalArgumentException e) {
        // Might be thrown by JDK versions not implementing the workaround.
      }

      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      StringWriter w = new StringWriter();
      StreamResult result = new StreamResult(w);

      DOMSource source = new DOMSource(doc);
      transformer.transform(source, result);
      String xml = w.toString();
      if (stripBlanks) xml = xml.replaceAll(">\\s+?<", "><");
      return xml;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public static Element createChild(Node node, String name) {
    Element elm = node.getOwnerDocument().createElement(name);
    Node firstNode = null;
    // Find existing child
    NodeList nl = node.getChildNodes();
    for (int i = 0; i < nl.getLength(); i++) {
      Node child = nl.item(i);
      if (child instanceof Element) {
        String aTag = ((Element) child).getTagName();
        if (aTag.equals(name)) {
          firstNode = child;
        }
      }
    }
    if (firstNode != null) {
      elm = (Element) node.insertBefore(elm, firstNode);
    } else {
      elm = (Element) node.appendChild(elm);
    }
    return elm;
  }

  public static void addAttribute(Element pmd, String name, String val) {
    pmd.setAttribute(name, val);
  }

  public static boolean contains(List<Element> list, String name, String val) {
    for (Element n : list) {
      String id = n.getAttribute("id");
      if (!id.equals(name)) continue;
      String aDefault = n.getAttribute("default");
      if (val.equals(aDefault)) return true;
    }
    return false;
  }
}
