import java.util.ArrayList;


public class NumericLeaf implements LeafNode {
	private double meanTrue;
	private double meanFalse;
	private double stderrTrue;
	private double stderrFalse;
	private ArrayList<Integer> truedata;
	private ArrayList<Integer> falsedata;
	Root parent;
	
	public NumericLeaf(int length, Root parent) {
		//length is the number of categories in the header
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
		// calculate mean
		if(classification) {
			int runningTotal = 0;
			for(int number : this.truedata) {
				runningTotal += number;
			}
			this.meanTrue = runningTotal / this.truedata.size();
		} else {
			int runningTotal = 0;
			for(int number : this.falsedata) {
				runningTotal += number;
			}
			this.meanFalse = runningTotal / this.falsedata.size();
		}
		// calculate stderr
		if(classification) {
			double runningTotal = 0;
			for(int number : this.truedata) {
				runningTotal += (number - this.meanTrue) * (number - this.meanTrue);
			}
			this.stderrTrue = runningTotal / this.truedata.size();
		} else {
			double runningTotal = 0;
			for(int number : this.falsedata) {
				runningTotal += (number - this.meanFalse) * (number - this.meanFalse);
			}
			this.stderrFalse = runningTotal / this.falsedata.size();
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
}
