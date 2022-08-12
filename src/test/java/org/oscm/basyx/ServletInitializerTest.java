/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/
package org.oscm.basyx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServletInitializerTest {

  @Mock private SpringApplicationBuilder springApplicationBuilder;

  @Test
  public void testIt() {
    ServletInitializer servletInitializer = new ServletInitializer();
    when(springApplicationBuilder.sources(DiscoveryApplication.class))
        .thenReturn(springApplicationBuilder);

    SpringApplicationBuilder result = servletInitializer.configure(springApplicationBuilder);

    verify(springApplicationBuilder).sources(DiscoveryApplication.class);
    assertEquals(springApplicationBuilder, result);
  }
}
