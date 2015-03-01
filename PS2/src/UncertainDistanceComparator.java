import java.awt.Point;
import java.util.Comparator;


public class UncertainDistanceComparator implements Comparator<UncertainMapPoint> {

	Point targetPoint;
	
	public UncertainDistanceComparator(Point targetPoint) {
		this.targetPoint = targetPoint;
	}
	
	private double score(UncertainMapPoint mp) {
		// Normal score
		double score = mp.distanceSq(targetPoint) + mp.getBestDist();
		// Mods
		
		return score;
	}
	
	@Override
	public int compare(UncertainMapPoint p1, UncertainMapPoint p2) {
		return (int) (score(p1) - score(p2));
	}

}
