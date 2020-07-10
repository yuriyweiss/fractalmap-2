package org.fractal.map.web;

import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.web.transceiver.WebServerTransceiverClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@SpringBootApplication( scanBasePackages = { "org.fractal.map.web" } )
public class WebServerApplication extends SpringBootServletInitializer {

    private static final Logger LOG = LoggerFactory.getLogger( WebServerApplication.class );

    public static void main( String[] args ) {
        SpringApplication.run( WebServerApplication.class, args );
    }

    @Override
    protected SpringApplicationBuilder configure( SpringApplicationBuilder builder ) {
        return builder.sources( WebServerApplication.class );
    }

    @Override
    public void onStartup( ServletContext servletContext ) throws ServletException {
        super.onStartup( servletContext );
        // save application context of web application
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext( servletContext );
        ApplicationContextHolder.setWebApplicationContext( context );
        MessagesRegistrator.registerMessages();
        WebServerTransceiverClient webServerTransceiverClient = context.getBean( WebServerTransceiverClient.class );
        webServerTransceiverClient.startClient();
        LOG.info( "SUCCESS application started" );
    }
}
