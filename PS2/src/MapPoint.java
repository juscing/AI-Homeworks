import java.awt.Point;


public class MapPoint extends Point {
	
	// Best value from start to here so far
	private int bestDist;
	
	public MapPoint(int bestDist) {
		this.bestDist = bestDist;
	}

	public int getBestDist() {
		return bestDist;
	}

	public void setBestDist(int bestDist) {
		this.bestDist = bestDist;
	}
	
	public double distance(MapPoint mp) {
		return super.distance(mp.x, mp.y);
	}
	
}
