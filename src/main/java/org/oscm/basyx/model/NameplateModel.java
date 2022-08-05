/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2022
 *
 *  Creation Date: 29.07.2022
 *
 *******************************************************************************/

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
