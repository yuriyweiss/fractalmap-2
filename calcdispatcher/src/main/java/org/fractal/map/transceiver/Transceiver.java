package org.fractal.map.transceiver;

import java.util.UUID;

public abstract class Transceiver {

    private final int readPort;
    private final int writePort;

    private MessageProcessor messageProcessor = null;

    public abstract void start();

    public abstract void stop();

    public Transceiver( int readPort, int writePort ) {
        this.readPort = readPort;
        this.writePort = writePort;
    }

    public void setMessageProcessor( MessageProcessor messageProcessor ) {
        this.messageProcessor = messageProcessor;
    }

    public void onMessageDecoded( UUID clientUUID, Transportable message ) {
        if ( messageProcessor != null ) {
            messageProcessor.onMessageDecoded( clientUUID, message );
        }
    }

    public int getReadPort() {
        return readPort;
    }

    public int getWritePort() {
        return writePort;
    }
}
