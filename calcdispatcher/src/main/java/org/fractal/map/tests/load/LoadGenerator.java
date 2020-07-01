package org.fractal.map.tests.load;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.fractal.map.conf.Configuration;
import org.fractal.map.monitor.MonitorThread;
import org.fractal.map.tests.load.calc.CalculationTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoadGenerator {

    private static final Logger logger = LogManager.getLogger();

    public static void main( String[] args ) throws Exception {
        // Launch MySQL before running.
        System.setProperty( "log4j.configurationFile", "conf/log4j2-load-test.xml" );
        LoggerContext.getContext( false ).reconfigure();
        Configuration.load( "conf/load-test.conf" );

        MonitorThread monitorThread = new MonitorThread();

        ExecutorService executor = Executors.newFixedThreadPool( 1 );
        try {
            //Future<?> future = executor.submit(new CalculationTask("result/result_sample"));
            Future<?> future = executor.submit( new CalculationTask( "result/result_opt_square" ) );
            monitorThread.start();
            try {
                future.get();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        } finally {
            monitorThread.terminate();
            executor.shutdown();
        }

        logger.info( "application stopped" );
    }
}
