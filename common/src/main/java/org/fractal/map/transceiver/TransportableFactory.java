package org.fractal.map.transceiver;

import org.fractal.map.exception.FatalFractalMapException;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class TransportableFactory {

    private static final Map<Integer, Class<?>> classRegistry = new HashMap<>();

    private TransportableFactory() {
    }

    public static void registerClass( Integer classId, Class<?> targetClass ) {
        classRegistry.put( classId, targetClass );
    }

    public static Transportable readFromByteBuffer( ByteBuffer inputBuffer ) {
        Integer classId = inputBuffer.getInt();
        Class<?> resultClass = classRegistry.get( classId );
        try {
            Transportable result = ( Transportable ) resultClass.newInstance(); // NOSONAR
            result.readFromByteBuffer( inputBuffer );
            return result;
        } catch ( Exception e ) {
            throw new FatalFractalMapException( "Transportable readFromByteBuffer failed.", e );
        }
    }
}
