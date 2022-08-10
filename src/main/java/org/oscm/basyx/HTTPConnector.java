/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/** @author goebell */
@Component
public class HTTPConnector {

  private static final Logger LOGGER = LoggerFactory.getLogger(HTTPConnector.class);

  private InputStream getConnectionStream(String url) throws IOException {
    try {
      CloseableHttpClient httpClient =
          HttpClients.custom()
              .setSSLContext(
                  new SSLContextBuilder()
                      .loadTrustMaterial(null, TrustAllStrategy.INSTANCE)
                      .build())
              .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setRedirectStrategy(new LaxRedirectStrategy())
              .build();
      HttpGet httpGet = new HttpGet(url);
      HttpResponse httpResponse = httpClient.execute(httpGet);
      return httpResponse.getEntity().getContent();
    } catch (IOException | KeyManagementException e) {
      LOGGER.error("Failed to open connection stream to resource: " + url);
      throw new IOException(e);
    } catch (NoSuchAlgorithmException | KeyStoreException e) {
      LOGGER.error("Failed to mock SSL validation before opening connection : " + url );
      throw new RuntimeException( "Failed to mock SSL validation before opening connection.", e);
    }
  }

  public String loadFromURL(String url) throws IOException {
    try (Scanner scanner = new Scanner(getConnectionStream(url), "UTF-8").useDelimiter("\\A")) {
      return scanner.hasNext() ? scanner.next() : "";
    } catch (IOException e) {
      LOGGER.error("Failed to download content file " + url + " " + e);
      throw e;
    }
  }
}
