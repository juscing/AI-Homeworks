
public class Facts {
	String var;
	String state;
	boolean bool;
	
	public Facts(String a, String b, boolean c){
		var = a;
		state = b;
		bool = c;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public void setVal(boolean val) {
		this.bool = val;
	}
	
	public String getVar() {
		return var;
	}
	
	public boolean getVal() {
		return this.bool;
	}

	public String getString() {
		return state;
	}
	

}
