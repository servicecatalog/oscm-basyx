package org.oscm.basyx.parser;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;

import org.oscm.basyx.oscmmodel.ServiceParameterBasyx;
import org.oscm.basyx.oscmmodel.TechnicalServicesXMLMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

public class TechnicalServiceXML {
  private final Node root;
  private final String xml;

  TechnicalServiceXML(String xml) throws ParserConfigurationException, IOException, SAXException {
    Document doc = XMLHelper.convertToDocument(xml);
    this.root = doc.getDocumentElement();
    this.xml = xml;
  }

  public static TechnicalServiceXML getDefaultServiceTemplate()
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
      throw new IllegalArgumentException("file not found " + resource);
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

  public static TechnicalServicesXMLMapper update(
      TechnicalServicesXMLMapper ts, List<ServiceParameterBasyx> ls, String tsId)
      throws ParserConfigurationException, IOException, SAXException {
    Node tsNode = getTSByIdOrDefault(ts, tsId);
    insert(tsNode, ls);
    final String xml = XMLHelper.toString(tsNode.getOwnerDocument(), false);
    return TechnicalServicesXMLMapper.getFrom(xml);
  }

  static Node getTSByIdOrDefault(TechnicalServicesXMLMapper ts, String id)
      throws ParserConfigurationException, IOException, SAXException {
    TechnicalServiceXML xml = new TechnicalServiceXML(TechnicalServicesXMLMapper.asXML(ts));
    Optional<Node> service = xml.get(id);
    if (!service.isPresent()) {
      service = getDefaultServiceTemplate().get("Machine_Rental");
      if (!service.isPresent()) {
        throw new RuntimeException(
            String.format("TS mit id=%s not found in default service template!", id));
      }
      Element elm = (Element) service.get();
      XMLHelper.addAttribute(elm, "id", id);
      return elm;
    }

    return service.get();
  }

  private static void insert(Node node, List<ServiceParameterBasyx> serviceParams) {
    List<Element> list = XMLHelper.getElementsByTag(node, "ParameterDefinition");

    for (ServiceParameterBasyx sp : serviceParams) {
      if (!XMLHelper.contains(list, "id", sp.name)) {
        Element pmd = XMLHelper.createChild(node, "ParameterDefinition");
        XMLHelper.addAttribute(pmd, "configurable", "true");
        XMLHelper.addAttribute(pmd, "valueType", "STRING");
        XMLHelper.addAttribute(pmd, "id", sp.name);
        XMLHelper.addAttribute(pmd, "default", sp.defaultValue);
        Element ldElm = XMLHelper.createChild(pmd, "LocalizedDescription");
        XMLHelper.addAttribute(ldElm, "locale", "en");
      }
    }
  }

  Optional<Node> get(String tsID) {
    return XMLHelper.getChildById(root, "tns:TechnicalService", tsID);
  }

  public String getSourceXML() {
    return xml;
  }

  public static Optional<TechnicalServicesXMLMapper> parseJson(String json) {
    try {
      TechnicalServicesXMLMapper rs = new Gson().fromJson(json, TechnicalServicesXMLMapper.class);
      return Optional.ofNullable(rs);
    } catch (Throwable e) {
      throw new IllegalStateException(json, e);
    }
  }
}
