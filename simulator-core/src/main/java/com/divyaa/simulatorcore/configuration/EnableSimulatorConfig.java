package com.divyaa.simulatorcore.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * Annotation to import AppConfiguration which has basic configurations to make this module
 * functional
 *
 * @author Divyaa P
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(AppConfiguration.class)
public @interface EnableSimulatorConfig {}
