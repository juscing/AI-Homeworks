import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import world.Robot;
import world.World;

//uses the planned path like HAL9000, if when following the path it runs into a wall where it
//thought there was none it will stop there and run the search again
public class WallE extends Robot {

	public static final int PQ_INIT_CAP = 100;

	LinkedList<MapPoint> path;
	Point startPosition;
	Point endPosition;
	Point currentPosition;
	int rows;
	int cols;
	HashSet<MapPoint> closedSet;
	HashMap<MapPoint,MapPoint> cameFrom;
	PriorityQueue<MapPoint> openSet;
	static int complete;

	public WallE() {
		super();
		this.closedSet = new HashSet<MapPoint>();
		this.cameFrom = new HashMap<MapPoint,MapPoint>();
		this.path = new LinkedList<MapPoint>();
		complete = 0;
		
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

	public void planPath() {
		//This is going to roughly be A*, planning for the path, if it can not find a path
		//it will return what it has so far
		MapPoint start = new MapPoint(0);
		start.setLocation(this.getPosition());
		System.out.println("START POSITION: "+start);
		MapPoint end = new MapPoint(0);
		end.setLocation(endPosition);
		System.out.println("END POSITION: "+end);
		
		openSet.clear();
		cameFrom.clear();
		closedSet.clear();
		
		openSet.add(start);
		cameFrom.put(start, start);
		MapPoint lastTried = start;

		while(!openSet.isEmpty()) {
			MapPoint next = openSet.poll();
			//System.out.println(next);
			if(next.equals(endPosition)) {
				// Got to target
				// Backtrack, create path
				while(!next.equals(start)) {
					path.addFirst(next);
					next = cameFrom.get(next);
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
					//System.out.println(neighborPoint);
					if(neighborPoint.x < 0 || neighborPoint.y < 0 || neighborPoint.x > rows - 1 
							|| neighborPoint.y > cols - 1) {
						//System.out.println("Out of bounds");
						continue;
					}
					if(closedSet.contains(neighborPoint)) {
						//System.out.println("Already tried");
						continue;
					}
					int tenative_score = (int) (next.getBestDist() + next.distanceSq(neighborPoint));
					//System.out.println(tenative_score);
					if(!openSet.contains(neighborPoint)) {
						//System.out.println("best option yet");
						cameFrom.put(neighborPoint, next);
						neighborPoint.setBestDist(tenative_score);
						openSet.add(neighborPoint);
						lastTried = neighborPoint;
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
		//could not find target, create path to farthest point yet
		System.out.println("Could not find destination, returning best.");
		while(!lastTried.equals(start)) {
			path.addFirst(lastTried);
			lastTried = cameFrom.get(lastTried);
		}
		return;
	}

	@Override
	public void travelToDestination() {
		// Travel the found path
		// If robot runs into wall, stop and plan again
		// If robot gets to the end of the path and it is not the target, call plan again
		// Else robot got to the end of the path and it is the target

		//System.out.println("Move the bot");
		
		Point current = this.getPosition();
		Point last = current;
		MapPoint currentMP = new MapPoint(0);
		currentMP.setLocation(current);
		System.out.println(currentMP);
		
		for(int x=0; x<path.size(); x++){
			MapPoint mp = path.get(x);
			if(mp.equals(endPosition))
				System.out.println(mp);
			current = this.move(mp);
			if(current.equals(endPosition)){
				//made it to target
				//something in the jar is overriding my end case - since I am checking the result
				//of the move command but it takes hold when move is called on the target
				//System.out.println("got to target: "+mp);
				//complete = 1;
				//return;
			}
			else if(current.equals(last)){
				//robot did not move (ran into wall)
				System.out.println("Ran into wall.");
				return;
			}
			else if(x+1==path.size() && !current.equals(endPosition)){
				//path did not make it to the end
				System.out.println(mp);
				System.out.println("End of current path.");
				return;
			}
			else{
				//continue on path
				//System.out.println("continue on path");
				last = current;
				//System.out.println(current);
				System.out.println(mp);
				continue;
			}
		}
		return;
		
	}




	public static void main(String[] args) {
		try {
			World myWorld = new World("worldFiles/simpleWorld.txt", true);
			WallE walle = new WallE();
			walle.addToWorld(myWorld);
			for(int c=0; c<5; c++){
				System.out.println("TRYING TO FIND PATH: " + (c+1));
				walle.planPath();
				walle.travelToDestination();
				if(complete == 1)
					break;
			}
			System.out.println("No path found.");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}

