/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/
package org.oscm.basyx.oscmmodel;

import java.util.Optional;

/** Author @goebel */
public class TechnicalServicesMapper {
  public Item[] items;

  public class Item {
      public long id;
      public String technicalServiceId;
  }

  public Optional<Long> getIdByTsName(String tsName) {
    if (items != null) {
      for (Item i : items) {
        if (tsName.equals(i.technicalServiceId)) {
          return Optional.of(i.id);
        }
      }
    }
    return Optional.empty();
  }
}
