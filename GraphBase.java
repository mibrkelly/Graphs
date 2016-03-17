/*
This class includes the main method. Game runs the loop to update the status of the game and draw to the screen. It also handles the listening for the mouse and keyboard input.
*/

package Graphs;
import javax.swing.JFrame;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.Graphics;
import java.awt.Color;

public class GraphBase extends Canvas implements Runnable, PixelDrawer {
	private static final long serialVersionUID = 1L;
	
	public static int width = 1200;
	public static int height = 650;
	public static String title = "Game";
	private boolean running = false;
	private int updates;
	
	private Thread thread;
	private JFrame frame;
	
	private boolean mouseIn = true;
	private Mouse mouse;
	
	private GraphPuz currState;
	
	private BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	public GraphBase() {
		Dimension size = new Dimension(width,height);
		setPreferredSize(size);
		
		currState = new GraphPuz(width, height);
		frame = new JFrame();
		
		mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}
	
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		updates = 0;
		
		while(running) {
			long now = System.nanoTime();
			delta += (now-lastTime)/ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer  > 1000) {
				timer += 1000;
				frame.setTitle(title + "   |   " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	public void update() {
		if (mouse.clicked) {
			currState.clickedAt(mouse.clickX, mouse.clickY);
			mouse.clicked = false;
		}
		if (mouse.pressing) {
			currState.pressedAt(mouse.pressX, mouse.pressY);
		}
		if (mouse.dragging) {
			currState.draggedTo(mouse.dragX, mouse.dragY);
		}
		if (mouse.released) {
			currState.released();
			mouse.released = false;
		}
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		currState.clear(0xa4aeeb);
		currState.render(updates);
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = pix[i % width][(i / width)];
		}
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.setColor(Color.BLACK);
		currState.write(g);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		GraphBase game = new GraphBase();
		game.frame.setResizable(false);
		game.frame.setTitle(GraphBase.title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
	}
}