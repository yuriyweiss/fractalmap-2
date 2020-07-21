$(document).ready(function () {
    FRMP.showStatus('Page loaded');

    let canvas = $('#fractal_canvas')[0];
    canvas.width = window.innerWidth - 60;
    canvas.height = window.innerHeight - 60;
    FRMP.fractalCanvas = $('#fractal_canvas')[0];
    FRMP.initializeForm();

    FRMP.fractalCanvas.addEventListener('click', onCanvasClick);
    FRMP.fractalCanvas.addEventListener('dblclick', onCanvasDblClick);
    FRMP.fractalCanvas.addEventListener('mousedown', onCanvasMouseDown);
    FRMP.fractalCanvas.addEventListener('mousemove', onCanvasMouseMove);
    FRMP.fractalCanvas.addEventListener('mouseup', onCanvasMouseUp);
});

function refreshPageInfo() {
    $('#point_re').val(FRMP.centerRe.toString());
    $('#point_im').val(FRMP.centerIm.toString());
    $('#layer_index').text('Layer index: ' + FRMP.currentLayerIndex);
}

function onCanvasClick(evt) {
    console.log('mouse clicked');
    let canvasCoords = getMouseCoordsInCanvas(evt);
    FRMP.getPointCoords(canvasCoords.canvasX, canvasCoords.canvasY);
    return false;
}

function getMouseCoordsInCanvas(evt) {
    let rect = FRMP.fractalCanvas.getBoundingClientRect();
    let result = {
        canvasX: evt.clientX - rect.left,
        canvasY: evt.clientY - rect.top
    }
    return result;
}

function onCanvasDblClick(evt) {
    console.log('mouse dblclicked');
    let canvasCoords = getMouseCoordsInCanvas(evt);
    FRMP.getPointCoords(canvasCoords.canvasX, canvasCoords.canvasY, function () {
        FRMP.changeViewportParams(FRMP.currentLayerIndex, FRMP.mousePosRe, FRMP.mousePosIm);
    });
    return false;
}

function onCanvasMouseDown(evt) {
    console.log('mouse down');
    let canvasCoords = getMouseCoordsInCanvas(evt);
    FRMP.mouseDown = true;
    FRMP.markedRect.x1 = canvasCoords.canvasX;
    FRMP.markedRect.y1 = canvasCoords.canvasY;
    FRMP.markedRect.x2 = canvasCoords.canvasX;
    FRMP.markedRect.y2 = canvasCoords.canvasY;
}

function onCanvasMouseMove(evt) {
    if (FRMP.mouseDown) {
        let canvasCoords = getMouseCoordsInCanvas(evt);
        let dx = Math.abs(FRMP.markedRect.x2 - canvasCoords.canvasX);
        let dy = Math.abs(FRMP.markedRect.y2 - canvasCoords.canvasY);
        if (dx > 5 || dy > 5) {
            console.log('mouse move, redraw selection rect');
            FRMP.redrawSelectionRect(canvasCoords);
        }
        FRMP.markedRect.x2 = canvasCoords.canvasX;
        FRMP.markedRect.y2 = canvasCoords.canvasY;
    }
}

function onCanvasMouseUp(evt) {
    if (FRMP.mouseDown) {
        console.log('mouse up after mouse down');
        let canvasCoords = getMouseCoordsInCanvas(evt);
        FRMP.markedRect.x2 = canvasCoords.canvasX;
        FRMP.markedRect.y2 = canvasCoords.canvasY;
        FRMP.restoreBackupImage();
        FRMP.mouseDown = false;
        FRMP.backupImage = null;
        FRMP.searchRoot();
    }
}
