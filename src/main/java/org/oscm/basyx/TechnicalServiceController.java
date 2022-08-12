/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.oscm.basyx.model.NameplateModel;
import org.oscm.basyx.oscmmodel.ServiceParameter;
import org.oscm.basyx.oscmmodel.TechnicalServices;
import org.oscm.basyx.parser.AAS;
import org.oscm.basyx.parser.Nameplate;
import org.oscm.basyx.parser.TechnicalServiceXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.thymeleaf.util.StringUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.oscm.basyx.Exceptions.InternalError;
import static org.oscm.basyx.Exceptions.*;

/** @author goebel */
@Controller
public class TechnicalServiceController {

  @Autowired HTTPConnector conn;

  @Value("${AAS_REGISTRY_URL}")
  protected String registryUrl;

  @Value("${REST_URL}")
  protected String restUrl;

  @GetMapping(
      value = "/techservice/json/{aasId}",
      produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
  public ResponseEntity<String> exportAsJson(@PathVariable String aasId) {
    try {
      final String[] auth = checkAccess();
      final String aasShortId = decode(aasId);

      try {
        NameplateModel npm = getNameplateModel(aasShortId);
        Optional<List<ServiceParameter>> parList = Nameplate.parseProperties(npm);
        if (parList.isPresent()) {
          final String tsApiUrl = getApiUrl(auth, aasShortId);
          Optional<String> updatedXML = buildTechnicalServices(aasShortId, parList.get(), tsApiUrl);

          if (updatedXML.isPresent()) {
            final String json = toJson(TechnicalServices.getFrom(updatedXML.get()));
            return ResponseEntity.ok().body(json);
          }
          throw NotFound(String.format("Got nothing from :\n %s", tsApiUrl));
        }

      } catch (NotFound | AccessDenied ade) {
        return asResponseEntity(ade);
      } catch (IOException | ParserConfigurationException | SAXException e) {
        throw InternalError(e);
      }
    } catch (InternalError e) {
      return asResponseEntity(e);
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

      try {
        NameplateModel npm = getNameplateModel(aasShortId);
        Optional<List<ServiceParameter>> parList = Nameplate.parseProperties(npm);
        if (parList.isPresent()) {
          final String tsApiUrl = getApiUrl(auth, aasShortId);
          Optional<String> updatedXML = buildTechnicalServices(aasShortId, parList.get(), tsApiUrl);
          if (updatedXML.isPresent()) {
            return ResponseEntity.ok().body(updatedXML.get());
          }
          throw NotFound(String.format("Got nothing from :\n %s", tsApiUrl));
        }

      } catch (NotFound | AccessDenied ade) {
        return Exceptions.asResponseEntity(ade);
      } catch (IOException | ParserConfigurationException | SAXException e) {
        throw InternalError(e);
      }
    } catch (InternalError e) {
      return asResponseEntity(e);
    }
    return ResponseEntity.ok().body("Done.");
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

  NameplateModel getNameplateModel(String id) {
    Optional<NameplateModel> npm = Optional.empty();
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

  private Optional<String> buildTechnicalServices(
      String aasShortId, List<ServiceParameter> parList, String tsApiUrl)
      throws IOException, ParserConfigurationException, SAXException {

    String json = conn.loadFromURL(tsApiUrl);

    if (StringUtils.isEmpty(json)) {
      json = buildJsonEmptyTemplate();
    }

    if (is401(json)) {
      throw AccessDenied("Authentication failed for " + tsApiUrl);
    }

    Optional<TechnicalServices> ts = TechnicalServiceXML.parseJson(json);
    if (ts.isPresent()) {
      final TechnicalServices updated = TechnicalServiceXML.update(ts.get(), parList, aasShortId);
      return Optional.of(TechnicalServices.asXML(updated));
    }
    return Optional.empty();
  }

  String buildJsonEmptyTemplate() {
    return "{ \n"+
      "\"items\": [\n" +
      "{\n" +
      "  \"technicalServiceXml\": \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><tns:TechnicalServices xmlns:tns=\\\"oscm.serviceprovisioning/1.9/TechnicalService.xsd\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xsi:schemaLocation=\\\"oscm.serviceprovisioning/1.9/TechnicalService.xsd ../../oscm-serviceprovisioning/javares/TechnicalServices.xsd\\\"></tns:TechnicalServices>\" " +
      "}\n" +
    "]\n"+
   "}";
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

  String getApiUrl(String[] auth, String tsId) {
    String prefix = String.format("https://%s:%s@", auth[0], auth[1]);
    String baseUrl = restUrl.replaceFirst("https://", prefix);
    return baseUrl + "/v1/technicalservices/xml/"+ tsId;
  }

  private String toJson(TechnicalServices services) {
    Gson gson =  new GsonBuilder().disableHtmlEscaping().create();
    String jsonData = gson.toJson(services);

    JsonElement jsonElement = new JsonParser().parse(jsonData);
    String json = gson.toJson(jsonElement);
    return jsonData;
  }
}
