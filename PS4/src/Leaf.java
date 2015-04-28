
public class Leaf implements LeafNode {

	private double[][] p;
	private int[][] rawData;
	Root parent;
	
	public Leaf(int length, Root parent) {
		//length is the number of categories in that header (column)
		this.p = new double[2][length];
		this.rawData = new int[2][length];
		this.parent = parent;
	}
	
	@Override
	public void addTrainingData(int data, boolean classification) {
		// update raw count
		//data is the single item, is the index of the category in the header
		if(classification) {
			this.rawData[1][data]++;
		} else {
			this.rawData[0][data]++;
		}
		//System.out.println("RAW DATA "+this.rawData[0][data] +" "+this.rawData[1][data]);
		// calculate probabilities
		for(int i = 0; i < p.length; i++) {
			for(int j = 0; j < p[i].length; j++) {
				if(i == 0) {
					if(this.parent.getNumFalse() > 0) {
						this.p[i][j] = this.rawData[i][j] / this.parent.getNumFalse();
						System.out.println(this.p[i][j]);
					} else {
						// System.out.println("ZERO");
						this.p[i][j] = 0;
					}
				} else {
					if(this.parent.getNumTrue() > 0) {
						this.p[i][j] = this.rawData[i][j] / this.parent.getNumTrue();
						System.out.println(this.p[i][j]);
					} else {
						// System.out.println("ZERO");
						this.p[i][j] = 0;
					}
				}
				 
			}
		}
	}
	
	@Override
	public double calculateProbGivenTrue(int val) {
		//System.out.println(this.p[1][val]);
		return this.p[1][val];
	}
	
	@Override
	public double calculateProbGivenFalse(int val) {
		//System.out.println(this.p[0][val]);
		return this.p[0][val];
	}
}
