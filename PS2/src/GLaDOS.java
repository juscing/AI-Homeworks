import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import world.Robot;
import world.World;


public class GLaDOS extends Robot {
	
	public static int PQ_INIT_CAP = 100;
	public static final int PING_DEPTH = 5;
	
	LinkedList<UncertainMapPoint> path;
	Point startPosition;
	Point endPosition;
	int rows;
	int cols;
	HashSet<UncertainMapPoint> closedSet;
	HashMap<UncertainMapPoint,UncertainMapPoint> cameFrom;
	PriorityQueue<UncertainMapPoint> openSet;
	double[] values = {100, 90, 80, 70, 60, 50, 40, 30, 20, 10};
	
	public GLaDOS(Point startPosition, Point endPosition, int x, int y) {
		super();
		this.closedSet = new HashSet<UncertainMapPoint>();
		this.cameFrom = new HashMap<UncertainMapPoint,UncertainMapPoint>();
		this.path = new LinkedList<UncertainMapPoint>();
	}
	
	@Override
	public void addToWorld(World world) {
		super.addToWorld(world);
		this.startPosition = world.getStartPos();
		this.endPosition = world.getEndPos();
		this.rows = world.numRows();
		this.cols = world.numCols();
		this.openSet = new PriorityQueue<UncertainMapPoint>(PQ_INIT_CAP,
				new UncertainDistanceComparator(endPosition));
	}
	
	@Override
	public void travelToDestination() {
		UncertainMapPoint start = new UncertainMapPoint(0);
		start.setLocation(startPosition);
		openSet.add(start);
		cameFrom.put(start, start);
		int plan_since_move = 0;
		
		while(!openSet.isEmpty()) {
			UncertainMapPoint next = openSet.poll();
			System.out.println(next);
			
			if(this.getPosition().distanceSq(next) > PING_DEPTH * PING_DEPTH) {
				// Point we are evaluating is too far away!
				
				
			} else if(next.equals(endPosition) || plan_since_move >= PING_DEPTH) {
				// Complete the path planning... and start moving!
				
				
			}
			// closedSet.add(next);
			/* if(!MapUtil.canMove(this.pingMap(next))) {
				continue;
			} */ 
					int tenative_score = (int) (next.getBestDist() + next.distanceSq(neighborPoint));
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
		System.out.println("No path possible");
	}
	
	private ArrayList<UncertainMapPoint> getNeighbors(UncertainMapPoint ump) {
		ArrayList<UncertainMapPoint> neighbors = new ArrayList<UncertainMapPoint>();
		for(int x = -1; x <= 1; x++) {
			for(int y = -1; y <= 1; y++){
				UncertainMapPoint neighborPoint = new UncertainMapPoint(0);
				neighborPoint.setLocation(x + ump.x, y + ump.y);
				if(neighborPoint.x < 0 || neighborPoint.y < 0 || neighborPoint.x > rows - 1 
						|| neighborPoint.y > cols - 1) {
					System.out.println("Out of bounds");
					continue;
				}
				if(closedSet.contains(neighborPoint)) {
					System.out.println("Already tried");
					continue;
				}
				neighbors.add(neighborPoint);
			}
		}
		return neighbors;
	}
	
	private void generate_path(UncertainMapPoint ump) {
		while(!ump.equals(this.startPosition)) {
			path.addFirst(ump);
			ump = cameFrom.get(ump);
		}
		System.out.println("MADE IT");
		System.out.println("The path the bot discovered:");
		System.out.println(this.startPosition);
		for(UncertainMapPoint mp : path) {
			System.out.println(mp);
		}
		return;
	}
	
	private int move_proc(UncertainMapPoint target) {
		// I LIKE TO MOVE IT MOVE IT
		int successful_moves = 0;
		LinkedList<UncertainMapPoint> temp_path = new LinkedList<UncertainMapPoint>();
		// Backtrack
		while(!target.equals(this.getPosition())) {
			temp_path.addFirst(target);
			target = cameFrom.get(target);
		}
		
		System.out.println("Move the bot");
		System.out.println(this.getPosition());
		for(UncertainMapPoint mp : temp_path) {
			System.out.println(mp);
			if(mp.equals(this.endPosition)) {
				this.generate_path(mp);
			}
			Point p = this.move(mp);
			if(mp.equals(p)) {
				// Robot Moved!
				successful_moves++;
			} else {
				// Robot hit wall
				break;
			}
		}
		return successful_moves;
	}
	
	public static void main(String[] args) {
		try {
			World myWorld = new World("worldFiles/25x25_lines.txt", true);
			GLaDOS glados = new GLaDOS(myWorld.getStartPos(), myWorld.getEndPos(), 
					myWorld.numRows(), myWorld.numCols());
			glados.addToWorld(myWorld);
			System.out.println(glados.getPosition());
			System.out.println(myWorld.getEndPos());
			glados.travelToDestination();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
