package com.divyaa.simulatorcore.controller;

import com.divyaa.simulatorcore.wiremock.WiremockServerKey;
import com.divyaa.simulatorcore.wiremock.WiremockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * All HTTP requests to this application will land on this controller. As we have one exposed
 * endpoint in the whole application via this controller.
 *
 * @author Divyaa P
 */
@RestController
public class ProxyController {

  private final WiremockService wiremockService;

  @Autowired
  public ProxyController(WiremockService wiremockService) {
    this.wiremockService = wiremockService;
  }

  /**
   * This endpoint is responsible for - Initiating creation, starting & registering of a wiremock
   * server for the provided application - Forward the request to an existing wiremock server via
   * ZuulProxy
   *
   * @param wiremockServerKey - A combination of application/environment given in the path, which is
   *     used to identify the wiremock server
   * @param servletRequest
   * @param servletResponse
   * @throws ServletException
   * @throws IOException
   */
  @RequestMapping("/wiremock/{application}/{environment}/**")
  public void routeMockRequest(
      WiremockServerKey wiremockServerKey,
      HttpServletRequest servletRequest,
      HttpServletResponse servletResponse)
      throws ServletException, IOException {
    wiremockService.registerWiremockInstance(wiremockServerKey);
    servletRequest
        .getRequestDispatcher(servletRequest.getRequestURI())
        .forward(servletRequest, servletResponse);
  }
}
