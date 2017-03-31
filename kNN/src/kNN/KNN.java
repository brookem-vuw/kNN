package kNN;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

/**
 * kNN is a class to implement the (k-) Nearest Neighbor algorithm 
 * in order to classify iris plants.
 * 
 * @author Brooke Meleah 300321819
 * 
 */
public class KNN {

	//this is where k is stored and changed. 
	private int k = 3;

	//storing the test and training sets 
	Set<IrisVector> testSet;
	Set<IrisVector> trainingSet;
	PriorityQueue<IrisVector> neighbours;
	List<IrisVector> nearestN;

	/**
	 * 
	 */
	public KNN(String testFile, String trainingFile){
		parseSets(testFile, trainingFile);

		for(IrisVector iv : testSet){
			findClass(iv);
		}
	}

	/**
	 * This is the main algorithm. It will use an algorithm to compare this 
	 * vector to each in the training set in order to classify it.
	 * 
	 * @param iris
	 */
	private void findClass (IrisVector iris){
		//make sure we are using a test vector.
		if (!iris.isTest) return; 

		//create and fill a priority queue sorted by distance from iris
		PriorityQueue<IrisVector> neighbours = new PriorityQueue<IrisVector>(testSet.size(), new IrisComparator());
		for(IrisVector tVec : trainingSet){
			tVec.measure(iris);
			neighbours.add(tVec);
		}
		
		//collect k nearest neighbours
		nearestN = new ArrayList<IrisVector>();
		for(int i = 0; i < k; i++){
			nearestN.add(neighbours.poll());
		}
		
		//figure out the majority class
		int class1 = 0;
		int class2 = 0; 
		int class3 = 0;
		for (IrisVector iv : nearestN){
			if(iv.iClass.equals("Iris-setosa")) class1++;
			else if(iv.iClass.equals("Iris-versicolor")) class2++;
			else if (iv.iClass.equals("Iris-virginica")) class3++;
		}
		
		//assign class guess according to nearest neighbours
		int newClass = (int)Math.max(class1, (Math.max(class2, class3)));
		if(newClass == class1){
			iris.guessClass("Iris-setosa");
		}else if (newClass == class2){
			iris.guessClass("Iris-versicolor");
		}else if(newClass == class3){
			iris.guessClass("Iris-virginica");
		}
	}

	/**
	 * This method houses the scanner as it converts the test and training
	 * sets from raw data to a set of IrisVectors. 
	 * 
	 * @param testName
	 * @param trainingName
	 */
	public void parseSets(String testName, String trainingName){

		try {
			//read the test set. 
			Scanner read = new Scanner(new FileReader(new File(testName)));
			while(read.hasNextLine()){
				//create a new IrisVector
				String[] vec = read.nextLine().split(" ");
				IrisVector iVec = new IrisVector(Double.parseDouble(vec[0]), Double.parseDouble(vec[1]), Double.parseDouble(vec[2]), Double.parseDouble(vec[3]), vec[4]);
				//add new vector to testSet
				iVec.setTest();
				testSet.add(iVec);
			}

			//read the training set. 
			read = new Scanner(new FileReader(new File(trainingName)));
			while(read.hasNextLine()){
				//create a new IrisVector
				String[] vec = read.nextLine().split(" ");
				IrisVector iVec = new IrisVector(Double.parseDouble(vec[0]), Double.parseDouble(vec[1]), Double.parseDouble(vec[2]), Double.parseDouble(vec[3]), vec[4]);
				//add new vector to trainingSet
				trainingSet.add(iVec);
			}

		} catch (FileNotFoundException e) {
			System.out.println("Please check your inputs.");
		}
	}

	/**
	 * This method prints the output of the kNN algorithm to a .txt file.
	 */
	public void print(){
		int iter = 0;
		File output = new File("part1-output"+iter+".txt");
		
		//make sure there's a space for the new file and then make it.
		try {
		if(!output.createNewFile()){
			iter++;
			output = new File("part1-output"+iter+".txt");
			output.createNewFile();
		}
		} catch (IOException e) {
			System.out.println("Output file failed to load.");
		}
		
		//write the new data to the file
		try {
			FileWriter writer = new FileWriter(output);
			
			for(IrisVector iv : testSet){
				String irisString = iv.toString() + '\n';
				writer.write(irisString);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public static void main (String[] args){
		//this should always have two string arguments, in order. 
		if(args.length != 2 ){
			System.out.println("Your arguments are incorrect. Please make sure you enter 'iris-test.txt' and 'iris-training.txt'.");
		}
		else{
			new KNN(args[0], args[1]);
		}

	}


	public class IrisComparator implements Comparator{
		@Override
		public int compare(Object o1, Object o2) {
			IrisVector v1 = (IrisVector) o1;
			IrisVector v2 = (IrisVector) o2;
			
			if (v1.length < v2.length) return 1;
			else return -1;
		}
	}
}
