FRMP.zoomInOneLayer = function() {
    FRMP.zoomOneLayer(true);
};

FRMP.zoomOutOneLayer = function() {
    FRMP.zoomOneLayer(false);
};

FRMP.zoomOneLayer = function(zoomIn) {
    let zoomOut = !zoomIn;
    // return if no zoom possible
    if ((zoomIn && !FRMP.hasNextLayer()) || (zoomOut && !FRMP.hasPrevLayer())) {
        console.log('Zoom impossible. Side layer reached.');
        return;
    }
    // get target layer index
    let changedLayerIndex = FRMP.getNextLayerIndex();
    if (!zoomIn) {
        changedLayerIndex = FRMP.getPrevLayerIndex();
    }
    FRMP.changeViewportParams(changedLayerIndex, FRMP.centerRe, FRMP.centerIm);
};
