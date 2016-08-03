package utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.stream.Stream;

import utilities.Util;

public class Filter {
	
	// Apply an ordered dither to a single image
	public static BufferedImage dither(BufferedImage src)
	{
		// Create a copy of the image
		int imgWidth = src.getWidth();
		int imgHeight = src.getHeight();
		BufferedImage copy = new BufferedImage(imgWidth,imgHeight, src.getType());
		
		// Bayer matrix
		int[] dithers = new int[]{ 1, 33, 9, 41, 3,  35, 11, 43, 49, 17, 57, 25, 51, 19, 59, 27, 13, 45, 5, 37, 15, 47, 7, 39, 61, 29, 53, 21, 63, 31, 55, 23, 4, 36, 12, 44, 2, 34, 10, 42, 52, 20, 60, 28, 50, 18, 58, 26, 16, 48, 8, 40, 14, 46, 6, 38, 64, 32, 56, 24, 62, 30, 54, 22 };		
		int threshold = 16; // Controls the white and black points in the image	
		int tolerance = 2; // Controls the sensitivity of the filter
		
		for (int x = 0; x < imgWidth; x++) 
		{
			for (int y = 0; y < imgHeight; y++) 
			{
				int rgb = src.getRGB(x, y);
				int dither = dithers[(x & 7) + ((y & 7) << 3)]; 
				
				// Set all pixel values above the tolerance threshold to 0
				int value = (
						(Util.getRed(rgb)	+ dither * 256 / threshold) > 0xff * tolerance || 
						(Util.getGreen(rgb)	+ dither * 256 / threshold) > 0xff * tolerance || 
						(Util.getBlue(rgb)	+ dither * 256 / threshold) > 0xff * tolerance) ? 0 : rgb;
				
				copy.setRGB(x, y, new Color(value).getRGB());
			}
		}
		return copy; 
	}
	
	// Apply an alpha matte to a single image
	public static BufferedImage matteAlpha(BufferedImage src, BufferedImage matte)
	{	
		// Prepare image
		int atomicUnit = 6;
		int w = src.getWidth();
		int h = src.getHeight();
		BufferedImage copy = new BufferedImage(w*atomicUnit, h*atomicUnit, BufferedImage.TYPE_INT_ARGB);
		
		for(int x=0; x<w; x++)
		{
			for(int y=0; y<h; y++)
			{
				// Prepare pattern
				int[][] pattern = new int[0][0];
				int rgb = matte.getRGB(x, y);
				
				switch(rgb) {
					case 0xff00ffff: // cyan
						pattern = Pattern.getOscillator(Pattern.Oscillator_2x2.figure_eight,0); 
						break; 
					case 0xff0000ff: // blue
						pattern = Pattern.getOscillator(Pattern.Oscillator_2x2.a_for_all,0); 
						break; 
					case 0xff00ff00: // green
						pattern = Pattern.getOscillator(Pattern.Oscillator_2x3.pentadecathlon,0); 
						break; 
					case 0xffff0000: // red
						pattern = Pattern.getOscillator(Pattern.Oscillator_2x3.coes_p8,0); 
						break; 
					case 0xffffff00: // yellow
						pattern = Pattern.getOscillator(Pattern.Oscillator_2x4.queen_bee_shuttle,1);
						break; 
					case 0xffff00ff: // purple
						pattern = Pattern.getOscillator(Pattern.Oscillator_2x4.caterer_on_figure_eight,1); 
						break; 
					case 0xffffffff: // white
						pattern = Pattern.getRandomOscillator(1,1); 
						break; 
					default:
						break;
				}
				
				if (pattern.length > 0) 
				{
					int rows = pattern.length;
					int cols = pattern[0].length;
		
					// trim one column for denser patterns
					for(int i = 0; i < cols-1; i++) 
					{
						// Get x-position
						int xPos = x*atomicUnit+i;
					
						// trim one row for denser patterns
						for(int j=0; j < rows-1; j++) 
						{
							// Get y-position
							int yPos = y*atomicUnit+j;
	
							// If the cell is in bounds
							if (xPos < w*atomicUnit && yPos < h*atomicUnit) 
							{
								// Calculate alpha value
								//int new_rgb = (pattern[j][i] == 1) ? matte.getRGB(x,y) : matte.getRGB(x,y) & 0xffffff;
								int value = (pattern[j][i] == 1) ? src.getRGB(x,y) : src.getRGB(x,y) & 0xffffff;
									
								// set pixel value
								copy.setRGB(xPos, yPos, value);
							}
						}
					}
				}
			}	
		}	
		return copy;
	}
	
