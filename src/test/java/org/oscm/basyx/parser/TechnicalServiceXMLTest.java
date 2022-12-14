/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/
package org.oscm.basyx.parser;

import org.junit.jupiter.api.Test;
import org.oscm.basyx.model.SubmodelDescriptorModel;
import org.oscm.basyx.oscmmodel.ServiceParameter;
import org.oscm.basyx.oscmmodel.TechnicalServicesXMLMapper;
import org.springframework.context.event.EventListenerMethodProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.oscm.basyx.parser.TechnicalServiceXML.getDefaultServiceTemplate;

public class TechnicalServiceXMLTest {

  @Test
  public void getTSByIdOrDefault() throws IOException, ParserConfigurationException, SAXException {

    // when
    TechnicalServiceXML ts = getDefaultServiceTemplate();
    Optional<Node> node = ts.get("Machine_Rental");

    // then
    assertTrue(node.isPresent(), "Technical Service not found.");
  }

  @Test
  public void update() throws IOException, ParserConfigurationException, SAXException {

    // given
    List<ServiceParameter> parameters = givenParameters();
    String defaultXML = getDefaultServiceTemplate().getSourceXML();
    TechnicalServicesXMLMapper ts = TechnicalServicesXMLMapper.getFrom(defaultXML);

    // when
    TechnicalServicesXMLMapper updated = TechnicalServiceXML.update(ts, parameters, "Machine_Rental_v2");
    TechnicalServiceXML xml = new TechnicalServiceXML(TechnicalServicesXMLMapper.asXML(updated));

    // then
    Optional<Node> tsNode = xml.get("Machine_Rental_v2");
    assertTrue(tsNode.isPresent(), "Technical Service not found.");
    List<Element> nodes = XMLHelper.getElementsByTag(tsNode.get(), "ParameterDefinition");

    // then
    assertTrue(
        XMLHelper.contains(nodes, "ManufactName", "kl_777"), "Property 'ManufactName' not found.");
  }

  List<ServiceParameter> givenParameters() {
    final String json =
        "{\"idShort\":\"Nameplate\",\"identification\":{\"idType\":\"IRI\",\"id\":\"www.company.com/ids/sm/4343_5072_7091_3242\"},\"dataSpecification\":[],\"embeddedDataSpecifications\":[],\"modelType\":{\"name\":\"Submodel\"},\"kind\":\"Instance\",\"submodelElements\":[{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"kl_777\",\"idShort\":\"ManufactName\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO677#002\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"700\",\"idShort\":\"ManufacturerProductDesignation\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAW338#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelElementCollection\"},\"kind\":\"Instance\",\"value\":[{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"DE\",\"idShort\":\"CountryCode\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO730#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\"},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"f\",\"idShort\":\"Street\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO128#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\"},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"73734\",\"idShort\":\"Zip\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO129#002\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\"},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"Esslingen-Berkheim\",\"idShort\":\"CityTown\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO132#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\"},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"Baden-Württemberg\",\"idShort\":\"StateCounty\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO133#002\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\"}],\"ordered\":false,\"allowDuplicates\":false,\"idShort\":\"PhysicalAddress\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"https://www.hsu-hh.de/aut/aas/physicaladdress\",\"idType\":\"IRI\"}]},\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"OVEL-5-H-10-P-VQ4-UA-Z-C-A-V1PNLK-H3\",\"idShort\":\"ManufacturerProductFamily\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAU731#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"anySimpleType\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"JO43\",\"idShort\":\"SerialNumber\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAM556#002\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"nlnlkjoi\",\"idShort\":\"BatchNumber\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAQ196#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"anySimpleType\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"DE\",\"idShort\":\"ProductCountryOfOrigin\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO841#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":2019,\"idShort\":\"YearOfConstruction\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAP906#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"integer\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelElementCollection\"},\"kind\":\"Instance\",\"value\":[{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":false,\"valueId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"0173-1#07-CAA016#001\",\"idType\":\"IRDI\"}]},\"idShort\":\"CEQualificationPresent\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-BAF053#008\",\"idType\":\"IRDI\"}]},\"valueType\":\"boolean\"},{\"modelType\":{\"name\":\"File\"},\"kind\":\"Instance\",\"value\":\"http://localhost:4001/aasServer/files/aasx/Nameplate/marking_ce.png\",\"mimeType\":\"image/png\",\"idShort\":\"File\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAD005#008\",\"idType\":\"IRDI\"}]}}],\"ordered\":false,\"allowDuplicates\":false,\"idShort\":\"Marking_CE\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"https://www.hsu-hh.de/aut/aas/productmarking\",\"idType\":\"IRI\"}]},\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelElementCollection\"},\"kind\":\"Instance\",\"value\":[{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":false,\"valueId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"0173-1#07-CAA016#001\",\"idType\":\"IRDI\"}]},\"idShort\":\"CRUUSLabelingPresent\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAR528#005\",\"idType\":\"IRDI\"}]},\"valueType\":\"boolean\"},{\"modelType\":{\"name\":\"File\"},\"kind\":\"Instance\",\"value\":\"http://localhost:4001/aasServer/files/aasx/Nameplate/marking_cruus.jpg\",\"mimeType\":\"image/png\",\"idShort\":\"File\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAD005#008\",\"idType\":\"IRDI\"}]}}],\"ordered\":false,\"allowDuplicates\":false,\"idShort\":\"Marking_CRUUS\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"https://www.hsu-hh.de/aut/aas/productmarking\",\"idType\":\"IRI\"}]},\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelElementCollection\"},\"kind\":\"Instance\",\"value\":[{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":false,\"idShort\":\"RCMLabelingPresent\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAR528#005\",\"idType\":\"IRDI\"}]},\"valueType\":\"boolean\"},{\"modelType\":{\"name\":\"File\"},\"kind\":\"Instance\",\"value\":\"http://localhost:4001/aasServer/files/aasx/Nameplate/marking_rcm.jpg\",\"mimeType\":\"image/png\",\"idShort\":\"File\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAD005#008\",\"idType\":\"IRDI\"}]}}],\"ordered\":false,\"allowDuplicates\":false,\"idShort\":\"Marking_RCM\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"https://www.hsu-hh.de/aut/aas/productmarking\",\"idType\":\"IRI\"}]},\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"JO43\",\"idShort\":\"SerialNo\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"www.company.com/ids/cd/9544_4082_7091_8596\",\"idType\":\"IRI\"}]},\"valueType\":\"string\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}}],\"semanticId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"https://www.hsu-hh.de/aut/aas/nameplate\",\"idType\":\"IRI\"}]},\"qualifiers\":[]}";
    Optional<SubmodelDescriptorModel> nm = Nameplate.parseSubmodelDescriptorModel(json);
    assertTrue((nm.isPresent()), "Failed parsing nameplate");

    return Nameplate.parseProperties(nm.get()).get();
  }
}
