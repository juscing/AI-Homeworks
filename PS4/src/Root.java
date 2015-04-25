

public class Root {
	
	private Leaf[] children;
	private double[] p;
	private int totalData;
	private int totalTrue;
	private boolean valid;
	
	
	public Root(int numCols) {
		this.children = new Leaf[numCols];
		this.p = new double[2];
		this.totalData = 0;
		this.totalTrue = 0;
		this.valid = false;
	}
	
	public int getNumTrue() {
		return this.totalTrue;
	}
	
	public int getNumFalse() {
		return this.totalData - this.totalTrue;
	}
	
	public void addNode(int column, int categories) {
		if(categories != 0) {
			this.children[column] = new Leaf(categories, this);
		} else {
			// make numeric node...
		}
	}
	
	public boolean addTrainingData(int column, int data, boolean classification) {
		if(!valid) {
			for(Leaf node : this.children) {
				if(node == null) {
					// You didn't set up all the nodes properly before doing this
					return false;
				}
			}
			this.valid = true;
		}
		// Update the leaf
		this.children[column].addTrainingData(data, classification);
		// Add to total number of trainings
		this.totalData++;
		// Recalculate the probabilities
		if(classification) {
			this.totalTrue++;
		}
		this.p[1] = ((double) this.totalTrue / this.totalData);
		this.p[0] = 1 - p[1];
		return true;
	}
	
	public boolean classify(int[] data) {
		return false;
	}
	
}
