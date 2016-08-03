package utilities;

import utilities.Util;

public class Pattern 
{
	public enum Oscillator_1x1 {
		beacon,
		clock,
		toad
	}
	public enum Oscillator_2x2 {
		figure_eight,
		a_for_all
	}
	public enum Oscillator_3x2 {
		pentadecathlon,
		coes_p8
	}
	public enum Oscillator_2x4 {
		caterer_on_figure_eight,
		queen_bee_shuttle
	}
	
	// Rotate 2D array
	public static int[][] rotateMatrixLeft(int[][] matrix, int rotations)
	{
		// width and height pre-swapped
	    int w = matrix.length;
	    int h = matrix[0].length;   
		int[][] ret = new int[h][w];

	    for (int i = 0; i < h; ++i) 
	    {
	        for (int j = 0; j < w; ++j) 
	        {
	            ret[i][j] = matrix[j][h - i - 1];
	        }
	    }
	    return (rotations > 1) ? rotateMatrixLeft(ret,rotations-1) : ret;
	}
	
	// Get random oscillator
	public static int[][] getRandomOscillator(int w, int h)
	{
		if (w == 1 && h == 1) 
		{
			int rotation = Util.randomInt(0, 2); // square shape, random rotate
			int index = Util.randomInt(0,Oscillator_1x1.values().length);
			return getOscillator(Oscillator_1x1.values()[index],rotation);
		}
		else if (w == 2 && h == 2)
		{
			int rotation = Util.randomInt(0, 2); // square shape, random rotate
			int index = Util.randomInt(0,Oscillator_2x2.values().length);
			return getOscillator(Oscillator_2x2.values()[index],rotation);
		} 
		else if (w == 3 && h == 2)
		{
			int rotation = 0; // irregular shape, don't rotate
			int index = Util.randomInt(0,Oscillator_3x2.values().length);
			return getOscillator(Oscillator_3x2.values()[index],rotation);
		} 
		else if (w == 4 && h == 2)
		{
			int rotation = 1; // rotate 2x4 array to 4x2
			int index = Util.randomInt(0,Oscillator_2x4.values().length);
			return getOscillator(Oscillator_2x4.values()[index],rotation);
		}
		else {
			return null;
		}
	}
	
	// Return 1x1 oscillator pattern as a 2D array
	public static int[][] getOscillator(Oscillator_1x1 osc, int r)
	{
		int[][] arr = null;
		
		switch(osc)
		{
			case toad:
				arr = new int[][]{
					{0,0,0,0,0,0},
					{0,0,1,0,0,0},
					{0,0,1,1,0,0},
					{0,0,1,1,0,0},
					{0,0,0,1,0,0},
					{0,0,0,0,0,0}};
					break;
			case beacon:
				arr = new int[][]{
					{0,0,0,0,0,0},
					{0,1,1,0,0,0},
					{0,1,1,0,0,0},
					{0,0,0,1,1,0},
					{0,0,0,1,1,0},
					{0,0,0,0,0,0}};
					break;
			case clock:
				arr = new int[][]{
					{0,0,0,0,0,0},
					{0,0,1,0,0,0},
					{0,0,0,1,1,0},
					{0,1,1,0,0,0},
					{0,0,0,1,0,0},
					{0,0,0,0,0,0}};
					break;
			default:
				break;
		}
		return r > 0 ? rotateMatrixLeft(arr,r) : arr;
	}
	
