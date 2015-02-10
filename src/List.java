
public class List {
	
	public static void list() {
		StringBuilder s = new StringBuilder();
		s.append("Variables:\n");
		for(String key: Main.facts_known.keySet()) {
			s.append("\t" + key + " = " + Main.facts_known.get(key).getString() + "\n");
		}
		s.append("\n");
		s.append("Facts:\n");
		for(String key: Main.facts_known.keySet()) {
			Facts f = Main.facts_known.get(key);
			if(f.getVal()) {
				s.append("\t" + key + "\n");
			}
		}
		s.append("\n");
		s.append("Rules:\n");
		for(String key: Main.rules.keySet()) {
			Facts f = Main.rules.get(key);			
			s.append("\t" + f.getString() + " -> " + key +"\n");
		}
		System.out.println(s.toString());
	}
	
}
