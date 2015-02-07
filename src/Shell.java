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
