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


public class GLaDOS extends Robot {
	
	public static final int PQ_INIT_CAP = 100;
	public static int PING_DEPTH = 5;
	
	LinkedList<UncertainMapPoint> path;
	Point startPosition;
	Point endPosition;
	int rows;
	int cols;
	ArrayList<UncertainMapPoint> closedSet;
	HashMap<UncertainMapPoint,UncertainMapPoint> cameFrom;
	PriorityQueue<UncertainMapPoint> openSet;
	
	
	public GLaDOS() {
		super();
		this.closedSet = new ArrayList<UncertainMapPoint>();
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
		while(!openSet.isEmpty()) {
			UncertainMapPoint next = openSet.poll();
			System.out.println("A* on: " + next);
			double distance = this.getPosition().distance(next);
			boolean val;
			if(MapUtil.canMove(this.pingMap(next))) {
				val = next.setMoveable(true, (int) distance);
			} else {
				val = next.setMoveable(false, (int) distance);
			}
			closedSet.add(next);
			if(!next.getMoveable()) {
				if(cameFrom.containsKey(next)) {
					UncertainMapPoint x = cameFrom.get(next);
					if(!openSet.contains(x)) {
						openSet.add(x);
						closedSet.remove(x);
					}
					cameFrom.remove(next);
				}
				continue;
			}
			if(distance >= PING_DEPTH || next.equals(endPosition)) {
				// Point that came out is too far away!
				System.out.println("Enough planning!");
				int successful = this.move_proc(next);
				if(successful >= PING_DEPTH) {
					PING_DEPTH++;
				} else {
					PING_DEPTH--;
				}
			}
			ArrayList<UncertainMapPoint> neighbors = this.getNeighbors(next);
			for(UncertainMapPoint neighborPoint : neighbors) {
				if(!closedSet.contains(neighborPoint)) {
					int tenative_score = (int) (next.getBestDist() + next.distanceSq(neighborPoint));
					if(!openSet.contains(neighborPoint)) {
						cameFrom.put(neighborPoint, next);
						neighborPoint.setBestDist(tenative_score);
						openSet.add(neighborPoint);
					} else if(openSet.contains(neighborPoint) && tenative_score < 
						neighborPoint.getBestDist()){
						openSet.remove(neighborPoint);
						neighborPoint.setBestDist(tenative_score);
						if(cameFrom.containsKey(neighborPoint)) {
							cameFrom.remove(neighborPoint);
						}
						cameFrom.put(neighborPoint, next);
						openSet.add(neighborPoint);
					}
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
				if(neighborPoint.equals(ump)) {
					continue;
				}
				if(neighborPoint.x < 0 || neighborPoint.y < 0 || neighborPoint.x > rows - 1 
						|| neighborPoint.y > cols - 1) {
					//System.out.println("Out of bounds");
					continue;
				}
				double distance = this.getPosition().distance(neighborPoint);
				boolean val = MapUtil.canMove(this.pingMap(neighborPoint));
				if(closedSet.contains(neighborPoint)) {
					neighborPoint = closedSet.get(closedSet.indexOf(neighborPoint));
					if(neighborPoint.setMoveable(val, (int) distance)) {
						// Found a closer value...
						if(val) {
							closedSet.remove(neighborPoint);
						}
					}
				} else {
					neighborPoint.setMoveable(val, (int) distance);
				}
				if(val) {
					neighbors.add(neighborPoint);
				}
			}
		}
		return neighbors;
	}
	
	private void generate_path(UncertainMapPoint ump) {
		for(UncertainMapPoint mp : path) {
			System.out.println(mp);
		}
		/*
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
		*/
	}
	
	private int move_proc(UncertainMapPoint target) {
		System.out.println("Move proc to" + target);
		// I LIKE TO MOVE IT MOVE IT
		int successful_moves = 0;
		LinkedList<UncertainMapPoint> temp_path = new LinkedList<UncertainMapPoint>();
		// Backtrack
		while(!target.equals(this.getPosition())) {
			temp_path.addFirst(target);
			target = cameFrom.get(target);
			System.out.println(target);
			if(target == null || temp_path.contains(target)) {
				if(target!=null) {
					cameFrom.remove(target);
					if(closedSet.contains(target)) {
						UncertainMapPoint doOver = closedSet.get(closedSet.indexOf(target));
						openSet.add(doOver);
					}else if(openSet.contains(target)) {
						
					}
				}
				return 0;
			}
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
				System.out.println(mp);
				// Robot Moved!
				System.out.println("successful move");
				path.add(mp);
				successful_moves++;
			} else {
				// Robot hit wall
				System.out.println("hit the wall");
				if(closedSet.contains(p)) {
					UncertainMapPoint moved = closedSet.get(closedSet.indexOf(p));
					if(moved.setMoveable(false, 0)) {
						// Found a closer value...
						ArrayList<UncertainMapPoint> neighbors = this.getNeighbors(moved);
						Collections.sort(neighbors, new Comparator<UncertainMapPoint>() {
							@Override
							public int compare(UncertainMapPoint o1,
									UncertainMapPoint o2) {
								return o1.getBestDist() - o2.getBestDist();
							}
						});
						for(UncertainMapPoint ump : cameFrom.keySet()) {
							if(ump.equals(moved)) {
								ump = neighbors.get(0);
							}
						}
					}
				}
				break;
			}
		}
		System.out.println("Stop moving");
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
