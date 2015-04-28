import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Class;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class BayesClassifier extends Classifier{

	//the two options we are trying to categorize
	//the first item in census.names is outZero, the second is outOne, corresponding to the 
	//  decision functions output of either 0 or 1 
	static String outZero;
	static String outOne;
	//reads in the outline for the data from census.names
	static ArrayList<ArrayList<String>> setUp =  new ArrayList<ArrayList<String>>();
	
	static ArrayList<String[]> census = new ArrayList<String[]>();
	
//----------------------------------------------------------------------------------------------------
	
	public BayesClassifier(String namesFilepath) {
		super(namesFilepath);
		//census.names read into an arraylist of arryalist's
		//each line in census.names get its own arraylist
		readNames(namesFilepath);
		//use census.names data to create the bayesian network
		Root root = new Root(setUp.size());
		for(int i = 0; i<setUp.size(); i++){
			//get desired row from setUp
			ArrayList<String> row = setUp.get(i);
			if(row.get(1).equals("numeric")){
				root.addNode(i, 0);
			}
			else{
				int x = row.size()-1;
				root.addNode(i, x);
			}
		}

		
	}

	@Override
	public void train(String trainingDataFilpath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void makePredictions(String testDataFilepath) {
		// System.out.println("going to open file:");
		Scanner scanr = null;
		try {
			File name = new File(testDataFilepath);
			scanr = new Scanner(name);
		} 
		catch (FileNotFoundException e) {
			System.out.println("Sorry, that file can not be found.");
			System.exit(1);
		}
		
		String[] split;
		String line;
		
		//read in the rest of census.train
		System.out.println("going to read file:");
		while (scanr.hasNext()) {
			line = scanr.nextLine();
			if (line.length() != 0) {
				split = line.split("\\s+");
				int[] data = new int[setUp.size() - 1];
				for(int i = 0; i < data.length; i++) {
					// Lets find out if this is numeric
					try {
						data[i] = Integer.parseInt(split[i]);
					}catch(NumberFormatException e) {
						// its non-numeric
						int pos = this.setUp.get(i).indexOf(split[i]);
						if(pos != -1) {
							data[i] = pos;
						} else {
							System.out.println("There was an error finding the category.");
						}
					}
				}
				if(root.classify(data)) {
					System.out.println(outOne);
				} else {
					System.out.println(outZero);
				}
			}
		}
		scanr.close();
	}
	
//----------------------------------------------------------------------------------------------------
	
	public boolean readNames(String names){
		//This is to read in census.names and set up our table that will store the data
		//  from census.train
		//System.out.println("going to read file:");
		Scanner scanr = null;
		try {
			File name = new File(names);
			scanr = new Scanner(name);
		} 
		catch (FileNotFoundException e) {
			System.out.println("Sorry, that file can not be found.");
			return false;
		}
		String[] split;
		//setUp = new ArrayList<ArrayList<String>>();
		String line;
		//get first line, the category we are trying to match
		//place in variable names
		line = scanr.nextLine();
		split = line.split("\\s+");
		outZero = split[0];
		outOne = split[1];
		//read in the rest of census.names
		
		while (scanr.hasNext()) {
			line = scanr.nextLine();
			if (line.length() != 0) {
				split = line.split("\\s+");
				ArrayList<String> types = new ArrayList<>();
				int i = 0;
				while(i < split.length){
					String item = split[i];
					//System.out.print(item + " / ");
					//place each item in that lines array list
					types.add(item);
					i++;
				}
				//place each lines array list in setUp
				setUp.add(types);
				//System.out.print("\n");
			}
			
		}
		
		scanr.close();
		return true;
	}

	public boolean readData(String data){
		System.out.println("going to open file:");
		Scanner scanr = null;
		try {
			File name = new File(data);
			scanr = new Scanner(name);
		} 
		catch (FileNotFoundException e) {
			System.out.println("Sorry, that file can not be found.");
			return false;
		}
		
		
		//System.out.println(lineData.length);
		
		String[] split;
		String line;
		
		//read in the rest of census.train
		System.out.println("going to read file:");
		while (scanr.hasNext()) {
			line = scanr.nextLine();
			if (line.length() != 0) {
				split = line.split("\\s+");				
				census.add(split);
			}
			
		}
		scanr.close();
		return true;
	}
//----------------------------------------------------------------------------------------------------
	public static void main(String[] args){
		
		BayesClassifier hw = new BayesClassifier("trainingData/census.names");
		
		System.out.println(outZero +" "+ outOne);
		System.out.println();
		
		for(int i = 0; i<setUp.size(); i++){
			for(int j = 0; j<setUp.get(i).size(); j++){
				System.out.print(setUp.get(i).get(j) + " ");
			}
			System.out.print("\n");
		}
		System.out.println();
		
		hw.readData("trainingData/census.train.short");
		System.out.println("read in data");
		
		for(int i = 0; i<3; i++){
			String[] printArray = census.get(i);
			//System.out.println(i);
			for(int j = 0; j<setUp.size()+1; j++){
				System.out.print(printArray[j] + " ");
			}
			System.out.print("\n");
		}
			
	}

}
