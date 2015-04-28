

public class Root {
	
	private LeafNode[] children;
	private double[] p;
	private int totalData;
	private int totalTrue;
	private boolean valid;
	
	
	public Root(int numCols) {
		this.children = new LeafNode[numCols];
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
			this.children[column] = new NumericLeaf(categories, this);
		}
	}
	
	public boolean addTrainingData(int column, int data, boolean classification) {
		if(!valid) { //valid is set to false in initializer
			for(LeafNode node : this.children) {
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
			System.out.println("Num >50K "+this.totalTrue++);
		}
		this.p[1] = ((double) this.totalTrue / this.totalData);
		this.p[0] = 1 - p[1];
		return true;
	}
	
	public boolean classify(int[] data) {
		int trueP = 1;
		int falseP = 1;
		for(int i=0; i < this.children.length; i++) {
			//System.out.println("child: " + this.children[i]);
			//System.out.println("data: " + data[i]);
			double tval = this.children[i].calculateProbGivenTrue(data[i]);
			double fval = this.children[i].calculateProbGivenFalse(data[i]);
			//System.out.println("True " + tval);
			//System.out.println("False " + fval);
			if(tval > 0) {
				trueP *= tval;
			}
			if(fval > 0) {
				falseP *= fval;
			}
		}
		trueP *= this.p[1];
		falseP *= this.p[0];
		
		return trueP > falseP;
	}
	
}
