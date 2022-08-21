/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.stubbing.Answer;
import org.oscm.basyx.oscmmodel.TechnicalServicesXMLMapper;
import org.oscm.basyx.parser.TechnicalServiceXML;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/** @author goebel */
public class TechnicalServiceControllerTest {
  TechnicalServiceController technicalServiceController;
  HTTPConnector hc = mock(HTTPConnector.class);
  HttpServletRequest request;
  final String AAS_JSON =
      "[{\"modelType\":{\"name\":\"AssetAdministrationShellDescriptor\"},\"idShort\":\"Festo_3S7PM0CP4BD\",\"identification\":{\"idType\":\"IRI\",\"id\":\"smart.festo.com/demo/aas/1/1/454576463545648365874\"},\"endpoints\":[{\"type\":\"http\",\"address\":\"http://estessbesci10:9081/aasServer/shells/smart.festo.com%2Fdemo%2Faas%2F1%2F1%2F454576463545648365874/aas\"}],\"asset\":{\"modelType\":{\"name\":\"Asset\"},\"dataSpecification\":[],\"embeddedDataSpecifications\":[],\"idShort\":\"\",\"identification\":{\"idType\":\"IRDI\",\"id\":\"\"},\"kind\":\"Instance\"},\"submodels\":[{\"modelType\":{\"name\":\"SubmodelDescriptor\"},\"idShort\":\"DeviceDescriptionFiles\",\"identification\":{\"idType\":\"IRI\",\"id\":\"smart.festo.com/demo/sm/instance/1/1/13B7CCD9BF7A3F24\"},\"endpoints\":[{\"type\":\"http\",\"address\":\"http://estessbesci10:9081/aasServer/shells/smart.festo.com%2Fdemo%2Faas%2F1%2F1%2F454576463545648365874/aas/submodels/DeviceDescriptionFiles/submodel\"}],\"semanticId\":{\"keys\":[{\"type\":\"Submodel\",\"local\":false,\"value\":\"http://admin-shell/sample/submodel/type/device-description-files\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelDescriptor\"},\"idShort\":\"Document\",\"identification\":{\"idType\":\"IRI\",\"id\":\"www.company.com/ids/sm/2543_5072_7091_2660\"},\"endpoints\":[{\"type\":\"http\",\"address\":\"http://estessbesci10:9081/aasServer/shells/smart.festo.com%2Fdemo%2Faas%2F1%2F1%2F454576463545648365874/aas/submodels/Document/submodel\"}],\"semanticId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"https://www.hsu-hh.de/aut/aas/document\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelDescriptor\"},\"idShort\":\"Nameplate\",\"identification\":{\"idType\":\"IRI\",\"id\":\"www.company.com/ids/sm/4343_5072_7091_3242\"},\"endpoints\":[{\"type\":\"http\",\"address\":\"http://estessbesci10:9081/aasServer/shells/smart.festo.com%2Fdemo%2Faas%2F1%2F1%2F454576463545648365874/aas/submodels/Nameplate/submodel\"}],\"semanticId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"https://www.hsu-hh.de/aut/aas/nameplate\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelDescriptor\"},\"idShort\":\"Identification\",\"identification\":{\"idType\":\"IRI\",\"id\":\"www.company.com/ids/sm/6563_5072_7091_4267\"},\"endpoints\":[{\"type\":\"http\",\"address\":\"http://estessbesci10:9081/aasServer/shells/smart.festo.com%2Fdemo%2Faas%2F1%2F1%2F454576463545648365874/aas/submodels/Identification/submodel\"}],\"semanticId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"https://www.hsu-hh.de/aut/aas/identification\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelDescriptor\"},\"idShort\":\"Service\",\"identification\":{\"idType\":\"IRI\",\"id\":\"www.company.com/ids/sm/6053_5072_7091_5102\"},\"endpoints\":[{\"type\":\"http\",\"address\":\"http://estessbesci10:9081/aasServer/shells/smart.festo.com%2Fdemo%2Faas%2F1%2F1%2F454576463545648365874/aas/submodels/Service/submodel\"}],\"semanticId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"https://www.hsu-hh.de/aut/aas/service\",\"idType\":\"IRI\"}]}}]}]";
  final String NAMEPLATE_JSON =
      "{\"idShort\":\"Nameplate\",\"identification\":{\"idType\":\"IRI\",\"id\":\"www.company.com/ids/sm/4343_5072_7091_3242\"},\"dataSpecification\":[],\"embeddedDataSpecifications\":[],\"modelType\":{\"name\":\"Submodel\"},\"kind\":\"Instance\",\"submodelElements\":[{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"kl_777\",\"idShort\":\"ManufactName\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO677#002\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"700\",\"idShort\":\"ManufacturerProductDesignation\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAW338#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelElementCollection\"},\"kind\":\"Instance\",\"value\":[{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"DE\",\"idShort\":\"CountryCode\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO730#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\"},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"f\",\"idShort\":\"Street\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO128#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\"},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"73734\",\"idShort\":\"Zip\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO129#002\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\"},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"Esslingen-Berkheim\",\"idShort\":\"CityTown\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO132#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\"},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"Baden-Württemberg\",\"idShort\":\"StateCounty\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO133#002\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\"}],\"ordered\":false,\"allowDuplicates\":false,\"idShort\":\"PhysicalAddress\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"https://www.hsu-hh.de/aut/aas/physicaladdress\",\"idType\":\"IRI\"}]},\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"OVEL-5-H-10-P-VQ4-UA-Z-C-A-V1PNLK-H3\",\"idShort\":\"ManufacturerProductFamily\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAU731#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"anySimpleType\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"JO43\",\"idShort\":\"SerialNumber\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAM556#002\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"nlnlkjoi\",\"idShort\":\"BatchNumber\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAQ196#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"anySimpleType\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"DE\",\"idShort\":\"ProductCountryOfOrigin\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAO841#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"string\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":2019,\"idShort\":\"YearOfConstruction\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAP906#001\",\"idType\":\"IRDI\"}]},\"valueType\":\"integer\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelElementCollection\"},\"kind\":\"Instance\",\"value\":[{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":false,\"valueId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"0173-1#07-CAA016#001\",\"idType\":\"IRDI\"}]},\"idShort\":\"CEQualificationPresent\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-BAF053#008\",\"idType\":\"IRDI\"}]},\"valueType\":\"boolean\"},{\"modelType\":{\"name\":\"File\"},\"kind\":\"Instance\",\"value\":\"http://localhost:4001/aasServer/files/aasx/Nameplate/marking_ce.png\",\"mimeType\":\"image/png\",\"idShort\":\"File\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAD005#008\",\"idType\":\"IRDI\"}]}}],\"ordered\":false,\"allowDuplicates\":false,\"idShort\":\"Marking_CE\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"https://www.hsu-hh.de/aut/aas/productmarking\",\"idType\":\"IRI\"}]},\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelElementCollection\"},\"kind\":\"Instance\",\"value\":[{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":false,\"valueId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"0173-1#07-CAA016#001\",\"idType\":\"IRDI\"}]},\"idShort\":\"CRUUSLabelingPresent\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAR528#005\",\"idType\":\"IRDI\"}]},\"valueType\":\"boolean\"},{\"modelType\":{\"name\":\"File\"},\"kind\":\"Instance\",\"value\":\"http://localhost:4001/aasServer/files/aasx/Nameplate/marking_cruus.jpg\",\"mimeType\":\"image/png\",\"idShort\":\"File\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAD005#008\",\"idType\":\"IRDI\"}]}}],\"ordered\":false,\"allowDuplicates\":false,\"idShort\":\"Marking_CRUUS\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"https://www.hsu-hh.de/aut/aas/productmarking\",\"idType\":\"IRI\"}]},\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"SubmodelElementCollection\"},\"kind\":\"Instance\",\"value\":[{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":false,\"idShort\":\"RCMLabelingPresent\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAR528#005\",\"idType\":\"IRDI\"}]},\"valueType\":\"boolean\"},{\"modelType\":{\"name\":\"File\"},\"kind\":\"Instance\",\"value\":\"http://localhost:4001/aasServer/files/aasx/Nameplate/marking_rcm.jpg\",\"mimeType\":\"image/png\",\"idShort\":\"File\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"0173-1#02-AAD005#008\",\"idType\":\"IRDI\"}]}}],\"ordered\":false,\"allowDuplicates\":false,\"idShort\":\"Marking_RCM\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"https://www.hsu-hh.de/aut/aas/productmarking\",\"idType\":\"IRI\"}]},\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}},{\"modelType\":{\"name\":\"Property\"},\"kind\":\"Instance\",\"value\":\"JO43\",\"idShort\":\"SerialNo\",\"category\":\"PARAMETER\",\"qualifiers\":[],\"semanticId\":{\"keys\":[{\"type\":\"ConceptDescription\",\"local\":true,\"value\":\"www.company.com/ids/cd/9544_4082_7091_8596\",\"idType\":\"IRI\"}]},\"valueType\":\"string\",\"parent\":{\"keys\":[{\"type\":\"Submodel\",\"local\":true,\"value\":\"www.company.com/ids/sm/4343_5072_7091_3242\",\"idType\":\"IRI\"}]}}],\"semanticId\":{\"keys\":[{\"type\":\"GlobalReference\",\"local\":false,\"value\":\"https://www.hsu-hh.de/aut/aas/nameplate\",\"idType\":\"IRI\"}]},\"qualifiers\":[]}";
  String mockJsonTechnicalServices =
      "{\"items\": [{\"technicalServiceId\": \"AppSampleService\",\"id\": 10000},{\"technicalServiceId\": \"NoneSenseService\",\"id\": 1000},{\"technicalServiceId\": \"Festo_3S7PM0CP4BD\",\"id\": 10002}]}";
  final String user = "user1";
  final String pass = "secret";

