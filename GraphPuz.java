/*The first puzzle. The player must build a logical and from only or and not gates.*/

package Graphs;

import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;

public class GraphPuz implements PixelDrawer {	
	private int width;
	private int height;
	
	protected Pieces[] pieces;
	protected Pieces moving;
	protected int[][] edges;
	protected boolean selectedMode;
	
	private String graphDiameter = "";
	private String graphHamiltonian = "";
	
	
	private int componentX = 0;
	private int componentY = 0;
	private boolean[] currentComponent;
	
	public GraphPuz(int w, int h) {
		width = w;
		height = h;
		
		int numPieces = 5;
		pieces = new Pieces[numPieces];
		pieces[0] = new GraphPiece(500,175);
		pieces[1] = new GraphPiece(400,250);
		pieces[2] = new GraphPiece(600,250);
		pieces[3] = new GraphPiece(450,350);
		pieces[4] = new GraphPiece(550,350);
		
		currentComponent = new boolean[pieces.length];
		for (int i = 0; i < pieces.length; i++) {
			currentComponent[i] = false;
		}
		
		edges = new int[numPieces][numPieces];
		
		for (int i = 0; i < numPieces; i++) {
			for (int j = 0; j < numPieces; j++) {
				edges[i][j] = 0;
			}
		}
		
		moving = null;
		selectedMode = false;
	}
	
	//***********************************************//
	//The following methods update the graph         //
	//***********************************************//
	
	public void selectComponent() {
		for (int i = 0; i < pieces.length; i++) {
			if (pieces[i] != null)
				pieces[i].selected = currentComponent[i];
		}
	}
	
	public void findCurrentComponent (int rootNode) {
		for (int i = 0; i < currentComponent.length; i++) {
			currentComponent[i] = false;
		}
		currentComponent[rootNode] = true;
		branchNeighbors(rootNode);
	}
	public void branchNeighbors (int rootNode) {
		for (int i = 0; i < pieces.length; i++) {
			if (i != rootNode && (edges[rootNode][i] != 0) || (edges[i][rootNode] != 0)) {
				if (!currentComponent[i]) {
					currentComponent[i] = true;
					branchNeighbors(i);
				}
			}
		}
	}
	
	public void removeVertex() {
		for (int i = 0; i < pieces.length; i++) {
			if (pieces[i] != null && pieces[i].selected) {
				for (int j = 0; j < pieces.length; j++) {
					edges[j][i] = 0;
					edges[i][j] = 0;
				}
				pieces[i] = null;
				currentComponent[i] = false;
			}
		}
		selectedMode = false;
	}
	
	public void addVertex() {
		for (int i = 0; i < pieces.length; i++) {
			if (pieces[i] == null) {
				pieces[i] = new GraphPiece(1030,100+(10*i)%460);
				return;
			}
		}
		Pieces[] piecesOld = new Pieces[pieces.length];
		int[][] edgesOld = new int[pieces.length][pieces.length];
		boolean[] componentOld = new boolean[currentComponent.length];
		for (int i = 0; i < pieces.length; i++) {
			piecesOld[i] = pieces[i];
			for (int j = 0; j < pieces.length; j++) {
				edgesOld[i][j] = edges[i][j];
			}
			componentOld[i] = currentComponent[i];
		}
		pieces = new Pieces[piecesOld.length+5];
		edges = new int[pieces.length][pieces.length];
		currentComponent = new boolean[pieces.length];
		for (int i = 0; i < pieces.length-5; i++) {
			pieces[i] = piecesOld[i];
			currentComponent[i] = componentOld[i];
		}
		
		
		for (int i = 0; i < pieces.length; i++) {
			if (i < pieces.length-5) {
				for (int j = 0; j < pieces.length-5; j++) {
					edges[i][j] = edgesOld[i][j];
				}
				for (int j = pieces.length-5; j < pieces.length; j++) {
					edges[i][j] = 0;
				}
			}
			else {
				for (int j = 0; j < pieces.length; j++) {
					edges[i][j] = 0;
				}
			}
		}
		pieces[pieces.length-5] = new GraphPiece(1030,100);
	}
	
	//***********************************************//
	//The following methods render the graphics      //
	//***********************************************//
	
