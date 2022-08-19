/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx.parser;

import com.google.gson.Gson;
import org.oscm.basyx.model.NameplateModel;
import org.oscm.basyx.model.SubmodelElement;
import org.oscm.basyx.oscmmodel.ServiceParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** @author goebel */
public class Nameplate {
  static Optional<NameplateModel> parseNameplate(String json) {
    NameplateModel rs = new Gson().fromJson(json, NameplateModel.class);

    return Optional.ofNullable(rs);
  }

  public static Optional<List<ServiceParameter>> parseProperties(NameplateModel npm) {

    List<ServiceParameter> params = new ArrayList<>();

    List<SubmodelElement> lse = Submodel.readProperties(npm);
    if (!lse.isEmpty()) {
      params =
          lse.stream()
              .filter(Nameplate::include)
              .map(ServiceParameter::new)
              .collect(Collectors.toList());
    }
    return Optional.of(params);
  }

  static boolean  include(SubmodelElement se) {
    return se.valueType != null && se.valueType.trim().length() > 0;
  }
}