  @BeforeEach
  public void setup() throws Exception {
    doReturn(AAS_JSON).when(hc).loadFromURL(contains("registry"));
    doReturn(NAMEPLATE_JSON).when(hc).loadFromURL(contains("aasServer"));
    technicalServiceController = new TechnicalServiceController();
    technicalServiceController.conn = hc;
    technicalServiceController.registryUrl = "http://registry:4001/registry/api/v1/registry";
    technicalServiceController.restUrl = "https://myserver/oscm-rest-api";
    request = mock(HttpServletRequest.class);
    final String authCoded = getBase64AuthString(user, pass);
    doReturn(authCoded).when(request).getHeader(eq("Authorization"));
    TokenHolder.set(getBase64AuthString(user, pass));
  }

  @Test
  void json() throws Exception {
    // given
    mockApiXMLResponse();
    // when
    ResponseEntity<String> resp = technicalServiceController.exportAsJson("Festo_3S7PM0CP4BD");

    // then
    assertEquals(HttpStatus.OK, resp.getStatusCode());
    System.out.println(resp.getBody());
    assertTrue(Objects.requireNonNull(resp.getBody()).contains("Festo_3S7PM0CP4BD"));
  }

  @Test
  void json_401_return_Unauthorized() throws Exception {
    // given
    mockApi401Response();
    // when
    ResponseEntity<String> resp = technicalServiceController.exportAsJson("Festo_3S7PM0CP4BD");

    // then
    assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
    System.out.println(resp.getBody());
  }

