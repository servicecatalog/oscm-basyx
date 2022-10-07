/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx;

import static org.oscm.basyx.Exceptions.AccessDenied;
import static org.oscm.basyx.Exceptions.InternalError;
import static org.oscm.basyx.Exceptions.NotFound;
import static org.oscm.basyx.Exceptions.asResponseEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.basyx.aas.metamodel.map.descriptor.ModelUrn;
import org.eclipse.basyx.submodel.metamodel.api.ISubmodel;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;
import org.oscm.basyx.Exceptions.AccessDenied;
import org.oscm.basyx.Exceptions.InternalError;
import org.oscm.basyx.Exceptions.NotFound;
import org.oscm.basyx.oscmmodel.ServiceParameterBasyx;
import org.oscm.basyx.oscmmodel.TechnicalServicesXMLMapper;
import org.oscm.basyx.parser.AASBasyx;
import org.oscm.basyx.parser.NameplateBasyx;
import org.oscm.basyx.parser.TechnicalServiceXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.util.StringUtils;
import org.xml.sax.SAXException;

/** @author farmaki */
@Controller
public class TechnicalServiceControllerBasyx {

  @Autowired HTTPConnector conn;

  @Value("${AAS_REGISTRY_URL}")
  protected String registryUrl;

  @Value("${REST_URL}")
  protected String restUrl;

  static final String XML_API_URI = "/v1/technicalservices/xml/";

