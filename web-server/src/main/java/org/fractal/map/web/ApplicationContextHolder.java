package org.fractal.map.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring context holder. Is used to acquire context beans from classes that are not in Spring context.
 */
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext( ApplicationContext applicationContext ) {
        ApplicationContextHolder.applicationContext = applicationContext; // NOSONAR
    }

    public static void setWebApplicationContext( ApplicationContext webApplicationContext ) {
        applicationContext = webApplicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
