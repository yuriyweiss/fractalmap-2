FRMP.mouseDown = false;
FRMP.markedRect = {
    x1: 0,
    y1: 0,
    x2: 0,
    y2: 0,
    xPrev: 0,
    yPrev: 0
};
FRMP.backupImage = null;

FRMP.redrawSelectionRect = function (canvasCoords) {
    if (!FRMP.needDrawRectangle(canvasCoords)) {
        return;
    }
    FRMP.restoreBackupImage();
    FRMP.saveBackupImage(canvasCoords.canvasX, canvasCoords.canvasY);
    FRMP.drawSelectionRect(canvasCoords.canvasX, canvasCoords.canvasY);
};

FRMP.needDrawRectangle = function (coords) {
    let rect = FRMP.rectFromCoords(FRMP.markedRect.x1, FRMP.markedRect.y1, coords.canvasX, coords.canvasY);
    return (rect.width > 5) && (rect.height > 5);
}

FRMP.restoreBackupImage = function () {
    if (FRMP.backupImage != null) {
        let ctx = FRMP.fractalCanvas.getContext('2d');
        ctx.putImageData(FRMP.backupImage.imageData, FRMP.backupImage.left, FRMP.backupImage.top);
    }
};

FRMP.saveBackupImage = function (canvasX, canvasY) {
    let rect = FRMP.rectFromCoords(FRMP.markedRect.x1, FRMP.markedRect.y1, canvasX, canvasY);
    let ctx = FRMP.fractalCanvas.getContext('2d');
    let backupImageData = ctx.getImageData(rect.left, rect.top, rect.width, rect.height);
    FRMP.backupImage = {
        left: rect.left,
        top: rect.top,
        imageData: backupImageData
    };
};

FRMP.rectFromCoords = function (x1, y1, x2, y2) {
    return {
        left: (x1 < x2) ? x1 : x2,
        top: (y1 < y2) ? y1 : y2,
        width: Math.abs(x2 - x1),
        height: Math.abs(y2 - y1)
    };
};

FRMP.drawSelectionRect = function (canvasX, canvasY) {
    let rect = FRMP.rectFromCoords(FRMP.markedRect.x1, FRMP.markedRect.y1, canvasX, canvasY);
    let ctx = FRMP.fractalCanvas.getContext('2d');
    ctx.strokeStyle = "#FF0000";
    ctx.strokeRect(rect.left + 1, rect.top + 1, rect.width - 2, rect.height - 2);
};
