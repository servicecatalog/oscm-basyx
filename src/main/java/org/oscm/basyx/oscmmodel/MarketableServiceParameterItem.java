/**
 * *****************************************************************************
 *
 * <p>Copyright FUJITSU LIMITED 2022
 *
 * <p>Creation Date: 02.11.2022
 *
 * <p>*****************************************************************************
 */
package org.oscm.basyx.oscmmodel;

/** @author farmaki */
public class MarketableServiceParameterItem {
  private String parameterId;
  private String value;
  private boolean configurable;

  public MarketableServiceParameterItem(String paramId, String value, boolean configurable) {
    this.parameterId = paramId;
    this.value = value;
    this.configurable = configurable;
  }
}