	// Return 2x2 oscillator pattern as a 2D array
	public static int[][] getOscillator(Oscillator_2x2 osc, int r)
	{
		int[][] arr = null;
		
		switch(osc)
		{
			case figure_eight:
				arr = new int[][]{
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,1,1,0,0,0,0,0,0,0},
					{0,0,0,1,1,0,1,0,0,0,0,0},
					{0,0,0,0,0,0,0,1,0,0,0,0},
					{0,0,0,0,1,0,0,0,0,0,0,0},
					{0,0,0,0,0,1,0,1,1,0,0,0},
					{0,0,0,0,0,0,0,1,1,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0}};
					break;
			case a_for_all:
				arr = new int[][]{
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,1,1,0,0,0,0,0},
					{0,0,0,0,1,0,0,1,0,0,0,0},
					{0,0,0,0,1,1,1,1,0,0,0,0},
					{0,0,1,0,1,0,0,1,0,1,0,0},
					{0,1,0,0,0,0,0,0,0,0,1,0},
					{0,1,0,0,0,0,0,0,0,0,1,0},
					{0,0,1,0,1,0,0,1,0,1,0,0},
					{0,0,0,0,1,1,1,1,0,0,0,0},
					{0,0,0,0,1,0,0,1,0,0,0,0},
					{0,0,0,0,0,1,1,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0}};
					break;
			default:
				break;
		}
		return r > 0 ? rotateMatrixLeft(arr,r) : arr;
	}
	
	// Return 2x3 oscillator pattern as a 2D array
	public static int[][] getOscillator(Oscillator_3x2 osc, int r)
	{
		int[][] arr = null;
		
		switch(osc)
		{
			case pentadecathlon:
				arr = new int[][]{
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,1,0,0,0,0,0,0},
					{0,0,0,0,0,1,0,0,0,0,0,0},
					{0,0,0,0,1,0,1,0,0,0,0,0},
					{0,0,0,0,0,1,0,0,0,0,0,0},
					{0,0,0,0,0,1,0,0,0,0,0,0},
					{0,0,0,0,0,1,0,0,0,0,0,0},
					{0,0,0,0,0,1,0,0,0,0,0,0},
					{0,0,0,0,1,0,1,0,0,0,0,0},
					{0,0,0,0,0,1,0,0,0,0,0,0},
					{0,0,0,0,0,1,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0}}; 
					break;
			case coes_p8:
				arr = new int[][]{
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,1,1,0,0,0},
					{0,0,0,0,0,0,0,1,1,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,1,0,1,0,0,0,0},
					{0,0,0,1,0,0,1,1,0,0,0,0},
					{0,0,0,0,0,0,1,0,0,0,0,0},
					{0,0,0,1,1,1,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,1,1,0,0,0,0,0,0,0},
					{0,0,0,1,1,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0}}; 
					break;
			default:
				break;
		}
		return r > 0 ? rotateMatrixLeft(arr,r) : arr;
	}

	// Return 2x4 oscillator pattern as a 2D array
	public static int[][] getOscillator(Oscillator_2x4 osc, int r)
	{
		int[][] arr = null;
		
		switch(osc)
		{
			case caterer_on_figure_eight:
				arr = new int[][]{
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,1,1,0,0,0,0,0,0,0},
					{0,0,0,1,1,0,1,0,0,0,0,0},
					{0,0,0,0,0,0,0,1,0,0,0,0},
					{0,0,0,0,1,0,0,0,0,0,0,0},
					{0,0,0,0,0,1,0,1,1,0,0,0},
					{0,0,0,0,0,0,0,1,1,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,1,1,1,0,0,0,0},
					{0,0,0,1,0,0,0,0,0,0,0,0},
					{0,0,0,1,0,0,0,0,1,0,0,0},
					{0,0,0,0,1,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,1,1,0,0,0,0},
					{0,0,0,0,0,0,0,1,0,0,0,0},
					{0,0,0,0,0,0,0,1,0,0,0,0},
					{0,0,0,0,0,0,0,1,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0}}; 
					break;
					
			case queen_bee_shuttle:
				arr = new int[][]{
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,1,1,0,0,0,0,0,0},
					{0,0,0,0,1,1,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,1,0,0,0,0,0,0},
					{0,0,0,0,1,0,1,0,0,0,0,0},
					{0,0,0,1,0,0,0,1,0,0,0,0},
					{0,0,0,0,1,1,1,0,0,0,0,0},
					{0,0,1,1,0,0,0,1,1,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,1,1,0,0,0,0,0,0},
					{0,0,0,0,1,1,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0}}; 
					break;
			default:
				break;
		}
		return r > 0 ? rotateMatrixLeft(arr,r) : arr;
	}
}