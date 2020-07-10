package org.fractal.map.exception;

/**
 * Created by Вайс Юрий Vays.Yur.Le@omega.sbrf.ru on 10.07.2020.
 */
public class FatalFractalMapException extends RuntimeException {

    public FatalFractalMapException( String message ) {
        super( message );
    }

    public FatalFractalMapException( String message, Throwable cause ) {
        super( message, cause );
    }
}
