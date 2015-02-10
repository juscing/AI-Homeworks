
public class ExpressionParser {
	
	public static void checkDefined(String[] vars) {
		
	}
	
	public static boolean evaluate(String s) {
		int lpar = s.indexOf("(");
		if(lpar >= 0) {
			// Found a parenthesis!
			int rpar = s.lastIndexOf(")");
		} else {
			// We have a big expression with no parenthesis...
			// ORDER OF OPERATIONS Not, And, Or
			int notPos = s.indexOf("!");
			int andPos = s.indexOf("&");
			int orPos = s.indexOf("|");
			
		}
		
		return false;
	}
	
}
