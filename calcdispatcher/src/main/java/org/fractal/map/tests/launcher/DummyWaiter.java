package org.fractal.map.tests.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.message.ServletMessage;
import org.fractal.map.waiterscache.ResponseWaiter;

public class DummyWaiter implements ResponseWaiter {

    @SuppressWarnings( "unused" )
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void setResponse( ServletMessage response ) {
        logger.debug( "request processed: {}", response.getRequestUUID() );
    }

    @Override
    public void setResponseTimeout() {
        // TODO empty
    }
}