  @Test
  void json_Empty_returns_Not_Found() throws Exception {
    // given
    mockApiEmptyResponse();
    // when
    ResponseEntity<String> resp = technicalServiceController.exportAsJson("Festo_3S7PM0CP4BD");

    // then
    assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    System.out.println(resp.getBody());
  }

  @Test
  void xml() throws Exception {
    // given
    mockApiXMLResponse();

    // when
    ResponseEntity<String> resp = technicalServiceController.exportAsXml("Festo_3S7PM0CP4BD");

    // then
    assertEquals(HttpStatus.OK, resp.getStatusCode());
    System.out.println(resp.getBody());
    assertTrue(resp.getBody().contains("Festo_3S7PM0CP4BD"));
  }

  @Test
  void getTechnicalServiceKey() throws Exception {
    // given
    mockApiJsonResponse();

    // when
    ResponseEntity<String> resp =
        technicalServiceController.getTechnicalServiceKey("Festo_3S7PM0CP4BD");

    // then
    assertEquals(HttpStatus.OK, resp.getStatusCode());
    System.out.println(resp.getBody());
    assertTrue(resp.getBody().contains("10002"));
  }

  void buildJsonEmptyTemplate() {
    // when
    technicalServiceController.buildJsonEmptyTemplate();
  }

  private void mockApiXMLResponse() throws IOException, ParserConfigurationException, SAXException {
    final String xml = TechnicalServiceXML.getDefaultServiceTemplate().getSourceXML();
    final String restResponse = toJson(TechnicalServicesXMLMapper.getFrom(xml));

    when(hc.loadFromURL(ArgumentMatchers.contains("xml")))
        .thenAnswer(
            (Answer<String>)
                invocation -> {
                  String url = (String) invocation.getArguments()[0];
                  assertAuthentication(url, user + ":" + pass);
                  return restResponse;
                });
  }

  private void mockApi401Response() {
    when(hc.loadFromURL(ArgumentMatchers.contains("xml")))
        .thenAnswer(
            (Answer<String>)
                invocation -> {
                  String url = (String) invocation.getArguments()[0];
                  assertAuthentication(url, user + ":" + pass);
                  return "<html>HTTP Status 401</html>";
                });
  }

  private void mockApiEmptyResponse() {
    when(hc.loadFromURL(ArgumentMatchers.contains("registry")))
            .thenAnswer(
                    (Answer<String>)
                            invocation -> {
                              String url = (String) invocation.getArguments()[0];
                              return "";
                            });
  }

  private void mockApiJsonResponse()
      throws IOException, ParserConfigurationException, SAXException {

    when(hc.loadFromURL(ArgumentMatchers.contains("technicalservices")))
        .thenAnswer(
            (Answer<String>)
                invocation -> {
                  String url = (String) invocation.getArguments()[0];
                  assertAuthentication(url, user + ":" + pass);
                  return mockJsonTechnicalServices;
                });
  }

  private String toJson(TechnicalServicesXMLMapper services) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(services);
  }

  private void assertAuthentication(String url, String expectedAuth) {
    String regs = "^https\\://(.*\\:.*)@.*";
    Pattern p = Pattern.compile(regs);
    Matcher m = p.matcher(url);

    assertTrue(m.matches());
    String authStr = m.group(1);
    assertEquals(expectedAuth, authStr);
  }

  String getBase64AuthString(String user, String pwd) {
    String cred = String.format("%s:%s", user, pwd);
    byte[] credBytes = Base64.getEncoder().encode(cred.getBytes(StandardCharsets.UTF_8));
    return String.format("basic %s", new String(credBytes, StandardCharsets.UTF_8));
  }
}
