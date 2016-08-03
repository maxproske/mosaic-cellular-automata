package utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import utilities.Util;

public class Filter {
	
	// Apply an ordered dither to a single image
	public static BufferedImage dither(BufferedImage src)
	{
		// Function variables
		int[] dithers = new int[]{ 1, 33, 9, 41, 3,  35, 11, 43, 49, 17, 57, 25, 51, 19, 59, 27, 13, 45, 5, 37, 15, 47, 7, 39, 61, 29, 53, 21, 63, 31, 55, 23, 4, 36, 12, 44, 2, 34, 10, 42, 52, 20, 60, 28, 50, 18, 58, 26, 16, 48, 8, 40, 14, 46, 6, 38, 64, 32, 56, 24, 62, 30, 54, 22 }; // Bayer matrix		
		int threshold = 16; // Controls the white and black points in the image	
		int tolerance = 2; // Controls the sensitivity of the filter
		int imgWidth = src.getWidth();
		int imgHeight = src.getHeight();
		BufferedImage result = new BufferedImage(imgWidth,imgHeight, src.getType());
		
		for (int i = 0; i < imgWidth; i++) 
		{
			for (int j = 0; j < imgHeight; j++) 
			{
				int rgb = src.getRGB(i, j);
				int dither = dithers[(i & 7) + ((j & 7) << 3)]; 
				
				// Set all pixel values above the tolerance threshold to 0
				int value = ((Util.getRed(rgb)	+ dither * 256 / threshold) > 0xff * tolerance || 
							 (Util.getGreen(rgb)+ dither * 256 / threshold) > 0xff * tolerance || 
							 (Util.getBlue(rgb)	+ dither * 256 / threshold) > 0xff * tolerance) ? 0 : rgb;
				
				result.setRGB(i, j, new Color(value).getRGB());
			}
		}
		return result; 
	}
	
