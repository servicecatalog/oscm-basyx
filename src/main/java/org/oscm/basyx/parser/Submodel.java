/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx.parser;

import org.oscm.basyx.model.SubmodelDescriptorModel;
import org.oscm.basyx.model.SubmodelElement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Submodel {

  static List<SubmodelElement> readProperties(SubmodelDescriptorModel npm) {
    return Arrays.stream(npm.submodelElements)
        .filter(sm -> "PARAMETER".equalsIgnoreCase(sm.category))
        .collect(Collectors.toList());
  }
}
