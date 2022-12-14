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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;

import org.oscm.basyx.Exceptions.AccessDenied;
import org.oscm.basyx.Exceptions.InternalError;
import org.oscm.basyx.Exceptions.NotFound;
import org.oscm.basyx.model.SubmodelDescriptorModel;
import org.oscm.basyx.oscmmodel.MarketableServiceInfo;
import org.oscm.basyx.oscmmodel.MarketableServiceMapper;
import org.oscm.basyx.oscmmodel.ServiceParameter;
import org.oscm.basyx.oscmmodel.TechnicalServicesMapper;
import org.oscm.basyx.oscmmodel.TechnicalServicesXMLMapper;
import org.oscm.basyx.parser.AAS;
import org.oscm.basyx.parser.Identification;
import org.oscm.basyx.parser.Nameplate;
import org.oscm.basyx.parser.TechnicalServiceXML;
import org.oscm.basyx.parser.TechnicalServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.util.StringUtils;
import org.xml.sax.SAXException;

/** @author goebel */
@Controller
public class TechnicalServiceController {

  @Autowired HTTPConnector conn;

  @Value("${AAS_REGISTRY_URL}")
  protected String registryUrl;

  @Value("${REST_URL}")
  protected String restUrl;

  static final String XML_API_URI = "/v1/technicalservices/xml/";

  @GetMapping(
      value = "/techservice/json/{aasId}",
      produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
  public ResponseEntity<String> exportAsJson(@PathVariable String aasId) {
    try {
      final String[] auth = checkAccess();
      final String aasShortId = decode(aasId);

      SubmodelDescriptorModel npm = getNameplateModel(aasShortId);
      Optional<List<ServiceParameter>> parList = Nameplate.parseProperties(npm);
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

  @GetMapping(value = "/mservice/json/{aasId}", produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
  public ResponseEntity<String> exportServiceAsJson(@PathVariable String aasId) {
    try {
      final String[] auth = checkAccess();

      final String aasShortId = decode(aasId);

      SubmodelDescriptorModel npm = getNameplateModel(aasShortId);
      Optional<List<ServiceParameter>> parList = Nameplate.parseProperties(npm);

      Optional<String> serviceName = Optional.empty();
      Optional<String> serviceDescription = Optional.empty();
      Optional<String> serviceShortDescription = Optional.empty();
      SubmodelDescriptorModel identificationModel = getIdentificationModel(aasShortId);
      if (identificationModel != null) {

        serviceName = Identification.parseManuProductName(identificationModel);

        serviceDescription = Identification.parseManuProductDescription(identificationModel);

        serviceShortDescription = serviceDescription;
      }
      if (parList.isPresent()) {
        final String serviceJson =
            buildMarketableServiceAsJson(
                aasShortId,
                parList.get(),
                serviceName,
                serviceDescription,
                serviceShortDescription);
        if (serviceJson != null) return ResponseEntity.ok().body(serviceJson);

        throw NotFound(String.format("Got  from :\n %s", registryUrl));
      }

    } catch (NotFound | AccessDenied | InternalError ade) {
      return asResponseEntity(ade);
    }

    return ResponseEntity.ok().body("Done.");
  }

  @GetMapping(
      value = "/techservice/xml/{aasId}",
      produces = MediaType.APPLICATION_PROBLEM_XML_VALUE)
  public ResponseEntity<String> exportAsXml(@PathVariable String aasId) {
    try {
      final String[] auth = checkAccess();
      final String aasShortId = decode(aasId);

      SubmodelDescriptorModel npm = getNameplateModel(aasShortId);
      Optional<List<ServiceParameter>> parList = Nameplate.parseProperties(npm);
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

  @GetMapping(value = "/techservice/id/{tsId}", produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
  public ResponseEntity<String> getTechnicalServiceKey(@PathVariable String tsId) {
    final String[] auth = checkAccess();
    final String tsIdClear = decode(tsId);

    final String tsApiUrl = getApiUrl(auth, "/v1/technicalservices/");

    try {
      String json = conn.loadFromURL(tsApiUrl);

      if (is401(json)) {
        throw AccessDenied("Authentication failed for " + "/v1/technicalservices/");
      }
      Optional<TechnicalServicesMapper> tsMap = TechnicalServices.parseJson(json);
      if (tsMap.isPresent()) {
        Optional<Long> ts = tsMap.get().getIdByTsName(tsIdClear);
        if (ts.isPresent()) {
          return ResponseEntity.ok().body(String.valueOf(ts.get()));
        }
      }
      throw NotFound(String.format("Got nothing from :\n %s", "/v1/technicalservices/"));
    } catch (NotFound | AccessDenied | InternalError ade) {
      return asResponseEntity(ade);
    }
  }

  private String decode(@PathVariable String aasId) {
    try {
      return URLDecoder.decode(aasId, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  String[] checkAccess() {
    Optional<String[]> auth = getAuthData(TokenHolder.get());
    if (!auth.isPresent()) throw AccessDenied("Authentication failed.");
    return auth.get();
  }

  SubmodelDescriptorModel getNameplateModel(String id) {
    Optional<SubmodelDescriptorModel> npm = Optional.empty();
    try {
      npm = AAS.getNameplate(conn, registryUrl, id);
    } catch (IOException e) {
      // log
    }
    if (!npm.isPresent()) {
      throw NotFound(String.format("Nameplate not found for %s", id));
    }
    return npm.get();
  }

  SubmodelDescriptorModel getIdentificationModel(String id) {
    Optional<SubmodelDescriptorModel> identSubmodel = Optional.empty();
    try {
        identSubmodel = AAS.getIdentification(conn, registryUrl, id);
    } catch (IOException e) {
      // log
    }
    if (!identSubmodel.isPresent()) {
      return null;
    }
    return identSubmodel.get();
  }

  private Optional<String> buildTechnicalServices(
      String aasShortId, List<ServiceParameter> parList, String tsApiUrl) {

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

  private String buildMarketableServiceAsJson(
      String aasShortId,
      List<ServiceParameter> parList,
      Optional<String> serviceName,
      Optional<String> shortDesc,
      Optional<String> description) {
    MarketableServiceInfo serviceInfo = new MarketableServiceInfo();
    serviceInfo.setTechnicalServiceId(aasShortId);
    serviceInfo.setServiceId(aasShortId);
    
    if (serviceName.isPresent()) {
      serviceInfo.setServiceName(serviceName.get());
    }
    else {
        serviceInfo.setServiceName(aasShortId);
    }

    if (description.isPresent()) {
      serviceInfo.setServiceDescription(description.get());
    }
    if (shortDesc.isPresent()) {
      serviceInfo.setServiceShortDescription(shortDesc.get());
    }

    serviceInfo.insert(parList);
    return MarketableServiceMapper.toJson(serviceInfo);
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
