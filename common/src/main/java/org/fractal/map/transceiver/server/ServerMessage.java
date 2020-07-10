package org.fractal.map.transceiver.server;

import org.fractal.map.transceiver.Transportable;

import java.util.UUID;

public class ServerMessage {

    private UUID clientUUID;
    private Transportable body;

    public ServerMessage() {
    }

    public ServerMessage( UUID clientUUID, Transportable body ) {
        this.clientUUID = clientUUID;
        this.body = body;
    }

    public UUID getClientUUID() {
        return clientUUID;
    }

    public void setClientUUID( UUID clientUUID ) {
        this.clientUUID = clientUUID;
    }

    public Transportable getBody() {
        return body;
    }

    public void setBody( Transportable body ) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ServerMessage [clientUUID=" + clientUUID + ", body=" + body + "]";
    }
}
