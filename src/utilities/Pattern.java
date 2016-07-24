package utilities;

public class Pattern {

	// http://conwaylife.com/wiki/List_of_common_oscillators
	public enum Oscillator {
		blinker,
		toad,
		beacon,
		pulsar,
		pentadecathlon,
		clock,
		bipole
	}
	
	// http://conwaylife.com/wiki/Agar
	public enum Agar {
		houndstooth_agar,
		squaredance,
		venetian_blinds,
		chicken_wire,
		phoenix
	}

	// http://conwaylife.com/wiki/Spaceship
	public enum Spaceship {
		glider,
		lightweight_spaceship,
		s_30P5H2V0,
		s_25P3H1V0_2,
		weekender,
		s_37P4H1V0
	}
	
	// Return oscillator pattern as a 2D array
	public int[][] getOscillator(Oscillator o, int period){
		switch(o){
			case blinker:
				switch(period){
					case 0: return new int[][]{
						{0,0,0},
						{1,1,1},
						{0,0,0}};
					case 1: return new int[][]{
						{0,1,0},
						{0,1,0},
						{0,1,0}};
					default: return null;
				}
			case toad:
				switch(period){
					case 0: return new int[][]{
						{0,1,0,0},
						{0,1,1,0},
						{0,1,1,0},
						{0,0,1,0}};
					case 1: return new int[][]{
						{0,1,1,0},
						{1,0,0,0},
						{0,0,0,1},
						{0,1,1,0}};
					default: return null;
				}
			case beacon:
				switch(period){
					case 0: return new int[][]{
						{1,1,0,0},
						{1,1,0,0},
						{0,0,1,1},
						{0,0,1,1}};
					case 1: return new int[][]{
						{1,1,0,0},
						{1,0,0,0},
						{0,0,0,1},
						{0,0,1,1}};
					default: return null;
				}
			case clock:
				switch(period){
					case 0: return new int[][]{
						{0,1,0,0},
						{0,0,1,1},
						{1,1,0,0},
						{0,0,1,0}};
					case 1: return new int[][]{
						{0,0,1,0},
						{1,0,1,0},
						{0,1,0,1},
						{0,1,0,0}};
					default: return null;
				}
			case bipole:
				switch(period){
					case 0: return new int[][]{
						{0,0,0,1,1},
						{0,0,1,0,1},
						{0,0,0,0,0},
						{1,0,1,0,0},
						{1,1,0,0,0}};
					case 1: return new int[][]{
						{0,0,0,1,1},
						{0,0,0,0,1},
						{0,1,0,1,0},
						{1,0,0,0,0},
						{1,1,0,0,0}};
					default: return null;
				}
			default:
				return null;
		}
	}
	
	// Return agar pattern as a 2D array
	public int[][] getAgar(Agar a, int period){
		switch(a){
			case houndstooth_agar:
				break;
			case squaredance:
				break;
			case venetian_blinds:
				break;
			case chicken_wire:
				break;
			case phoenix:
				break;
			default:
				break;
		}
		return null;
	}
	
	// Return spaceship pattern as a 2D array
	public int[][] getSpaceship(Spaceship s, int period){
		switch(s){
			case glider:
				break;
			case lightweight_spaceship:
				break;
			case s_30P5H2V0:
				break;
			case s_25P3H1V0_2:
				break;
			case weekender:
				break;
			case s_37P4H1V0:
				break;
			default:
				break;
		}
		return null;
	}
}