package org.fractal.map.web.response;

import java.util.ArrayList;
import java.util.List;

public class LoadTextObjectsInfo extends AbstractInfo {

    private List<TextObjectInfo> textObjects = new ArrayList<>();

    public List<TextObjectInfo> getTextObjects() {
        return textObjects;
    }

    public void addTextObject( double re, double im, int canvasLeftX, int canvasTopY, String text ) {
        textObjects.add( new TextObjectInfo( re, im, canvasLeftX, canvasTopY, text ) );
    }
}
