import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import world.Robot;
import world.World;


public class MyRobot extends Robot {
	
	public static final int PQ_INIT_CAP = 100;
	
	LinkedList<MapPoint> path;
	Point startPosition;
	Point endPosition;
	int xSize;
	int ySize;
	HashSet<MapPoint> closedSet;
	HashMap<MapPoint,MapPoint> cameFrom;
	PriorityQueue<MapPoint> openSet;
	
	public MyRobot(Point startPosition, Point endPosition, int x, int y) {
		super();
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.openSet = new PriorityQueue<MapPoint>(PQ_INIT_CAP, 
				new DistanceComparator(endPosition));
		this.closedSet = new HashSet<MapPoint>();
		this.cameFrom = new HashMap<MapPoint,MapPoint>();
		this.xSize = x;
		this.ySize = y;
		this.path = new LinkedList<MapPoint>();
	}
	
	@Override
	public void travelToDestination() {
		// This is going to roughly be A* totally planning not moving
		MapPoint start = new MapPoint(0);
		start.setLocation(startPosition);
		openSet.add(start);
		cameFrom.put(start, start);
		
		while(!openSet.isEmpty()) {
			MapPoint next = openSet.poll();
			System.out.println(next);
			if(next.equals(endPosition)) {
				// Made it!
				
				// Backtrack
				while(!next.equals(start)) {
					path.addFirst(next);
					next = cameFrom.get(next);
				}
				
				System.out.println("Move the bot");
				System.out.println(start);
				for(MapPoint mp : path) {
					System.out.println(mp);
					this.move(mp);
				}
				
				return;
			}
			closedSet.add(next);
			if(!MapUtil.canMove(this.pingMap(next))) {
				continue;
			}
			for(int x = -1; x <= 1; x++) {
				for(int y = -1; y <= 1; y++){
					MapPoint neighborPoint = new MapPoint(0);
					neighborPoint.setLocation(x + next.x, y + next.y);
					System.out.println(neighborPoint);
					if(neighborPoint.x < 0 || neighborPoint.y < 0 || neighborPoint.x > xSize - 1 
							|| neighborPoint.y > ySize - 1) {
						System.out.println("Out of bounds");
						continue;
					}
					if(closedSet.contains(neighborPoint)) {
						System.out.println("Already tried");
						continue;
					}
					int tenative_score = (int) (next.getBestDist() + next.distance(neighborPoint));
					System.out.println(tenative_score);
					if(!openSet.contains(neighborPoint)) {
						cameFrom.put(neighborPoint, next);
						neighborPoint.setBestDist(tenative_score);
						openSet.add(neighborPoint);
					} else if(openSet.contains(neighborPoint) && tenative_score < 
							neighborPoint.getBestDist()){
						openSet.remove(neighborPoint);
						neighborPoint.setBestDist(tenative_score);
						cameFrom.put(neighborPoint, next);
						openSet.add(neighborPoint);
					}
				}
			}
		}
		System.out.println("No path possible");
	}
	
	public static void main(String[] args) {
		try {
			World myWorld = new World("worldFiles/simpleWorld.txt", false);
			MyRobot hal9000 = new MyRobot(myWorld.getStartPos(), myWorld.getEndPos(), 
					myWorld.numRows(), myWorld.numCols());
			hal9000.addToWorld(myWorld);
			System.out.println(hal9000.getPosition());
			hal9000.travelToDestination();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
