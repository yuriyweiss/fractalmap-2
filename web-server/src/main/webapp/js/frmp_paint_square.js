FRMP.paintSquare = function(squareResult) {
	FRMP.showStatus('Painting square');
	if (squareResult.iterations === -2) {
		FRMP.showStatus('Paint square body');
		FRMP.paintSquareIterationsDiffer(squareResult);
	} else {
		FRMP.showStatus('Paint square iterations: ' + squareResult.iterations);
		FRMP.paintSquareIterationsCommon(squareResult.iterations);
	}
};

FRMP.paintSquareIterationsCommon = function(iterations) {
	let colorValue = FRMP.getColorValue(iterations);
	let canvas = FRMP.fractalCanvas;
	let ctx = canvas.getContext('2d');
	let fillStyleStr = 'rgb(' + colorValue + ', ' + colorValue + ', ' + colorValue + ')';
	ctx.fillStyle = fillStyleStr;
	ctx.fillRect(FRMP.currentSquare.canvasLeftX, FRMP.currentSquare.canvasTopY, FRMP.squareSideSize, FRMP.squareSideSize);
};

FRMP.getColorValue = function(iterations) {
	if (iterations === -1)
		return 0;
	let result = (iterations % 16) + 1;
	result = (result * 16) - 1;
	return result;
};

FRMP.paintSquareIterationsDiffer = function(squareResult) {
	let square = FRMP.currentSquare;
	let inverted = (square.topIm <= 0) ? true : false;
	let squareBody = squareResult.points;
	FRMP.showStatus('Square inverted: ' + inverted + ' leftRe: '
			+ square.leftRe + ' topIm: ' + square.topIm + ' canvasLeftX: '
			+ square.canvasLeftX + ' canvasTopY: ' + square.canvasTopY);
	let canvas = FRMP.fractalCanvas;
	let ctx = canvas.getContext('2d');
	console.log('painting square with different iterations');
	let imgData = ctx.createImageData(FRMP.squareSideSize, FRMP.squareSideSize);
	for (let i = 0; i < FRMP.squareSideSize; i++) {
		for (let j = 0; j < FRMP.squareSideSize; j++) {
			let targetI = (inverted) ? ((FRMP.squareSideSize - 1) - i) : i;
			let dataIndex = ((i * FRMP.squareSideSize) + j) * 4;
			let colorValue = FRMP.getColorValue(squareBody[targetI][j]);
			imgData.data[dataIndex + 0] = colorValue;
			imgData.data[dataIndex + 1] = colorValue;
			imgData.data[dataIndex + 2] = colorValue;
			imgData.data[dataIndex + 3] = 255; // alpha
		}
	}
	ctx.putImageData(imgData, square.canvasLeftX, square.canvasTopY);
};
