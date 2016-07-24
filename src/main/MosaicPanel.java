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

@SuppressWarnings("serial")
public class MosaicPanel extends JPanel implements ActionListener {
	
		// Define instance variables
		private static final int PANEL_W = 860;
		private static final int PANEL_H = 780;
		private Timer timer;
		private BufferedImage lennaImage;
		private BufferedImage filteredImage;
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
			
			// Apply filter
			filteredImage = Filter.dither(lennaImage);
			
			// Start timer (ticks every 10 microseconds)
			timer = new Timer(10, this);
			timer.start();
			
			// Prepare filtered image
			BufferedImage prepped = prepImage(filteredImage);
			gol = startGOL(prepped);
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
			gol.drawCells(g2,20 + lennaImage.getWidth() + 20,30,.355);
			
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
					c.add(new Color(rgb, true));
				}
			}
			gol2.readList(c);
			return gol2;
		}
		
		//Prep the image
		public BufferedImage prepImage(BufferedImage img){
			
			BufferedImage copy = new BufferedImage(img.getWidth()*6, img.getHeight()*6, BufferedImage.TYPE_INT_ARGB);
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
							int x = i*6 + k;
							int y = j*6 + l;
							switch(patternTick){
								case 0:
									if((l==2 && (k == 2 || k == 3 || k == 4)) || (l == 3 && (k == 1 || k == 2 || k == 3))) copy.setRGB(x, y, rgb);
									else copy.setRGB(x,y, new Color(Util.getRed(rgb),Util.getGreen(rgb),Util.getBlue(rgb),0).getRGB());
									break;
								case 1:
									if((k == 1 && (l == 2 || l == 3)) || (k == 2 && l == 4) || (k == 3 && l == 1) || (k == 4 && (l == 2 || l == 3))) copy.setRGB(x, y, rgb);
									else copy.setRGB(x,y, new Color(Util.getRed(rgb),Util.getGreen(rgb),Util.getBlue(rgb),0).getRGB());
									break;
								case 2:
									if((k == 1 && (l == 1 || l == 2)) || (k == 2 && l == 1) || (k == 3 && l == 4) || (k == 4 && (l == 3 || l == 4))) copy.setRGB(x, y, rgb);
									else copy.setRGB(x,y, new Color(Util.getRed(rgb),Util.getGreen(rgb),Util.getBlue(rgb),0).getRGB());
									break;
								case 3:
									if((k == 1 && (l == 1 || l == 2)) || (k == 2 && (l == 1 || l == 2)) || (k == 3 && (l == 3 || l == 4)) || (k == 4 && (l == 3 || l == 4))) copy.setRGB(x, y, rgb);
									else copy.setRGB(x,y, new Color(Util.getRed(rgb),Util.getGreen(rgb),Util.getBlue(rgb),0).getRGB());
									break;
							}
							if(k==0 || k==6 || l==0 || l==6) copy.setRGB(x,y, new Color(Util.getRed(rgb),Util.getGreen(rgb),Util.getBlue(rgb),0).getRGB());
						}
					}
				}
			}
			
			return copy;
		}

}