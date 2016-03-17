/*This is the abstract superclass of all of the game peices*/

package Graphs;

public abstract class Pieces implements PixelDrawer {
	private int xPos; //Store x position
	private int yPos; //Store y position
	private boolean changable; //Is this a piece the player can change in the puzzle?
	private int numInputs;
	public int inputs;
	
	public Pieces (int x, int y, boolean isChangable, int num) {
		xPos = x;
		yPos = y;
		changable = isChangable;
		numInputs = num;
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	public void setX(int x) {
		xPos = x;
	}
	
	public void setY(int y) {
		yPos = y;
	}
	
	public boolean getChangable() {
		return changable;
	}
	
	public void setChangable(boolean isChangable) {
		changable = isChangable;
	}
	
	public abstract int getOut();
	public abstract void draw();
}