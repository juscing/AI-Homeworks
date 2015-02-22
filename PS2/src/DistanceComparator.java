import java.awt.Point;
import java.util.Comparator;


public class DistanceComparator implements Comparator<MapPoint> {

	Point targetPoint;
	
	public DistanceComparator(Point targetPoint) {
		this.targetPoint = targetPoint;
	}
	
	private double score(MapPoint mp) {
		return mp.distance(targetPoint) + mp.getBestDist();
	}
	
	@Override
	public int compare(MapPoint p1, MapPoint p2) {
		return (int) (score(p1) - score(p2));
	}

}
