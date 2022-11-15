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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

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

  public void setName(String name) {
    this.name = name;
  }

  List<MarketableServiceParameter> parameters = loadDefaultParamsFromJsonFile();

  public String parametersToJson() {
    Gson gson = new Gson();
    return gson.toJson(parameters);
  }

  public List<MarketableServiceParameter> loadDefaultParamsFromJsonFile() {
    InputStream inputStream = this.getClass()
            .getResourceAsStream("/default_params.json");
    if (inputStream == null) {
      return new ArrayList<>();
    }
    try (Reader reader = new InputStreamReader(inputStream)) {
      Gson gson = new Gson();
      Type parametersListType = new TypeToken<ArrayList<MarketableServiceParameter>>(){}.getType();
      return gson.fromJson(reader, parametersListType);
    } catch (IOException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  public void insert(List<ServiceParameter> serviceParams) {
    for (ServiceParameter sp : serviceParams) {
      parameters.add(new MarketableServiceParameter(sp.name, sp.defaultValue, true));
    }
  }
}
