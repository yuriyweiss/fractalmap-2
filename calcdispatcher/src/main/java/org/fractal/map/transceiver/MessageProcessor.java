package org.fractal.map.transceiver;

import java.util.UUID;

public interface MessageProcessor {

    void onMessageDecoded( UUID clientUUID, Transportable message );
}
