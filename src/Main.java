import java.util.HashSet;
import java.util.LinkedHashMap;

public class Main {

	static LinkedHashMap<String, String> rules = new LinkedHashMap<String, String>();
	static LinkedHashMap<String, String> defs = new LinkedHashMap<String, String>();
	static HashSet<String> facts_known = new HashSet<String>();
	static HashSet<String> facts_inferred = new HashSet<String>();
	
	public static void main(String[] args) {
		Shell shell = new Shell();
		shell.run();
	}

}
