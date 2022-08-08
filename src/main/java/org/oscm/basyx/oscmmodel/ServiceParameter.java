/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx.oscmmodel;

import org.oscm.basyx.model.SubmodelElement;

/** @author goebel */
public class ServiceParameter {
  public String type;
  public String defaultValue;
  public String name;
  public String category;

  public ServiceParameter(SubmodelElement prop) {
    this.type = prop.valueType;
    this.defaultValue = prop.value.toString();
    this.name = prop.idShort;
    this.category = prop.category;
  }
}
