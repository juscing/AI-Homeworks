
public class Teach {
	
	public static void teach_string(String var, String a){
		Facts f = Main.facts_known.get(var);
		if(f != null) {
			f.setState(a);
		} else {
			Main.facts_known.put(var, new Facts(var, a, false));
		}
	}
	
	public static void teach_bool(String var, boolean bool){
		Facts f = Main.facts_known.get(var);
		if(f != null) {
			f.setVal(bool);
		} else {
			Main.facts_known.put(var, new Facts(var, "", bool));
		}
	}
	
	public static void teach_exp(String rule, String var){
		Facts f = new Facts(var, rule, false);
		Main.rules.put(var, f);
	}

}
