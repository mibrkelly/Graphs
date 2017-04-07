var canvas;
var ctx;
var output;

var WIDTH = 1200;
var HEIGHT = 800;

tileW = 20;
tileH = 20;

tileRowCount = 25;
tileColumnCount = 40;

dragok = false;

boundX = 0;
boundY = 0;

var tiles = [];
for (c = 0; c < tileColumnCount; c++) {
	tiles[c] = [];
	for (r = 0; r < tileRowCount; r++) {
		tiles[c][r] = {x: c*(tileW+3), y: r*(tileH+3), state: 'e'}; //state is e for empty
	}
}
tiles[0][0].state = 's';
tiles[tileColumnCount-1][tileRowCount-1].state = 'f';

function rect(x,y,w,h,state) {
	//red red green green blue blue
	if (state == 's') {
		ctx.fillStyle = '#00FF00';
	}
	else if (state == 'f') {
		ctx.fillStyle = '#FF0000';
	}
	else if (state == 'e') {
		ctx.fillStyle = '#CCCCCC';
	}
	else if (state == 'w') {
		ctx.fillStyle = '#003366';
	}
	else if (state == 'x') {
		ctx.fillStyle = '#FFCC00';
	}
	else {
		ctx.fillStyle = '#CCCCCC';
	}
	
	  
	ctx.beginPath();
	ctx.rect(x,y,w,h);
	ctx.closePath();
	ctx.fill();
}

function clear() {
	ctx.clearRect(0,0,WIDTH,HEIGHT);
}

function draw() {
	clear();
	
	
	for (c = 0; c < tileColumnCount; c++) {
		for (r = 0; r < tileRowCount; r++) {
			rect(tiles[c][r].x, tiles[c][r].y, tileW, tileH, tiles[c][r].state);
		}
	}
}

function solveMaze() {
	var queue = [[0, 0]];
	
	var pathFound = false;
	
	var xLoc;
	var yLoc;
	
	while (queue.length > 0 && !pathFound) {
		var nextStep = queue.shift();
		xLoc = nextStep[0];
		yLoc = nextStep[1];
		
		if (xLoc < tileColumnCount - 1) {
			if (tiles[xLoc+1][yLoc].state == 'f') {
				pathFound = true;
			}
		}
		if (yLoc < tileRowCount - 1) {
			if (tiles[xLoc][yLoc+1].state == 'f') {
				pathFound = true;
			}
		}
		
		if (xLoc > 0) {
			if (tiles[xLoc-1][yLoc].state == 'e') {
				queue.push([xLoc-1, yLoc]);
				tiles[xLoc-1][yLoc].state = tiles[xLoc][yLoc].state + 'l';
			}
		}
		if (xLoc < tileColumnCount - 1) {
			if (tiles[xLoc+1][yLoc].state == 'e') {
				queue.push([xLoc+1, yLoc]);
				tiles[xLoc+1][yLoc].state = tiles[xLoc][yLoc].state + 'r';
			}
		}
		if (yLoc > 0) {
			if (tiles[xLoc][yLoc-1].state == 'e') {
				queue.push([xLoc, yLoc-1]);
				tiles[xLoc][yLoc-1].state = tiles[xLoc][yLoc].state + 'u';
			}
		}
		if (yLoc < tileRowCount - 1) {
			if (tiles[xLoc][yLoc+1].state == 'e') {
				queue.push([xLoc, yLoc+1]);
				tiles[xLoc][yLoc+1].state = tiles[xLoc][yLoc].state + 'd';
			}
		}
	}
	
	if (!pathFound) {
		output.innerHTML = 'No Solution';
	}
	else {
		output.innerHTML = 'Solved!';
		var path = tiles[xLoc][yLoc].state;
		var pathLength = path.length;
		var currX = 0;
		var currY = 0;
		for (var i = 0; i < pathLength-1; i++) {
			if (path.charAt(i+1) == 'u') {
				currY -= 1;
			}
			if (path.charAt(i+1) == 'd') {
				currY += 1;
			}
			if (path.charAt(i+1) == 'r') {
				currX += 1;
			}
			if (path.charAt(i+1) == 'l') {
				currX -= 1;
			}
			tiles[currX][currY].state = 'x';
		}
	}
}

function reset() {
	for (c = 0; c < tileColumnCount; c++) {
		tiles[c] = [];
		for (r = 0; r < tileRowCount; r++) {
			tiles[c][r] = {x: c*(tileW+3), y: r*(tileH+3), state: 'e'}; //state is e for empty
		}
	}
	tiles[0][0].state = 's';
	tiles[tileColumnCount-1][tileRowCount-1].state = 'f';
	
	output.innerHTML = 'Green is start. Red is finish.';
}

function init() {
	canvas = document.getElementById("myCanvas");
	ctx = canvas.getContext("2d");
	output = document.getElementById("outcome");
	return setInterval(draw, 10);
}

function myMove(e) {
    x = e.pageX - canvas.offsetLeft;
    y = e.pageY - canvas.offsetTop;
	
    for(c=0; c<tileColumnCount; c++) {
        for(r=0; r<tileRowCount; r++) {
  			if (c*(tileW+3) < x && x < c*(tileW+3)+tileW && r*(tileH+3) < y && y < r*(tileH+3)+tileH) {
  				if (tiles[c][r].state == "e" && (c != boundX || r != boundY)) {
  					tiles[c][r].state = "w";
					boundX = c;
					boundY = r;
  				}
  				else if (tiles[c][r].state == "w" && (c != boundX || r != boundY)) {
  					tiles[c][r].state = "e";
					boundX = c;
					boundY = r;
  				}
  			}
		}
    }
}

function myDown(e) {
	canvas.onmousemove = myMove;
	
    x = e.pageX - canvas.offsetLeft;
    y = e.pageY - canvas.offsetTop;
	
    for(c=0; c<tileColumnCount; c++) {
        for(r=0; r<tileRowCount; r++) {
  			if (c*(tileW+3) < x && x < c*(tileW+3)+tileW && r*(tileH+3) < y && y < r*(tileH+3)+tileH) {
  				if (tiles[c][r].state == "e") {
  					tiles[c][r].state = "w";
					boundX = c;
					boundY = r;
  				}
  				else if (tiles[c][r].state == "w") {
  					tiles[c][r].state = "e";
					boundX = c;
					boundY = r;
  				}
  			}
		}
    } 
}

function myUp() {
	canvas.onmousemove = null;
}

init();
canvas.onmousedown = myDown;
canvas.onmouseup = myUp;