package com.divyaa.simulatorcore.configuration;

import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

/**
 * Enable component scan in this module so that objects can be created and used Enable zuul proxy so
 * that we can use it to redirect requests to a wiremock server
 *
 * @author Divyaa P
 */
@ComponentScan("com.divyaa.simulatorcore")
@EnableZuulProxy
public class AppConfiguration {}
