package MeltdownMayhem;

public class Extra {
	// Distance calculator between 2 coordinates
	public static double distance(int x1, int y1, int x2, int y2) {
		return Math.pow(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2), 0.5);
	}

}