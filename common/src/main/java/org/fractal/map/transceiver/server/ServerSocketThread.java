package org.fractal.map.transceiver.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.util.StoppableThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerSocketThread extends StoppableThread {

    private static final Logger logger = LogManager.getLogger();

    private final TransceiverServer owner;
    private final int selectionMode;
    private final int errorIntervalSeconds;

    public ServerSocketThread( TransceiverServer owner, int selectionMode, int errorIntervalSeconds ) {
        super();
        this.owner = owner;
        this.selectionMode = selectionMode;
        this.errorIntervalSeconds = errorIntervalSeconds;
    }

    private int getPort() {
        return ( selectionMode == SelectionKey.OP_READ ) ? owner.getReadPort() : owner.getWritePort();
    }

    @Override
    public void run() {
        while ( canRun() ) {
            boolean error = false;
            try {
                ServerSocketChannel serverChannel = ServerSocketChannel.open(); // NOSONAR
                int port = getPort();
                serverChannel.bind( new InetSocketAddress( port ) );
                logger.info( "server socket bound to port: {}", port );
                while ( canRun() ) {
                    SocketChannel socketChannel = serverChannel.accept();
                    owner.createNewWorker( selectionMode, socketChannel );
                }
            } catch ( IOException e ) {
                if ( !( e instanceof ClosedByInterruptException ) ) {
                    error = true;
                    logger.error( "server socket error" );
                    logger.debug( "error stacktrace:", e );
                } else {
                    logger.error( "server socket interrupted" );
                }
            }
            if ( error ) {
                try {
                    int interval = errorIntervalSeconds;
                    logger.info( "waiting {} seconds before resurrect attempt", interval );
                    Thread.sleep( interval * 1000L );
                } catch ( InterruptedException e ) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        stopped = true;
        logger.info( "server socket thread stopped" );
    }
}
