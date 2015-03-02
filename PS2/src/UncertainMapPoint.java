import java.awt.Point;


public class UncertainMapPoint extends Point {
	
	// Best value from start to here so far
	private int bestDist;
	private boolean moveable;
	private int pingDist;
	private int openNeighbors;
	
	public UncertainMapPoint(int bestDist) {
		this.bestDist = bestDist;
		this.moveable = true;
		this.pingDist = Integer.MAX_VALUE;
		this.openNeighbors = 0;
	}
	
	public boolean setMoveable(boolean val, int distance) {
		if(distance < pingDist) {
			this.moveable = val;
			this.pingDist = distance;
			return true;
		}
		return false;
	}
	
	public int getOpenNeighbors() {
		return this.openNeighbors;
	}
	
	public void setOpenNeighbors(int neighbors) {
		this.openNeighbors = neighbors;
	}
	
	public boolean getMoveable() {
		return moveable;
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
