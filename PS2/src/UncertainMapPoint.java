import java.awt.Point;
import java.util.ArrayList;


public class UncertainMapPoint extends Point {
	
	// Best value from start to here so far
	private int bestDist;
	private boolean moveable;
	
	public UncertainMapPoint(int bestDist) {
		this.bestDist = bestDist;
	}

	public int getBestDist() {
		return bestDist;
	}

	public void setBestDist(int bestDist) {
		this.bestDist = bestDist;
	}
	
	public double distance(UncertainMapPoint mp) {
		return super.distance(mp.x, mp.y);
	}
	
}
