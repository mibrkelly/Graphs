/*The first puzzle. The player must build a logical and from only or and not gates.*/

package Graphs;

import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;

public class GraphPuz implements PixelDrawer {
	
	private int level;
	
	private int width;
	private int height;
	
	protected Pieces[] pieces;
	protected Pieces moving;
	protected int[][] edges;
	protected boolean selectedMode;
	protected boolean checkingMode;
	protected int selected;
	protected int numInputs;
	
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
		selected = -1;
		checkingMode = false;
	}
	
	public void update() {
		boolean[] added = new boolean[pieces.length];
		for (int i = 0; i < pieces.length; i++) {
			added[i] = false;
		}
		int countAdded = 0;
		int lastCount = -1;
		while (countAdded != lastCount) {
			lastCount = countAdded;
			for (int i = 0; i < pieces.length; i++) {
				if (!added[i]) {
					boolean addNow = true;
					for (int j = 0; j < pieces.length; j++) {
						if (!added[j] && edges[j][i] > -1) {
							addNow = false;
						}
					}
					if (addNow) {
						added[i] = true;
						countAdded++;
						int j = 0;
						numInputs = 1;
						while (numInputs > 0 && j < pieces.length) {
							if (edges[j][i] > -1) {
								numInputs--;
								pieces[i].inputs = pieces[j].getOut();
								edges[j][i] = pieces[j].getOut();
							}
							j++;
						}
					}
				}
			}
		}
	}
	
	//***********************************************//
	//The following methods render the graphics      //
	//***********************************************//
	
	public void render(int keepTime) {
		Square(50,50,1080,550,0x000000);
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces.length; j++) {
				if (edges[i][j] != -1) {
					drawLn(pieces[j].getX()+15,pieces[j].getY()+15,pieces[i].getX()+15,pieces[i].getY()+15,0xffffff);
				}
			}
		}
		
		for (int i = 0; i < pieces.length; i++) {
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
			//if (x < 1100 & x > 100 && y < 600 && y > 50) {
				pix[(int)x][(int)y] = color;
				pix[(int)x+1][(int)y] = color;
				pix[(int)x-1][(int)y] = color;
				pix[(int)x][(int)y+1] = color;
				pix[(int)x][(int)y-1] = color;
			//}
		}
	}
	
	//***********************************************//
	//This following methods handle the mouse input  //
	//***********************************************//
	
	public boolean clickedAt(int x, int y) {
		if ((1050 < x && x < 1150 && 530 < y && y < 560) || (1050 < x && x < 1150 && 490 < y && y < 520)) {
			return true;
		}
		if (selectedMode) {
			for (int i = 0; i < pieces.length; i++) {
				if (pieces[i].getX() < x && pieces[i].getX()+30 > x && pieces[i].getY() < y && pieces[i].getY()+30 > y && edges[i][selected] == -1) {
					if (pieces[i] == pieces[selected]) {
						for (int j = 0; j < pieces.length; j++) {
							edges[j][selected] = -1;
							edges[selected][j] = -1;
						}
						pieces[selected].inputs = 0x00ff00;
						selectedMode = false;
						return false;
					}
					edges[i][selected] = 0;
					numInputs--;
					if (numInputs <= 0) {
						pieces[selected].inputs = 0x00ff00;
						selectedMode = false;
						update();
						return false;
					}
				}
			}
		}
		if (!selectedMode && !checkingMode) {
			for (int i = 0; i < pieces.length; i++) {
				if (pieces[i].getX() < x && pieces[i].getX()+30 > x && pieces[i].getY() < y && pieces[i].getY()+30 > y && pieces[i].getChangable()) {
					pieces[i].inputs = 0x0000ff;
					selected = i;
					numInputs = 1;
					selectedMode = true;
				}
			}
		}
		return false;
	}
	
	public void pressedAt(int x, int y) {
		if (!selectedMode && !checkingMode) {
			for (int i = 0; i < pieces.length; i++) {
				if (pieces[i].getX() < x && pieces[i].getX()+30 > x && pieces[i].getY() < y && pieces[i].getY()+30 > y) {
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