package utilities;

public class Util {
	
	// Return red pixel value
	public static int getRed(int p)
	{
		return (p >>> 16) & 0xFF;
	}

	// Return green pixel value
	public static int getGreen(int p)
	{
		return (p >>> 8) & 0xFF;
	}

	// Return blue pixel value
	public static int getBlue(int p)
	{
		return p & 0xFF;
	}
}
