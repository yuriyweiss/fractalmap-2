package org.fractal.map.util.buffer;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class BufferPoolBuilder {

    private BufferPoolBuilder() {
    }

    public static ObjectPool<Buffer> build( int bufferSize, int poolSize ) {
        GenericObjectPool<Buffer> result =
                new GenericObjectPool<>( new BufferPoolObjectFactory( bufferSize ) );
        result.setMaxTotal( poolSize );
        result.setMaxIdle( poolSize );
        result.setMinIdle( 0 );
        result.setBlockWhenExhausted( true );
        result.setMaxWaitMillis( 5 * 60 * 1000L );
        return result;
    }
}
