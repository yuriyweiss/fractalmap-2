package org.fractal.map.message.response;

import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.ServletMessage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetAreaTextObjectsResponse extends ServletMessage implements Response {

    private List<TextObjectInfo> textObjects = new ArrayList<>();

    public GetAreaTextObjectsResponse() {
    }

    public GetAreaTextObjectsResponse( UUID requestUUID ) {
        super( requestUUID );
    }

    @Override
    public int getClassId() {
        return MessagesRegistrator.RESPONSE_GET_AREA_TEXT_OBJECTS;
    }

    public void addTextObjectInfo( TextObjectInfo textObjectInfo ) {
        textObjects.add( textObjectInfo );
    }

    @Override
    public void readFromByteBuffer( ByteBuffer buffer ) {
        textObjects.clear();
        super.readFromByteBuffer( buffer );
        int textObjectsCount = buffer.getInt();
        for ( int i = 0; i < textObjectsCount; i++ ) {
            TextObjectInfo textObjectInfo = new TextObjectInfo();
            textObjectInfo.readFromByteBuffer( buffer );
            addTextObjectInfo( textObjectInfo );
        }
    }

    @Override
    public int estimateLength() {
        int result = super.estimateLength();
        result += 4; // messages count
        for ( TextObjectInfo textObjectInfo : textObjects ) {
            result += textObjectInfo.estimateLength();
        }
        return result;
    }

    @Override
    public void writeToByteBuffer( ByteBuffer buffer ) {
        super.writeToByteBuffer( buffer );
        buffer.putInt( textObjects.size() );
        for ( TextObjectInfo textObjectInfo : textObjects ) {
            textObjectInfo.writeToByteBuffer( buffer );
        }
    }

    public List<TextObjectInfo> getTextObjects() {
        return textObjects;
    }

    @Override
    public String toString() {
        return "GetAreaTextObjectsResponse [textObjects=" + textObjects + ", getRequestUUID()="
                + getRequestUUID() + "]";
    }
}
