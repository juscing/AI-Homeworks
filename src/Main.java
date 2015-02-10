import java.util.LinkedHashMap;

public class Main {

	static LinkedHashMap<String,Facts> facts_known = new LinkedHashMap<String, Facts>();
	static LinkedHashMap<String,Facts> facts_inferred = new LinkedHashMap<String, Facts>();
	static LinkedHashMap<String,Facts> rules = new LinkedHashMap<String, Facts>();
	
	public static void main(String[] args) {
		Shell shell = new Shell();
		shell.run();
	}

}
