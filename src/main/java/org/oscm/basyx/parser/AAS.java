/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx.parser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.oscm.basyx.HTTPConnector;
import org.oscm.basyx.model.Endpoint;
import org.oscm.basyx.model.Model;
import org.oscm.basyx.model.SubmodelDescriptorModel;

import com.google.gson.Gson;

/** @author goebel */
public class AAS {

  public static Model[] parse(String json) {
    return new Gson().fromJson(json, Model[].class);
  }

  static Optional<Model> getFirstSubmodel(Model m, String submodelIdShort) {
    Model rs = null;
    for (Model sm : m.submodels) {
      if (submodelIdShort.equals(sm.idShort)) {
        rs = sm;
      }
    }
    return Optional.ofNullable(rs);
  }

  static Optional<String> getSubmodelEndpointForAAS(
      String json, String aasId, String submodelIdShort) {
    final Model[] ms = parse(json);
    String rs = null;
    if (ms != null) {
      List<Model> lm =
          Arrays.stream(ms).filter(m -> aasId.equals(m.idShort)).collect(Collectors.toList());

      if (lm.size() == 1) {
        Model[] sm = lm.get(0).submodels;
        Optional<Model> npm = findSubmodelFromModels(sm, submodelIdShort);
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

  static Optional<Model> findSubmodelFromModels(Model[] sm, String submodelIdShort) {
    final List<Model> ml =
        Arrays.stream(sm)
            .filter(s -> submodelIdShort.equalsIgnoreCase(s.idShort))
            .collect(Collectors.toList());
    if (ml.isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(ml.get(0));
  }

  static Optional<String> getSubmodelEndpoint(
      HTTPConnector conn, String url, String aasId, String submodelIdShort) throws IOException {
    String json = conn.loadFromURL(url);
    return getSubmodelEndpointForAAS(json, aasId, submodelIdShort);
  }

  static Optional<String> getIdentificationEndpoint(HTTPConnector conn, String url, String aasId)
      throws IOException {
    String json = conn.loadFromURL(url);
    return getSubmodelEndpointForAAS(json, aasId, "Identification");
  }

  public static Optional<SubmodelDescriptorModel> getNameplate(HTTPConnector conn, String url, String aasId)
      throws IOException {

    Optional<String> npUrl = getSubmodelEndpoint(conn, url, aasId, "Nameplate");
    if (npUrl.isPresent()) {
      return Nameplate.parseSubmodelDescriptorModel(conn.loadFromURL(npUrl.get()));
    }
    return Optional.empty();
  }

  public static Optional<SubmodelDescriptorModel> getIdentification(
      HTTPConnector conn, String url, String aasId) throws IOException {

    Optional<String> npUrl = getSubmodelEndpoint(conn, url, aasId, "Identification");
    if (npUrl.isPresent()) {
      return Nameplate.parseSubmodelDescriptorModel(conn.loadFromURL(npUrl.get()));
    }
    return Optional.empty();
  }
}
