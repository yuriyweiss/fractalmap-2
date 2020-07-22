FRMP.moveLeft = function () {
    FRMP.moveViewport(-FRMP.fractalCanvas.width / 5, 0);
};

FRMP.moveRight = function () {
    FRMP.moveViewport(FRMP.fractalCanvas.width / 5, 0);
};

FRMP.moveUp = function () {
    FRMP.moveViewport(0, -FRMP.fractalCanvas.height / 5);
};

FRMP.moveDown = function () {
    FRMP.moveViewport(0, FRMP.fractalCanvas.height / 5);
};

FRMP.moveViewport = function (shiftX, shiftY) {
    let data = {
        shiftX: Math.round(shiftX),
        shiftY: Math.round(shiftY)
    };
    $.get('get-point-coords', data, function (coordsResult) {
        if (!coordsResult.wasError) {
            $('#left_re').val(coordsResult.re.toString());
            FRMP.centerRe = coordsResult.re;
            $('#top_im').val(coordsResult.im.toString());
            FRMP.centerIm = coordsResult.im;
            FRMP.changeViewportParams(FRMP.currentLayerIndex, FRMP.centerRe, FRMP.centerIm);
        }
    });
};
