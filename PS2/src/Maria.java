import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import world.Robot;
import world.World;


//prints out entire map based on pings - gives us an idea on uncertainty
//Maria - from Metropolis (1927), one of the first science fiction movies
public class Maria extends Robot{
	
public static final int PQ_INIT_CAP = 100;
	
	LinkedList<MapPoint> path;
	Point startPosition;
	Point endPosition;
	int rows;
	int cols;
	HashSet<MapPoint> closedSet;
	HashMap<MapPoint,MapPoint> cameFrom;
	PriorityQueue<MapPoint> openSet;
	
	public Maria() {
		super();
		this.closedSet = new HashSet<MapPoint>();
		this.cameFrom = new HashMap<MapPoint,MapPoint>();
		this.path = new LinkedList<MapPoint>();
	}
	
	@Override
	public void addToWorld(World world) {
		super.addToWorld(world);
		this.startPosition = world.getStartPos();
		this.endPosition = world.getEndPos();
		this.rows = world.numRows();
		this.cols = world.numCols();
		this.openSet = new PriorityQueue<MapPoint>(PQ_INIT_CAP, 
				new DistanceComparator(endPosition));
	}
	
	@Override
	public void travelToDestination() {
		// TODO Auto-generated method stub
		
	}
	
	public void printMap(World world){
		for (int x=0; x<world.numCols(); x++){
			for(int y=0; y<world.numRows(); y++){
				System.out.print(this.pingMap(new Point(x, y)) + " ");
				
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		try {
			World myWorld = new World("worldFiles/25x25_wall.txt", true);
			Maria maria = new Maria();
			maria.addToWorld(myWorld);
			maria.printMap(myWorld);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
}
