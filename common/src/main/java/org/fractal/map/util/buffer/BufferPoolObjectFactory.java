package org.fractal.map.util.buffer;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BufferPoolObjectFactory extends BasePooledObjectFactory<Buffer> {

    private static final Logger logger = LogManager.getLogger();

    private final int bufferSize;

    public BufferPoolObjectFactory( int bufferSize ) {
        this.bufferSize = bufferSize;
    }

    @Override
    public Buffer create() throws Exception {
        logger.debug( "creating new buffer" );
        return new Buffer( bufferSize );
    }

    @Override
    public PooledObject<Buffer> wrap( Buffer buffer ) {
        return new DefaultPooledObject<>( buffer );
    }
}
