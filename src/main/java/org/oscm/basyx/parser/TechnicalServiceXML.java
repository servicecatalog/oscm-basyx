package org.oscm.basyx.parser;

import org.oscm.basyx.oscmmodel.ServiceParameter;
import org.oscm.basyx.oscmmodel.TechnicalServices;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public class TechnicalServiceXML {
  private Document doc;
  private Node root;
  private String xml;

  TechnicalServiceXML(String xml) throws ParserConfigurationException, IOException, SAXException {
    this.doc = XMLHelper.convertToDocument(xml);
    this.root = doc.getDocumentElement();
    this.xml = xml;
  }

  static TechnicalServiceXML getDefaultServiceTemplate()
      throws IOException, ParserConfigurationException, SAXException {
    final URL resource =
        TechnicalServiceXML.class.getClassLoader().getResource("TechnicalServiceBooking.xml");
    if (resource == null) {
      throw new IllegalArgumentException("file not found!");
    }

    try {
      File xmlFile = new File(resource.toURI());
      String src = readFile(xmlFile);
      return new TechnicalServiceXML(src);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("file not found " + resource.toString());
    }
  }

  private static String readFile(File sourceFile) throws IOException {
    try (BufferedInputStream inBuf = new BufferedInputStream(new FileInputStream(sourceFile));
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream()) {
      byte[] b = new byte[1024];
      int len;
      while ((len = inBuf.read(b)) != -1) {
        outBuf.write(b, 0, len);
      }
      outBuf.flush();
      return outBuf.toString("UTF-8");
    }
  }

  static TechnicalServices update(TechnicalServices ts, List<ServiceParameter> ls, String tsId)
      throws ParserConfigurationException, IOException, SAXException {
    Node tsNode = getTSByIdOrDefault(ts, tsId);
    insert(tsNode, ls);
    TechnicalServices result = new TechnicalServices();
    result.xml = XMLHelper.toString(tsNode.getOwnerDocument(), false);
    return result;
  }

  static Node getTSByIdOrDefault(TechnicalServices ts, String id)
      throws ParserConfigurationException, IOException, SAXException {
    TechnicalServiceXML xml = new TechnicalServiceXML(ts.xml);
    Optional<Node> service = xml.get(id);
    if (!service.isPresent()) {
      service = getDefaultServiceTemplate().get(id);
    }
    return service.get();
  }

  private static void insert(Node node, List<ServiceParameter> serviceParams) {
    List<Node> list = XMLHelper.getChildrenByTag(node, "ParameterDefinition");
    for (ServiceParameter sp : serviceParams) {

      if (!XMLHelper.contains(list, "id", sp.name)) {
        Element pmd = XMLHelper.createChild(node, "ParameterDefinition");
        XMLHelper.addAttribute(pmd, "configurable", "true");
        XMLHelper.addAttribute(pmd, "valueType", "String");
        XMLHelper.addAttribute(pmd, "id", sp.name);
        XMLHelper.addAttribute(pmd, "default", sp.defaultValue);
      }
    }
  }

  Optional<Node> get(String tsID) {
    return XMLHelper.getChildById(root, "tns:TechnicalService", tsID);
  }

  private Node getRoot() {
    return root;
  }

  String getSourceXML() {
    return xml;
  }
}
