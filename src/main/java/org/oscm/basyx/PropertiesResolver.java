/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/
package org.oscm.basyx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Author @goebel */
@Component
public class PropertiesResolver {

  @Value("${API_USER_KEY}")
  private String userKey;

  @Value("${API_PASS}")
  private String apiPassword;

   public String getApiUserKey() {
    return userKey;
  }

  public String getApiPassword() {
    return apiPassword;
  }

  @Value("${AAS_REGISTRY_URL}")
  private String registryUrl;

  public String getRegistryUrl() {
    return registryUrl;
  }
}
