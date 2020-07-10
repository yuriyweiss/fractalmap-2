package org.fractal.map.transceiver;

import java.util.UUID;

public abstract class Transceiver {

    private final int readPort;
    private final int writePort;
    private final int bufferSize;
    private final int errorIntervalSeconds;

    private MessageProcessor messageProcessor = null;

    public abstract void start();

    public abstract void stop();

    public Transceiver( int readPort, int writePort, int bufferSize, int errorIntervalSeconds ) {
        this.readPort = readPort;
        this.writePort = writePort;
        this.bufferSize = bufferSize;
        this.errorIntervalSeconds = errorIntervalSeconds;
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

    public int getBufferSize() {
        return bufferSize;
    }

    public int getErrorIntervalSeconds() {
        return errorIntervalSeconds;
    }
}
