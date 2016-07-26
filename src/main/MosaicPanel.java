package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import utilities.Filter;
import utilities.Util;
import objects.Cell;
import objects.GameOfLife;
import utilities.Pattern;

@SuppressWarnings("serial")
public class MosaicPanel extends JPanel implements ActionListener {
	
		// Define instance variables
		private static final int PANEL_W = 860;
		private static final int PANEL_H = 780;
		private Timer timer;
		private BufferedImage lennaImage;
		private BufferedImage filteredImage;
		private BufferedImage preparedImage;
		private GameOfLife gol;
		private int tick;
		
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
			
			// Start timer (ticks every 10 microseconds)
			timer = new Timer(10, this);
			timer.start();
			
			// Apply dither filter
			filteredImage = Filter.dither(lennaImage);

			// Prepare filtered image
			preparedImage = prepImage(filteredImage);
			gol = startGOL(preparedImage);
			tick = 5;
		}	
		
		// Callback method
		public void paintComponent(Graphics g)
		{
			// Call the paint method
			super.paintComponent(g);
			
			// Upgrade the graphics tool
			Graphics2D g2 = (Graphics2D)g;

//			if(tick == 0){
//				gol.tick();
//				tick = 5;
//			}else tick--;
			// Draw background
			g2.setColor(new Color(0,0,0));
			g2.fill(new Rectangle2D.Double(0, 0, PANEL_W, PANEL_H));

			gol.tick();
			gol.drawCells(g2, -100, 50, 2);
			g2.fillRect(0, 0, PANEL_W, 30 + lennaImage.getHeight() + 30 + filteredImage.getHeight() + 30);
			g2.fillRect(0, 0, 20, PANEL_H);
			g2.fillRect(PANEL_W - 20, 0, 20, PANEL_H);
			g2.fillRect(0, PANEL_H-20, PANEL_W, 20);
			
			// Draw image 1
			g2.setColor(new Color(255,255,255));
			g2.drawString("lenna image", 20, 25);
			g2.drawImage(lennaImage,20,30,this);
			
			// Draw image 2
			g2.drawString("filtered image", 20, 30 + lennaImage.getHeight() + 25);
		    g2.drawImage(filteredImage,20, 30 + lennaImage.getHeight() + 30,this);
		    
		    //Draw GOL
			g2.drawString("game of life (scaled)", 20 + lennaImage.getWidth() + 20, 25);
			g2.drawString("close up of game of life", 20, 30 + lennaImage.getHeight() + 30 + filteredImage.getHeight() + 25);
			gol.drawCells(g2,20 + lennaImage.getWidth() + 20,30,1);
			
		    // Repaint
			repaint();
		}
		
		// Callback method
		public void actionPerformed(ActionEvent e) {
			
			// Code here...
			
		}
		
		// Dump the image into GOL
		public GameOfLife startGOL(BufferedImage img){
			
			ArrayList<Color> c = new ArrayList<Color>();
			int w = img.getWidth();
			int h = img.getHeight();
			GameOfLife gol2 = new GameOfLife(w,h);
			
			for(int i=0;i<w;i++){
				for(int j=0;j<h;j++){
					int rgb = img.getRGB(i, j);
					c.add(new Color(rgb, true));
				}
			}
			gol2.readList(c);
			return gol2;
		}
		
		// Prepare the image
		public BufferedImage prepImage(BufferedImage src){
			
			BufferedImage copy = new BufferedImage(src.getWidth()*5, src.getHeight()*5, BufferedImage.TYPE_INT_ARGB);
			int w = src.getWidth();
			int h = src.getHeight();	
			
			for(int x=0; x<w; x++)
			{
				
				for(int y=0; y<h; y++)
				{
					
					// Get rgb value
					int rgb = src.getRGB(x, y);
					
					// Initialize pattern array
					int[][] pattern = Pattern.getOscillator(Pattern.Oscillator.blinker,0);
					
					for(int i=0; i<5; i++) 
					{
						for(int j=0; j<5; j++) 
						{
							int xPos = x*5 + i;
							int yPos = y*5 + j;
							
							if(pattern[i][j] == 1) {
								copy.setRGB(xPos, yPos, new Color(255,255,255,255).getRGB());
								if (x < 5 && y < 5) System.out.println("white at [" + (xPos) + "," + (yPos) + "]");
							} else {
								copy.setRGB(xPos, yPos, new Color(0,0,0,0).getRGB());
								if (x < 5 && y < 5) System.out.println("black at [" + (xPos) + "," + (yPos) + "]");
							}
						}
					}
				}
				
			}	
			return copy;
		}
}