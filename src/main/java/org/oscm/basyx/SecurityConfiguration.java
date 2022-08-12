/*
 ****************************************************************************

 <p>Copyright FUJITSU LIMITED 2021

 <p>*************************************************************************
*/
package org.oscm.basyx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/** Author @goebel */
@Configuration
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.addFilterAfter(new CustomAuthFilter(), BasicAuthenticationFilter.class);
    return http.build();
  }
}
