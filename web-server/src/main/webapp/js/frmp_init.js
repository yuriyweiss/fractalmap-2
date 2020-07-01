var FRMP = {};

FRMP.squares = null;
FRMP.centerRe = 0;
FRMP.centerIm = 0;
FRMP.processingSquare = 0;
FRMP.mousePosRe = 0;
FRMP.mousePosIm = 0;

FRMP.showStatus = function(message) {
	$('#status_bar').text(message);
	console.log(message);
};

FRMP.getNextLayerIndex = function() {
	var result = FRMP.currentLayerIndex;
	if (result < FRMP.layers[FRMP.layers.length - 1].layerIndex) {
		result++;
	}
	return result;
};

FRMP.getPrevLayerIndex = function() {
	var result = FRMP.currentLayerIndex;
	if (result > FRMP.layers[0].layerIndex) {
		result--;
	}
	return result;
};

FRMP.hasNextLayer = function() {
	return FRMP.currentLayerIndex !== FRMP.getNextLayerIndex();
};

FRMP.hasPrevLayer = function() {
	return FRMP.currentLayerIndex !== FRMP.getPrevLayerIndex();
};
