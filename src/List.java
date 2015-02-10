
public class List {
	
	public static void list() {
		StringBuilder s = new StringBuilder();
		s.append("Variables:\n");
		for(String key: Main.defs.keySet()) {
			s.append("\t" + key + " = " + Main.defs.get(key) + "\n");
		}
		s.append("\n");
		s.append("Facts:\n");
		for(String var: Main.facts_known) {
			s.append("\t" + var + "\n");
		}
		s.append("\n");
		s.append("Rules:\n");
		for(String key: Main.rules.keySet()) {
			s.append("\t" + Main.rules.get(key) + " -> " + key +"\n");
		}
		System.out.println(s.toString());
	}
	
}
