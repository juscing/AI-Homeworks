import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import world.Robot;
import world.World;


public class C3PO extends Robot {
	
	public static final int PQ_INIT_CAP = 100;
	public static int PING_DEPTH = 5;
	
	LinkedList<MapPoint> path;
	Point startPosition;
	Point endPosition;
	int rows;
	int cols;
	HashSet<MapPoint> closedSet;
	HashMap<MapPoint,MapPoint> cameFrom;
	PriorityQueue<MapPoint> openSet;
	Boolean[][] map;
	Integer[][] prob;
	
	public C3PO() {
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
		map = new Boolean[this.rows][this.cols];
		prob = new Integer[this.rows][this.cols];
		for(Integer[] i: prob) {
			for(Integer j: i) {
				j = Integer.MAX_VALUE;
			}
		}
		this.openSet = new PriorityQueue<MapPoint>(PQ_INIT_CAP, 
				new DistanceComparator(endPosition));
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
			closedSet.add(next);
			int distance = (int) this.getPosition().distance(next);
			if(map[next.x][next.y] != null && !map[next.x][next.y] && prob[next.x][next.y] < PING_DEPTH) {
				continue;
			}else if(!MapUtil.canMove(this.pingMap(next))) {
				map[next.x][next.y] = false;
				prob[next.x][next.y] = distance;
				continue;
			}
			if(distance >= PING_DEPTH || next.equals(endPosition)) {
				// Point that came out is too far away!
				System.out.println("Enough planning!");
				int successful = this.move_proc(next);
				if(successful >= PING_DEPTH) {
					PING_DEPTH++;
				} else {
					if(PING_DEPTH > 1) {
						PING_DEPTH--;
					}
				}
				System.out.println("new ping depth " + PING_DEPTH);
				if(successful < 1) {
					continue;
				}
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
					if(map[neighborPoint.x][neighborPoint.y] != null) {
						if(!map[neighborPoint.x][neighborPoint.y] && prob[neighborPoint.x][neighborPoint.y] < PING_DEPTH) {
							continue;
						}
					}
					if(closedSet.contains(neighborPoint)) {
						//System.out.println("Already tried");
						continue;
					}
					int tenative_score = (int) (next.getBestDist() + next.distanceSq(neighborPoint));
					//System.out.println(tenative_score);
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
	
	private void generate_path(MapPoint ump) {
		System.out.println("Path taken");
		for(MapPoint mp : path) {
			System.out.println(mp);
		}
	}
	
	private int move_proc(MapPoint target) {
		System.out.println("Move proc to " + target);
		// I LIKE TO MOVE IT MOVE IT
		int successful_moves = 0;
		LinkedList<MapPoint> temp_path = new LinkedList<MapPoint>();
		// Backtrack
		while(!target.equals(this.getPosition())) {
			//System.out.println(target);
			temp_path.addFirst(target);
			target = cameFrom.get(target);
			if(target == null || temp_path.contains(target)) {
				System.out.println("Robot hit wall");
				openSet.clear();
				closedSet.clear();
				cameFrom.clear();
				MapPoint start = new MapPoint(0);
				start.setLocation(this.getPosition());
				openSet.add(start);
				cameFrom.put(start, start);
				System.out.println("Restarting from: " + start);
				return 0;
			}
		}
		System.out.println("Move the bot");
		System.out.println(this.getPosition());
		for(MapPoint mp : temp_path) {
			// System.out.println(mp);
			if(mp.equals(this.endPosition)) {
				this.generate_path(mp);
			}
			Point p = this.move(mp);
			if(mp.equals(p)) {
				// System.out.println(mp);
				// Robot Moved!
				System.out.println("successful move");
				path.add(mp);
				map[mp.x][mp.y] = true;
				prob[mp.x][mp.y] = 0;
				successful_moves++;
			} else {
				System.out.println("Robot hit wall");
				map[mp.x][mp.y] = false;
				prob[mp.x][mp.y] = 0;
				openSet.clear();
				closedSet.clear();
				cameFrom.clear();
				MapPoint start = new MapPoint(0);
				start.setLocation(this.getPosition());
				openSet.add(start);
				cameFrom.put(start, start);
				System.out.println("Restarting from: " + start);
				break;
			}
		}
		System.out.println("Stop moving");
		return successful_moves;
	}
	
	public static void main(String[] args) {
		try {
			World myWorld = new World("worldFiles/25x25_lines.txt", true);
			C3PO rosie = new C3PO();
			rosie.addToWorld(myWorld);
			System.out.println(rosie.getPosition());
			rosie.travelToDestination();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
