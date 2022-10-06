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

  public ServiceParameterBasyx(IProperty prop) {
    this.type = prop.getValueType().toString();
    this.defaultValue = prop.getValue().toString();
    this.name = prop.getIdShort();
    this.category = prop.getCategory();
  }
}
