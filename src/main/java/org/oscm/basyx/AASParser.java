/**
 * ******************************************************************************
 *
 * <p>Copyright FUJITSU LIMITED 2022
 *
 * <p>*****************************************************************************
 */
package org.oscm.basyx;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.oscm.basyx.model.Endpoint;
import org.oscm.basyx.model.Model;
import org.oscm.basyx.model.NameplateModel;

import com.google.gson.Gson;

/** @author goebel */
public class AASParser {

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

  static Optional<NameplateModel> parseNameplate(String json) {
    NameplateModel rs = null;

    rs = new Gson().fromJson(json, NameplateModel.class);

    return Optional.ofNullable(rs);
  }

  static Optional<String> getFirstNameplateEndpoint(String json) {

    Model[] ms = parse(json);

    List<Endpoint> l =
        Arrays.asList(ms).stream()
            .map(m -> m.endpoints)
            .map(e -> e[0])
            .collect(Collectors.toList());
    String rs = null;
    if (!l.isEmpty()) {
      rs = l.get(0).address;
    }
    return Optional.ofNullable(rs);
  }

  static Optional<String> getNameplateEndpointForAAS(String json, String aasId) {
    final Model[] ms = parse(json);
    List<Model> lm =
        Arrays.asList(ms).stream()
            .filter(m -> aasId.equals(m.idShort))
            .collect(Collectors.toList());
    String rs = null;
    if (lm.size() == 1) {
      Endpoint[] es = lm.get(0).endpoints;
      if (es.length > 0) {
        rs = es[0].address;
      }
    }
    return Optional.ofNullable(rs);
  }

  static Optional<String> getFirstNameplateEndpoint(HTTPConnector conn, String url)
      throws IOException {
    String json = conn.loadFromURL(url);
    return getFirstNameplateEndpoint(json);
  }

  static Optional<String> getNameplateEndpoint(HTTPConnector conn, String url, String aasId)
      throws IOException {
    String json = conn.loadFromURL(url);
    return getNameplateEndpointForAAS(json, aasId);
  }

  static Optional<NameplateModel> getNameplateModel(HTTPConnector conn, String url, String aasId)
      throws IOException {

    Optional<String> npUrl = getNameplateEndpoint(conn, url, aasId);
    if (npUrl.isPresent()) {
      return parseNameplate(conn.loadFromURL(npUrl.get()));
    }
    return Optional.empty();
  }
}
