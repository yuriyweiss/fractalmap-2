package org.fractal.map.transceiver.iomethods;

import org.fractal.map.exception.FatalFractalMapException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public class ClientUUIDExchange {

    private ClientUUIDExchange() {
    }

    public static void writeClientUUID( UUID clientUUID, SocketChannel socketChannel )
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( clientUUID );
        oos.flush();
        oos.close();
        byte[] clientUUIDArray = baos.toByteArray();

        ByteBuffer lengthBuffer = ByteBuffer.allocate( 4 );
        lengthBuffer.putInt( clientUUIDArray.length );
        lengthBuffer.flip();
        while ( lengthBuffer.hasRemaining() ) {
            socketChannel.write( lengthBuffer );
        }

        ByteBuffer uuidBuffer = ByteBuffer.wrap( clientUUIDArray );
        while ( uuidBuffer.hasRemaining() ) {
            socketChannel.write( uuidBuffer );
        }
    }

    public static UUID readClientUUID( SocketChannel socketChannel ) throws IOException {
        UUID result = null;

        ByteBuffer lengthBuffer = ByteBuffer.allocate( 4 );
        socketChannel.read( lengthBuffer );
        lengthBuffer.flip();
        int uuidLength = lengthBuffer.getInt();
        ByteBuffer uuidBuffer = ByteBuffer.allocate( uuidLength );
        socketChannel.read( uuidBuffer );
        uuidBuffer.flip();
        byte[] uuidArray = new byte[uuidLength];
        uuidBuffer.get( uuidArray );

        try {
            ObjectInputStream ois = new ObjectInputStream( new ByteArrayInputStream( uuidArray ) );
            result = ( UUID ) ois.readObject();
            ois.close();
        } catch ( ClassNotFoundException e ) {
            throw new FatalFractalMapException( "clientUUID decoding failed", e );
        }

        return result;
    }
}
