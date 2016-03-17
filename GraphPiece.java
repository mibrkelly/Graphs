/*This is the basic logical or unit*/

package Graphs;

public class GraphPiece extends Pieces {
	
	public GraphPiece (int x, int y) {
		super (x,y,true,1);
		inputs = 0x00ff00;
	}
	
	@Override
	public int getOut() {
		return inputs;
	}
	
	@Override
	public void draw() {
		int x = getX();
		int y = getY();
		for(int i = 0; i < 30; i++) {
			for(int j = 0; j < 30; j++) {
				if((i-15)*(i-15)+(j-15)*(j-15) < 15*15 && (i-15)*(i-15)+(j-15)*(j-15) > 10*10)
					pix[getX()+i][getY()+j] = inputs;
			}
		}
	}
}