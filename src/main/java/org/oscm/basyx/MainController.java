/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.oscm.basyx.model.NameplateModel;
import org.oscm.basyx.oscmmodel.ServiceParameter;
import org.oscm.basyx.oscmmodel.TechnicalServices;
import org.oscm.basyx.parser.AAS;
import org.oscm.basyx.parser.Nameplate;
import org.oscm.basyx.parser.TechnicalServiceXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Optional;

/** @author goebel */
@Controller
public class MainController {

  @Autowired HTTPConnector conn;

  @Value("${BASYX_REGISTRY_URL}")
  protected String registryUrl;

  @Value("${REST_URL}")
  protected String restUrl;

  @GetMapping(value = "/json/{aasId}", produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
  public ResponseEntity<String> index(@PathVariable String aasId) {

    String aasShortId;
    try {
      aasShortId = URLDecoder.decode(aasId, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }

    try {
      Optional<NameplateModel> npm = AAS.getNameplate(conn, registryUrl, aasShortId);
      if (npm.isPresent()) {
        final String json = toJson(npm.get());
        return ResponseEntity.ok().body(json);
      }
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping(value = "/xml/{aasId}", produces = MediaType.APPLICATION_PROBLEM_XML_VALUE)
  public ResponseEntity<String> xml(@PathVariable String aasId) {

    String aasShortId;
    try {
      aasShortId = URLDecoder.decode(aasId, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }

    try {
      Optional<NameplateModel> npm = AAS.getNameplate(conn, registryUrl, aasShortId);
      if (npm.isPresent()) {
        Optional<List<ServiceParameter>> parList = Nameplate.parseProperties(npm.get());
        if (parList.isPresent()) {
          String json = conn.loadFromURL(restUrl + "/v1/technicalservices/xml");

          Optional<TechnicalServices> ts = TechnicalServiceXML.parseJson(json);
          if (ts.isPresent()) {
            final TechnicalServices updated =
                TechnicalServiceXML.update(ts.get(), parList.get(), aasShortId);

            String updatedXML = TechnicalServices.asXML(updated);
            return ResponseEntity.ok().body(updatedXML);
          }
        }
      }
      return ResponseEntity.notFound().build();

    } catch (IOException | ParserConfigurationException | SAXException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  private String toJson(NameplateModel nameplate) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(nameplate);
  }

  private String toJson(TechnicalServices services) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(services);
  }
}
