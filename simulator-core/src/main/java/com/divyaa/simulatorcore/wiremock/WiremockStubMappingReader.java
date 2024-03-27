package com.divyaa.simulatorcore.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.standalone.JsonFileMappingsSource;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Provides stub mapping reader ability
 * @author Divyaa P
 */
@Component
public class WiremockStubMappingReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(WiremockStubMappingReader.class);
    private static final String PATH_FORMAT = "%s/%s/%s";
    private static final String MOCKS_PATH_FORMAT = PATH_FORMAT + "/mocks";
    private final String filesClassPathRoot;

    @Autowired
    public WiremockStubMappingReader(@Value("${wiremock.filesClassPath}") String filesClassPathRoot) {
        this.filesClassPathRoot = filesClassPathRoot;
    }

    /**
     * Retrieves all the api mocks that are found under the resources/{application}/{environment}/mocks
     * @param wiremockServerKey - A combination of application/environment, which is used to identify the wiremock server
     * @return JsonFileMappingsSource
     */
    public JsonFileMappingsSource getMapping(WiremockServerKey wiremockServerKey){
        String filesClassPath = getFilesClassPath(wiremockServerKey);
        ClasspathFileSource mappingsFileSource = new ClasspathFileSource(filesClassPath);
        return new JsonFileMappingsSource(mappingsFileSource);
    }

    /**
     * Prints the available api mocks of the given wiremock server
     * @param wireMockServer - Instance of a wiremock server whose mocks needs to be printed
     */
    void printMappings(WireMockServer wireMockServer){
        for(StubMapping mapping : wireMockServer.listAllStubMappings().getMappings()){
            LOGGER.info("Loading all the mocks : {}", mapping);
        }
    }

    /**
     * This is almost similar to the stub mapping reader but in this case WireMockServer searches for a directory named
     * __files and there should stay all the static files response that the Wiremock server should read.
     * @param wiremockServerKey
     *              - used to identify the correct classpath
     * @return the path to where the __files folder is placed for the given wiremockServerKey.
     */
    public String getFilesClassPathRoot(WiremockServerKey wiremockServerKey){
        return getClasspathByFormatter(PATH_FORMAT, wiremockServerKey);
    }

    private String getFilesClassPath(WiremockServerKey wiremockServerKey){
        return  getClasspathByFormatter(MOCKS_PATH_FORMAT, wiremockServerKey);
    }

    private String getClasspathByFormatter(String formatter, WiremockServerKey wiremockServerKey){
        return String.format(formatter, filesClassPathRoot, wiremockServerKey.getApplication(), wiremockServerKey.getEnvironment());
    }
}
