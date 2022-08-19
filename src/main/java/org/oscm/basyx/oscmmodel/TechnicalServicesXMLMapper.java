/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/
package org.oscm.basyx.oscmmodel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TechnicalServicesXMLMapper {
  public Item[] items;

  public class Item {
    public String technicalServiceXml;
  }

  public static TechnicalServicesXMLMapper getFrom(String xml) {
    TechnicalServicesXMLMapper model = new TechnicalServicesXMLMapper();
    model.items = new TechnicalServicesXMLMapper.Item[1];
    model.items[0] = model.new Item();
    model.items[0].technicalServiceXml = xml;
    return model;
  }

  public static String asXML(TechnicalServicesXMLMapper ts) {
    Optional<List<Item>> xmls = Optional.of(Arrays.asList(ts.items));
    if (xmls.isPresent() && !xmls.get().isEmpty()) {
      return xmls.get().get(0).technicalServiceXml;
    }
    return "";
  }

  public static String toJson(TechnicalServicesXMLMapper services) {

    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    String jsonData = gson.toJson(services);

    JsonElement jsonElement = new JsonParser().parse(jsonData);
    return gson.toJson(jsonElement);
  }
}
