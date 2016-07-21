import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class MosaicPanel extends JPanel implements ActionListener {
	
		// Define instance variables
		private static final int PANEL_W = 600;
		private static final int PANEL_H = 600;
		private Timer timer;
		private BufferedImage lennaImage;
		private BufferedImage filteredImage;
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
			
			// Apply filter
			filteredImage = filterImage(lennaImage, Filters.orderedDither);
			
			// Start timer (ticks every 10 microseconds)
			timer = new Timer(10, this);
			timer.start();
			
			BufferedImage prepped = prepImage(filteredImage);
			gol = startGOL(prepped);
		}	
		
		// Callback method
		public void paintComponent(Graphics g)
		{
			// Call the paint method
			super.paintComponent(g);
			
			// Upgrade the graphics tool
			Graphics2D g2 = (Graphics2D)g;
			
			gol.drawCells(g2,0,0);
			// Draw background
			g2.setColor(new Color(0,0,0));
			g2.fill(new Rectangle2D.Double(0, 0, PANEL_W, PANEL_H));
			
			// Draw image 1
			g2.setColor(new Color(255,255,255));
			g2.drawString("lenna image", 20, 45);
			g2.drawImage(lennaImage,20,50,this);
			
			// Draw image 2
			g2.drawString("filtered image", 300, 45);
		    g2.drawImage(filteredImage,300,50,this);
			
		    // Repaint
			repaint();
		}
		
		// Callback method
		public void actionPerformed(ActionEvent e) {
			
			// Code here...
			
		}
		
		//Dump the image into GOL
		public GameOfLife startGOL(BufferedImage img){
			
			ArrayList<Color> c = new ArrayList<Color>();
			int w = img.getWidth();
			int h = img.getHeight();
			GameOfLife gol2 = new GameOfLife(w,h);
			
			for(int i=0;i<w;i++){
				for(int j=0;j<h;j++){
					int rgb = img.getRGB(i, j);
					c.add(new Color(rgb));
				}
			}
			gol2.readList(c);
			return gol2;
		}
		
		//Prep the image
		public BufferedImage prepImage(BufferedImage img){
			
			BufferedImage copy = new BufferedImage(img.getWidth()*6,img.getHeight()*6, img.getType());
			int w = img.getWidth();
			int h = img.getHeight();
			
			for(int i=0;i<w;i++){
				for(int j=0;j<h;j++){
					int rgb = img.getRGB(i, j);
					int patternTick = 0;
					if(Math.random() > .75){
						patternTick = 1;
					}else if(Math.random() > .75){
						patternTick = 2;
					}else if(Math.random() > .75){
						patternTick = 3;
					}
					for(int k=0;k<6;k++){
						for(int l=0;l<6;l++){
							int x = i + k;
							int y = j + l;
							switch(patternTick){
								case 0:
									if((l==2 && (k == 2 || k == 3 || k == 4)) || (l == 3 && (k == 1 || k == 2 || k == 3))) copy.setRGB(x, y, rgb);
									else copy.setRGB(x,y, new Color(0,0,0).getRGB());
									break;
								case 1:
									if((k == 1 && (l == 2 || l == 3)) || (k == 2 && l == 4) || (k == 3 && l == 1) || (k == 4 && (l == 2 || l == 3))) copy.setRGB(x, y, rgb);
									else copy.setRGB(x,y, new Color(0,0,0).getRGB());
									break;
								case 2:
									if((k == 1 && (l == 1 || l == 2)) || (k == 2 && l == 1) || (k == 3 && l == 4) || (k == 4 && (l == 3 || l == 4))) copy.setRGB(x, y, rgb);
									else copy.setRGB(x,y, new Color(0,0,0).getRGB());
									break;
								case 3:
									if((k == 1 && (l == 1 || l == 2)) || (k == 2 && (l == 1 || l == 2)) || (k == 3 && (l == 3 || l == 4)) || (k == 4 && (l == 3 || l == 4))) copy.setRGB(x, y, rgb);
									else copy.setRGB(x,y, new Color(0,0,0).getRGB());
									break;
							}
							if(k==0 || k==6 || l==0 || l==6) copy.setRGB(x,y, new Color(0,0,0).getRGB());
						}
					}
				}
			}
			
			return copy;
		}
		
		// Apply a filter to a single image
		public BufferedImage filterImage(BufferedImage src, Filters filt)
		{
			// Create a copy of the image
			BufferedImage copy = new BufferedImage(src.getWidth(),src.getHeight(), src.getType());
			
			// Bayer matrix
			int[] dithers = new int[]{ 1, 33, 9, 41, 3,  35, 11, 43, 49, 17, 57, 25, 51, 19, 59, 27, 13, 45, 5, 37, 15, 47, 7, 39, 61, 29, 53, 21, 63, 31, 55, 23, 4, 36, 12, 44, 2, 34, 10, 42, 52, 20, 60, 28, 50, 18, 58, 26, 16, 48, 8, 40, 14, 46, 6, 38, 64, 32, 56, 24, 62, 30, 54, 22 };		
			int threshold = 8; // Controls the white and black points in the image	
			int tolerance = 3; // Controls the sensitivity of the filter
			
			for (int y = 0; y < src.getHeight(); y++) 
			{
				for (int x = 0; x < src.getWidth(); x++) 
				{
					int rgb = src.getRGB(x, y);
					int dither = dithers[(x & 7) + ((y & 7) << 3)]; 
					
					// Set all pixel values above the tolerance threshold to 0
					if ((getRed(rgb)	+ dither * 256 / threshold) > 0xff * tolerance || 
						(getGreen(rgb)	+ dither * 256 / threshold) > 0xff * tolerance || 
						(getBlue(rgb)	+ dither * 256 / threshold) > 0xff * tolerance)
						rgb = 0;
					
					copy.setRGB(x, y, new Color(rgb).getRGB());
				}
			}
			return copy; 
		}

		// Clip bounds
		private int clip(int v) 
		{
			v = v > 255 ? 255 : v;
			v = v < 0 ? 0 : v;
			return v;
		}
		
		// Return red pixel value
		protected int getRed(int p)
		{
			return (p >>> 16) & 0xFF;
		}

		// Return green pixel value
		protected int getGreen(int p)
		{
			return (p >>> 8) & 0xFF;
		}

		// Return blue pixel value
		protected int getBlue(int p)
		{
			return p & 0xFF;
		}
}