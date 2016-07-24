package utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
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
}