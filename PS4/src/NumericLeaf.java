import java.util.ArrayList;


public class NumericLeaf extends LeafNode {
	private double meanTrue;
	private double meanFalse;
	private double stderrTrue;
	private double stderrFalse;
	private ArrayList<Integer> truedata;
	private ArrayList<Integer> falsedata;
	Root parent;
	
	public NumericLeaf(String name, int length, Root parent) {
		super(name, parent);
		this.meanTrue = 0;
		this.meanFalse = 0;
		this.stderrTrue = 0;
		this.stderrFalse = 0;
		this.parent = parent;
		
		this.truedata = new ArrayList<Integer>();
		this.falsedata = new ArrayList<Integer>();
	}
	
	@Override
	public void addTrainingData(int data, boolean classification) {
		// update raw count
		if(classification) {
			this.truedata.add(data);
		} else {
			this.falsedata.add(data);
		}
		
	}

	@Override
	public double calculateProbGivenTrue(int val) {
		return Math.exp( - ( (val - this.meanTrue) * (val - this.meanTrue) / (2 * this.stderrTrue) ) )
				/ Math.sqrt(2 * this.parent.getNumTrue() * this.stderrTrue);
	}

	@Override
	public double calculateProbGivenFalse(int val) {
		return Math.exp( - ( (val - this.meanFalse) * (val - this.meanFalse) / (2 * this.stderrFalse) ) )
				/ Math.sqrt(2 * this.parent.getNumTrue() * this.stderrFalse);
	}

	@Override
	public void calculateProbabilities() {
		// calculate mean
		int runningTotal = 0;
		for(int number : this.truedata) {
			runningTotal += number;
		}
		this.meanTrue = runningTotal / this.truedata.size();
		runningTotal = 0;
		for(int number : this.falsedata) {
			runningTotal += number;
		}
		this.meanFalse = runningTotal / this.falsedata.size();
		
		// calculate stderr
		double runningdubTotal = 0;
		for(int number : this.truedata) {
			runningdubTotal += (number - this.meanTrue) * (number - this.meanTrue);
		}
		this.stderrTrue = runningdubTotal / this.truedata.size();
		runningdubTotal = 0;
		for(int number : this.falsedata) {
			runningdubTotal += (number - this.meanFalse) * (number - this.meanFalse);
		}
		this.stderrFalse = runningdubTotal / this.falsedata.size();
	}
	
	@Override
	public String toString() {
		return "Numeric node: " + this.getname();
	}
}
