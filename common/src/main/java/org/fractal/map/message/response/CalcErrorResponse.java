package org.fractal.map.message.response;

import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.ServletMessage;

import java.nio.ByteBuffer;
import java.util.UUID;

public class CalcErrorResponse extends ServletMessage implements Response {

    private int errorCode;

    public CalcErrorResponse() {
    }

    public CalcErrorResponse( UUID requestUUID, int errorCode ) {
        super( requestUUID );
        this.errorCode = errorCode;
    }

    @Override
    public int getClassId() {
        return MessagesRegistrator.RESPONSE_CALC_ERROR;
    }

    @Override
    public void readFromByteBuffer( ByteBuffer buffer ) {
        super.readFromByteBuffer( buffer );
        errorCode = buffer.getInt();
    }

    @Override
    public int estimateLength() {
        int result = super.estimateLength();
        result += 4; // errorCode
        return result;
    }

    @Override
    public void writeToByteBuffer( ByteBuffer buffer ) {
        super.writeToByteBuffer( buffer );
        buffer.putInt( errorCode );
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "CalcErrorResponse [errorCode=" + errorCode + ", toString()=" + super.toString()
                + "]";
    }
}
