import java.util.ArrayList;


public class ExpressionParser {
	
	private static final String falseFact = "I KNOW IT IS NOT TRUE THAT ";
	private static final String trueFact = "I KNOW IT IS TRUE THAT ";
	private static final String falseRulep1 = "BECAUSE IT IS NOT TRUE THAT ";
	private static final String falseRulep2 = " I CANNOT PROVE ";
	private static final String trueRulep1 = "BECAUSE ";
	private static final String trueRulep2 = " I KNOW THAT ";
	private static final String falseExpression = "THUS I CANNOT PROVE ";
	private static final String trueExpression = "THUS I CAN PROVE ";
	
	private static String why = "";
	
	public static boolean checkDefined(String[] vars) {
		for(String var : vars) {
			if(!Main.defs.containsKey(var)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean checkDefined(String expr) {
		expr = expr.replaceAll("[&!|()]", " ");
		expr = expr.trim().replaceAll("\\s+", " ");
		String[] vars = expr.split(" ");
		return checkDefined(vars);
	}
	
	public static String why(String s) {
		why = "";
		if(evaluate(s,true)) {
			why = "true\n" + why;
			why += trueExpression;
		} else {
			why = "false\n" + why;
			why += falseExpression;
		}
		why += Main.defs.get(s) +"\n";;
		
		return why;
		
	}
	
	public static boolean evaluate(String s) {
		return evaluate(s, false);
	}
	
	private static boolean evaluate(String s, boolean whyFlag) {
		//System.out.println("EVALUATE");
		//System.out.println(s);
		//go through string and split on OR
		int openCount = 0;
		int pos = 0;
		while(pos < s.length()) {
			if(s.charAt(pos) == '('){
				openCount++;
			}else if(s.charAt(pos) == ')') {
				if(pos == s.length() - 1 && openCount > 0 && s.charAt(0) == '(') {
					// Found enclosing paren!
					return evaluate(s.substring(1, s.length() - 1));
				}
				openCount--;
			}
			
			pos++;
		}
		boolean result = orProcess(s, whyFlag);
		
		return result;
	}
	
	private static boolean andProcess(String s, boolean whyFlag) {
		//System.out.println("AND PROCESS");
		ArrayList<String> entries = new ArrayList<String>();
		int rcount = 0;
		int lcount = 0;
		int pos = 0;
		int lastPos = 0;
		while(pos < s.length()) {
			if(s.charAt(pos) == '('){
				lcount++;
			} else if(s.charAt(pos) == ')') {
				rcount++;
			}
			if(lcount == rcount) {
				if(s.charAt(pos) == '&') {
					entries.add(s.substring(lastPos,pos));
					lastPos = pos+1;
				} else if(pos == s.length()-1) {
					entries.add(s.substring(lastPos,pos+1));
				}
			}
			pos++;
		}
		
		/*
		for(String str : entries)
			System.out.println(str);
		*/
		
		int numTrue = 0;
		for(int i = 0; i<entries.size(); i++) {
			String entry = entries.get(i);
			if(entry.charAt(0) == '!') { //entry starts with !
				entry = entry.substring(1);
				
				//somehow deal with parenthesis, should probably start by removing
				//them before calling evaluate on the substring
				
				//also, this probably should probably be handled first
				
				if(entry.contains("(")) {
					if(evaluate(entry, whyFlag)){
						continue;
					} else {
						numTrue++;
					}
				} else if(Main.fact_cache.containsKey(entry) && !whyFlag) {
					// Cache hit!
					if(Main.fact_cache.get(entry)) {
						continue;
					} else {
						numTrue++;
					}
				} else if(Main.facts_known.contains(entry)) {
					if(whyFlag) {
						why += falseFact + "!" + Main.defs.get(entry) + "\n";
					}
					continue;
				} else if(Main.rules.containsKey(entry)) {
					// BACKWARD CHAIN
					if(evaluate(Main.rules.get(entry), whyFlag)) {
						why += falseRulep1 + "!" + Main.rules.get(entry) + falseRulep2 + Main.defs.get(entry) + "\n";
						Main.fact_cache.put(entry, false);
						continue;
					} else {
						why += trueRulep1 + "!" + Main.rules.get(entry) + trueRulep2 + Main.defs.get(entry) + "\n";
						Main.fact_cache.put(entry, true);
						numTrue++;
					}
				} else{
					if(whyFlag) {
						why += falseFact + "!" + Main.defs.get(entry) + "\n";
					}
					numTrue++;
				}
			} else { //entry does not start with !
				if(entry.contains("(")) {
					if(evaluate(entry, whyFlag)){
						numTrue++;
					} else {
						continue;
					}
				} else if(Main.facts_known.contains(entry)) {
					if(whyFlag) {
						System.out.println("true fact");
						why += trueFact + Main.defs.get(entry) + "\n";
					}
					numTrue++;
				} else if(Main.fact_cache.containsKey(entry) && !whyFlag) {
					// Cache hit!
					if(Main.fact_cache.get(entry)) {
						numTrue++;
					} else {
						continue;
					}
				} else if(Main.rules.containsKey(entry)) {
					// BACKWARD CHAIN
					if(evaluate(Main.rules.get(entry), whyFlag)) {
						why += trueRulep1 + Main.rules.get(entry) + trueRulep2 + Main.defs.get(entry) + "\n";
						Main.fact_cache.put(entry, true);
						numTrue++;
					} else {
						why += falseRulep1 + Main.rules.get(entry) + falseRulep2 + Main.defs.get(entry) + "\n";
						Main.fact_cache.put(entry, false);
						continue;
					}
				}
				else {
					if(whyFlag) {
						why += falseFact + Main.defs.get(entry) + "\n";
					}
					continue;
				}
			}
		}
		if(numTrue == entries.size())
			return true;
		else
			return false;
	}
	
	private static boolean orProcess(String s, boolean whyFlag) {
		// System.out.println("OR PROCESS");
		ArrayList<String> entries = new ArrayList<String>();
		int rcount = 0;
		int lcount = 0;
		int pos = 0;
		int lastPos = 0;
		while(pos < s.length()) {
			if(s.charAt(pos) == '('){
				lcount++;
			} else if(s.charAt(pos) == ')') {
				rcount++;
			}
			if(lcount == rcount) {
				if(s.charAt(pos) == '|') {
					entries.add(s.substring(lastPos,pos));
					lastPos = pos+1;
				} else if(pos == s.length()-1) {
					entries.add(s.substring(lastPos,pos+1));
				}
			}
			pos++;
		}
		
		/*
		for(String str : entries)
			System.out.println(str);
		*/
		
		for(int i = 0; i<entries.size(); i++) {
			String entry = entries.get(i);
			if(entry.charAt(0) == '!' && !entry.contains("&")) { //entry starts with !
				entry = entry.substring(1);
				//something along these longs to handle &
				
				if(entry.contains("(")) {
					if(evaluate(entry, whyFlag)){
						continue;
					} else {
						return true;
					}
				} else if(Main.facts_known.contains(entry)){					
					if(i < entries.size()) {
						if(whyFlag) {
							why += falseFact + "!" + Main.defs.get(entry) + "\n";
						}
						continue;
					} else {
						return false;
					}
						
				} else if(Main.fact_cache.containsKey(entry) && !whyFlag) {
					// Cache hit!
					if(Main.fact_cache.get(entry)) {
						if(i < entries.size())
							continue;
						else
							return false;
					} else {
						return true;
					}
				} else if(Main.rules.containsKey(entry)) {
					// BACKWARD CHAIN
					if(evaluate(Main.rules.get(entry), whyFlag)) {
						why += falseRulep1 + "!" + Main.rules.get(entry) + falseRulep2 + Main.defs.get(entry) + "\n";
						Main.fact_cache.put(entry, true);
						if(i < entries.size())
							continue;
						else
							return false;
					} else {
						why += trueRulep1 + "!" + Main.rules.get(entry) + trueRulep2 + Main.defs.get(entry) + "\n";
						Main.fact_cache.put(entry, false);
					}
				} else {
					if(whyFlag) {
						why += trueFact + "!" + Main.defs.get(entry) + "\n";
					}
					return true;
				}
			} else { //entry does not start with !
				if(entry.contains("(") && !entry.contains("&")) {
					if(evaluate(entry, whyFlag)){
						return true;
					} else {
						continue;
					}
				} else if(entry.contains("&")){
					if(andProcess(entry, whyFlag)) {
						//System.out.println("and process was true");
						return true;
					} else {
						//System.out.println("and process was FALSE");
						if(i < entries.size()) {
							//System.out.println("more entries");
							continue;
						} else {
							//System.out.println("since last entry, return false");
							return false;
						}
							
					}
				} else if(Main.facts_known.contains(entry)){
					// Known Fact
					if(whyFlag) {
						why += trueFact + Main.defs.get(entry) + "\n";
					}
					return true;
				} else if(Main.fact_cache.containsKey(entry) && !whyFlag) {
					// Cache hit!
					if(Main.fact_cache.get(entry)) {
						return true;
					}
				} else if(Main.rules.containsKey(entry)) {
					// BACKWARD CHAIN
					if(evaluate(Main.rules.get(entry), whyFlag)) {
						why += trueRulep1 + Main.rules.get(entry) + trueRulep2 + Main.defs.get(entry) + "\n";
						Main.fact_cache.put(entry, true);
						return true;
					} else {
						why += falseRulep1 + Main.rules.get(entry) + falseRulep2 + Main.defs.get(entry) + "\n";
						Main.fact_cache.put(entry, false);
					}
				} else {
					if(whyFlag) {
						why += falseFact + Main.defs.get(entry) + "\n";
					}
					if(i < entries.size())
						continue;
					else
						return false;
				}
			}
		}
		return false;
	}
	
}
