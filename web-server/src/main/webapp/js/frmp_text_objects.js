FRMP.loadTextObjects = function () {
    FRMP.showStatus("loadTextObjects started");
    let data = {
        areaWidth: FRMP.fractalCanvas.width,
        areaHeight: FRMP.fractalCanvas.height
    };
    $.get('load-text-objects', data, function (textObjectsResult) {
        if (textObjectsResult.wasError) {
            FRMP.showStatus("ERROR loading text objects: " + textObjectsResult.errorMessage);
        } else {
            FRMP.showStatus("SUCCESS Loaded text objects count: " + textObjectsResult.textObjects.length);
            FRMP.textObjects = textObjectsResult.textObjects;
            FRMP.paintTextObjects();
        }
    });
};

FRMP.paintTextObjects = function () {
    if (!FRMP.textObjects) {
        FRMP.showStatus("no text objects were loaded");
        return;
    }
    let canvas = FRMP.fractalCanvas;
    let ctx = canvas.getContext('2d');
    ctx.strokeStyle = "#FF0000";
    ctx.fillStyle = "#00fd04";
    ctx.font = "14px tahoma";
    for (let i = 0; i < FRMP.textObjects.length; i++) {
        let textObjectInfo = FRMP.textObjects[i];
        let x = textObjectInfo.canvasX;
        let y = textObjectInfo.canvasY;
        ctx.beginPath();
        ctx.arc(x, y, 3, 0, 2 * Math.PI);
        ctx.stroke();
        // add font height to put text under the circle
        ctx.fillText(textObjectInfo.text, x, y + 14 + 3);
    }
    ctx.strokeStyle = "#000000";
};
