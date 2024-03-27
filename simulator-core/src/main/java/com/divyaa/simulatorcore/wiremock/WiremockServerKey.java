package com.divyaa.simulatorcore.wiremock;

/**
 * @author Divyaa P
 */
public class WiremockServerKey {

    private String application;
    private String environment;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
