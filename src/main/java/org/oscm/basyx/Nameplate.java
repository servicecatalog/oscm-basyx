/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2022
 *
 *  Creation Date: 03.08.2022
 *
 *******************************************************************************/

package org.oscm.basyx;

import com.google.gson.Gson;
import org.oscm.basyx.model.NameplateModel;
import org.oscm.basyx.model.SubmodelElement;
import org.oscm.basyx.oscmmodel.ServiceParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** @author goebel */
public class Nameplate {
  static Optional<NameplateModel> parseNameplate(String json) {
    NameplateModel rs = null;

    rs = new Gson().fromJson(json, NameplateModel.class);

    return Optional.ofNullable(rs);
  }

  static Optional<List<ServiceParameter>> parseProperties(NameplateModel npm) {

    List<ServiceParameter> params = new ArrayList<ServiceParameter>();

    SubmodelElement[] se = npm.submodelElements;
    List<SubmodelElement> lse =
        Arrays.asList(se).stream()
            .filter(sm -> "Property".equalsIgnoreCase(sm.modelType.name))
            .collect(Collectors.toList());
    if (!lse.isEmpty()) {

      for (SubmodelElement e : lse) {
        if ("String".equalsIgnoreCase(e.valueType)) {
          ServiceParameter sp = new ServiceParameter(e.valueType, e.value.toString(), e.idShort);
          params.add(sp);
        }
      }
    }
    return Optional.of(params);
  }
}
