
public class Teach {
	
	public static void teach_string(String var, String a){
		if(!Main.defs.containsKey(var)) {
			Main.defs.put(var, a);
		}
	}
	
	public static void teach_bool(String var, boolean bool){
		if(Main.defs.containsKey(var)) {
			if(bool){
				Main.facts_known.add(var);
			} else {
				Main.facts_known.remove(var);
			}
		}
	}
	
	public static void teach_exp(String rule, String var){
		if(ExpressionParser.checkDefined(rule)) {
			Main.rules.put(var, rule);
		}
	}

}
