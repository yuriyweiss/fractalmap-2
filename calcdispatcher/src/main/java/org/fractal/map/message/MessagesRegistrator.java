package org.fractal.map.message;

import org.fractal.map.message.request.AreaSquarePartitionRequest;
import org.fractal.map.message.request.GetAreaTextObjectsRequest;
import org.fractal.map.message.request.PointCoordsRequest;
import org.fractal.map.message.request.SquareRequest;
import org.fractal.map.message.response.AreaSquarePartitionResponse;
import org.fractal.map.message.response.CalcErrorResponse;
import org.fractal.map.message.response.GetAreaTextObjectsResponse;
import org.fractal.map.message.response.PointCoordsResponse;
import org.fractal.map.message.response.SquareResponse;
import org.fractal.map.transceiver.TransportableFactory;

public class MessagesRegistrator {

    public static final int REQUEST_POINT_COORDS = 1;
    public static final int RESPONSE_POINT_COORDS = 2;
    public static final int REQUEST_AREA_SQUARE_PARTITION = 3;
    public static final int RESPONSE_AREA_SQUARE_PARTITION = 4;
    public static final int REQUEST_SQUARE = 5;
    public static final int RESPONSE_SQUARE = 6;
    public static final int REQUEST_GET_AREA_TEXT_OBJECTS = 7;
    public static final int RESPONSE_GET_AREA_TEXT_OBJECTS = 8;

    public static final int RESPONSE_CALC_ERROR = 100;

    private MessagesRegistrator() {
    }

    public static void registerMessages() {
        TransportableFactory.registerClass( REQUEST_POINT_COORDS, PointCoordsRequest.class );
        TransportableFactory.registerClass( RESPONSE_POINT_COORDS, PointCoordsResponse.class );
        TransportableFactory.registerClass( REQUEST_AREA_SQUARE_PARTITION,
                AreaSquarePartitionRequest.class );
        TransportableFactory.registerClass( RESPONSE_AREA_SQUARE_PARTITION,
                AreaSquarePartitionResponse.class );
        TransportableFactory.registerClass( REQUEST_SQUARE, SquareRequest.class );
        TransportableFactory.registerClass( RESPONSE_SQUARE, SquareResponse.class );
        TransportableFactory.registerClass( REQUEST_GET_AREA_TEXT_OBJECTS, GetAreaTextObjectsRequest.class );
        TransportableFactory.registerClass( RESPONSE_GET_AREA_TEXT_OBJECTS, GetAreaTextObjectsResponse.class );
        TransportableFactory.registerClass( RESPONSE_CALC_ERROR, CalcErrorResponse.class );
    }
}
