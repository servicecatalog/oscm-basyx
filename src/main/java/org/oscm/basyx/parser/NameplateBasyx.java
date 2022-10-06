/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.basyx.submodel.metamodel.api.ISubmodel;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.dataelement.IProperty;
import org.oscm.basyx.oscmmodel.ServiceParameterBasyx;

/** @author farmaki */
public class NameplateBasyx {

  public static Optional<List<ServiceParameterBasyx>> parseProperties(ISubmodel npm) {

    List<ServiceParameterBasyx> params = new ArrayList<>();

    Map<String, IProperty> smPropertiesMap = npm.getProperties();
    List<IProperty> lse = new ArrayList<>(smPropertiesMap.values());

    //    prop.getValue() : OK
    //    prop.getValueType() : OK
    //    prop.getCategory() : OK
    //    prop.getIdShort() : OK

    //    prop.getKind(); : not yet supported
    //    prop.getModelType(); : not yet supported
    //    prop.getSemanticId(); : not yet supported

    if (!lse.isEmpty()) {
      params =
          lse.stream()
              .filter(NameplateBasyx::include)
              .map(ServiceParameterBasyx::new)
              .collect(Collectors.toList());
    }
    return Optional.of(params);
  }

  static boolean include(IProperty smProp) {
    return smProp.getValueType() != null && smProp.getValueType().toString().trim().length() > 0;
  }
}
