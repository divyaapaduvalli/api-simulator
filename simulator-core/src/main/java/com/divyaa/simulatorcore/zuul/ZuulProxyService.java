package com.divyaa.simulatorcore.zuul;

import com.divyaa.simulatorcore.wiremock.WiremockServerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private final String applicationEndpoint;
    private final String wiremockBasePath;
    private final ZuulProperties zuulProperties;
    private final ZuulHandlerMapping zuulHandlerMapping;

    @Autowired
    public ZuulProxyService(@Value("${wiremock.basePath}") String wiremockBasePath, @Value("${application.endpoint}") String applicationEndpoint,
                            ZuulProperties zuulProperties, ZuulHandlerMapping zuulHandlerMapping) {
        this.wiremockBasePath = wiremockBasePath;
        this.applicationEndpoint = applicationEndpoint;
        this.zuulProperties = zuulProperties;
        this.zuulHandlerMapping = zuulHandlerMapping;
    }

    /**
     * Creates a zuul route with the following mapping
     *      - All requests to the endpoint /wiremock/{application}/{environment}/** is forwarded to http://localhost:{port}
     * @param wiremockServerKey - A combination of application/environment, which is used to identify the wiremock server
     * @param port - Wiremock server port
     */
    public void createZuulRoute(WiremockServerKey wiremockServerKey, int port){
        String path = String.format(applicationEndpoint, wiremockServerKey.getApplication(), wiremockServerKey.getEnvironment());

        String forwardedToUrl = String.format(wiremockBasePath, port);

        zuulProperties.getRoutes().put(UUID.randomUUID().toString(), new ZuulProperties.ZuulRoute(path, forwardedToUrl));
        zuulHandlerMapping.setDirty(true);
    }
}