	// Apply an alpha matte to a single image
	public static BufferedImage matteAlpha(BufferedImage src, BufferedImage matte)
	{	
		// Function variables
		int atomicUnit = 6;
		int imgWidth = src.getWidth();
		int imgHeight = src.getHeight();
		BufferedImage result = new BufferedImage(imgWidth*atomicUnit, imgHeight*atomicUnit, BufferedImage.TYPE_INT_ARGB);
		
		for (int x = 0; x < imgWidth; x++)
		{
			for (int y = 0; y < imgHeight; y++)
			{
				int[][] pattern = new int[0][0];
				int rgb = matte.getRGB(x, y);
				
				// Choose an oscillator based on the matte pixel value
				switch(rgb) 
				{
					case 0xffffffff: // white (1x1)
						pattern = Pattern.getRandomOscillator(1,1); 
						break; 
					case 0xff00ffff: // cyan (2x2)
						pattern = Pattern.getRandomOscillator(2,2);
						break; 
					case 0xff0000ff: // blue (2x2)
						pattern = Pattern.getRandomOscillator(2,2);
						break; 
					case 0xff00ff00: // green (3x2)
						pattern = Pattern.getRandomOscillator(3,2);
						break; 
					case 0xffff0000: // red (3x2)
						pattern = Pattern.getRandomOscillator(3,2);
						break; 
					case 0xffffff00: // yellow (4x2)
						pattern = Pattern.getRandomOscillator(4,2);
						break; 
					case 0xffff00ff: // purple (4x2)
						pattern = Pattern.getRandomOscillator(4,2);
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
							if (xPos < imgWidth*atomicUnit && yPos < imgHeight*atomicUnit) 
							{
								// Calculate alpha value
								int value = (pattern[j][i] == 1) ? src.getRGB(x,y) : src.getRGB(x,y) & 0xffffff;
								
								// Debug mode
								//int value = (pattern[j][i] == 1) ? matte.getRGB(x,y) : matte.getRGB(x,y) & 0xffffff;
									
								// set pixel value
								result.setRGB(xPos, yPos, value);
							}
						}
					}
				}
			}	
		}	
		return result;
	}
	
	// Apply an merge matte to a single image
	public static BufferedImage populationMatte(BufferedImage src)
	{
		// Function variables
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
				
				// Assign pixel value based on neighbor count (can't have more than 4 neighbors)
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
		// Function variables
		int imgWidth = src.getWidth();
		int imgHeight = src.getHeight();
		BufferedImage copy = new BufferedImage(imgWidth, imgHeight, src.getType());
		BufferedImage result = new BufferedImage(imgWidth, imgHeight, src.getType());

		for (int x = 0; x < imgWidth; x++) 
		{
			for (int y = 0; y < imgHeight; y++) 
			{
				// Initialize pixel variables
				int max_i = 4; // max accepted oscillator width
				int max_j = 4; // max accepted oscillator height
				int bb_i = 0; // calculates number of oscillator columns
				int bb_j = 0; // calculates number of oscillator rows
				boolean overlapped = false; // are oscillators overlapping
				int rgb = src.getRGB(x,y);
				int[][] rgbArr = new int[max_i][max_j];
				
				// Create array of neighbors (1 to the right, 1 to the bottom, 1 to the bottom right)
				for (int i=0; i<max_i; i++)
				{
					// Get x-position
					int xPos = x+i;
					
					for (int j=0; j<max_j; j++)
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

				// Determine the pattern
				if (rgbArr[0][0] == 0xffc6c6c6 && rgbArr[0][1] == 0xff000000 && rgbArr[1][0] == 0xff000000 && rgbArr[1][1] == 0xffffffff){
					rgb = 0xff00ffff; // cyan (2x2)
					bb_i = 2;
					bb_j = 2;
				} else if (rgbArr[0][0] == 0xffffffff && rgbArr[0][1] == 0xff000000 && rgbArr[1][0] == 0xff000000 && rgbArr[1][1] == 0xffc6c6c6){
					rgb = 0xff0000ff; // blue (2x2)
					bb_i = 2;
					bb_j = 2;
				} else if (rgbArr[0][0] == 0xff1c1c1c && rgbArr[0][1] == 0xff000000 && rgbArr[0][2] == 0xff1c1c1c && rgbArr[1][0] == 0xff000000 && rgbArr[1][1] == 0xff000000 && rgbArr[1][2] == 0xff000000) {			
					rgb = 0xff00ff00; // green (3x2)
					bb_i = 3;
					bb_j = 2;
				} else if (rgbArr[0][0] == 0xff555555 && rgbArr[0][1] == 0xff000000 && rgbArr[0][2] == 0xff555555 && rgbArr[1][0] == 0xff000000 && rgbArr[1][1] == 0xff000000 && rgbArr[1][2] == 0xff000000) {
					rgb = 0xffff0000; // red (3x2)
					bb_i = 3;
					bb_j = 2;
				} else if (rgbArr[0][0] == 0xff555555 && rgbArr[0][1] == 0xff000000 && rgbArr[1][0] == 0xff000000 && rgbArr[1][1] == 0xffffffff && rgbArr[2][0] == 0xff555555 && rgbArr[2][1] == 0xff000000 && rgbArr[3][0] == 0xff000000 && rgbArr[3][1] == 0xff000000){
					rgb = 0xffffff00; // yellow (4x2)
					bb_i = 2;
					bb_j = 4;
				} else if (rgbArr[0][0] == 0xff8d8d8d && rgbArr[0][1] == 0xff000000 && rgbArr[1][0] == 0xff000000 && rgbArr[1][1] == 0xffffffff && rgbArr[2][0] == 0xff8d8d8d && rgbArr[2][1] == 0xff000000 && rgbArr[3][0] == 0xff000000 && rgbArr[3][1] == 0xff000000){
					rgb = 0xffff00ff; // purple (4x2)
					bb_i = 2;
					bb_j = 4;
				} else {
					rgb = 0xff000000; // default to black
				}

				// Set flag if it's drawing over top of something
				for (int i = 0; i < max_i; i++) 
				{
					for (int j = 0; j < max_j; j++) 
					{
						// If the position is within the oscillator bounding box
						if (i < bb_i && j < bb_j) 
						{
							if (rgb != 0xff000000 && rgbArr[0][0] != 0 && copy.getRGB(x+j, y+i) != 0xff000000) 
							{
								overlapped = true;
							}
						} else {
							rgbArr[i][j] = 0; // zero-out all positions past the oscillator bounding box
						}
					}
				}
				
				// If it's not drawing over top of something, push color to final result
				if (!overlapped) 
				{
					result.setRGB(x, y, new Color(rgb).getRGB());
					for (int i = 0; i < bb_i; i++)
					{
						for (int j = 0; j < bb_j; j++)
						{
							copy.setRGB(x+j, y+i, new Color(rgb).getRGB());
						}
					}
				}
				
				// if the space is still empty, paint the dither pattern that would have been here
				if (copy.getRGB(x, y) == 0xff000000 && src.getRGB(x, y) != 0xff000000) 
				{
					copy.setRGB(x, y, new Color(0xffffffff).getRGB());
					result.setRGB(x, y, new Color(0xffffffff).getRGB()); // push white pixels to final result
				}
			}
		}
		return debug ? copy : result; 
	}
}