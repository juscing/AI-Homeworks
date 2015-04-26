
public interface LeafNode {
	
	public double calculateProbGivenTrue(int val);
	
	public double calculateProbGivenFalse(int val);
	
	public void addTrainingData(int data, boolean classification);
}
