package org.fractal.map.tests.transceiver.message;

import org.fractal.map.transceiver.TransportableFactory;

public class MessagesRegistrator {

    public static final int SHORT_DUMMY = 1;
    public static final int LONG_DUMMY = 2;

    public static void registerMessages() {
        TransportableFactory.registerClass( SHORT_DUMMY, ShortDummyMessage.class );
        TransportableFactory.registerClass( LONG_DUMMY, LongDummyMessage.class );
    }
}
