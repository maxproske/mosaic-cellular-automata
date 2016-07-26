package utilities;

public class Pattern {

	// http://conwaylife.com/wiki/List_of_common_oscillators
	public enum Oscillator {
		blinker,
		toad,
		beacon,
		clock,
		bipole,
		pulsar,
		pentadecathlon
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
	
	// Get random oscillator
	public static int[][] getRandomOscillator()
	{
		// Get pattern
		double random = Math.random(); 
		Pattern.Oscillator osc = (random < 0.333) ? Pattern.Oscillator.toad : (random < 0.666) ? Pattern.Oscillator.beacon : Pattern.Oscillator.clock;

		// Get period
		int period = Math.random() < 0.5 ? 0 : 1;
		
		return Pattern.getOscillator(osc,period);
	}
	
	// Return oscillator pattern as a 2D array
	public static int[][] getOscillator(Oscillator o, int period){
		switch(o){
			case blinker:
				switch(period){
					case 0: return new int[][]{
						{0,0,0,0,0},
						{0,0,0,0,0},
						{0,1,1,1,0},
						{0,0,0,0,0},
						{0,0,0,0,0}};
					case 1: return new int[][]{
						{0,0,0,0,0},
						{0,0,1,0,0},
						{0,0,1,0,0},
						{0,0,1,0,0},
						{0,0,0,0,0}};
					default: return null;
				}
			case toad:
				switch(period){
					case 0: return new int[][]{
						{0,0,0,0,0,0},
						{0,0,1,0,0,0},
						{0,0,1,1,0,0},
						{0,0,1,1,0,0},
						{0,0,0,1,0,0},
						{0,0,0,0,0,0}};
					case 1: return new int[][]{
						{0,0,0,0,0,0},
						{0,0,1,1,0,0},
						{0,1,0,0,0,0},
						{0,0,0,0,1,0},
						{0,0,1,1,0,0},
						{0,0,0,0,0,0}};
					default: return null;
				}
			case beacon:
				switch(period){
					case 0: return new int[][]{
						{0,0,0,0,0,0},
						{0,1,1,0,0,0},
						{0,1,1,0,0,0},
						{0,0,0,1,1,0},
						{0,0,0,1,1,0},
						{0,0,0,0,0,0}};
					case 1: return new int[][]{
						{0,0,0,0,0,0},
						{0,1,1,0,0,0},
						{0,1,0,0,0,0},
						{0,0,0,0,1,0},
						{0,0,0,1,1,0},
						{0,0,0,0,0,0}};
					default: return null;
				}
			case clock:
				switch(period){
					case 0: return new int[][]{
						{0,0,0,0,0,0},
						{0,0,1,0,0,0},
						{0,0,0,1,1,0},
						{0,1,1,0,0,0},
						{0,0,0,1,0,0},
						{0,0,0,0,0,0}};
					case 1: return new int[][]{
						{0,0,0,0,0,0},
						{0,0,0,1,0,0},
						{0,1,0,1,0,0},
						{0,0,1,0,1,0},
						{0,0,1,0,0,0},
						{0,0,0,0,0,0}};
					default: return null;
				}
			case bipole:
				switch(period){
					case 0: return new int[][]{
						{0,0,0,0,0,0,0},
						{0,0,0,0,1,1,0},
						{0,0,0,1,0,1,0},
						{0,0,0,0,0,0,0},
						{0,1,0,1,0,0,0},
						{0,1,1,0,0,0,0},
						{0,0,0,0,0,0,0}};
					case 1: return new int[][]{
						{0,0,0,0,0,0,0},
						{0,0,0,0,1,1,0},
						{0,0,0,0,0,1,0},
						{0,0,1,0,1,0,0},
						{0,1,0,0,0,0,0},
						{0,1,1,0,0,0,0},
						{0,0,0,0,0,0,0}};
					default: return null;
				}
			case pulsar:
				switch(period){
					default: return null;
				}
			case pentadecathlon:
				switch(period){
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