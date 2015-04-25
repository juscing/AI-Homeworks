import java.util.ArrayList;
import java.util.List;


public class Root {
	
	private List<Leaf> children;
	private double[] p;
	private int totalData;
	private int totalTrue;
	
	public Root() {
		this.children = new ArrayList<Leaf>();
		this.p = new double[2];
		this.totalData = 0;
		this.totalTrue = 0;
	}
	
	public void addTraining() {
		
	}
	
}
