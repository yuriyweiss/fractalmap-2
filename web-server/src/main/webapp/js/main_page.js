$(document).ready(function() {
	FRMP.showStatus('Page loaded');
	
	let canvas = $('#fractal_canvas')[0];
	canvas.width = window.innerWidth - 60;
	canvas.height = window.innerHeight - 60;
	FRMP.fractalCanvas = $('#fractal_canvas')[0];
	FRMP.initializeForm();
	
	FRMP.fractalCanvas.addEventListener('click', onCanvasClick);
	FRMP.fractalCanvas.addEventListener('dblclick', onCanvasDblClick);
});

function refreshPageInfo() {
	$('#point_re').val(FRMP.centerRe.toString());
	$('#point_im').val(FRMP.centerIm.toString());
	$('#layer_index').text('Layer index: ' + FRMP.currentLayerIndex);
}

function onCanvasClick(evt) {
	console.log('mouse clicked');
	let canvasCoords = getClickCoordsInCanvas(evt);
	FRMP.getPointCoords(canvasCoords.canvasX, canvasCoords.canvasY);
	return false;
}

function getClickCoordsInCanvas(evt) {
	let rect = FRMP.fractalCanvas.getBoundingClientRect();
	let result = {
		canvasX : evt.clientX - rect.left,
		canvasY : evt.clientY - rect.top
	}
	return result;
}

function onCanvasDblClick(evt) {
	console.log('mouse dblclicked');
	let canvasCoords = getClickCoordsInCanvas(evt);
	FRMP.getPointCoords(canvasCoords.canvasX, canvasCoords.canvasY, function() {
		FRMP.changeViewportParams(FRMP.currentLayerIndex, FRMP.mousePosRe, FRMP.mousePosIm);
	});
	return false;
}
