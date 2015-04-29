import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class SmoothingBayesClassifier extends Classifier{

	//the two options we are trying to categorize
	//the first item in census.names is outZero, the second is outOne, corresponding to the 
	//  decision functions output of either 0 or 1 
	String outZero;
	String outOne;
	//reads in the outline for the data from census.names
	ArrayList<ArrayList<String>> names;
	SmoothRoot root;
	
//----------------------------------------------------------------------------------------------------
	
	public SmoothingBayesClassifier(String namesFilepath) {
		super(namesFilepath);
		//census.names read into an arraylist of arryalist's
		//each line in census.names get its own arraylist
		names = new ArrayList<ArrayList<String>>();
		readNames(namesFilepath);
		
		//use census.names data to create the bayesian network
		ArrayList<Integer> numeric = new ArrayList<Integer>(names.size());
		ArrayList<String> nodeNames = new ArrayList<String>(names.size());
		for(ArrayList<String> category : names) {
			if(category.get(category.size() - 1).equals("numeric")) {
				numeric.add(0);
			} else {
				numeric.add(category.size() - 1);
			}
			nodeNames.add(category.get(0));
		}
		
		root = new SmoothRoot(nodeNames, numeric);
		
	}

	@Override
	public void train(String trainingDataFilpath) {
		Scanner scanr = null;
		try {
			File name = new File(trainingDataFilpath);
			scanr = new Scanner(name);
		} 
		catch (FileNotFoundException e) {
			System.out.println("Sorry, that file can not be found. Exiting.");
			System.exit(1);
		}
		
		String[] split;
		String line;
		//read in the rest of census.train
		//System.out.println("going to read file:");
		while (scanr.hasNext()) {
			line = scanr.nextLine();
			split = line.split("\\s+");
			int[] data = new int[split.length];
			for(int i=0; i<split.length - 1; i++){
				//convert string data to int
				String item = split[i];
				int itemNum = -1;
				try{
					itemNum = Integer.parseInt(item);
				}
				catch (NumberFormatException e){}
				if (itemNum == -1){
					//need to do string matching
					itemNum = names.get(i).indexOf(split[i]) - 1;
				}
				data[i] = itemNum;
				//i is the column, itemNum is the data value, classification is >/=<50K
			}
			if(split[split.length - 1].equals(">50K")) {
				root.addTrainingData(data, true);
			} else {
				root.addTrainingData(data, false);
			}
			//System.out.print("\n");
		}
		scanr.close();
		
		root.calculateProbabilities();
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
		//System.out.println("going to read file:");
		while (scanr.hasNext()) {
			line = scanr.nextLine();
			if (line.length() != 0) {
				split = line.split("\\s+");
				int[] data = new int[split.length];
				for(int i = 0; i < data.length; i++) {
					// Lets find out if this is numeric
					try {
						data[i] = Integer.parseInt(split[i]);
					}catch(NumberFormatException e) {
						// its non-numeric
						int pos = this.names.get(i).indexOf(split[i]) - 1;
						if(pos != -1) {
							data[i] = pos;
						} else {
							System.out.println("There was an error finding the category.");
						}
					}
				}
				/*
				for(int i : data) {
					System.out.print(i + " ");
				}
				System.out.println();
				*/
				if(root.classify(data)) {
					System.out.println(outOne);
				} else {
					System.out.println(outZero);
				}
			}
		}
		scanr.close();
	}
	
	public void readNames(String namesfile){
		/*
		 * This is to read in census.names and set up our table that will store the data
		 * from census.train
		 */
		Scanner scanr = null;
		try {
			File name = new File(namesfile);
			scanr = new Scanner(name);
		} 
		catch (FileNotFoundException e) {
			System.out.println("Sorry, that file can not be found. Exiting.");
			System.exit(1);
		}
		String[] split;
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
				for(String word : split) {
					types.add(word);
				}
				//place each lines array list in setUp
				this.names.add(types);
			}
			
		}
		
		scanr.close();
		// Print checking
		/*
		for(ArrayList<String> category : setUp) {
			System.out.println(category);
		}
		*/
	}

	
//----------------------------------------------------------------------------------------------------
	public static void main(String[] args){
		
		String trainingFile;
		String testFile;
		
		if(args.length > 1) {
			trainingFile = args[0];
			testFile = args[1];
		} else {
			trainingFile = "trainingData/census.train.short";
			testFile = "trainingData/census.test.short";
		}
		SmoothingBayesClassifier hw = new SmoothingBayesClassifier("trainingData/census.names");

		hw.train(trainingFile);

		hw.makePredictions(testFile);
	}

}
