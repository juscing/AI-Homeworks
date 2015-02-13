import java.util.ArrayList;


public class ExpressionParser {
	
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
	
	public static boolean evaluate(String s) {
		System.out.println(s);
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
		
		return orProcess(s);
		
		/*
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
		

		for(String str : entries){
			System.out.println(str);
		}
		
		for(String entry : entries) {
			if(entry.charAt(0) == '!') {
				entry = entry.substring(1);
				if(entry.contains("|")){
					if(orProcess(entry)) {
						return false;
					}
				}
				if(entry.contains("(")) {
					if(evaluate(entry)){
						return false;
					}
				}
			} else {
				System.out.println("No !");
				if(entry.contains("|")){
					if(!orProcess(entry)) {
						return false;
					}
				} else if(!Main.facts_known.contains(entry)){
					System.out.println("didn't find " + entry);
					return false;
				}
			}
		}
		*/
		
		
		//return evaluate(s.substring(1,s.length()-1));
		
		/*
		
		ArrayList<String> vars = new ArrayList<String>();
		ArrayList<char>
		
		
		int lpar = s.indexOf("(");
		boolean result;
		if(lpar >= 0) {
			// Found a parenthesis!
			int lcount = 1;
			int rcount = 0;
			int pos = lpar + 1;
			while(pos < s.length()) {
				if(s.charAt(pos) == '('){
					lcount++;
				} else if(s.charAt(pos) == ')') {
					rcount++;
					if(lcount == rcount) {
						//Found the matching paren!
						//RECURSE!
						result = evaluate(s.substring(lpar + 1,pos));
						break;
					}
				}
				pos++;
			}
		} else {
			// ORDER OF OPERATIONS Not, And, Or
			int notPos = s.indexOf("!");
			int andPos = s.indexOf("&");
			int orPos = s.indexOf("|");
			
		}
		*/
		//return true;
	}
	
	private static boolean andProcess(String s) {
		System.out.println("AND PROCESS");
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
		

		for(String str : entries)
			System.out.println(str);
		
		int numTrue = 0;
		for(int i = 0; i<entries.size(); i++) {
			String entry = entries.get(i);
			if(entry.charAt(0) == '!') { //entry starts with !
				entry = entry.substring(1);
				
				//somehow deal with parenthesis, should probably start by removing
				//them before calling evaluate on the substring
				
				//also, this probably should probably be handled first
				
				if(entry.contains("(")) {
					if(evaluate(entry)){
						continue;
					} else {
						numTrue++;
					}
				//BACKWARD CHAIN
				} else if(Main.facts_known.contains(entry)) {
					continue;
				} else{
					numTrue++;
				}
			} else { //entry does not start with !
				if(entry.contains("(")) {
					if(evaluate(entry)){
						numTrue++;
					} else {
						continue;
					}
				// BACKWARD CHAIN
				}else if(Main.facts_known.contains(entry))
					numTrue++;
				else
					continue;
			}
		}
		if(numTrue == entries.size())
			return true;
		else
			return false;
	}
	
	private static boolean orProcess(String s) {
		System.out.println("OR PROCESS");
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
		
		for(String str : entries)
			System.out.println(str);
		
		for(int i = 0; i<entries.size(); i++) {
			String entry = entries.get(i);
			if(entry.charAt(0) == '!' && !entry.contains("&")) { //entry starts with !
				entry = entry.substring(1);
				//something along these longs to handle &
				
				if(entry.contains("(")) {
					if(evaluate(entry)){
						continue;
					} else {
						return true;
					}
				// BACKWARD CHAIN
				} else if(Main.facts_known.contains(entry)){					
					if(i < entries.size())
						continue;
					else
						return false;
				} else {
						return true;
				}
			} else { //entry does not start with !
				if(entry.contains("(") && !entry.contains("&")) {
					if(evaluate(entry)){
						return true;
					} else {
						continue;
					}
				} else if(entry.contains("&")){
					if(andProcess(entry)) {
						System.out.println("and process was true");
						return true;
					} else {
						System.out.println("and process was FALSE");
						if(i < entries.size()) {
							System.out.println("more entries");
							continue;
						} else {
							System.out.println("since last entry, return false");
							return false;
						}
							
					}
				// BACKWARD CHAIN
				} else if(Main.facts_known.contains(entry)){					
					return true;
				} else {
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
