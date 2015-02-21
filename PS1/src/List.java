
public class List {
	
	public static void list() {
		StringBuilder s = new StringBuilder();
		s.append("Variables:\n");
		for(String key: Main.defs.keySet()) {
			s.append("\t" + key + " = " + Main.defs.get(key) + "\n");
		}
		s.append("\n");
		s.append("Facts:\n");
		// Fix the way this prints to use the original ordering!
		for(String var: Main.defs.keySet()) {
			if(Main.facts_known.contains(var) || Main.facts_inferred.contains(var)) {
				s.append("\t" + var + "\n");
			}
		}
		s.append("\n");
		s.append("Rules:\n");
		for(String key: Main.rules.keySet()) {
			s.append("\t" + Main.rules.get(key) + " -> " + key +"\n");
		}
		System.out.println(s.toString());
	}
	
}
