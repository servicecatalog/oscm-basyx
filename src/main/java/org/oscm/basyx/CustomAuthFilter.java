package org.oscm.basyx;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthFilter extends GenericFilterBean {

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || authHeader.trim().length() == 0) {
      putAuthorizationHeader(response);
    } else {
        TokenHolder.set(authHeader);
        filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  private void putAuthorizationHeader(HttpServletResponse response) {
    response.setHeader(
            "WWW-Authenticate",
            "Basic realm=\"oscm-basyx\"");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }
}
