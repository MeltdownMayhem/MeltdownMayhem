package MeltdownMayhem;

public class Extra {
	public int spriteCounter = 0;
	public int spriteNum = 1;
	public int AmountOfSwitches = 10;
	
	// Distance calculator between 2 coordinates
	public static double distance(int x1, int y1, int x2, int y2) {
		return Math.pow(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2), 0.5);
	}
	// Updates static graphics such as object animations
	
	public void updateGraphs(int TimeBetweenAnimation, int DelayFactor) {
		// ********OLD FUNCTION, NOT GENERAL ENOUGH**********
	/*	
	    spriteCounter++;
		if (spriteNum == 1) {
			if (spriteCounter > DelayFactor*TimeBetweenAnimation) {
				spriteNum = 2;
				spriteCounter = 0;
			}
		} 
		else if (spriteNum == 2) {
			if (spriteCounter > TimeBetweenAnimation) {
				spriteNum = 1;
				spriteCounter = 0;
	
}
		} 
		*/
		
		
		//spriteCounter makes sure we have long enough intervals between animations
		spriteCounter++;
		int Multiplier;
		//Checks if the animation is a long or a short one (even or uneven)
		if (spriteNum%2 == 1) {
			Multiplier = DelayFactor;
			}
		else {
			Multiplier = 1;
			}
		//Runs through the 10 possible animations by iterating spriteNum in long and short turns
		if(spriteCounter > Multiplier*TimeBetweenAnimation) {
			spriteNum++;
			spriteCounter = 0;
			
			}
		if (spriteNum > AmountOfSwitches) {
			spriteNum = 1;
			}
		}
	
	}
