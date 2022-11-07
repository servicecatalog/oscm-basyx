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

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/** @author farmaki */
public class MarketableServiceInfo {
  private String technicalServiceId;
  private String serviceId;

  private String description = "A sample service for deploying Industrie 4.0 machines";
  private String shortDescription = "Short description for Industrie 4.0 machine";
  private String name;

  private String configuratorUrl = "http://somerandom-url.com";
  private String customTabName = "Tab Name";
  private String customTabUrl = "http://somerandom-tab-url.com";

  public void setTechnicalServiceId(String techServiceId) {
    this.technicalServiceId = techServiceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  List<MarketableServiceParameterItem> parameters = new ArrayList<>();

  public String parametersToJson() {
    Gson gson = new Gson();
    String parametersJson = gson.toJson(parameters);
    return parametersJson;
  }

  public void insert(List<ServiceParameter> serviceParams) {

    for (ServiceParameter sp : serviceParams) {
      parameters.add(new MarketableServiceParameterItem(sp.name, sp.defaultValue, true));
    }
  }
}
