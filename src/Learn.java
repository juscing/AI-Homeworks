import java.util.ArrayList;


public class Learn {

	public static void learn() {
		boolean changed = true;
		while(changed) {
			changed = false;
			for(String var : Main.rules.keySet()) {
				if(!Main.facts_inferred.contains(var)) {
					if(evaluate(Main.rules.get(var))) {
						Main.facts_inferred.add(var);
						changed = true;
					}
				}
			}
		}
	}
	
	private static boolean evaluate(String s) {
		//System.out.println("Learn");
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
		boolean result = orProcess(s);
		return result;
	}
	
	private static boolean andProcess(String s) {
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
				} else if(Main.facts_known.contains(entry)) {
					continue;
				} else if(Main.facts_inferred.contains(entry)) {
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
				} else if(Main.facts_known.contains(entry)) {
					numTrue++;
				} else if(Main.facts_inferred.contains(entry)) {
					numTrue++;
				} else {
					continue;
				}
			}
		}
		if(numTrue == entries.size())
			return true;
		else
			return false;
	}
	
	private static boolean orProcess(String s) {
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
					if(evaluate(entry)){
						continue;
					} else {
						return true;
					}
				} else if(Main.facts_known.contains(entry)){					
					if(i < entries.size())
						continue;
					else
						return false;
				} else if(Main.facts_inferred.contains(entry)){					
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
					return true;
				} else if(Main.facts_inferred.contains(entry)){
					// Known Fact
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
