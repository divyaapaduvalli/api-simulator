package com.divyaa.simulatorcore.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Holds all the created wiremock servers.
 * Initiates creation, starting of a new wiremock server for the provided application.
 * Provides the registration of a new wiremock server.
 * @author Divyaa P
 */
@Service
public class WiremockService {

    private static final Map<WiremockServerKey, WireMockServer> WIREMOCK_SERVER_MAP = Maps.newConcurrentMap();
    private final AtomicInteger wireMockStartServerPort;
    private final WiremockInstanceProvider wiremockInstanceProvider;

    public WiremockService(@Value("${server.port}") int port, WiremockInstanceProvider wiremockInstanceProvider) {
        this.wireMockStartServerPort = new AtomicInteger(port);
        this.wiremockInstanceProvider = wiremockInstanceProvider;
    }

  /**
   * Registers a new wiremock server if not already registered.
   * Initiates creation, starting of a new wiremock server for the provided application if not present.
   *
   * @param wiremockServerKey - A combination of application/environment, which is used to identify
   *                            the wiremock server
   */
  public void registerWiremockInstance(WiremockServerKey wiremockServerKey) {
        WIREMOCK_SERVER_MAP.computeIfAbsent(wiremockServerKey, key -> wiremockInstanceProvider.createWiremockServer(key,
                wireMockStartServerPort.incrementAndGet()));
    }
}
