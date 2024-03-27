package com.divyaa.simulatorcore.zuul;

import com.divyaa.simulatorcore.wiremock.WiremockServerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Provides the creation of Zuul routes that would be used by the application after the wiremock server hos started so
 * all the requests that comes to this application is forwarded to the wiremock server via zuul.
 * @author Divyaa P
 */
@Service
public class ZuulProxyService {

    private static final String WIREMOCK_APP_PATH = "/wiremock/%s/%s/**";
    private static final String LOCALHOST = "http://localhost:%s";
    private final ZuulProperties zuulProperties;
    private final ZuulHandlerMapping zuulHandlerMapping;

    @Autowired
    public ZuulProxyService(ZuulProperties zuulProperties, ZuulHandlerMapping zuulHandlerMapping) {
        this.zuulProperties = zuulProperties;
        this.zuulHandlerMapping = zuulHandlerMapping;
    }

    /**
     * Creates a zuul route with the following mapping
     *      - All requests to the endpoint /wiremock/{application}/{environment}/** is forwarded to http://localhost:{port}
     * @param wiremockServerKey
     * @param port
     */
    public void createZuulRoute(WiremockServerKey wiremockServerKey, int port){
        String path = String.format(WIREMOCK_APP_PATH, wiremockServerKey.getApplication(), wiremockServerKey.getEnvironment());

        String forwardedToUrl = String.format(LOCALHOST, port);

        zuulProperties.getRoutes().put(UUID.randomUUID().toString(), new ZuulProperties.ZuulRoute(path, forwardedToUrl));
        zuulHandlerMapping.setDirty(true);
    }
}
