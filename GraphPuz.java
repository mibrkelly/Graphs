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
		
		
		edges = new int[numPieces][numPieces];
		
		for (int i = 0; i < numPieces; i++) {
			for (int j = 0; j < numPieces; j++) {
				edges[i][j] = -1;
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
			if (pieces[i] != null && pieces[i].selected) {
				branchNeighbors(i);
			}
		}
	}
	public void branchNeighbors (int rootNode) {
		for (int i = 0; i < pieces.length; i++) {
			if (i != rootNode && (edges[rootNode][i] != -1) || (edges[i][rootNode] != -1)) {
				if (!pieces[i].selected) {
					pieces[i].selected = true;
					branchNeighbors(i);
				}
			}
		}
	}
	
	public void removeVertex() {
		for (int i = 0; i < pieces.length; i++) {
			if (pieces[i] != null && pieces[i].selected) {
				for (int j = 0; j < pieces.length; j++) {
					edges[j][i] = -1;
					edges[i][j] = -1;
				}
				pieces[i] = null;
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
		for (int i = 0; i < pieces.length; i++) {
			piecesOld[i] = pieces[i];
			for (int j = 0; j < pieces.length; j++) {
				edgesOld[i][j] = edges[i][j];
			}
		}
		pieces = new Pieces[piecesOld.length+5];
		edges = new int[pieces.length][pieces.length];
		for (int i = 0; i < pieces.length-5; i++) {
			pieces[i] = piecesOld[i];
		}
		
		for (int i = 0; i < pieces.length; i++) {
			if (i < pieces.length-5) {
				for (int j = 0; j < pieces.length-5; j++) {
					edges[i][j] = edgesOld[i][j];
				}
				for (int j = pieces.length-5; j < pieces.length; j++) {
					edges[i][j] = -1;
				}
			}
			else {
				for (int j = 0; j < pieces.length; j++) {
					edges[i][j] = -1;
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
				if (edges[i][j] != -1) {
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
	
	public boolean clickedAt(int x, int y) {
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
							edges[k][i] = -1;
							edges[i][k] = -1;
						}
					}
					else {
						for (int j = 0; j < pieces.length; j++) {
							if (pieces[j] != null && pieces[j].selected) {
								edges[i][j] = 0;
							}
						}
					}
				}
			}
			for (int i = 0; i < pieces.length; i++) {
				if (pieces[i] != null && pieces[i].selected) {
					pieces[i].selected = false;
				}
			}
			selectedMode = false;
			
			
			/*
			for (int i = 0; i < pieces.length; i++) {
				if (pieces[i] != null && pieces[i].selected) {
					for (int j = 0; j < pieces.length; j++) {
						if (pieces[j] != null && pieces[j].getX() < x && pieces[j].getX()+30 > x && pieces[j].getY() < y && pieces[j].getY()+30 > y && edges[j][i] == -1) {
							if (pieces[j] == pieces[i]) {
								for (int k = 0; k < pieces.length; k++) {
									edges[k][i] = -1;
									edges[i][k] = -1;
								}
								pieces[i].selected = false;
								selectedMode = false;
								return false;
							}
							else {
								edges[j][i] = 0;
								pieces[i].selected = false;
								selectedMode = false;
								return false;
							}
						}
					}
				}
			}*/
		}
		else if (!selectedMode) {
			for (int i = 0; i < pieces.length; i++) {
				if (pieces[i] != null && pieces[i].getX() < x && pieces[i].getX()+30 > x && pieces[i].getY() < y && pieces[i].getY()+30 > y && pieces[i].getChangable()) {
					pieces[i].selected = true;
					selectedMode = true;
				}
			}
		}
		return false;
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
}