  @GetMapping(value = "/techservice/json/**", produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
  public ResponseEntity<String> exportAsJson(HttpServletRequest request) {
    try {
      final String[] auth = checkAccess();

      String aasId = getAasIdFromRequestURL(request);

      final ModelUrn aasIdentifier = new ModelUrn(aasId);
      String aasShortId = aasIdentifier.getId();
      ISubmodel npm = getNameplateSubmodel(aasIdentifier);

      Optional<List<ServiceParameterBasyx>> parList = NameplateBasyx.parseProperties(npm);
      if (parList.isPresent()) {
        final String tsApiUrl = getApiUrl(auth);
        Optional<String> updatedXML = buildTechnicalServices(aasShortId, parList.get(), tsApiUrl);

        if (updatedXML.isPresent()) {
          TechnicalServicesXMLMapper servicesList =
              TechnicalServicesXMLMapper.getFrom(updatedXML.get());
          final String json = TechnicalServicesXMLMapper.toJson(servicesList);
          return ResponseEntity.ok().body(json);
        }
        throw NotFound(String.format("Got nothing from :\n %s", XML_API_URI));
      }

    } catch (NotFound | AccessDenied | InternalError ade) {
      return asResponseEntity(ade);
    }

    return ResponseEntity.ok().body("Done.");
  }

  @GetMapping(value = "/techservice/xml/**", produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
  public ResponseEntity<String> exportAsXml(HttpServletRequest request) {
    try {
      final String[] auth = checkAccess();

      String aasId = getAasIdFromRequestURL(request);

      final ModelUrn aasIdentifier = new ModelUrn(aasId);
      String aasShortId = aasIdentifier.getId();
      ISubmodel npm = getNameplateSubmodel(aasIdentifier);

      Optional<List<ServiceParameterBasyx>> parList = NameplateBasyx.parseProperties(npm);
      if (parList.isPresent()) {
        final String tsApiUrl = getApiUrl(auth);
        Optional<String> updatedXML = buildTechnicalServices(aasShortId, parList.get(), tsApiUrl);
        if (updatedXML.isPresent()) {
          return ResponseEntity.ok().body(updatedXML.get());
        }
        throw NotFound(String.format("Got nothing from :\n %s", XML_API_URI));
      }

    } catch (NotFound | AccessDenied | InternalError ade) {
      return asResponseEntity(ade);
    }

    return ResponseEntity.ok().body("Done.");
  }
   

  /**
   * @param request
   * @return
   */
  private String getAasIdFromRequestURL(HttpServletRequest request) {
    String requestURL = request.getRequestURL().toString();
    String aasId = requestURL.split("/techservice/json/")[1];
    return aasId;
  }

  private Optional<String> buildTechnicalServices(
      String aasShortId, List<ServiceParameterBasyx> parList, String tsApiUrl) {

    String json = conn.loadFromURL(tsApiUrl);

    if (StringUtils.isEmpty(json)) {
      json = buildJsonEmptyTemplate();
    }

    if (is401(json)) {
      throw AccessDenied("Authentication failed for OSCM API endpoint " + XML_API_URI);
    }

    Optional<TechnicalServicesXMLMapper> ts = TechnicalServiceXML.parseJson(json);
    if (ts.isPresent()) {
      try {
        final TechnicalServicesXMLMapper updated =
            TechnicalServiceXML.update(ts.get(), parList, aasShortId);
        return Optional.of(TechnicalServicesXMLMapper.asXML(updated));
      } catch (IOException | SAXException | ParserConfigurationException e) {
        throw InternalError(e);
      }
    }
    return Optional.empty();
  }

  @GetMapping(value = "/nameplateProductName/**")
  public ResponseEntity<String> getManufacturerProductName(HttpServletRequest request) {
    try {
      final String[] auth = checkAccess();

      String requestURL = request.getRequestURL().toString();

      String aasId = requestURL.split("/nameplateProductName/")[1];

      final ModelUrn aasIdentifier = new ModelUrn(aasId);

      ISubmodel npmSubmodel = getNameplateSubmodel(aasIdentifier);
      Optional<String> manufacturerProductName =
          AASBasyx.readPropertyFromModel(npmSubmodel, "ManufactName");

      if (manufacturerProductName.isPresent()) {
        return ResponseEntity.ok().body("Manufacturer product name is." + manufacturerProductName);
      }

    } catch (AccessDenied | InternalError ade) {
      return ResponseEntity.ok().body("Error has happened.");
    }

    return ResponseEntity.ok().body("Done.");
  }

  String[] checkAccess() {
    Optional<String[]> auth = getAuthData(TokenHolder.get());
    if (!auth.isPresent()) throw new AccessDenied("Authentication failed.");
    return auth.get();
  }

  ISubmodel getNameplateSubmodel(IIdentifier aasIdentifier) {
    Optional<ISubmodel> npm = Optional.empty();
    try {
      npm = AASBasyx.getNameplate(registryUrl, aasIdentifier);
    } catch (IOException e) {
      // log
    }
    if (!npm.isPresent()) {
      throw NotFound(String.format("Nameplate not found for %s", aasIdentifier.getId()));
    }
    return npm.get();
  }

  String buildJsonEmptyTemplate() {
    return "{ \n"
        + "\"items\": [\n"
        + "{\n"
        + "  \"technicalServiceXml\": \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><tns:TechnicalServices xmlns:tns=\\\"oscm.serviceprovisioning/1.9/TechnicalService.xsd\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xsi:schemaLocation=\\\"oscm.serviceprovisioning/1.9/TechnicalService.xsd ../../oscm-serviceprovisioning/javares/TechnicalServices.xsd\\\"></tns:TechnicalServices>\" "
        + "}\n"
        + "]\n"
        + "}";
  }

  boolean is401(String msg) {
    return msg.contains("HTTP Status 401");
  }

  Optional<String[]> getAuthData(final String auth) {
    if (auth != null && auth.toUpperCase().startsWith("BASIC")) {
      byte[] credBytes = Base64.getDecoder().decode(auth.substring(5).trim());
      return Optional.of(new String(credBytes, StandardCharsets.UTF_8).split(":", 2));
    }
    return Optional.empty();
  }

  String getApiUrl(String[] auth, String path) {
    String prefix = String.format("https://%s:%s@", auth[0], auth[1]);
    String baseUrl = restUrl.replaceFirst("https://", prefix);
    return baseUrl + path;
  }

  String getApiUrl(String[] auth) {
    return getApiUrl(auth, XML_API_URI);
  }
}
