package com.divyaa.simulatorcore.wiremock;

import com.divyaa.simulatorcore.zuul.ZuulProxyService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * Creates and starts a wiremock server instance for each of the given application/environment key
 * and creates the zuul route for it
 * @author Divyaa P
 */
@Component
public class WiremockInstanceProvider {

    @Value("${maxRequestJournalEntries:100}")
    private int maxRequestJournalEntries;
    @Value("${asynchronousResponseEnabled:false}")
    private boolean asynchronousResponseEnabled;
    @Value("${disableRequestJournal:false}")
    private boolean disableRequestJournal;
    @Value("${asynchronousResponseThreads:10}")
    private int asynchronousResponseThreads;
    @Value("${containerThreads:10}")
    private int containerThreads;
    @Value("${jettyAcceptQueueSize:100}")
    private int jettyAcceptQueueSize;
    @Value("${jettyAcceptors:2}")
    private int jettyAcceptors;
    @Value("${jettyHeaderBufferSize:8192}")
    private int jettyHeaderBufferSize;

    private final ZuulProxyService zuulProxyService;
    private final WiremockStubMappingReader wiremockStubMappingReader;

    @Autowired
    public WiremockInstanceProvider(ZuulProxyService zuulProxyService,
                                    WiremockStubMappingReader wiremockStubMappingReader) {
        this.zuulProxyService = zuulProxyService;
        this.wiremockStubMappingReader = wiremockStubMappingReader;
    }

  /**
   * Creates, configures, starts a wiremock server.
   * Creates a zuul route for the new wiremock server.
   *
   * @param wiremockServerKey - A combination of application/environment, which is used to identify the wiremock server
   * @param port - port on which the wiremock server will accept requests on
   * @return WireMockServer
   */
  public WireMockServer createWiremockServer(WiremockServerKey wiremockServerKey, int port) {
        WireMockConfiguration wireMockConfiguration = configureWiremockServer(wiremockServerKey, port);
        WireMockServer wireMockServer = new WireMockServer(wireMockConfiguration);
        wireMockServer.start();
        zuulProxyService.createZuulRoute(wiremockServerKey, wireMockServer.port());
        return wireMockServer;
    }

    /**
     * Creates configuration of the wiremock server.
     * @param wiremockServerKey - A combination of application/environment, which is used to identify the wiremock server
     * @param port - port on which the wiremock server will accept requests on
     * @return WireMockConfiguration
     */
    private WireMockConfiguration configureWiremockServer(WiremockServerKey wiremockServerKey, int port){
        WireMockConfiguration wireMockConfiguration = wireMockConfig().port(port)
                .usingFilesUnderClasspath(wiremockStubMappingReader.getFilesClassPathRoot(wiremockServerKey))
                .mappingSource(wiremockStubMappingReader.getMapping(wiremockServerKey))
                .maxRequestJournalEntries(maxRequestJournalEntries)
                .asynchronousResponseEnabled(asynchronousResponseEnabled)
                .jettyAcceptQueueSize(jettyAcceptQueueSize)
                .jettyAcceptors(jettyAcceptors)
                .jettyHeaderBufferSize(jettyHeaderBufferSize)
                .asynchronousResponseThreads(asynchronousResponseThreads)
                .containerThreads(containerThreads);

        if(disableRequestJournal){
            wireMockConfiguration.disableRequestJournal();
        }

        return wireMockConfiguration;
    }
}
