package org.fractal.map.transceiver.server;

import java.nio.channels.SelectionKey;

public class ServerThreadsTuple {

    private ServerCommunicationThread reader = null;
    private ServerCommunicationThread writer = null;

    public ServerThreadsTuple() {
    }

    public boolean isEmpty() {
        return reader == null && writer == null;
    }

    public void removeWorker( int selectionMode ) {
        setWorker( selectionMode, null );
    }

    public ServerCommunicationThread getWorker( int selectionMode ) {
        return ( selectionMode == SelectionKey.OP_READ ) ? reader : writer;
    }

    public void setWorker( int selectionMode, ServerCommunicationThread worker ) {
        if ( selectionMode == SelectionKey.OP_READ ) {
            reader = worker;
        } else {
            writer = worker;
        }
    }

    public ServerCommunicationThread getBrother( int selectionMode ) {
        return ( selectionMode == SelectionKey.OP_READ ) ? writer : reader;
    }
}
