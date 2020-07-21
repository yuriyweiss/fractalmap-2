var FRMP = {};

FRMP.squares = null;
FRMP.textObjects = null;
FRMP.centerRe = 0;
FRMP.centerIm = 0;
FRMP.processingSquare = 0;
FRMP.mousePosRe = 0;
FRMP.mousePosIm = 0;

FRMP.browsingMode = 'Normal';
FRMP.normalMode = 'Normal';
FRMP.findRootMode = 'Find root'

FRMP.showStatus = function (message) {
    $('#status_bar').text(message);
    console.log(message);
};

FRMP.getNextLayerIndex = function () {
    let result = FRMP.currentLayerIndex;
    if (result < FRMP.layers[FRMP.layers.length - 1].layerIndex) {
        result++;
    }
    return result;
};

FRMP.getPrevLayerIndex = function () {
    let result = FRMP.currentLayerIndex;
    if (result > FRMP.layers[0].layerIndex) {
        result--;
    }
    return result;
};

FRMP.hasNextLayer = function () {
    return FRMP.currentLayerIndex !== FRMP.getNextLayerIndex();
};

FRMP.hasPrevLayer = function () {
    return FRMP.currentLayerIndex !== FRMP.getPrevLayerIndex();
};

FRMP.switchBrowsingMode = function () {
    if (FRMP.browsingMode === FRMP.normalMode) {
        FRMP.browsingMode = FRMP.findRootMode;
        $('#home_button').prop('disabled', true);
        $('#zoom_in_button').prop('disabled', true);
        $('#zoom_out_button').prop('disabled', true);
    } else {
        FRMP.browsingMode = FRMP.normalMode;
        $('#home_button').prop('disabled', false);
        $('#zoom_in_button').prop('disabled', false);
        $('#zoom_out_button').prop('disabled', false);
    }
    $('#switch_mode').text(FRMP.browsingMode);
};
