/*
  ******************************************************************************

  <p>Copyright FUJITSU LIMITED 2022

  <p>*****************************************************************************
 */

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
