package org.fractal.map.transceiver.client;

import org.fractal.map.transceiver.Transceiver;
import org.fractal.map.transceiver.Transportable;

import java.nio.channels.SelectionKey;
import java.util.UUID;

public class TransceiverClient extends Transceiver {

    private final String ipAddress;
    private final UUID clientUUID = UUID.randomUUID();
    private ClientCommunicationThread reader;
    private ClientCommunicationThread writer;

    public TransceiverClient( int readPort, int writePort, String ipAddress ) {
        super( readPort, writePort );
        this.ipAddress = ipAddress;
    }

    @Override
    public void start() {
        reader = new ClientCommunicationThread( SelectionKey.OP_READ, this );
        reader.start();
        writer = new ClientCommunicationThread( SelectionKey.OP_WRITE, this );
        writer.start();
    }

    @Override
    public void stop() {
        writer.terminate();
        reader.terminate();
    }

    public void send( Transportable message ) {
        writer.offerMessage( message );
    }

    public UUID getClientUUID() {
        return clientUUID;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
