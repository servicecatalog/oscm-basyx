/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx.oscmmodel;

import org.eclipse.basyx.submodel.metamodel.api.submodelelement.dataelement.IProperty;

/** @author farmaki */
public class ServiceParameterBasyx {
  public String type;
  public String defaultValue;
  public String name;
  public String category;

  public ServiceParameterBasyx(IProperty submodelProperty) {
    this.type = submodelProperty.getValueType().toString();
    this.defaultValue = submodelProperty.getValue().toString();
    this.name = submodelProperty.getIdShort();
    this.category = submodelProperty.getCategory();
  }
}
