package org.fractal.map.transceiver.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.conf.Configuration;
import org.fractal.map.transceiver.CommunicationThread;
import org.fractal.map.transceiver.iomethods.ClientUUIDExchange;
import org.fractal.map.transceiver.iomethods.ReadMessagesMethod;
import org.fractal.map.transceiver.iomethods.WriteMessagesMethod;
import org.fractal.map.util.ThreadUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ClientCommunicationThread extends CommunicationThread {

    private static final Logger logger = LogManager.getLogger();

    private int selectionMode;

    public ClientCommunicationThread( int selectionMode, TransceiverClient owner ) {
        super( owner );
        this.selectionMode = selectionMode;
    }

    private TransceiverClient getOwner() {
        return ( TransceiverClient ) owner;
    }

    private int getPort() {
        return ( selectionMode == SelectionKey.OP_READ ) ? owner.getReadPort() : owner.getWritePort();
    }

    @Override
    public void run() {
        while ( canRun() ) {
            boolean error = false;
            SocketChannel socketChannel = null;
            Selector selector = null;
            try {
                InetSocketAddress addr =
                        new InetSocketAddress( getOwner().getIpAddress(), getPort() );
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking( true );
                socketChannel.socket().connect( addr, 30000 );
                if ( socketChannel.isConnected() ) {
                    logger.info( "connected to server://{}:{}",
                            socketChannel.socket().getInetAddress().getHostName(),
                            socketChannel.socket().getPort() );
                } else {
                    throw new IOException( "Connection timed out." );
                }

                ClientUUIDExchange.writeClientUUID( getOwner().getClientUUID(), socketChannel );
                logger.info( "client UUID sent to server: {}", getOwner().getClientUUID() );

                socketChannel.configureBlocking( false );
                selector = Selector.open();
                socketChannel.register( selector, selectionMode );

                ByteBuffer inputBuffer =
                        ByteBuffer.allocate( Configuration.getTransceiverBufferSize() );
                ByteBuffer outputBuffer =
                        ByteBuffer.allocate( Configuration.getTransceiverBufferSize() );

                transceiver_loop:
                while ( canRun() ) {
                    selector.select( 50 );
                    Iterator<SelectionKey> ki = selector.selectedKeys().iterator();
                    while ( ki.hasNext() ) {
                        SelectionKey key = ki.next();
                        ki.remove();

                        if ( key.isReadable() ) {
                            int rr = socketChannel.read( inputBuffer );
                            if ( rr == -1 || rr == 0 ) break transceiver_loop; // end-of-stream
                            new ReadMessagesMethod( this, inputBuffer ).execute();
                        } else if ( key.isWritable() ) {
                            // writable channel is always ready
                            // so if there is no messages in queue, wait some time to prevent 100% CPU load
                            if ( getMessages().size() == 0 ) {
                                ThreadUtils.sleep( 50 );
                            } else {
                                new WriteMessagesMethod( getMessages(), socketChannel, outputBuffer )
                                        .execute();
                            }
                        }
                    }
                }
            } catch ( RuntimeException e ) {
                error = true;
                logger.error( "runtime error: {}", e.getMessage() );
                logger.debug( "error stack: ", e );
            } catch ( IOException e ) {
                error = true;
                logger.error( "network failure: {}", e.getMessage() );
                logger.debug( "error stack: ", e );
            } finally {
                // disconnect at the end
                try {
                    if ( socketChannel != null ) {
                        socketChannel.close();
                        socketChannel = null;
                        logger.info( "disconnected from://{}:{}", getOwner().getIpAddress(), getPort() );
                    }
                    if ( selector != null ) {
                        selector.close();
                        selector = null;
                    }
                } catch ( IOException e ) {
                    logger.warn( "disconnection failed", e );
                    logger.debug( "error stack: ", e );
                }
                if ( error ) {
                    try {
                        int interval = Configuration.getTransceiverErrorIntervalSeconds();
                        logger.info( "waiting {} seconds before resurrect attempt", interval );
                        Thread.sleep( interval * 1000L );
                    } catch ( InterruptedException e ) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        stopped = true;
        logger.info( "client communication thread stopped" );
    }
}
