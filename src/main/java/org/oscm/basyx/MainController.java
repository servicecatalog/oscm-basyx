/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2022
 *
 *  Creation Date: 29.07.2022
 *
 *******************************************************************************/

package org.oscm.basyx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.oscm.basyx.model.NameplateModel;
import org.oscm.basyx.oscmmodel.ServiceParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

  @GetMapping(value = "/json/{aasId}", produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
  public ResponseEntity<String> index(@PathVariable String aasId) {

    String aasShortId = aasId;
    try {
      aasShortId = URLDecoder.decode(aasId, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }

    try {
      Optional<NameplateModel> npm = AASParser.getNameplateModel(conn, registryUrl, aasShortId);
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

    String aasShortId = aasId;
    try {
      aasShortId = URLDecoder.decode(aasId, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }

    try {
      Optional<NameplateModel> npm = AASParser.getNameplateModel(conn, registryUrl, aasShortId);
      if (npm.isPresent()) {
        Optional<List<ServiceParameter>> parList = Nameplate.parseProperties(npm.get());

        String tsId = "TS_" + aasShortId;

        if (!parList.isPresent()) {
          final String xml = "";
          return ResponseEntity.ok().body(xml);
        }
      }
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  private String toJson(NameplateModel nameplate) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(nameplate);
  }
}
