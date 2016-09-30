package ca.mcmaster.magarveylab.matching.algorithm.alignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditTranscript {
	// Insertion, Deletion, Match (where the "match" may be a substitution), N for a match that is not included in a local alignment
	// Note that these refer to an alignment with the Grape string first. 
	public enum EditOperations {I, D, M, N};
	private List<EditOperations> editOperations = new ArrayList<EditOperations>();
	private List<Double> scores = new ArrayList<Double>();
	public EditTranscript() {
		
	}
	public void addEditOperation(EditOperations operation, double score) {
		editOperations.add(operation);
		scores.add(score);
	}
	public List<EditOperations> getEditOperations() {
		return editOperations;
	}
	public List<Double> getScores() {
		return scores;
	}
	public void reverse() {
		Collections.reverse(editOperations);
		Collections.reverse(scores);
	}
	
	public void flip(){
		for(int i = 0; editOperations.size() > i; i++){
			if(editOperations.get(i).equals(EditOperations.I)){
				editOperations.set(i, EditOperations.D);
				continue;
			}
			if(editOperations.get(i).equals(EditOperations.D)){
				editOperations.set(i, EditOperations.I);
			}
		}
	}
}
