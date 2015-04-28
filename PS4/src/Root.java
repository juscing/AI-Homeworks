import java.util.ArrayList;



public class Root {
	
	private ArrayList<LeafNode> children;
	private double[] p;
	private int totalData;
	private int totalTrue;
	
	public Root(ArrayList<String> nodeNames, ArrayList<Integer> numeric) {
		this.children = new ArrayList<LeafNode>(nodeNames.size());
		for(int i = 0; i < nodeNames.size(); i++) {
			if(numeric.get(i) == 0) {
				children.add(new NumericLeaf(nodeNames.get(i), 0, this));
			} else {
				children.add(new CategoryLeaf(nodeNames.get(i), numeric.get(i), this));
			}
		}
		this.p = new double[2];
		this.totalData = 0;
		this.totalTrue = 0;
	}
	
	public int getNumTrue() {
		return this.totalTrue;
	}
	
	public int getNumFalse() {
		return this.totalData - this.totalTrue;
	}
	
	public void addTrainingData(int[] data, boolean classification) {
		// Update the leaf
		this.totalData++;
		if(classification) {
			this.totalTrue++;
		}
		for(int i = 0; i < data.length - 1; i++) {
			children.get(i).addTrainingData(data[i], classification);
		}
	}
	
	public void calculateProbabilities() {
		this.p[0] = ((double) (this.getNumFalse()) / this.totalData);
		this.p[1] = ((double) (this.totalTrue) / this.totalData);
		System.out.println("Root probabilities:");
		System.out.println(">50K: " + this.p[1]);
		System.out.println("<=50K: " + this.p[0]);
		for(LeafNode child : this.children) {
			child.calculateProbabilities();
		}
	}
	
	public boolean classify(int[] data) {
		double trueP = 1;
		double falseP = 1;
		for(int i=0; i < data.length; i++) {
			//System.out.println("child: " + this.children[i]);
			//System.out.println("data: " + data[i]);
			double tval = this.children.get(i).calculateProbGivenTrue(data[i]);
			double fval = this.children.get(i).calculateProbGivenFalse(data[i]);
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
		//System.out.println("True " + trueP);
		//System.out.println("False " + falseP);
		return trueP > falseP;
	}
	
}
