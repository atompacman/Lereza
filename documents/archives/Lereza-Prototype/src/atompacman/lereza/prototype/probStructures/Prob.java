package atompacman.lereza.prototype.probStructures;

public class Prob {

	private double probability;
	
	private int count;
	private int total;
	
	
	public Prob(int count, int total) {
		this.count = count;
		this.total = total;
		this.probability = (double) count / (double) total;
	}
	
	public double getProb() {
		return probability;
	}
	
	public String toString() {
		return "(" + count + "/" + total + ") " + String.format("%.2f", probability * 100) + "%";
	}
}
