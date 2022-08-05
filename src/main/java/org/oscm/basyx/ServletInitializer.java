/*
  ******************************************************************************

  <p>Copyright FUJITSU LIMITED 2022

  <p>*****************************************************************************
 */

package org.oscm.basyx;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(DiscoveryApplication.class);
  }
}
