package org.fractal.map.transceiver.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.transceiver.Transceiver;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TransceiverServer extends Transceiver {

    private static final Logger logger = LogManager.getLogger();

    private ServerSocketThread readAcceptor;
    private ServerSocketThread writeAcceptor;
    private final Set<ServerCommunicationThread> readers = new HashSet<>();
    private final Set<ServerCommunicationThread> writers = new HashSet<>();
    private final Map<UUID, ServerThreadsTuple> clientsToWorkersMap = new HashMap<>();

    private volatile boolean disabled = false;

    public TransceiverServer( int readPort, int writePort ) {
        super( readPort, writePort );
    }

    @Override
    public void start() {
        readAcceptor = new ServerSocketThread( this, SelectionKey.OP_READ );
        readAcceptor.start();
        writeAcceptor = new ServerSocketThread( this, SelectionKey.OP_WRITE );
        writeAcceptor.start();
    }

    @Override
    public void stop() {
        disabled = true;
        logger.info( "stopping transceiver server" );
        readAcceptor.terminate();
        writeAcceptor.terminate();
        stopWorkers( SelectionKey.OP_READ );
        stopWorkers( SelectionKey.OP_WRITE );
        logger.info( "transceiver server stopped" );
    }

    private void stopWorkers( int selectionMode ) {
        Set<ServerCommunicationThread> workers =
                ( selectionMode == SelectionKey.OP_READ ) ? readers : writers;
        List<ServerCommunicationThread> workersCopy = new ArrayList<>( workers );
        for ( ServerCommunicationThread worker : workersCopy ) {
            worker.terminate();
        }
    }

    public void send( ServerMessage message ) {
        if ( disabled ) return;

        UUID clientUUID = message.getClientUUID();
        if ( isWriterRegistered( clientUUID ) ) {
            ServerCommunicationThread writer =
                    clientsToWorkersMap.get( clientUUID ).getWorker( SelectionKey.OP_WRITE );
            writer.offerMessage( message.getBody() );
        } else {
            logger.error( "client not found for message" );
            logger.trace( message );
        }
    }

    public void createNewWorker( int selectionMode, SocketChannel socketChannel ) {
        ServerCommunicationThread worker =
                new ServerCommunicationThread( this, socketChannel, selectionMode );
        synchronized ( this ) {
            if ( selectionMode == SelectionKey.OP_READ ) {
                readers.add( worker );
            } else {
                writers.add( worker );
            }
        }
        worker.start();
    }

    public boolean isWriterRegistered( UUID clientUUID ) {
        ServerThreadsTuple tuple = clientsToWorkersMap.get( clientUUID );
        return tuple != null && tuple.getWorker( SelectionKey.OP_WRITE ) != null;
    }

    public synchronized void linkClientToWorker( int selectionMode, UUID clientUUID,
            ServerCommunicationThread worker ) {
        ServerThreadsTuple tuple = clientsToWorkersMap.get( clientUUID );
        if ( tuple == null ) {
            logger.info( "registering new client: {}", clientUUID );
            tuple = new ServerThreadsTuple();
            clientsToWorkersMap.put( clientUUID, tuple );
        }
        tuple.setWorker( selectionMode, worker );
    }

    public void unregisterWorker( int selectionMode, UUID clientUUID,
            ServerCommunicationThread worker ) {
        if ( disabled ) return;
        ServerCommunicationThread brotherThread = null;

        String selectionModeName = ( selectionMode == SelectionKey.OP_READ ) ? "reader" : "writer";
        logger.info( "unregister {} for client: {}", selectionModeName, clientUUID );
        synchronized ( this ) {
            removeWorker( worker );
            if ( clientUUID != null ) {
                ServerThreadsTuple tuple = clientsToWorkersMap.remove( clientUUID );
                if ( tuple != null ) {
                    brotherThread = tuple.getBrother( selectionMode );
                    if ( brotherThread != null ) {
                        removeWorker( brotherThread );
                    }
                }
            }
        }
        if ( brotherThread != null ) {
            brotherThread.terminate();
        }
    }

    private void removeWorker( ServerCommunicationThread worker ) {
        if ( worker.getSelectionMode() == SelectionKey.OP_READ ) {
            readers.remove( worker );
        } else {
            writers.remove( worker );
        }
    }
}
