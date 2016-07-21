import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class GameOfLife {

	public ArrayList<Cell> cells = new ArrayList<Cell>();
	private int width;
	private int height;
	
	public GameOfLife(int w, int h){
		
		width = w;
		height = h;
		
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
	
	public void tick(){
		preTick();
		for(int i=0;i<cells.size();i++){
			Cell c = cells.get(i);
			c.checkLife();
		}
	}
	
	public void drawCells(Graphics2D g, int x, int y, double s){
		AffineTransform tx = g.getTransform();
		g.translate(x, y);
		g.scale(s, s);
		for(int i=0;i<cells.size();i++){
			Cell c = cells.get(i);
			c.drawMe(g);
		}
		g.setTransform(tx);
	}
	
	private void preTick(){
		for(int i=0;i<width-1;i++){
			for(int j=0;j<height-1;j++){
				int home = i*height + j;
				Cell c = cells.get(home);
				c.resetNeightbors();
				if(j > 0 && i > 0) if(cells.get(home - height - 1).isAlive()) c.incrementNeighbors();
				if(j > 0) if(cells.get(home - 1).isAlive()) c.incrementNeighbors();
				if(j > 0 && i < width) if(cells.get(home + height - 1).isAlive()) c.incrementNeighbors();
				if(i > 0) if(cells.get(home - height).isAlive()) c.incrementNeighbors();
				if(i < width) if(cells.get(home + height).isAlive()) c.incrementNeighbors();
				if(j < height && i > 0) if(cells.get(home - height + 1).isAlive()) c.incrementNeighbors();
				if(j < height) if(cells.get(home + 1).isAlive()) c.incrementNeighbors();
				if(j < height && i < width) if(cells.get(home + height + 1).isAlive()) c.incrementNeighbors();
			}
		}
	}
	
}
