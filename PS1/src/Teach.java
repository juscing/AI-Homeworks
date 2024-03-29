
public class Teach {
	
	public static void teach_string(String var, String a){
		if(!Main.defs.containsKey(var)) {
			if(a.charAt(0) == '"') {
				a = a.substring(1);
			}
			if(a.charAt(a.length() - 1) == '"') {
				a = a.substring(0,a.length() - 1);
			}
			Main.defs.put(var, a);
		}
	}
	
	public static void teach_bool(String var, boolean bool){
		if(Main.defs.containsKey(var)) {
			if(!Main.rules.containsKey(var)) {
				if(bool){
					Main.facts_known.add(var);
				} else {
					Main.facts_known.remove(var);
				}
			}
		}
	}
	
	public static void teach_exp(String rule, String var){
		if(ExpressionParser.checkDefined(rule)) {
			Main.facts_known.remove(var);
			Main.rules.put(var, rule);
		}
	}

}
