package utilities;

public class Pattern {

	// http://conwaylife.com/wiki/List_of_common_oscillators
	public enum Oscillator_1x1 {
		beacon,
		clock,
		toad
	}
	public enum Oscillator_2x1 {
		killer_toads,
		cis_beacon_and_cap
	}
	public enum Oscillator_2x2 {
		figure_eight,
		a_for_all
	}
	public enum Oscillator_2x3 {
		pentadecathlon,
		coes_p8
	}
	public enum Oscillator_2x4 {
		caterer_on_figure_eight,
		queen_bee_shuttle
	}
	
	// http://conwaylife.com/wiki/Eater
	public enum Eater {
	}

	// http://conwaylife.com/wiki/Spaceship
	public enum Spaceship {
	}
	
	// rotate array
	public static int[][] rotate (int[][] passedIn, int r){
	    int[][] newArray = new int[r][r];
	    for (int i = 0; i < r; i++){
	        for (int j =0; j<r; j++){
	        	newArray[i][j] = passedIn [r-1-j][i];
	        }
	    }   

	    return newArray;
	}
	
	// Get random oscillator
	public static int[][] getRandomOscillator(int w, int h)
	{
		// Get pattern
		double random = Math.random(); 

		if(w == 1 && h == 1) {
			Oscillator_1x1 osc = (random < 0.333) ? Oscillator_1x1.toad : (random < 0.666) ? Oscillator_1x1.beacon : Oscillator_1x1.clock;
			return getOscillator(osc,0);
		} else if (w == 2 & h == 1) {
			Oscillator_2x1 osc = (random < 0.5) ? Oscillator_2x1.killer_toads : Oscillator_2x1.cis_beacon_and_cap;
			return getOscillator(osc,0);
		} else if (w == 2 && h == 2){
			Oscillator_2x2 osc = (random < 0.5) ? Oscillator_2x2.figure_eight : Oscillator_2x2.a_for_all;
			return getOscillator(osc,0);
		} else if (w == 2 && h == 3){
			Oscillator_2x3 osc = (random < 0.5) ? Oscillator_2x3.pentadecathlon : Oscillator_2x3.coes_p8;
			return getOscillator(osc,0);
		} else if (w == 2 && h == 4){
			Oscillator_2x4 osc = (random < 0.5) ? Oscillator_2x4.caterer_on_figure_eight : Oscillator_2x4.queen_bee_shuttle;
			return getOscillator(osc,0);
		}
		return null;
	}
	
	// Return 1x1 oscillator pattern as a 2D array
	public static int[][] getOscillator(Oscillator_1x1 osc, int r){
		switch(osc){
			case toad:
				int[][] arr = new int[][]{
					{0,0,0,0,0,0},
					{0,0,1,0,0,0},
					{0,0,1,1,0,0},
					{0,0,1,1,0,0},
					{0,0,0,1,0,0},
					{0,0,0,0,0,0}};
				return r > 0 ? rotate(arr,r) : arr;
			case beacon:
				int[][] arr1 = new int[][]{
					{0,0,0,0,0,0},
					{0,1,1,0,0,0},
					{0,1,1,0,0,0},
					{0,0,0,1,1,0},
					{0,0,0,1,1,0},
					{0,0,0,0,0,0}};
				return r > 0 ? rotate(arr1,r) : arr1;
			case clock:
				int[][] arr2 = new int[][]{
					{0,0,0,0,0,0},
					{0,0,1,0,0,0},
					{0,0,0,1,1,0},
					{0,1,1,0,0,0},
					{0,0,0,1,0,0},
					{0,0,0,0,0,0}};
				return r > 0 ? rotate(arr2,r) : arr2;
			default:
				return null;
		}
	}
	
	// Return 2x1 oscillator pattern as a 2D array
	public static int[][] getOscillator(Oscillator_2x1 osc, int r){
		switch(osc){
			case killer_toads:
				return new int[][]{
					{0,0,0,0,0,0},
					{0,0,0,0,0,0},
					{0,0,1,1,1,0},
					{0,1,1,1,0,0},
					{0,0,0,0,0,0},
					{0,0,0,0,0,0},
					{0,0,0,0,0,0},
					{0,1,1,1,0,0},
					{0,0,1,1,1,0},
					{0,0,0,0,0,0},
					{0,0,0,0,0,0},
					{0,0,0,0,0,0}};
			case cis_beacon_and_cap:
				return new int[][]{
					{0,0,0,0,0,0},
					{0,0,0,0,0,0},
					{0,0,0,1,1,0},
					{0,0,0,0,1,0},
					{0,1,0,0,0,0},
					{0,1,1,0,0,0},
					{0,0,0,0,0,0},
					{0,1,1,1,1,0},
					{0,1,0,0,1,0},
					{0,0,1,1,0,0},
					{0,0,0,0,0,0},
					{0,0,0,0,0,0}};
			default:
				return null;
		}
	}
	
	// Return 2x2 oscillator pattern as a 2D array
	public static int[][] getOscillator(Oscillator_2x2 osc, int r){
		switch(osc){
			case figure_eight:
				return new int[][]{
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
			case a_for_all:
				return new int[][]{
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
			default:
				return null;
		}
	}
	
	// Return 2x3 oscillator pattern as a 2D array
	public static int[][] getOscillator(Oscillator_2x3 osc, int r){
		switch(osc){
			case pentadecathlon:
				return new int[][]{
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
					{0,0,0,0,0,1,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0}};
			case coes_p8:
				return new int[][]{
					{0,0,0,0,0,0,0,0,0,0,0,0},
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
					{0,0,0,1,1,0,0,0,0,0,0,0},
					{0,0,0,1,1,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0}};
			default:
				return null;
		}
	}

	// Return 2x4 oscillator pattern as a 2D array
	public static int[][] getOscillator(Oscillator_2x4 osc, int r){
		switch(osc){
			case caterer_on_figure_eight:
				return new int[][]{
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
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
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},};
			case queen_bee_shuttle:
				return new int[][]{
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
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
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0}};
			default:
				return null;
		}
	}
}