package org.fractal.map.message.response;

import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.ServletMessage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AreaSquarePartitionResponse extends ServletMessage implements Response {

    private List<SquareInfo> squares = new ArrayList<>();

    public AreaSquarePartitionResponse() {
    }

    public AreaSquarePartitionResponse( UUID requestUUID ) {
        super( requestUUID );
    }

    @Override
    public int getClassId() {
        return MessagesRegistrator.RESPONSE_AREA_SQUARE_PARTITION;
    }

    public void addSquareInfo( SquareInfo squareInfo ) {
        squares.add( squareInfo );
    }

    @Override
    public void readFromByteBuffer( ByteBuffer buffer ) {
        squares.clear();
        super.readFromByteBuffer( buffer );
        int squaresCount = buffer.getInt();
        for ( int i = 0; i < squaresCount; i++ ) {
            SquareInfo squareInfo = new SquareInfo();
            squareInfo.readFromByteBuffer( buffer );
            addSquareInfo( squareInfo );
        }
    }

    @Override
    public int estimateLength() {
        int result = super.estimateLength();
        result += 4; // messages count
        for ( SquareInfo squareInfo : squares ) {
            result += squareInfo.estimateLength();
        }
        return result;
    }

    @Override
    public void writeToByteBuffer( ByteBuffer buffer ) {
        super.writeToByteBuffer( buffer );
        buffer.putInt( squares.size() );
        for ( SquareInfo squareInfo : squares ) {
            squareInfo.writeToByteBuffer( buffer );
        }
    }

    public List<SquareInfo> getSquares() {
        return squares;
    }

    @Override
    public String toString() {
        return "AreaSquarePartitionResponse [squares=" + squares + ", getRequestUUID()="
                + getRequestUUID() + "]";
    }
}
