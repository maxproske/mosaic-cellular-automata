package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameOfLife {

	public ArrayList<Cell> cells = new ArrayList<Cell>();
	private int width;
	private int height;
	
	public GameOfLife(BufferedImage src)
	{
		ArrayList<Color> c = new ArrayList<Color>();
		width = src.getWidth();
		height = src.getHeight();
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				int rgb = src.getRGB(i, j);
				c.add(new Color(rgb, true));
			}
		}
		readList(c);
	}
	
	public void readList(ArrayList<Color> c){
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				Color color = c.get(i*height + j);
				boolean alive = (color.getAlpha() == 0) ? false : true;
				cells.add(new Cell(i,j, new Color(color.getRed(),color.getGreen(),color.getBlue(),255), alive));
			}
		}
		
	}
	
	public void drawCells(Graphics2D g, int x, int y, double s, int origX, int origY, int width, int height){
		AffineTransform tx = g.getTransform();
		width *= 6;
		height *= 6;
		origY *= 6;
		origX *= 6;
		g.translate(x - origX*s, y - origY*s);
		g.scale(s, s);
		for(int i=origX;i<origX + width;i++){
			for(int j=origY;j<origY + height;j++){
				cells.get(i*this.height + j).drawMe(g);
			}
		}
		g.setTransform(tx);
	}
	
	public void tick(){
		for(int i=0;i<width-1;i++){
			for(int j=0;j<height-1;j++){
				int home = i*height + j;
				Cell c = cells.get(home);
				c.resetNeightbors();
				if(j > 0 && i > 0) if(cells.get(home - height - 1).wasAlive()) c.incrementNeighbors();
				if(j > 0) if(cells.get(home - 1).wasAlive()) c.incrementNeighbors();
				if(j > 0 && i < width) if(cells.get(home + height - 1).isAlive()) c.incrementNeighbors();
				if(i > 0) if(cells.get(home - height).wasAlive()) c.incrementNeighbors();
				if(i < width) if(cells.get(home + height).isAlive()) c.incrementNeighbors();
				if(j < height && i > 0) if(cells.get(home - height + 1).wasAlive()) c.incrementNeighbors();
				if(j < height) if(cells.get(home + 1).isAlive()) c.incrementNeighbors();
				if(j < height && i < width) if(cells.get(home + height + 1).isAlive()) c.incrementNeighbors();
				c.checkLife();
			}
		}
	}
	
}
