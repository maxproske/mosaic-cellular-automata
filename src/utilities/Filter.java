package utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import utilities.Util;

public class Filter {
	
	// Apply an ordered dither to a single image
	public static BufferedImage dither(BufferedImage src)
	{
		// Create a copy of the image
		BufferedImage copy = new BufferedImage(src.getWidth(),src.getHeight(), src.getType());
		
		// Bayer matrix
		int[] dithers = new int[]{ 1, 33, 9, 41, 3,  35, 11, 43, 49, 17, 57, 25, 51, 19, 59, 27, 13, 45, 5, 37, 15, 47, 7, 39, 61, 29, 53, 21, 63, 31, 55, 23, 4, 36, 12, 44, 2, 34, 10, 42, 52, 20, 60, 28, 50, 18, 58, 26, 16, 48, 8, 40, 14, 46, 6, 38, 64, 32, 56, 24, 62, 30, 54, 22 };		
		int threshold = 16; // Controls the white and black points in the image	
		int tolerance = 2; // Controls the sensitivity of the filter
		
		for (int y = 0; y < src.getHeight(); y++) 
		{
			for (int x = 0; x < src.getWidth(); x++) 
			{
				int rgb = src.getRGB(x, y);
				int dither = dithers[(x & 7) + ((y & 7) << 3)]; 
				
				// Set all pixel values above the tolerance threshold to 0
				if ((Util.getRed(rgb)	+ dither * 256 / threshold) > 0xff * tolerance || 
					(Util.getGreen(rgb)	+ dither * 256 / threshold) > 0xff * tolerance || 
					(Util.getBlue(rgb)	+ dither * 256 / threshold) > 0xff * tolerance)
					rgb = 0;
				
				copy.setRGB(x, y, new Color(rgb).getRGB());
			}
		}
		return copy; 
	}
	
	// Apply an alpha matte to a single image
	public static BufferedImage matteAlpha(BufferedImage src)
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
				int[][] pattern = Pattern.getRandomOscillator(1,1);
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
	
	// Apply an merge matte to a single image
	public static BufferedImage mergeMatte(BufferedImage src)
	{
		// Create a copy of the image
		BufferedImage copy = new BufferedImage(src.getWidth(),src.getHeight(), src.getType());
		
		for (int y = 1; y < src.getHeight()-1; y++) 
		{
			for (int x = 1; x < src.getWidth()-1; x++) 
			{
				
				int rgb = src.getRGB(x,y);
				int neighbors = -1;
				
				// If the pixel is alive
				if(rgb > 0xff000000) {
				
					// Set to white
					rgb = 0xffffffff;				
					
					// Increment counter if neighbors are alive
					for (int i=0; i<3; i++){
						for (int j=0; j<3; j++){
							neighbors += (src.getRGB(x+i-1, y+j-1) > 0xff000000) ? 1 : 0;
						}
					}
					

					switch(neighbors){
						case -1:
							rgb = 0xff1c1c1c; 
							break;
						case 0: 
							rgb = 0xff1c1c1c; 
							break;
						case 1: 
							rgb = 0xff555555; 
							break;
						case 2: 
							rgb = 0xff8d8d8d; 
							break;
						case 3: 
							rgb = 0xffc6c6c6; 
							break;
						case 4:
							rgb = 0xffffffff; 
							break;
						// below cases won't happen
						default: 
							rgb = 0xff000000; 
							break;
					}
				} 
				// Else set to black
				else {
					rgb = 0xff000000;
				}

				

				
				/*
				if (rgb2 > 0xff000000 || rgb3 > 0xff000000 || rgb4 > 0xff000000) {
					rgb = 0xffff0000;
				}
				if (rgb2 > 0xff000000 && rgb3 > 0xff000000 || rgb3 > 0xff000000 && rgb4 > 0xff000000 || rgb2 > 0xff000000 && rgb4 > 0xff000000) {
					rgb = 0xff00ff00;
				}
				if (rgb2 > 0xff000000 && rgb3 > 0xff000000 && rgb4 > 0xff000000) {
					rgb = 0xff0000ff;
				}
				*/

				
				copy.setRGB(x, y, new Color(rgb).getRGB());
			}
		}
		return copy; 
	}
}