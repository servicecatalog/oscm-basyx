/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2022
 *
 *  Creation Date: 29.07.2022
 *
 *******************************************************************************/

package org.oscm.basyx.model;

/** @author goebel */
public class Model {
  public ModelType modelType;
  public String idShort;
  public Identification identification;
  public Endpoint[] endpoints;
  public Model[] submodels;
  public SemanticId semanticId;
}
