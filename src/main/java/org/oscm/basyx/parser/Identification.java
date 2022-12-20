/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx.parser;

import java.util.List;
import java.util.Optional;

import org.oscm.basyx.model.SubmodelDescriptorModel;
import org.oscm.basyx.model.SubmodelElement;

import com.google.gson.Gson;

/** @author goebel */
public class Identification {
  static Optional<SubmodelDescriptorModel> parseIdentificationModel(String json) {
    SubmodelDescriptorModel rs = new Gson().fromJson(json, SubmodelDescriptorModel.class);

    return Optional.ofNullable(rs);
  }

  public static Optional<String> parseManuProductDescription(SubmodelDescriptorModel npm) {
    List<SubmodelElement> lse = Submodel.readProperties(npm);
    if (!lse.isEmpty()) {
      Optional<String> manufacturerProductDescription =
          lse.stream()
              .filter(Identification::include)
              .filter(Identification::includeProductDesc)
              .map(SubmodelElement::getValue)
              .findAny();

      return manufacturerProductDescription;
    }
    return Optional.empty();
  }

  public static Optional<String> parseManuProductName(SubmodelDescriptorModel npm) {

    List<SubmodelElement> lse = Submodel.readProperties(npm);
    if (!lse.isEmpty()) {
      Optional<String> manufacturerProductName =
          lse.stream()
              .filter(Identification::include)
              .filter(Identification::includeProductName)
              .map(SubmodelElement::getValue)
              .findAny();

      return manufacturerProductName;
    }
    return Optional.empty();
  }

  static boolean include(SubmodelElement se) {
    return se.valueType != null && se.valueType.trim().length() > 0;
  }

  static boolean includeProductDesc(SubmodelElement se) {
    return se.idShort != null && ("ManufacturerProductDesignation").equals(se.idShort);
  }

  static boolean includeProductName(SubmodelElement se) {
    return se.idShort != null && ("ManufacturerProductDescription").equals(se.idShort);
  }
}
