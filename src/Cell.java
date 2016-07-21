import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Vector;

public class Cell {

	private int neighbors;
	private int xPos;
	private int yPos;
	private boolean alive;
	public Color color;
	
	public Cell(int x, int y, Color color, boolean alive){
		xPos = x;
		yPos = y;
		this.color = color;
		this.alive = alive;
		neighbors = 0;
	}
	
	public void checkLife(){
		
		alive = (alive) ? (neighbors > 3 || neighbors < 2) ? false : true : (neighbors == 3) ? true : false;
		
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public void resetNeightbors(){
		
		neighbors = 0;
		
	}
	
	public void incrementNeighbors(){
		
		neighbors++;
		
	}
	
	public void drawMe(Graphics2D g){
		
		AffineTransform tx = g.getTransform();
		g.translate(xPos, yPos);
		g.setColor((alive) ? color : Color.BLACK);
		g.fillRect(0, 0, 1, 1);
		g.setTransform(tx);
		
	}
	
}
