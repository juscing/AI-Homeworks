
public class Leaf implements LeafNode {

	private double[][] p;
	private int[][] rawData;
	Root parent;
	
	public Leaf(int length, Root parent) {
		this.p = new double[2][length];
		this.rawData = new int[2][length];
		this.parent = parent;
	}
	
	@Override
	public void addTrainingData(int data, boolean classification) {
		// update raw count
		if(classification) {
			this.rawData[1][data]++;
		} else {
			this.rawData[0][data]++;
		}
		// calculate probabilities
		for(int i = 0; i < p.length; i++) {
			for(int j = 0; j < p[i].length; j++) {
				if(i == 0) {
					try{
						this.p[i][j] = this.rawData[i][j] / this.parent.getNumFalse();
					} catch(ArithmeticException e) {
						this.p[i][j] = 0;
					}
				} else {
					try{
						this.p[i][j] = this.rawData[i][j] / this.parent.getNumTrue();
					} catch(ArithmeticException e) {
						this.p[i][j] = 0;
					}
				}
				 
			}
		}
	}
	
	@Override
	public double calculateProbGivenTrue(int val) {
		return this.p[1][val];
	}
	
	@Override
	public double calculateProbGivenFalse(int val) {
		return this.p[0][val];
	}
}
