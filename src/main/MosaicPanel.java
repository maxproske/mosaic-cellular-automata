package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
public class MosaicPanel extends JPanel implements MouseListener, MouseMotionListener{
	
		// Define instance variables
		public static final int PANEL_W = 1000;
		public static final int PANEL_H = 840;
		public static final int ATOMIC_UNIT = 6;
		private int view_x;
		private int view_y;
		private int view_h = 30;
		private int view_w = 120;
		private double golScale = 0.345;
		private boolean moving = false;
		private int view_x_offset;
		private int view_y_offset;
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
			
			addMouseListener(this);
			addMouseMotionListener(this);
			// Prepare image
			prepareImage();
			
			// Execute game of life
			gol = new GameOfLife(preparedImage);
			
			view_x = (int) (20 + lennaImage.getWidth() + 20 );
			view_y = (int) (30);
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

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			
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
			g2.drawString("scaled down game of life", 20 + lennaImage.getWidth() + 20,30-5);
			gol.drawCells(g2,20 + lennaImage.getWidth() + 20,30,golScale,0,0,preparedImage.getWidth(),preparedImage.getHeight());
			
			// Scaled up game of life
			g2.setColor(new Color(255,255,255));
			g2.drawString("game of life simulation", 20 + lennaImage.getWidth() + 20, (int)(30 + preparedImage.getHeight()*golScale + 25));
			gol.drawCells(g2, 20 + lennaImage.getWidth() + 20, (int)(30 + preparedImage.getHeight()*golScale + 30), 2, (int)((view_x - 20 - lennaImage.getWidth() - 20)/golScale), (int)((view_y - 30)/golScale),(int)(view_w/golScale), (int)(view_h/golScale));
			
			//the thing
			g2.setColor(new Color(255,255,255));
			g2.drawRect(view_x, view_y, view_w, view_h);
			
			// Tick
			gol.tick();
			
		    // Call paintComponent method
			repaint();
			
		}

		@Override public void mouseClicked(MouseEvent e) { }

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			if(e.getX() > view_x && e.getX() < view_x + view_w && e.getY() > view_y && e.getY() < view_y + view_h){
				view_x_offset = e.getX() - view_x;
				view_y_offset = e.getY() - view_y;
				moving = true;
			}
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			moving = false;
			view_x_offset = 0;
			view_y_offset = 0;
		}

		@Override public void mouseEntered(MouseEvent e) { }

		@Override public void mouseExited(MouseEvent e) { }

		@Override public void mouseMoved(MouseEvent e) { }

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			if(moving){
				view_x = (int) ((!(e.getX() - view_x_offset > 20 + lennaImage.getWidth() + 20)) ?  20 + lennaImage.getWidth() + 20 : (!(e.getX() - view_x_offset < 20 + lennaImage.getWidth() + 20 + preparedImage.getWidth()*golScale - view_w)) ? 20 + lennaImage.getWidth() + 20 + preparedImage.getWidth()*golScale - view_w : e.getX() - view_x_offset);
				view_y = (int) ((!(e.getY() - view_y_offset > 30)) ? 30 : (!(e.getY() - view_y_offset < 30 + preparedImage.getHeight()*golScale - view_h)) ? 30 + preparedImage.getHeight()*golScale - view_h : e.getY() - view_y_offset);
			}
		}

}