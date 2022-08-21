/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/
package org.oscm.basyx;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@Tag("IntegrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExportIT {

  @Autowired private TestRestTemplate template;

  @Autowired private Environment env;

  @Test
  public void get_shouldFailWith401() throws Exception {
    ResponseEntity<String> result =
        template.getForEntity("/techservice/xml/Festo_3S7PM0CP4BD", String.class);
    assertUnauthorizedUserGets401(result);
  }

  @Test
  public void getWithAuth_shouldSucceedWith200() throws Exception {

    // given
    String user = env.getProperty("API_USER_KEY", "1234");
    String passwd = env.getProperty("API_PASS", "secret");
    String restUrl = env.getProperty("REST_URL", "https://myoscmserver/oscm-rest-api");

    final String restEndpoint = restUrl + "/v1/technicalservices/xml";

    // when
    ResponseEntity<String> response =
        template
            .withBasicAuth(user, passwd)
            .getForEntity("/techservice/xml/Festo_3S7PM0CP4BD", String.class);

    // then
    assertUnauthorizedUserGets401(restEndpoint, response);
    assertAuthorizedUserGets200(response);
  }

  @Test
  public void getTechnicalServiceKey() {
    // given
    String user = env.getProperty("API_USER_KEY", "1234");
    String passwd = env.getProperty("API_PASS", "secret");
    String restUrl = env.getProperty("REST_URL", "https://myoscmserver/oscm-rest-api");


    // when
    ResponseEntity<String> response =
        template
            .withBasicAuth(user, passwd)
            .getForEntity("/techservice/id/Festo_3S7PM0CP4BD", String.class);

    // then
    assertAuthorizedUserGets200(response);
  }

  private void assertUnauthorizedUserGets401(String xmlUrl, ResponseEntity<String> response) {
    boolean unAuthorized = env.getProperty("API_USER_KEY", "1234").equals("1234");
    if (unAuthorized) {
      assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), response.getBody());
      assertTrue(
          contains(response.getBody(), String.format("Authentication failed for %s", xmlUrl)));
    }
  }

  private void assertUnauthorizedUserGets401(ResponseEntity<String> response) {
    boolean unAuthorized = env.getProperty("API_USER_KEY", "1234").equals("1234");
    if (unAuthorized) {
      assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), response.getBody());
    }
  }

  private void assertAuthorizedUserGets200(ResponseEntity<String> response) {
    boolean isUserSet = !env.getProperty("API_USER_KEY", "1234").equals("1234");
    if (isUserSet) {
      assertEquals(HttpStatus.OK, response.getStatusCode(), response.getBody());
      assertNotNull(response.getBody());
    }
  }

  boolean contains(String msg, String... parts) {
    for (String part : parts) {
      if (!msg.contains(part)) {
        return false;
      }
    }
    return true;
  }
}
