/*
  ******************************************************************************

  <p>Copyright FUJITSU LIMITED 2022

  <p>*****************************************************************************
 */

package org.oscm.basyx;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Tag("IntegrationTest")
@SpringBootApplication
public class DiscoveryApplication {

  public static void main(String[] args) {
    SpringApplication.run(DiscoveryApplication.class, args);
  }
}
