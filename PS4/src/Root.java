import java.util.ArrayList;
import java.util.List;


public class Root {
	
	private List<Leaf> children;
	private double[] p;
	private int totalData;
	private int totalTrue;
	private boolean valid;
	
	
	public Root(int numCols) {
		this.children = new ArrayList<Leaf>();
		this.p = new double[2];
		this.totalData = 0;
		this.totalTrue = 0;
		this.valid = false;
	}
	
	public void addNode(int pos, boolean numeric) {
		
	}
	
	public void addTrainingData(int column, int data, boolean classification) {
		if(!valid) {
			
		}
	}
	
}
