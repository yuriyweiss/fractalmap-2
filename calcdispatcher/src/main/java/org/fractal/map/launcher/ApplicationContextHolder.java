package org.fractal.map.launcher;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext( ApplicationContext applicationContext ) {
        org.fractal.map.launcher.ApplicationContextHolder.applicationContext = applicationContext; // NOSONAR
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
