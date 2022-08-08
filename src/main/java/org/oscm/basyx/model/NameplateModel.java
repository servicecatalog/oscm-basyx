/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx.model;

/** @author goebel */
public class NameplateModel {
  public ModelType modelType;
  public String idShort;
  public String kind;
  public Identification identification;
  public SubmodelElement[] submodelElements;
  public SemanticId semanticId;
}
