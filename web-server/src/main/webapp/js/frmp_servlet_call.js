FRMP.initializeForm = function () {
    $.get('initialize-form', function (initParams) {
        FRMP.centerRe = initParams.centerRe;
        FRMP.centerIm = initParams.centerIm;
        FRMP.currentLayerIndex = initParams.currentLayerIndex;
        refreshPageInfo();
        FRMP.squareSideSize = initParams.squareSideSize;
        FRMP.layers = initParams.layers;
        FRMP.showStatus('SUCCESS Form initialized');
        FRMP.loadSquares();
    });
};

FRMP.returnToInitialState = function () {
    FRMP.centerRe = -0.5;
    FRMP.centerIm = 0;
    FRMP.currentLayerIndex = 1;
    FRMP.changeViewportParams(FRMP.currentLayerIndex, FRMP.centerRe, FRMP.centerIm);
};

FRMP.changeViewportParams = function (layerIndex, re, im) {
    let data = {
        layerIndex: layerIndex,
        re: re,
        im: im
    };
    $.get('change-viewport-params', data, function () {
        FRMP.centerRe = data.re;
        FRMP.centerIm = data.im;
        FRMP.currentLayerIndex = data.layerIndex;
        FRMP.loadSquares();
        refreshPageInfo();
    });
};

FRMP.loadSquares = function () {
    let data = {
        areaWidth: FRMP.fractalCanvas.width,
        areaHeight: FRMP.fractalCanvas.height
    };
    $.get('squares-partition', data, function (partitionResult) {
        if (partitionResult.wasError) {
            FRMP.showStatus('ERROR loading squares partition: '
                + partitionResult.errorMessage);
        } else {
            FRMP.showStatus('SUCCESS Area partition squares count: '
                + partitionResult.squares.length);
            FRMP.squares = partitionResult.squares;
            FRMP.clearCanvas();
            FRMP.processingSquare = 0;
            FRMP.loadNextSquare();
        }
    });
};

FRMP.clearCanvas = function () {
    let canvas = FRMP.fractalCanvas;
    let ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
}

FRMP.loadNextSquare = function () {
    // Stop recursion when last square processed.
    if ((FRMP.squares.length - 1) < FRMP.processingSquare) {
        FRMP.showStatus("all squares were loaded");
        FRMP.loadTextObjects();
        return;
    }
    // Prepare ajax request.
    let square = FRMP.squares[FRMP.processingSquare];
    FRMP.currentSquare = square;
    FRMP.showStatus("current square: " + square.leftRe + ", " + square.topIm
        + ", " + square.canvasLeftX + ", " + square.canvasTopY);
    let data = {
        leftRe: square.leftRe,
        topIm: square.topIm
    };
    // Perform request, wait for response.
    $.get('get-square', data, function (squareResult) {
        if (squareResult.iterations === -2) {
            FRMP.showStatus('SUCCESS square body loaded: [leftRe: '
                + FRMP.currentSquare.leftRe + ', topIm:'
                + FRMP.currentSquare.topIm + ']');
            FRMP.paintSquare(squareResult);
        } else if (squareResult.wasError) {
            FRMP.showStatus('ERROR loading square: ' + squareResult.errorMessage);
        } else {
            FRMP.showStatus('SUCCESS square iterations loaded: [leftRe: '
                + FRMP.currentSquare.leftRe + ', topIm:'
                + FRMP.currentSquare.topIm + ', iterations: '
                + squareResult.iterations + ']');
            FRMP.paintSquare(squareResult);
        }
        // Increase processing square index and call next ajax request.
        FRMP.processingSquare++;
        FRMP.loadNextSquare();
    });
};

FRMP.getPointCoords = function (canvasX, canvasY, onSuccess) {
    // get canvas center coords
    let canvasCenterX = Math.round(FRMP.fractalCanvas.width / 2);
    let canvasCenterY = Math.round(FRMP.fractalCanvas.height / 2);
    // calculate shiftX, shiftY relative to the center
    let calcShiftX = canvasX - canvasCenterX;
    // y coord shift is inverted
    let calcShiftY = canvasCenterY - canvasY;
    let data = {
        shiftX: calcShiftX,
        shiftY: calcShiftY
    };
    $.get('get-point-coords', data, function (coordsResult) {
        if (!coordsResult.wasError) {
            $('#point_re').val(coordsResult.re.toString());
            FRMP.mousePosRe = coordsResult.re;
            $('#point_im').val(coordsResult.im.toString());
            FRMP.mousePosIm = coordsResult.im;
            if (onSuccess) {
                onSuccess();
            }
        }
    });
};
