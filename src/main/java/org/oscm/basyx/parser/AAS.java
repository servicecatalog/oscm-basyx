/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx.parser;

import com.google.gson.Gson;
import org.oscm.basyx.HTTPConnector;
import org.oscm.basyx.model.Endpoint;
import org.oscm.basyx.model.Model;
import org.oscm.basyx.model.NameplateModel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** @author goebel */
public class AAS {

  public static Model[] parse(String json) {
    return new Gson().fromJson(json, Model[].class);
  }

  static Optional<Model> getFirstNameplate(Model m) {
    Model rs = null;
    for (Model sm : m.submodels) {
      if ("Nameplate".equals(sm.idShort)) {
        rs = sm;
      }
    }
    return Optional.ofNullable(rs);
  }

  static Optional<String> getNameplateEndpointForAAS(String json, String aasId) {
    final Model[] ms = parse(json);
    String rs = null;
    if (ms != null) {
      List<Model> lm =
          Arrays.stream(ms).filter(m -> aasId.equals(m.idShort)).collect(Collectors.toList());


      if (lm.size() == 1) {
        Model[] sm = lm.get(0).submodels;
        Optional<Model> npm = findNameplateFromModels(sm);
        if (npm.isPresent()) {
          Endpoint[] eps = npm.get().endpoints;
          if (eps.length > 0) {
            rs = eps[0].address;
          }
        }
      }
    }
    return Optional.ofNullable(rs);
  }

  static Optional<Model> findNameplateFromModels(Model[] sm) {
    final List<Model> ml =
        Arrays.stream(sm)
            .filter(s -> "Nameplate".equalsIgnoreCase(s.idShort))
            .collect(Collectors.toList());
    if (ml.isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(ml.get(0));
  }

  static Optional<String> getNameplateEndpoint(HTTPConnector conn, String url, String aasId)
      throws IOException {
    String json = conn.loadFromURL(url);
    return getNameplateEndpointForAAS(json, aasId);
  }

  public static Optional<NameplateModel> getNameplate(HTTPConnector conn, String url, String aasId)
      throws IOException {

    Optional<String> npUrl = getNameplateEndpoint(conn, url, aasId);
    if (npUrl.isPresent()) {
      return Nameplate.parseNameplate(conn.loadFromURL(npUrl.get()));
    }
    return Optional.empty();
  }
}
