package ca.mcmaster.magarveylab.matching.algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.mcmaster.magarveylab.matching.algorithm.alignment.AlignmentObject;
import ca.mcmaster.magarveylab.matching.algorithm.scoring.ScoreCalculator;
import ca.mcmaster.magarveylab.matching.breakdowns.Breakdown;
import ca.mcmaster.magarveylab.matching.breakdowns.GrapeBreakdown;
import ca.mcmaster.magarveylab.matching.breakdowns.PrismBreakdown;
import ca.mcmaster.magarveylab.matching.inOut.out.JSONout;

public class AnalysisObject {
	private List<AlignmentObject> alignmentObjects = new ArrayList<AlignmentObject>();
	private boolean sorted = false; //checks to see if results are sorted already
	private int resultsToReturn = 100;

/**
 * @param queryBreakdown
 * @param subjectBreakdowns
 * @param scoreCalculator
 * @throws CDKException
 * @throws IOException
 */
	public AnalysisObject(Breakdown queryBreakdown, List<Breakdown> subjectBreakdowns, ScoreCalculator scoreCalculator) throws IOException{
	
		AlignmentObject selfAlignment = scoreCalculator.getUnpermutedAlignment(queryBreakdown, queryBreakdown);
		Double selfScoreQ = selfAlignment.getScore();
		
		//Perform alignments against all
		for (int j = 0; j < subjectBreakdowns.size(); j++) {
			AlignmentObject alignment;
			if(subjectBreakdowns.get(j) instanceof PrismBreakdown && queryBreakdown instanceof PrismBreakdown || subjectBreakdowns.get(j) instanceof GrapeBreakdown && queryBreakdown instanceof GrapeBreakdown){ //if both same data types don't permute
				alignment = scoreCalculator.getUnpermutedAlignment(queryBreakdown, subjectBreakdowns.get(j));
			}else{
				if(subjectBreakdowns.get(j) instanceof GrapeBreakdown){ // remove iterative node strings when doing clusters on compounds
					subjectBreakdowns.get(j).getChemicalAbstraction().removeIterativeNodeStrings();
				}
				alignment = scoreCalculator.getBestAlignment(queryBreakdown, subjectBreakdowns.get(j));
			}
			alignment.setSelfScore(selfScoreQ);
			alignment.setRelativeScore(alignment.getScore()/(selfScoreQ));
			if(alignment.getRelativeScore() >= 0.3){
				alignmentObjects.add(alignment);
			}
		}
	}
	
	public void setResultsToReturn(int num){
		resultsToReturn = num;
	}
	
	public List<AlignmentObject> getAlignmentObjects(){
		return alignmentObjects;
	}

	public List<String> getSortedJSON() throws IOException{
		if(!sorted)
			sort();
		int counter = 0;
		List<String> jsonSorted = new ArrayList<String>();
		for(AlignmentObject ao : alignmentObjects){
			if(counter >= resultsToReturn)
				break;
			jsonSorted.add(JSONout.toJSON(ao.getGrapeBreakdown(), ao.getPrismBreakdown(), ao, true).toString());
			counter ++;
		}
		return jsonSorted;
	}
	
	public List<String> getSortedString(){
		if(!sorted)
			sort();
		int counter = 0;
		List<String> stringSorted = new ArrayList<String>();
		for(AlignmentObject ao : alignmentObjects){
			if(counter >= resultsToReturn)
				break;
			stringSorted.add(ao.toString());
			counter ++;
		}
		return stringSorted;
	}
	
	@SuppressWarnings("unchecked")
	public void sort(){
		if(!sorted){
			Collections.sort(alignmentObjects);
			Collections.reverse(alignmentObjects);
			sorted = true;
		}
	}
}
