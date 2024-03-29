
public abstract class LeafNode {
	
	private String name;
	protected Root parent;
	
	public LeafNode(String categoryName, Root parent) {
		this.name = categoryName;
		this.parent = parent;
	}
	
	public String getname() {
		return name;
	}
	
	public abstract double calculateProbGivenTrue(int val);
	
	public abstract double calculateProbGivenFalse(int val);
	
	public abstract void addTrainingData(int data, boolean classification);
	
	public abstract void calculateProbabilities();
}
