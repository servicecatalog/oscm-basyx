/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/
package org.oscm.basyx.oscmmodel;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TechnicalServices {
  public Item[] items;

  public class Item {
    public String technicalServiceXml;
  }

  public static TechnicalServices getFrom(String xml) {
    TechnicalServices model = new TechnicalServices();
    model.items = new TechnicalServices.Item[1];
    model.items[0] = model.new Item();
    model.items[0].technicalServiceXml = xml;
    return model;
  }

  public static String asXML(TechnicalServices ts) {
    Optional<List<Item>> xmls = Optional.of(Arrays.asList(ts.items));
    if (xmls.isPresent() && !xmls.get().isEmpty()) {
      return xmls.get().get(0).technicalServiceXml;
    }
    return "";
  }
}
