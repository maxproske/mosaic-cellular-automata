package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import objects.GameOfLife;
import utilities.Filter;
import utilities.Pattern;

@SuppressWarnings("serial")
public class MosaicPanel extends JPanel {
	
		// Define instance variables
		private static final int PANEL_W = 860;
		private static final int PANEL_H = 780;
		private BufferedImage lennaImage;
		private BufferedImage filteredImage;
		private BufferedImage preparedImage;
		private GameOfLife gol;
		
		// Constructor
		public MosaicPanel()
		{	
			// Call the creation method from JPanel
			super();
			
			// Set preferred size/color/visibility of the component
			this.setPreferredSize(new Dimension(PANEL_W, PANEL_H));
			this.setBackground(Color.BLACK);
			this.setVisible(true);
			
			// Import image
			try { 
				lennaImage = ImageIO.read(new File("./assets/lenna.png"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// Apply dither filter
			filteredImage = Filter.dither(lennaImage);

			// Prepare filtered image
			preparedImage = prepareImage(filteredImage);
			
			// Execute game of life
			gol = new GameOfLife(preparedImage);
		}	
		
		// Callback method
		public void paintComponent(Graphics g)
		{
			// Call the paint method
			super.paintComponent(g);
			
			// Upgrade the graphics tool
			Graphics2D g2 = (Graphics2D)g;

			// Draw background
			g2.setColor(new Color(0,0,0));
			g2.fill(new Rectangle2D.Double(0, 0, PANEL_W, PANEL_H));
			
			// Original image preview
			g2.setColor(new Color(255,255,255));
			g2.drawString("lenna image", 20, 25);
			g2.drawImage(lennaImage,20,30,this);
			
			// Filtered image preview
			g2.drawString("filtered image", 20, 30 + lennaImage.getHeight() + 25);
		    g2.drawImage(filteredImage,20, 30 + lennaImage.getHeight() + 30,this);
		    
		    // Scaled down game of life
			g2.drawString("game of life (scaled)", 20 + lennaImage.getWidth() + 20, 25);
			gol.drawCells(g2,20 + lennaImage.getWidth() + 20,30,0.355);
			
			// Scaled up game of life
			g2.setColor(new Color(255,255,255));
			g2.drawString("close up of game of life", 20, 30*2 + lennaImage.getHeight()*2 + 25);
			gol.drawCells(g2, 20, 30*2 + lennaImage.getHeight()*2 + 35, 2);
			
			// Tick
			gol.tick();
			
		    // Call paintComponent method
			repaint();
		}
		
		public BufferedImage prepareImage(BufferedImage src)
		{	
			// Prepare image
			BufferedImage copy = new BufferedImage(src.getWidth()*6, src.getHeight()*6, BufferedImage.TYPE_INT_ARGB);
			int w = src.getWidth();
			int h = src.getHeight();
			
			for(int x=0; x<w; x++)
			{
				for(int y=0; y<h; y++)
				{
					// Prepare pattern
					int[][] pattern = Pattern.getRandomOscillator();
					int cols = pattern[0].length;
					int rows = pattern.length;

					for(int i=0; i<cols; i++)
					{
						// Get x-position
						int xPos = x*cols+i;
					
						for(int j=0; j<rows; j++) 
						{
							// Get y-position
							int yPos = y*rows+j;
							
							// Calculate alpha value
							int rgb = (pattern[j][i] == 1) ? src.getRGB(x,y) : src.getRGB(x,y) & 0xffffff;
							
							// Set pixel value
							copy.setRGB(xPos, yPos, rgb);
						}
					}
				}
			}	
			return copy;
		}
}