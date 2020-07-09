package org.fractal.map.launcher;

import org.fractal.map.calc.LoadSquareStrategy;
import org.fractal.map.calc.SaveSquareStrategy;
import org.fractal.map.calc.request.AreaTextObjectsCalculator;
import org.fractal.map.calc.request.PointCoordsCalculator;
import org.fractal.map.calc.request.SquareCalculator;
import org.fractal.map.calc.request.SquaresPartitionCalculator;
import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.ServletMessage;
import org.fractal.map.message.request.AreaSquarePartitionRequest;
import org.fractal.map.message.request.GetAreaTextObjectsRequest;
import org.fractal.map.message.request.PointCoordsRequest;
import org.fractal.map.message.request.SquareRequest;
import org.fractal.map.storage.combined.CombinedLoadSquareStrategy;
import org.fractal.map.storage.combined.CombinedSaveSquareStrategy;
import org.fractal.map.transceiver.server.ServerMessage;
import org.fractal.map.transceiver.server.TransceiverServer;

public class MessageProcessingTask implements Runnable {

    private final ServerMessage message;
    private final TransceiverServer transceiverServer;

    public MessageProcessingTask( ServerMessage message, TransceiverServer transceiverServer ) {
        this.message = message;
        this.transceiverServer = transceiverServer;
    }

    @Override
    public void run() {
        ServletMessage servletMessage = ( ServletMessage ) message.getBody();
        ServletMessage response;
        switch ( servletMessage.getClassId() ) {
            case MessagesRegistrator.REQUEST_AREA_SQUARE_PARTITION:
                response =
                        new SquaresPartitionCalculator( ( AreaSquarePartitionRequest ) message.getBody() ).calculate();
                break;
            case MessagesRegistrator.REQUEST_POINT_COORDS:
                response =
                        new PointCoordsCalculator( ( PointCoordsRequest ) message.getBody() ).calculate();
                break;
            case MessagesRegistrator.REQUEST_SQUARE:
                LoadSquareStrategy loadStrategy = new CombinedLoadSquareStrategy();
                SaveSquareStrategy saveStrategy = new CombinedSaveSquareStrategy();
                response =
                        new SquareCalculator( ( SquareRequest ) message.getBody(), loadStrategy,
                                saveStrategy ).calculate();
                break;
            case MessagesRegistrator.REQUEST_GET_AREA_TEXT_OBJECTS:
                response =
                        new AreaTextObjectsCalculator( ( GetAreaTextObjectsRequest ) message.getBody() ).calculate();
                break;
            default:
                response = null;
                break;
        }
        if ( response != null ) {
            transceiverServer.send( new ServerMessage( message.getClientUUID(), response ) );
        }
    }

    public ServerMessage getMessage() {
        return message;
    }
}
