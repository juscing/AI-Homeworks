import java.util.Scanner;


public class Shell {
	Scanner input;
	boolean end;
	
	public Shell() {
		input = new Scanner(System.in);
		end = false;
	}
	
	public void run() {
		while(!end) {
			String[] command = getNextCommand();
			switch(command[0].toUpperCase()){
			case "TEACH":
				if(!Main.fact_cache.isEmpty()) {
					Main.fact_cache.clear();
				}
				// Not sure on this one, check piazza
				if(!Main.facts_inferred.isEmpty()) {
					Main.facts_inferred.clear();
				}
				if(command[1].contains("=")) {
					//fact
					String[] parts = command[1].split("=");
					String var = parts[0];
					// because java strings
					var = var.trim();
					String state = parts[1];
					// because java strings
					state = state.trim();
					if(state.contains("\""))
						Teach.teach_string(var, state);
					else{
						if(state.toLowerCase().equals("false"))
							Teach.teach_bool(var, false);
						else
							Teach.teach_bool(var, true);
					}
				} else {
					//rule
					String[] express = command[1].split("->");
					Teach.teach_exp(express[0].trim(), express[1].trim());
				}
				break;
			case "LIST":
				List.list();
				break;
			case "LEARN":
				Learn.learn();
				break;
			case "QUERY":
				System.out.println(ExpressionParser.evaluate(command[1].trim()));
				break;
			case "WHY":
				System.out.println(ExpressionParser.why(command[1].trim()));
				break;
			default:
				System.out.println("Unsupported Command");
			}
		}
	}
	
	private String[] getNextCommand() {
		String line = input.nextLine();
		line = line.trim().replaceAll("\\s+", " ");
		int spacePos = line.indexOf(" ");
		String command;
		String expression;
		if(spacePos != -1) {
			command = line.substring(0, spacePos);
			expression = line.substring(spacePos);
		} else {
			command = line;
			expression = "";
		}
		String[] comm = {command,expression};
		return comm;
	}
}
