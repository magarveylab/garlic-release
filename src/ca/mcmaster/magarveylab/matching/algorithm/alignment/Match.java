package ca.mcmaster.magarveylab.matching.algorithm.alignment;

public class Match implements Comparable<Match> {
	private double score;
	private String outputString;

	public Match(double score, String outputString) {
		this.score = score;
		this.setOutputString(outputString);
	}
	
	@Override
	public int compareTo(Match o) {
		return ((Double) score).compareTo(o.score);
	}

	public String getOutputString() {
		return outputString;
	}

	public void setOutputString(String outputString) {
		this.outputString = outputString;
	}
	
	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
	
	@Override
	public String toString() {
		return this.outputString;
	}
}
