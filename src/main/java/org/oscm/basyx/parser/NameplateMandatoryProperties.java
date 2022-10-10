/**
 * *****************************************************************************
 *
 * <p>Copyright FUJITSU LIMITED 2022
 *
 * <p>Creation Date: 10.10.2022
 *
 * <p>*****************************************************************************
 */
package org.oscm.basyx.parser;

/** @author farmaki */
public enum NameplateMandatoryProperties {
  MANUFACTURER_NAME("ManufactName"),
  MANUFACTURER_PRODUCT_DESIGNATION("ManufacturerProductDesignation"),
  MANUFACTURER_PRODUCT_FAMILY("ManufacturerProductFamily"),
  YEAR_OF_CONSTRUCTION("YearOfConstruction");

  protected String idShort = null;

  NameplateMandatoryProperties(String idShort) {
    this.setIdShort(idShort);
  }

  public String getIdShort() {
    return idShort;
  }

  public void setIdShort(String idShort) {
    this.idShort = idShort;
  }
}
