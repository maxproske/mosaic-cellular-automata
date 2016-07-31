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
		public static final int PANEL_W = 860;
		public static final int PANEL_H = 780;
		public static final int ATOMIC_UNIT = 6;
		private int imgWidth;
		private int imgHeight;
		private BufferedImage lennaImage;
		private BufferedImage ditheredImage;
		private BufferedImage preparedImage;
		private BufferedImage populationImage;
		private BufferedImage similarImage;
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
			
			// Similar matte
			similarImage = Filter.similarMatte(populationImage);
			
			// Apply alpha matte
			preparedImage = Filter.matteAlpha(ditheredImage,populationImage);
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
			g2.drawString("1. population matte", 20, 25);
			g2.drawImage(populationImage,20,30,this);
			
			// Filtered image preview
			g2.drawString("2. similar matte", 20, 30 + imgHeight + 25);
		    g2.drawImage(similarImage,20, 30 + imgHeight + 30,this);
		    
		    // Scaled down game of life
			g2.drawString("merge matte (scaled)", 20 + lennaImage.getWidth() + 20, 25);
			//gol.drawCells(g2,20 + imgWidth + 20,30,0.355,0,0,imgWidth,imgHeight);
			
			
			AffineTransform tx = g2.getTransform();
			g2.scale(2, 2);
			g2.drawImage(similarImage,150, 16,this);
			g2.setTransform(tx);
			
			
			// Scaled up game of life
			g2.setColor(new Color(255,255,255));
			g2.drawString("close up of merge matte", 20, 30*2 + lennaImage.getHeight()*2 + 25);
			gol.drawCells(g2, 20, 30*2 + imgHeight*2 + 35, 2,0,0,70,20);
			
			// Tick
			gol.tick();
			
		    // Call paintComponent method
			repaint();
		}
}