	public void render(int keepTime) {
		Square(50,50,1080,550,0x000000); //Background
		Square(730,20,100,30,0x333333); //Component
		Square(880,20,100,30,0x333333); //Delete
		Square(1030,20,100,30,0x333333); //New
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces.length; j++) {
				if (edges[i][j] != 0) {
					drawLn(pieces[j].getX()+15,pieces[j].getY()+15,pieces[i].getX()+15,pieces[i].getY()+15,0xffffff);
				}
			}
		}
		
		for (int i = 0; i < pieces.length; i++) {
			if (pieces[i] != null)
				pieces[i].draw();
		}
	}
	
	public void clear(int color) {
		Square(0,0,width,height,color);
	}
	
	public void write (Graphics g) {
		g.setColor(Color.YELLOW);
		g.drawString("Drag the vertices with your mouse to move them around.",100,100);
		g.drawString("Click one vertex then another to connect them with an edge.",100,120);
		g.drawString("Click a vertex twice to remove all edges from it.",100,140);
		
		if (selectedMode) {
			g.drawString("Delete",884,42);
			g.drawString("Component",734,42);
		}
		g.drawString("New Vertex",1034,42);
		
		Color textBackground = new Color(255,255,255,50);
		g.setColor(textBackground);
		g.fillRect(componentX-5, componentY-20, 150, 50);
		g.setColor(Color.RED);
		g.drawString(graphDiameter, componentX, componentY);
		g.drawString(graphHamiltonian, componentX, componentY+15);
	}
	
	public void Square (int x, int y, int w, int h, int color) {
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if(x+i >= 0 && x+i <= 1200 && y+j >= 0 && y+j <= 650)
					pix[x+i][y+j] = color;
			}
		}
	}
	
	public void drawLn (int x0, int y0, int x1, int y1, int color) {
		double t = 0;
		for (int i = 0; i < 1500; i++) {
			t = (double)i/1500;
			double x = (double)x0*(1-t)+(double)x1*t;
			double y = (double)y0*(1-t)+(double)y1*t;
			pix[(int)x][(int)y] = color;
			pix[(int)x+1][(int)y] = color;
			pix[(int)x-1][(int)y] = color;
			pix[(int)x][(int)y+1] = color;
			pix[(int)x][(int)y-1] = color;
		}
	}
	
	//***********************************************//
	//This following methods handle the mouse input  //
	//***********************************************//
	
	public void clickedAt(int x, int y) {
		if (20 < y && y < 50) {
			if (selectedMode) {
				if (730 < x && x < 830) {
					selectComponent();
				}
				if (880 < x && x < 980) {
					removeVertex();
				}
			}
			if (1030 < x && x < 1130) {
				addVertex();
			}
		}
		
		else if (selectedMode) {
			for (int i = 0; i < pieces.length; i++) {
				if (pieces[i] != null && pieces[i].getX() < x && pieces[i].getX()+30 > x && pieces[i].getY() < y && pieces[i].getY()+30 > y) {
					if (pieces[i].selected) {
						for (int k = 0; k < pieces.length; k++) {
							edges[k][i] = 0;
							edges[i][k] = 0;
						}
						findCurrentComponent(i);
					}
					else {
						for (int j = 0; j < pieces.length; j++) {
							if (pieces[j] != null && pieces[j].selected) {
								edges[i][j] = 1;
								edges[j][i] = 1;
							}
						}
						findCurrentComponent(i);
					}
				}
			}
			for (int i = 0; i < pieces.length; i++) {
				if (pieces[i] != null && pieces[i].selected) {
					pieces[i].selected = false;
				}
			}
			selectedMode = false;
		}
		else if (!selectedMode) {
			for (int i = 0; i < pieces.length; i++) {
				if (pieces[i] != null && pieces[i].getX() < x && pieces[i].getX()+30 > x && pieces[i].getY() < y && pieces[i].getY()+30 > y && pieces[i].getChangable()) {
					pieces[i].selected = true;
					selectedMode = true;
					findCurrentComponent(i);
				}
			}
		}
		graphDiameter = "Diameter " + computeDiameter();
		if (computeHamiltonian())
			graphHamiltonian = "Hamiltonian";
		else
			graphHamiltonian = "non-Hamiltonian";
	}
	
	public void pressedAt(int x, int y) {
		if (!selectedMode) {
			for (int i = 0; i < pieces.length; i++) {
				if (pieces[i] != null && pieces[i].getX() < x && pieces[i].getX()+30 > x && pieces[i].getY() < y && pieces[i].getY()+30 > y) {
					if (pieces[i].getChangable()) {
						moving = pieces[i];
						break;
					}
				}
			}
		}
	}
		
	public void draggedTo(int x, int y) {
		if (moving != null) {
			if (canMove(x,y)) {
				moving.setX(x);
				moving.setY(y);
			}
		}
	}
	
	public boolean canMove (int x, int y) {
		if (x >= PUZMINX && x <= PUZMAXX && y >= PUZMINY && y <= PUZMAXY) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void released() {
		moving = null;
	}
	
	//***********************************************//
	//The following methods gather graph data        //
	//***********************************************//
	
	public void update() {
		coordinateData();
	}
	
	public void coordinateData() {
		double xCenter = 0;
		double yCenter = 0;
		double numberOfCurrent = 0;
		for (int i = 0; i < pieces.length; i++) {
			if (currentComponent[i]) {
				xCenter += pieces[i].getX();
				yCenter += pieces[i].getY();
				numberOfCurrent += 1;
			}
		}
		componentX = (int)(xCenter/numberOfCurrent);
		componentY = (int)(yCenter/numberOfCurrent);
	}
	
	public boolean computeHamiltonian() {
		boolean isHamiltonian = true;
		for (int i = 0; i < edges.length; i++) {
			int count = 0;
			for (int j = 0; j < edges.length; j++) {
				count += edges[i][j];
			}
			if (count % 2 == 1)
				isHamiltonian = false;
		}
		return isHamiltonian;
	}
	
	public int computeDiameter() {
		int[][] edgesPower = new int[edges.length][edges.length];
		int[][] edgesSum = new int[edges.length][edges.length];
		for (int i = 0; i < edges.length; i++) {
			for (int j = 0; j < edges.length; j++) {
				if (i == j)
					edgesPower[i][j] = 1;
				else
					edgesPower[i][j] = 0;
				edgesSum[i][j] = 0;
			}
		}
		
		int diameter = -1;
		boolean done;
		do {
			diameter++;
			done = true;
			for (int i = 0; i < edges.length; i++) {
				for (int j = 0; j < edges.length; j++) {
					if (currentComponent[i] && currentComponent[j] && edgesSum[i][j] == 0 && i != j) {
						done = false;
					}
				}
			}
			if (!done) {
				edgesPower = multiplyMatrix(edgesPower, edges);
				edgesSum = sumMatrix(edgesSum,edgesPower);
			}
		} while (!done);
		
		return diameter;
	}
	
	public int[][] multiplyMatrix(int[][] mtx1, int[][] mtx2) {
		if (mtx1[0].length != mtx2.length) {
			return null;
		}
		int[][] product = new int[mtx1.length][mtx2[0].length];
		for (int i = 0; i < product.length; i++) {
			for (int j = 0; j < product[0].length; j++) {
				product[i][j] = 0;
			}
		}
		for (int i = 0; i < mtx1.length; i++) {
			for (int j = 0; j < mtx2[0].length; j++) {
				for (int k = 0; k < mtx1[0].length; k++) {
					product[i][j] += mtx1[i][k]*mtx2[k][j];
				}
			}
		}
		return product;
	}
	
	public int[][] sumMatrix(int[][] mtx1, int[][] mtx2) {
		if ((mtx1.length != mtx2.length) || (mtx1[0].length != mtx2[0].length)) {
			return null;
		}
		int[][] sum = new int[mtx1.length][mtx1[0].length];
		for (int i = 0; i < mtx1.length; i++) {
			for (int j = 0; j < mtx2[0].length; j++) {
				sum[i][j] = mtx1[i][j] + mtx2[i][j];
			}
		}
		return sum;
	}
	
	/*
	public void printMatrix(int[][] mtx) {
		for (int i = 0; i < mtx.length; i++) {
			for (int j = 0; j < mtx.length; j++) {
				System.out.print(mtx[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}*/
}