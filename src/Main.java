import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class Main {

	static LinkedHashMap<String, String> rules = new LinkedHashMap<String, String>();
	static LinkedHashMap<String, String> defs = new LinkedHashMap<String, String>();
	static HashSet<String> facts_known = new HashSet<String>();
	static HashSet<String> facts_inferred = new HashSet<String>();
	static HashMap<String,Boolean> fact_cache = new HashMap<String,Boolean>();
	
	public static void main(String[] args) {
		Shell shell = new Shell();
		shell.run();
	}

}
