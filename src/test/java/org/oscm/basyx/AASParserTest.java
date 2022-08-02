/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2022
 *
 *  Creation Date: 29.07.2022
 *
 *******************************************************************************/

package org.oscm.basyx;

import org.junit.jupiter.api.Test;
import org.oscm.basyx.model.Model;
import org.oscm.basyx.model.NameplateModel;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

/** @author goebel */
public class AASParserTest {

  @Test
  public void parse_simple() {
    // given
    String json = givenAASJson();

    // when
    AASParser.parse(json);
  }

  @Test
  public void getFirstNameplate() {

    // given
    final String json = givenAASJson();
    Model[] ms = AASParser.parse(json);

    assertTrue(ms.length > 0);

    // when
    Optional<Model> np = AASParser.getFirstNameplate(ms[0]);

    // then
    assertTrue(np.isPresent());
  }

  @Test
  public void parseNameplate() {
    // given
    final String json = givenNameplateJson();

    // when
    Optional<NameplateModel> nm = AASParser.parseNameplate(json);

    // then
    assertTrue(nm.isPresent());
  }

  @Test
  public void getNameplateEndpointForAAS() {
    // given
    final String json = givenAASJson();

    // when
    Optional<String> address = AASParser.getNameplateEndpointForAAS(json, "Festo_3S7PM0CP4BD");

    // then
    assertTrue(address.isPresent());
    assertTrue(address.get().contains("aasServer"));
  }

  String givenAASJson() {
    return "[{\"modelType\":{\"name\":\"AssetAdministrationShellDescriptor\"},\"idShort\":\"Festo_3S7PM0CP4BD\",\"identification\":{\"idType\":\"IRI\",\"id\":\"smart.festo.com/demo/aas/1/1/454576463545648365874\"},\"endpoints\":[{\"type\":\"http\",\"address\":\"http://estessbesci10:9081/aasServer/shells/smart.festo.com%2Fdemo%2Faas%2F1%2F1%2F454576463545648365874/aas\"}],\"asset\":{\"modelType\":{\"name\":\"Asset\"},\"dataSpecification\":[],\"embeddedDataSpecifications\":[],\"idShort\":\"\",\"identification\":{\"idType\":\"IRDI\",\"id\":\"\"},\"kind\":\"Instance\"},\"submodels\":[{\"modelType\":{\"name\":\"SubmodelDescriptor\"},\"idShort\":\"DeviceDescriptionFiles\",\"identification\":{\"idType\":\"IRI\",\"id\":\"smart.festo.com/demo/sm/instance/1/1/13B7CCD9BF7A3F24\"},\"endpoints\":[{\"type\":\"http\",\"address\":\"http://estessbesci10:9081/aasServer/shells/smart.festo.com%2Fdemo%2Faas%2F1%2F1%2F454576463545648365874/aas/submodels/DeviceDescriptionFiles/submodel\"}],\"semanticId\":{\"keys\":[{\"type\":\"Submodel\",\"local\":false,\"value\":\"http://admin-shell/sample/submodel/type/device-description-files\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelDescriptor\"},\"idShort\":\"Document\",\"identification\":{\"idType\":\"IRI\",\"id\":\"www.company.com/ids/sm/2543_5072_7091_2660\"},\"endpoints\":[{\"type\":\"http\",\"address\":\"http://estessbesci10:9081/aasServer/shells/smart.festo.com%2Fdemo%2Faas%2F1%2F1%2F454576463545648365874/aas/submodels/Document/submodel\"}],\"semanticId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"https://www.hsu-hh.de/aut/aas/document\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelDescriptor\"},\"idShort\":\"Nameplate\",\"identification\":{\"idType\":\"IRI\",\"id\":\"www.company.com/ids/sm/4343_5072_7091_3242\"},\"endpoints\":[{\"type\":\"http\",\"address\":\"http://estessbesci10:9081/aasServer/shells/smart.festo.com%2Fdemo%2Faas%2F1%2F1%2F454576463545648365874/aas/submodels/Nameplate/submodel\"}],\"semanticId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"https://www.hsu-hh.de/aut/aas/nameplate\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelDescriptor\"},\"idShort\":\"Identification\",\"identification\":{\"idType\":\"IRI\",\"id\":\"www.company.com/ids/sm/6563_5072_7091_4267\"},\"endpoints\":[{\"type\":\"http\",\"address\":\"http://estessbesci10:9081/aasServer/shells/smart.festo.com%2Fdemo%2Faas%2F1%2F1%2F454576463545648365874/aas/submodels/Identification/submodel\"}],\"semanticId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"https://www.hsu-hh.de/aut/aas/identification\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelDescriptor\"},\"idShort\":\"Service\",\"identification\":{\"idType\":\"IRI\",\"id\":\"www.company.com/ids/sm/6053_5072_7091_5102\"},\"endpoints\":[{\"type\":\"http\",\"address\":\"http://estessbesci10:9081/aasServer/shells/smart.festo.com%2Fdemo%2Faas%2F1%2F1%2F454576463545648365874/aas/submodels/Service/submodel\"}],\"semanticId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"https://www.hsu-hh.de/aut/aas/service\",\"idType\":\"IRI\"}]}}]}]";
  }

