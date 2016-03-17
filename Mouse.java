package Graphs;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public class Mouse implements MouseListener, MouseMotionListener {
	public boolean pressing;
	public boolean clicked;
	public boolean dragging;
	public boolean released;
	public int clickX;
	public int clickY;
	public int pressX;
	public int pressY;
	public int dragX;
	public int dragY;
	
	
	public void mouseClicked (MouseEvent e) {
		clickX = e.getX();
		clickY = e.getY();
		clicked = true;
	}
	
	public void mousePressed (MouseEvent e) {
		pressing = true;
		pressX = e.getX();
		pressY = e.getY();
	}
	
	public void mouseReleased (MouseEvent e) {
		pressing = false;
		dragging = false;
		released = true;
	}
	
	public void mouseEntered (MouseEvent e) {
		
	}
	
	public void mouseExited (MouseEvent e) {
		
	}
	
	public void mouseDragged (MouseEvent e) {
		dragging = true;
		dragX = e.getX();
		dragY = e.getY();
	}
	
	public void mouseMoved (MouseEvent e) {
		
	}

}