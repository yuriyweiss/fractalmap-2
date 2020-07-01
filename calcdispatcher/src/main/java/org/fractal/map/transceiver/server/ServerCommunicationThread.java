package org.fractal.map.transceiver.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.conf.Configuration;
import org.fractal.map.transceiver.CommunicationThread;
import org.fractal.map.transceiver.iomethods.ClientUUIDExchange;
import org.fractal.map.transceiver.iomethods.ReadMessagesMethod;
import org.fractal.map.transceiver.iomethods.WriteMessagesMethod;
import org.fractal.map.util.ThreadUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.UUID;

public class ServerCommunicationThread extends CommunicationThread {

    private static final Logger logger = LogManager.getLogger();

    private final SocketChannel socketChannel;
    private UUID clientUUID = null;
    private int selectionMode;

    public ServerCommunicationThread( TransceiverServer owner, SocketChannel socketChannel,
            int selectionMode ) {
        super( owner );
        this.socketChannel = socketChannel;
        this.selectionMode = selectionMode;
    }

    private TransceiverServer getOwner() {
        return ( TransceiverServer ) owner;
    }

    public int getSelectionMode() {
        return selectionMode;
    }

    @Override
    public void run() {
        logger.info( "communication thread started" );
        Selector selector = null;
        ByteBuffer inputBuffer = ByteBuffer.allocate( Configuration.getTransceiverBufferSize() );
        ByteBuffer outputBuffer = ByteBuffer.allocate( Configuration.getTransceiverBufferSize() );
        try {
            socketChannel.configureBlocking( true );
            clientUUID = ClientUUIDExchange.readClientUUID( socketChannel );
            getOwner().linkClientToWorker( selectionMode, clientUUID, this );

            socketChannel.configureBlocking( false );
            selector = Selector.open();
            socketChannel.register( selector, selectionMode );

            // transceiver
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
            logger.error( "runtime error: " + e.getMessage() );
            logger.debug( "error stack: ", e );
        } catch ( IOException e ) {
            logger.error( "network failure: " + e.getMessage() );
            logger.debug( "error stack: ", e );
        } finally {
            getOwner().unregisterWorker( selectionMode, clientUUID, this );
            logger.info( "releasing resources" );
            try {
                socketChannel.close();
                logger.info( "socketChannel disconnected" );
                if ( selector != null ) {
                    selector.close();
                    selector = null;
                }
            } catch ( IOException e ) {
                logger.warn( "disconnection failed: " + e.getMessage() );
                logger.debug( "error stack: ", e );
            }
        }
        stopped = true;
        logger.info( "communication thread stopped" );
    }

    @Override
    protected UUID getClientUUID() {
        return clientUUID;
    }
}