	// Apply an merge matte to a single image
	public static BufferedImage populationMatte(BufferedImage src)
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
				} else {
					neighbors = -1;
				}
				
				// Assign rgb values based on neighbor count (can't have more than 4 neighbors)
				int value = 0;
				switch(neighbors)
				{
					case 0: value = 0xff1c1c1c; break;
					case 1: value = 0xff555555; break;
					case 2: value = 0xff8d8d8d; break;
					case 3: value = 0xffc6c6c6; break;
					case 4: value = 0xffffffff; break;
					default: value = 0xff000000; break;
				}
				
				// Set pixel value
				copy.setRGB(x, y, new Color(value).getRGB());
			}
		}
		return copy; 
	}
	
	// Apply an merge matte to a single image
	public static BufferedImage similarMatte(BufferedImage src, boolean debug)
	{
		// Prepare image
		int imgWidth = src.getWidth();
		int imgHeight = src.getHeight();
		BufferedImage copy = new BufferedImage(imgWidth, imgHeight, src.getType());
		BufferedImage result = new BufferedImage(imgWidth, imgHeight, src.getType());

		for (int x = 0; x < imgWidth; x++) 
		{
			for (int y = 0; y < imgHeight; y++) 
			{
				// Initialize pixel variables
				int rgb = src.getRGB(x,y);
				int[][] rgbArr = new int[4][4];
				
				// Create array of neighbors (1 to the right, 1 to the bottom, 1 to the bottom right)
				for (int i=0; i<4; i++)
				{
					// Get x-position
					int xPos = x+i;
					
					for (int j=0; j<4; j++)
					{
						// Get y-position
						int yPos = y+j;
						
						// If the value is in bounds
						if (xPos < imgWidth && yPos < imgHeight) 
						{
							rgbArr[i][j] = src.getRGB(xPos,yPos);
						}
					}
				}
				
				// Assign rgb values based on neighbor count
				int max_i = 0;
				int max_j = 0;
				
				// Determine the pattern
				if (rgbArr[0][0] == 0xffc6c6c6 && rgbArr[0][1] == 0xff000000 && 
					rgbArr[1][0] == 0xff000000 && rgbArr[1][1] == 0xffffffff) {
						rgb = 0xff00ffff; // cyan (2x2)
						max_i = 2;
						max_j = 2;
				} else if (
					rgbArr[0][0] == 0xffffffff && rgbArr[0][1] == 0xff000000 && 
					rgbArr[1][0] == 0xff000000 && rgbArr[1][1] == 0xffc6c6c6) {
						rgb = 0xff0000ff; // blue (2x2)
						max_i = 2;
						max_j = 2;
				} else if (
					rgbArr[0][0] == 0xff1c1c1c && rgbArr[0][1] == 0xff000000 && rgbArr[0][2] == 0xff1c1c1c && 
					rgbArr[1][0] == 0xff000000 && rgbArr[1][1] == 0xff000000 && rgbArr[1][2] == 0xff000000) {			
						rgb = 0xff00ff00; // green (3x2)
						max_i = 3;
						max_j = 2;
				} else if (
					rgbArr[0][0] == 0xff555555 && rgbArr[0][1] == 0xff000000 && rgbArr[0][2] == 0xff555555 && 
					rgbArr[1][0] == 0xff000000 && rgbArr[1][1] == 0xff000000 && rgbArr[1][2] == 0xff000000) {
						rgb = 0xffff0000; // red (3x2)
						max_i = 3;
						max_j = 2;
				} else if (
					rgbArr[0][0] == 0xff555555 && rgbArr[0][1] == 0xff000000 && 
					rgbArr[1][0] == 0xff000000 && rgbArr[1][1] == 0xffffffff && 
					rgbArr[2][0] == 0xff555555 && rgbArr[2][1] == 0xff000000 && 
					rgbArr[3][0] == 0xff000000 && rgbArr[3][1] == 0xff000000){
						rgb = 0xffffff00; // yellow (4x2)
						max_i = 2;
						max_j = 4;
				} else if (
					rgbArr[0][0] == 0xff8d8d8d && rgbArr[0][1] == 0xff000000 && 
					rgbArr[1][0] == 0xff000000 && rgbArr[1][1] == 0xffffffff && 
					rgbArr[2][0] == 0xff8d8d8d && rgbArr[2][1] == 0xff000000 && 
					rgbArr[3][0] == 0xff000000 && rgbArr[3][1] == 0xff000000){
						rgb = 0xffff00ff; // purple (4x2)
						max_i = 2;
						max_j = 4;
				} else if (rgb > 0xff000000){
					rgb = 0xff000000; // unassigned patterns
				} else {
					rgb = 0xff000000; // default to black
				}
				
				// zero-out all positions past the oscillator bounding box
				for (int i = max_i; i < 4; i++){
					for (int j = max_j; j < 4; j++){
						rgbArr[i][j] = 0;
					}
				}
				
				// Set flag if it's drawing over top of something
				int flag = 0;
				for (int i = 0; i < max_i; i++){
					for (int j = 0; j < max_j; j++){
						if (rgb != 0xff000000 && rgbArr[0][0] != 0) {
							if(copy.getRGB(x+j, y+i) != 0xff000000) {
								flag = 1;
							}
						}
					}
				}
				// If it's not drawing over top of something, paint
				if (flag != 1) {
					result.setRGB(x, y, new Color(rgb).getRGB()); // push color to final result
					for (int i = 0; i < max_i; i++){
						for (int j = 0; j < max_j; j++){
							copy.setRGB(x+j, y+i, new Color(rgb).getRGB());
						}
					}
				}
				// if the space is still empty, paint the dither pattern that would have been here
				if (copy.getRGB(x, y) == 0xff000000 && src.getRGB(x, y) != 0xff000000) {
					copy.setRGB(x, y, new Color(0xffffffff).getRGB());
					result.setRGB(x, y, new Color(0xffffffff).getRGB()); // push white pixels to final result
				}
			}
		}
		return debug ? copy : result; 
	}
}