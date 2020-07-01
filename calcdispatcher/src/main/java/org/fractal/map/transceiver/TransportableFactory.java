package org.fractal.map.transceiver;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class TransportableFactory {

    private static final Map<Integer, Class<?>> classRegistry = new HashMap<>();

    public static void registerClass( Integer classId, Class<?> targetClass ) {
        classRegistry.put( classId, targetClass );
    }

    public static Transportable readFromByteBuffer( ByteBuffer inputBuffer ) {
        Integer classId = inputBuffer.getInt();
        Class<?> resultClass = classRegistry.get( classId );
        try {
            Transportable result = ( Transportable ) resultClass.newInstance();
            result.readFromByteBuffer( inputBuffer );
            return result;
        } catch ( Exception e ) {
            throw new RuntimeException( "Transportable readFromByteBuffer failed.", e );
        }
    }
}