  String givenNameplateJson() {
    return "{\"idShort\":\"Nameplate\",\"identification\":{\"idType\":\"IRI\",\"id\":\"www.company.com/ids/sm/4343_5072_7091_3242\"},\"dataSpecification\":[],\"embeddedDataSpecifications\":[],\"modelType\":{\"name\":\"Submodel\"},\"kind\":\"Instance\",\"submodelElements\":[{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"kl_777\",\"idShort\":\"ManufactName\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO677#002\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"700\",\"idShort\":\"ManufacturerProductDesignation\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAW338#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelElementCollection\"},\"kind\":\"Instance\",\"value\":[{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"DE\",\"idShort\":\"CountryCode\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO730#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\"},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"f\",\"idShort\":\"Street\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO128#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\"},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"73734\",\"idShort\":\"Zip\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO129#002\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\"},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"Esslingen-Berkheim\",\"idShort\":\"CityTown\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO132#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\"},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"Baden-Württemberg\",\"idShort\":\"StateCounty\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO133#002\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\"}],\"ordered\":false,\"allowDuplicates\":false,\"idShort\":\"PhysicalAddress\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"https://www.hsu-hh.de/aut/aas/physicaladdress\",\"idType\":\"IRI\"}]},\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"OVEL-5-H-10-P-VQ4-UA-Z-C-A-V1PNLK-H3\",\"idShort\":\"ManufacturerProductFamily\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAU731#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"anySimpleType\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"JO43\",\"idShort\":\"SerialNumber\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAM556#002\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"nlnlkjoi\",\"idShort\":\"BatchNumber\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAQ196#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"anySimpleType\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"DE\",\"idShort\":\"ProductCountryOfOrigin\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO841#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":2019,\"idShort\":\"YearOfConstruction\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAP906#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"integer\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelElementCollection\"},\"kind\":\"Instance\",\"value\":[{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":false,\"valueId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"0173-1#07-CAA016#001\",\"idType\":\"IRDI\"}]},\"idShort\":\"CEQualificationPresent\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-BAF053#008\",\"idType\":\"IRDI\"}]},\"valueType\":\"boolean\"},{\"modelType\":{\"name\":\"File\"},\"kind\":\"Instance\",\"value\":\"http://localhost:4001/aasServer/files/aasx/Nameplate/marking_ce.png\",\"mimeType\":\"image/png\",\"idShort\":\"File\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAD005#008\",\"idType\":\"IRDI\"}]}}],\"ordered\":false,\"allowDuplicates\":false,\"idShort\":\"Marking_CE\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"https://www.hsu-hh.de/aut/aas/productmarking\",\"idType\":\"IRI\"}]},\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelElementCollection\"},\"kind\":\"Instance\",\"value\":[{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":false,\"valueId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"0173-1#07-CAA016#001\",\"idType\":\"IRDI\"}]},\"idShort\":\"CRUUSLabelingPresent\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAR528#005\",\"idType\":\"IRDI\"}]},\"valueType\":\"boolean\"},{\"modelType\":{\"name\":\"File\"},\"kind\":\"Instance\",\"value\":\"http://localhost:4001/aasServer/files/aasx/Nameplate/marking_cruus.jpg\",\"mimeType\":\"image/png\",\"idShort\":\"File\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAD005#008\",\"idType\":\"IRDI\"}]}}],\"ordered\":false,\"allowDuplicates\":false,\"idShort\":\"Marking_CRUUS\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"https://www.hsu-hh.de/aut/aas/productmarking\",\"idType\":\"IRI\"}]},\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelElementCollection\"},\"kind\":\"Instance\",\"value\":[{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":false,\"idShort\":\"RCMLabelingPresent\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAR528#005\",\"idType\":\"IRDI\"}]},\"valueType\":\"boolean\"},{\"modelType\":{\"name\":\"File\"},\"kind\":\"Instance\",\"value\":\"http://localhost:4001/aasServer/files/aasx/Nameplate/marking_rcm.jpg\",\"mimeType\":\"image/png\",\"idShort\":\"File\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAD005#008\",\"idType\":\"IRDI\"}]}}],\"ordered\":false,\"allowDuplicates\":false,\"idShort\":\"Marking_RCM\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"https://www.hsu-hh.de/aut/aas/productmarking\",\"idType\":\"IRI\"}]},\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"JO43\",\"idShort\":\"SerialNo\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"www.company.com/ids/cd/9544_4082_7091_8596\",\"idType\":\"IRI\"}]},\"valueType\":\"string\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}}],\"semanticId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"https://www.hsu-hh.de/aut/aas/nameplate\",\"idType\":\"IRI\"}]},\"qualifiers\":[]}";
  }
}
