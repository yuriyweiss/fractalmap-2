package org.fractal.map.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayerRegistry {

    private static final Map<Integer, Layer> layers = new HashMap<>();

    private LayerRegistry() {
    }

    static {
        init();
    }

    private static void init() {
        // TODO load layers from DB
        addLayer( 1, 1000, 1024 );
        addLayer( 2, 1000, 4096 );
        addLayer( 3, 1000, 16384 );
        addLayer( 4, 1000, 65536 );
        addLayer( 5, 1500, 262144 );
        addLayer( 6, 1500, 1048576 );
        addLayer( 7, 1500, 4194304 );
        addLayer( 8, 2000, 16777216 );
        addLayer( 9, 2000, 67108864 );
        addLayer( 10, 2000, 268435456 );
    }

    private static void addLayer( int layerIndex, int iterations, long layerSideSize ) {
        layers.put( layerIndex, new Layer( layerIndex, iterations, layerSideSize ) );
    }

    public static Layer getLayerByIndex( int layerIndex ) {
        return layers.get( layerIndex );
    }

    public static List<Layer> getLayers() {
        List<Layer> result = new ArrayList<>();
        for ( Layer layer : layers.values() ) {
            result.add( layer );
        }
        Collections.sort( result );
        return result;
    }
}
