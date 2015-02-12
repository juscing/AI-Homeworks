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
		//go through string and split on OR
		
		boolean result;
		result = orProcess(s);
		//System.out.println(result);
		return result;
		
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
		return true;
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
		
		for(String str : entries){
			System.out.println(str);
		}
		
		for(int i = 0; i<entries.size(); i++) {
			String entry = entries.get(i);
			if(entry.charAt(0) == '!') {
				entry = entry.substring(1);
				if(entry.contains("(")) {
					if(!evaluate(entry)){
						return true;
					}
				}
				else{
					if(Main.facts_known.contains(entry)){
						if(i < entries.size())
							continue;
						else
							return false;
					}
					else
						return true;
				}
			} else {
				if(Main.facts_known.contains(entry)){
					return true;
				}
				else{
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
