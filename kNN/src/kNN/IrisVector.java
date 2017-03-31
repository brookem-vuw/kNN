package kNN;

/***
 * LABELED VECTOR: A helper object for kNN Algorithm.
 * 
 * @author Brooke
 * 
 */
public class IrisVector {

	private double sepLength, sepWidth, petLength, petWidth;
	private double[] values;
	protected String iClass;
	protected String classGuess;
	protected boolean isTest = false;
	protected double length = 0;    //storing the length to the neighbour in quesiton.
	
	//Storing the vectors as both an array and separately until I figure out wtf to do. 
	public IrisVector(double v0, double v1, double v2, double v3, String label){
		sepLength = v0;
		sepWidth = v1;
		petLength = v2; 
		petWidth = v3;
		double[] values = {v0, v1, v2, v3};
		iClass = label;
	}
	
	/**
	 * Measure: A method to compare how 'near' this vector is to a given alternative
	 * IrisVector, in order to find the nearest neighbors using kNN.
	 * 
	 * @param other vector to compare
	 * @return distance between the two
	 */
	protected double measure (IrisVector other){
		
		//d^2 = sqrt(sum(1:4) (ai - bi))
		//keeping track of the sigma sum over the series.
		double sum = 0;
		
		//getting (a-b)^2 for each part of the series.
		for (int i = 0; i < 4; i++){
			double dist = this.values[i] - other.values[i];
			sum = sum + Math.pow(dist, 2);
		}
		
		//taking square root to get difference/distance
		double dist = Math.sqrt(sum);
		this.length = dist;
		
		return dist;
	}
	
	/**
	 * Assign a 'class' to the vector based on it's nearest neighbours.
	 */
	protected void guessClass(String guess){
		this.classGuess = guess;
	}
	
	public String toString(){
		return values.toString() + ", " + classGuess;
	}
	
	protected void setTest(){
		this.isTest = true;
	}
	
}
