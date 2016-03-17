/*This is the basic logical or unit*/

package Graphs;

public class GraphPiece extends Pieces {
	
	public GraphPiece (int x, int y) {
		super (x,y,true,1);
		selected = false;
	}
	
	//@Override
	//public int getOut() {
	//	return color;
	//}
	
	@Override
	public void draw() {
		int x = getX();
		int y = getY();
		int color = 0x00ff00;
		if (selected) {
			color = 0x0000ff;
		}
		for(int i = 0; i < 30; i++) {
			for(int j = 0; j < 30; j++) {
				if((i-15)*(i-15)+(j-15)*(j-15) < 15*15 && (i-15)*(i-15)+(j-15)*(j-15) > 10*10)
					pix[getX()+i][getY()+j] = color;
			}
		}
	}
}