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
				
				if(command[1].contains("=")) {
					//fact
					String[] parts = command[1].split("=");
					String var = parts[0];
					var.trim();
					String state = parts[1];
					state.trim();
					if(state.contains("\""))
						Teach.teach_string(var, state);
					else{
						if(state.equals("false"))
							Teach.teach_bool(var, false);
						else
							Teach.teach_bool(var, true);
					}
				} else {
					//rule
					//can be complex, just pass the whole thing
					Teach.teach_exp(command[1]);
				}
				
				
				
				break;
			case "LIST":
				
				break;
			case "LEARN":
			
				break;
			case "QUERY":
			
				break;
			case "WHY":
				
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
