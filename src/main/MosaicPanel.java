package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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
		public static final int PANEL_W = 1000;
		public static final int PANEL_H = 840;
		public static final int ATOMIC_UNIT = 6;
		private int imgWidth;
		private int imgHeight;
		private BufferedImage lennaImage;
		private BufferedImage ditheredImage;
		private BufferedImage preparedImage;
		private BufferedImage populationImage;
		private BufferedImage similarImage;
		private BufferedImage oscillatorImage;
		private BufferedImage nearestImage;
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
			
			// Prepare image
			prepareImage();
			
			// Execute game of life
			gol = new GameOfLife(preparedImage);
		}	
		
		// Prepare image for Game of Life simulation
		private void prepareImage()
		{
			// Import image
			try { 
				lennaImage = ImageIO.read(new File("./assets/lenna.png"));
				imgWidth = lennaImage.getWidth();
				imgHeight = lennaImage.getHeight();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// Apply dither filter
			ditheredImage = Filter.dither(lennaImage);
	
			// Population matte
			populationImage = Filter.populationMatte(ditheredImage);
			
			// nearest neighbor matte
			nearestImage = Filter.similarMatte(populationImage,true);
			
			// Similar matte
			similarImage = Filter.similarMatte(populationImage,false);
			
			// Apply alpha matte
			preparedImage = Filter.matteAlpha(lennaImage,similarImage);
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
			
			// Dithered filter preview
			g2.setColor(new Color(255,255,255));
			g2.drawString("step 1. dither filter", 10, 15);
			g2.drawImage(ditheredImage,10,20,this);
			
			// Population matte preview
			g2.drawString("step 2. population matte", 10, 15+imgHeight+20);
		    g2.drawImage(populationImage,10,20+imgHeight+20,this);
		    
		    // Similar neighbor matte preview
			g2.drawString("step 3. similar neighbor matte", 10, 15+imgHeight*2+20*2);
			g2.drawImage(nearestImage,10,20+imgHeight*2+20*2,this);
			
			// Scaled down game of life
			g2.setColor(new Color(255,255,255));
			g2.drawString("scaled down (0.355x) game of life", 20 + lennaImage.getWidth() + 20,30-5);
			gol.drawCells(g2,20 + lennaImage.getWidth() + 20,30,0.355,0,0,lennaImage.getWidth(),lennaImage.getHeight());
			
			// Scaled up game of life
			g2.setColor(new Color(255,255,255));
			g2.drawString("scaled up (2x) game of life", 20 + lennaImage.getWidth() + 20, 30*2 + lennaImage.getHeight()*2 + 35-5);
			gol.drawCells(g2, 20 + lennaImage.getWidth() + 20, 30*2 + lennaImage.getHeight()*2 + 35, 2,lennaImage.getWidth()/2,lennaImage.getHeight()/2,70,20);
			
			// Tick
			gol.tick();
			
		    // Call paintComponent method
			repaint();
		}
}