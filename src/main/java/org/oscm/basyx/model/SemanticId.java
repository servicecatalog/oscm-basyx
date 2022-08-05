/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2022
 *
 *  Creation Date: 29.07.2022
 *
 *******************************************************************************/

package org.oscm.basyx.model;

/** @author goebel */
public class SemanticId {
  public Key[] keys;

  public class Key {
    public String idType;
    public boolean local;
    public String value;
    public String type;
  }
}
