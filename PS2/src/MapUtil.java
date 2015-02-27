import java.awt.Point;


public class MapUtil {

	public static boolean canMove(String s) {
		return s.equals("O") || s.equals("S") || s.equals("F");
	}
	
	public static boolean didMove(Point previous, Point current) {
		return previous.x != current.x || previous.y != current.y;
	}
	
}
