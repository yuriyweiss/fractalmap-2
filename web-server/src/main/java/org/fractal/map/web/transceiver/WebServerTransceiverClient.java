package org.fractal.map.web.transceiver;

import org.fractal.map.transceiver.Transportable;
import org.fractal.map.transceiver.client.TransceiverClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class WebServerTransceiverClient {

    private TransceiverClient transceiverClient;

    @Autowired
    public WebServerTransceiverClient(
            @Value( "${calcdisp.ip.address}" ) String ipAddress,
            @Value( "${calcdisp.read.port}" ) int readPort,
            @Value( "${calcdisp.write.port}" ) int writePort ) {
        transceiverClient = new TransceiverClient( readPort, writePort, ipAddress );
        transceiverClient.setMessageProcessor( new ResponseMessageProcessor() );
    }

    public void startClient() {
        transceiverClient.start();
    }

    @PreDestroy
    public void shutdown() {
        transceiverClient.stop();
    }

    public void send( Transportable message ) {
        transceiverClient.send( message );
    }
}
