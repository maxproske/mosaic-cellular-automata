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
	public static BufferedImage matteAlpha(BufferedImage src, BufferedImage matte)
	{	
		// Prepare image
		int atomicUnit = 6;
		int w = matte.getWidth();
		int h = matte.getHeight();
		BufferedImage copy = new BufferedImage(w*atomicUnit, h*atomicUnit, BufferedImage.TYPE_INT_ARGB);
		
		for(int x=0; x<w; x++)
		{
			for(int y=0; y<h; y++)
			{
				// Prepare pattern
				int[][] pattern = new int[atomicUnit][atomicUnit];
				int rgb = matte.getRGB(x, y);
				switch(rgb) {
					case 0xff1c1c1c: pattern = Pattern.getOscillator(Pattern.Oscillator_1x1.toad,0); break;
					case 0xff555555: pattern = Pattern.getOscillator(Pattern.Oscillator_1x1.toad,6); break;
					case 0xff8d8d8d: pattern = Pattern.getOscillator(Pattern.Oscillator_1x1.beacon,0); break;
					case 0xffc6c6c6: pattern = Pattern.getOscillator(Pattern.Oscillator_1x1.beacon,6); break;
					case 0xffffffff: pattern = Pattern.getOscillator(Pattern.Oscillator_1x1.clock,0); break;
					default: pattern = Pattern.getOscillator(Pattern.Oscillator_1x1.toad,0); break;
				}
				int cols = pattern[0].length;
				int rows = pattern.length;

				for(int i=0; i<cols-1; i++)
				{
					// Get x-position
					int xPos = x*cols+i;
				
					for(int j=0; j<rows-1; j++) 
					{
						// Get y-position
						int yPos = y*rows+j;
						
						// Calculate alpha value
						int new_rgb = (pattern[j][i] == 1) ? src.getRGB(x,y) : src.getRGB(x,y) & 0xffffff;
						
						// Set pixel value
						copy.setRGB(xPos, yPos, new_rgb);
					}
				}
			}
		}	
		return copy;
	}
	
	// Apply an merge matte to a single image
	public static BufferedImage mergeMatte(BufferedImage src)
	{
		// Prepare image
		int imgWidth = src.getWidth();
		int imgHeight = src.getHeight();
		BufferedImage copy = new BufferedImage(imgWidth, imgHeight, src.getType());

		for (int x = 0; x < imgWidth; x++) 
		{
			for (int y = 0; y < imgHeight; y++) 
			{
				// Initialize pixel variables
				int rgb = src.getRGB(x,y);
				int neighbors = 0;
				
				// If the pixel is alive
				if(rgb > 0xff000000) 
				{			
					// Increment neighbor count if neighbors in 3x3 area are alive
					for (int i=0; i<3; i++)
					{
						// Get x-position
						int xPos = x+i-1;
						
						for (int j=0; j<3; j++)
						{
							// Get y-position
							int yPos = y+j-1;
							
							// If the value is in bounds
							if (xPos >= 0 && yPos >= 0 && xPos < imgWidth && yPos < imgHeight) 
							{
								// And if the value is not itself
								if (xPos != x && yPos != y) 
								{
									// And if the value is not black
									if (src.getRGB(xPos, yPos) != 0xff000000) {
										neighbors++;
									}
								}
							}
						}
					}
					// Assign rgb values based on neighbor count
					switch(neighbors)
					{
						case 0: rgb = 0xff1c1c1c; break;
						case 1: rgb = 0xff555555; break;
						case 2: rgb = 0xff8d8d8d; break;
						case 3: rgb = 0xffc6c6c6; break;
						case 4: rgb = 0xffffffff; break;
						default:rgb = 0xffff0000; break; // can't have more than 4 neighbors
					}
				} else {
					rgb = 0xff000000; 
				}
				// Set pixel value
				copy.setRGB(x, y, new Color(rgb).getRGB());
			}
		}
		return copy; 
	}
}