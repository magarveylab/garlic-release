package ca.mcmaster.magarveylab.matching.algorithm;

import java.io.PrintWriter;
import java.util.List;

import ca.mcmaster.magarveylab.matching.algorithm.scoring.ScoreCalculator;
import ca.mcmaster.magarveylab.matching.breakdowns.Breakdown;
import ca.mcmaster.magarveylab.matching.breakdowns.PrismBreakdown;

public class PairwiseSimilarity {

	/**
	 * @param breakdowns
	 * @param mustBeComplete
	 * @param removeSmall
	 * @param writer 
	 * @return
	 */
	public static void doPairwiseAnalysis(List<Breakdown> breakdowns, PrintWriter writer, ScoreCalculator scoreCalculator) {
		double[][] scores = new double[breakdowns.size()][breakdowns.size()];
		for(int i = 0; i < breakdowns.size(); i++){
			Double selfScoreI = scoreCalculator.getUnpermutedAlignment(breakdowns.get(i), breakdowns.get(i)).getScore();
			for(int j = i; j < breakdowns.size(); j++){
				Double selfScoreJ = scoreCalculator.getUnpermutedAlignment(breakdowns.get(j), breakdowns.get(j)).getScore();
				Double score = scoreCalculator.getUnpermutedAlignment(breakdowns.get(j), breakdowns.get(i)).getScore();
				if(score/selfScoreI > score/selfScoreJ){ //get the lower of the self scores and set this as the pair
					scores[i][j] = score/selfScoreJ;
				}else{
					scores[i][j] = score/selfScoreI;
				}
			}
		}
		for(int i = 0; i < breakdowns.size(); i++) {
			if(i!=0) {
				writer.append("\t");
			}
			if(breakdowns.get(i) instanceof PrismBreakdown){
				PrismBreakdown pb = (PrismBreakdown) breakdowns.get(i);
				writer.append(pb.getName() + "_" + pb.getAccession());
			}else{
				writer.append(breakdowns.get(i).getName());
			}
		}
		writer.append("\n");
		for(int i = 0; i < breakdowns.size(); i++) {
			if(breakdowns.get(i) instanceof PrismBreakdown){
				PrismBreakdown pb = (PrismBreakdown) breakdowns.get(i);
				writer.append(pb.getName() + "_" + pb.getAccession());
			}else{
				writer.append(breakdowns.get(i).getName());
			}
			for(int j = 0; j < breakdowns.size(); j++) {
				if(j >= i) {
					writer.append("\t" + scores[i][j]);
				}
				else {
					writer.append("\t" + scores[j][i]);
				}
			}
			writer.append("\n");
		}
	}
}
