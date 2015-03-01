import java.awt.Point;


public class MapUtil {

	public static boolean canMove(String s) {
		return s.equals("O") || s.equals("S") || s.equals("F");
	}
	
}
