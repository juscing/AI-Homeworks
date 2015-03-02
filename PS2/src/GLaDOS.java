import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import world.Robot;
import world.World;


public class GLaDOS extends Robot {
	
	public static final int PQ_INIT_CAP = 100;
	public static int PING_DEPTH = 5;
	
	LinkedList<UncertainMapPoint> path;
	Point startPosition;
	Point endPosition;
	int rows;
	int cols;
	HashSet<UncertainMapPoint> closedSet;
	HashMap<UncertainMapPoint,UncertainMapPoint> cameFrom;
	PriorityQueue<UncertainMapPoint> openSet;
	UncertainMapPoint[][] map;
	
	
	public GLaDOS() {
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
		this.map = new UncertainMapPoint[this.rows][this.cols];
	}
	
	@Override
	public void travelToDestination() {
		UncertainMapPoint start = new UncertainMapPoint(0);
		start.setLocation(startPosition);
		openSet.add(start);
		cameFrom.put(start, start);
		map[start.x][start.y] = start;
		int plan_since_move = 0;
		while(!openSet.isEmpty()) {
			UncertainMapPoint next = openSet.poll();
			System.out.println(next);
			double distance = this.getPosition().distance(next);
			if(next.equals(endPosition) || plan_since_move >= PING_DEPTH) {
				// Complete the path planning... and start moving!
				int successful = this.move_proc(next);
				if(successful >= PING_DEPTH) {
					PING_DEPTH++;
				} else {
					PING_DEPTH--;
				}
			} else if(distance > PING_DEPTH) {
				// Point that came out is too far away!
				this.move_proc(next);
				plan_since_move = 0;
			}
			if(MapUtil.canMove(this.pingMap(next))) {
				next.setMoveable(true, (int) distance);
			} else {
				next.setMoveable(false, (int) distance);
			}
			closedSet.add(next);
			ArrayList<UncertainMapPoint> neighbors = this.getNeighbors(next);
			for(UncertainMapPoint neighborPoint : neighbors) {
				int tenative_score = (int) (next.getBestDist() + next.distanceSq(neighborPoint));
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
				double distance = this.getPosition().distance(neighborPoint);
				if(map[neighborPoint.x][neighborPoint.y] == null) {
					neighborPoint.setMoveable(MapUtil.canMove(this.pingMap(neighborPoint)), (int) distance);
				} else {
					map[neighborPoint.x][neighborPoint.y].setMoveable(
							MapUtil.canMove(this.pingMap(neighborPoint)), (int) distance);
				}
				neighbors.add(neighborPoint);
				map[neighborPoint.x][neighborPoint.y] = neighborPoint;
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
				if(map[mp.x][mp.y] != null) {
					map[mp.x][mp.y].setMoveable(true, 0);
				}
				successful_moves++;
			} else {
				// Robot hit wall
				if(map[mp.x][mp.y] != null) {
					map[mp.x][mp.y].setMoveable(false, 0);
				}
				break;
			}
		}
		return successful_moves;
	}
	
	public static void main(String[] args) {
		try {
			World myWorld = new World("worldFiles/simpleWorld.txt", true);
			GLaDOS glados = new GLaDOS();
			glados.addToWorld(myWorld);
			System.out.println(glados.getPosition());
			System.out.println(myWorld.getEndPos());
			glados.travelToDestination();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
