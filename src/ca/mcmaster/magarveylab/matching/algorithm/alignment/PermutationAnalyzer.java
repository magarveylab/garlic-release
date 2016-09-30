package ca.mcmaster.magarveylab.matching.algorithm.alignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ca.mcmaster.magarveylab.matching.algorithm.scoring.ScoringScheme;
import ca.mcmaster.magarveylab.matching.chem.ChemicalAbstraction;
import ca.mcmaster.magarveylab.matching.chem.ChemicalNodeString;

/**
 * Takes chemical abstractions too big to be fully permuted and analyzed so instead does refinements on smaller permutation samples to find a better score.
 * This class gets x number of combinations and scores them. Takes the top y matches and attempts to refine them by randomly selecting two pieces to swap their order and score. do this x times with the original.
 * This refinement step is done z times. 
 * @author cDejong
 *
 */
public class PermutationAnalyzer {
	//TODO: make these variables
	private static int initialPermutation = 720;//120;//720;
	private static int permuteSize = 50;//30;//50;
	private static int numScoresToKeep = 3;
	private static int numRefinements = 10;//5;//10;
	private static Random random = new Random(12345);  //Fix RNG generator so same result always, even for threaded operation
	
	private ChemicalAbstraction largeAbstraction;
	private ScoringScheme scoringScheme;
	private int[][]  smallPermutation; //The chemical abstraction that is smaller permutation (should be smaller than 3 ideally
	
	//Save the best results
	private double bestScore = Double.NEGATIVE_INFINITY;
	private List<ChemicalNodeString> bestLargeNodeStringsOrdered = null;
	private List<ChemicalNodeString> bestSmallNodeStringsOrdered = null;
	private EditTranscript bestEditTranscript = null;
	private boolean queryPrism;
	
	/**
	 * @param largeAbstraction, with more permutations
	 * @param smallAbstraction, should be small, IE 3! (6) or less, else this can take VERY long... TODO add a check on this
	 * @param queryPrism, if the query (largeAbstraction) is from a PrismBreakdown object for unsymmetrical scoring
	 * @param scoringScheme
	 */
	public PermutationAnalyzer(ChemicalAbstraction largeAbstraction, ChemicalAbstraction smallAbstraction, ScoringScheme scoringScheme, boolean queryPrism){
		this.largeAbstraction = largeAbstraction;
		this.scoringScheme = scoringScheme;
		this.queryPrism = queryPrism;
		smallPermutation = Permutation.getPermutations(smallAbstraction.getNodeStrings().size(), 5); //This is assuming that smallAbstraction is < 6
		
		//Build base array with numbers for each position of the nodeString so can permute a random sample of it
		int n = largeAbstraction.getNodeStrings().size();
		int[] array = new int[n];
		for(int i = 0; i < n; i++) {
			array[i] = i;
		}
		int[][] randomOrderSamples = Permutation.getSampledPermutations(array, initialPermutation);
		for(int i = 0; smallPermutation.length > i; i++){
			List<ChemicalNodeString> smallSampleNodeString = reorderNodeString(smallAbstraction.getNodeStrings(), smallPermutation[i]);
			bestHuristicllyDerivedAlignment(smallSampleNodeString, randomOrderSamples, 0);
		}		
	}

	/**
	 * Recursive class for refinements of the original permutation
	 * @param smallSampleNodeString
	 * @param randomOrderSamples
	 * @param refinementNumber
	 */
	private void bestHuristicllyDerivedAlignment(List<ChemicalNodeString> smallSampleNodeString, int[][] randomOrderSamples, int refinementNumber) {
		int[] topScoresIndex = getTopScoreIndecies(randomOrderSamples, smallSampleNodeString);
		
		if(refinementNumber < numRefinements){
			List<int[]> reorderedSamples = new ArrayList<int[]>(permuteSize*numScoresToKeep);
			for(int i : topScoresIndex){
				List<int[]> reorderedSample = reorderSample(randomOrderSamples[i]);
				reorderedSamples.addAll(reorderedSample);
				reorderedSamples.add(randomOrderSamples[i]); //This adds the seeded order to the new list so that it can be checked to see if it is still the best
			}
			bestHuristicllyDerivedAlignment(smallSampleNodeString, toArray(reorderedSamples), refinementNumber + 1);
		}
	}

	private int[] getTopScoreIndecies(int[][] sampled, List<ChemicalNodeString> smallSampleNodeStrings){
		int[] topScoresIndex = null;
		double[] scores = new double[permuteSize];
		for(int i = 0; permuteSize > i; i++){
			List<ChemicalNodeString> largeNodeStrings = reorderNodeString(largeAbstraction.getNodeStrings(), sampled[i]);
			EditTranscript editTranscript = new EditTranscript();
			switch(scoringScheme.alignmentType) {
			case GLOBAL:
				scores[i] = Alignment.performGlobalAlignment(smallSampleNodeStrings,
						largeNodeStrings,
						editTranscript,
						scoringScheme,
						queryPrism);
				break;
			case LOCAL:
				scores[i] = Alignment.performLocalAlignment(smallSampleNodeStrings, 
						largeNodeStrings,
						editTranscript,
						scoringScheme,
						queryPrism);
				break;
			}
			topScoresIndex = maxKIndex(scores, numScoresToKeep);
			if(bestScore < max(scores)){
				bestScore = max(scores);
				bestSmallNodeStringsOrdered = smallSampleNodeStrings;
				bestLargeNodeStringsOrdered = largeNodeStrings;
				bestEditTranscript = editTranscript;
			}
		}
		return topScoresIndex;
	}
	
	/**
	 * Swaps two values randomly from sample 'permutseSize' times.
	 * @param sample
	 * @return
	 */
	private ArrayList<int[]> reorderSample(int[] sample) {
		ArrayList<int[]> reordered = new ArrayList<int[]>();
		for(int i = 0; permuteSize > i; i++){
			int[] shuffledOrder = Arrays.copyOf(sample, sample.length);
			int takeIndex = random.nextInt(sample.length);
			int putIndex = random.nextInt(sample.length);
			putIndex = correctSame(putIndex, takeIndex, sample.length, random);
			int moving = shuffledOrder[takeIndex];
			shuffledOrder[takeIndex] = shuffledOrder[putIndex];
			shuffledOrder[putIndex] = moving; 
			reordered.add(shuffledOrder);
		}
		return reordered;
	}

	/**
	 * Recursive class to make sure the put and the take index are not the same, if they are it selects another number and calls itself
	 * @param putIndex
	 * @param takeIndex
	 * @param length
	 * @return
	 */
	private int correctSame(int putIndex, int takeIndex, int length, Random random) {
		if(putIndex == takeIndex){
			putIndex = random.nextInt(length);
			putIndex = correctSame(putIndex, takeIndex, length, random);
		}
		return(putIndex);
	}
	
	public List<ChemicalNodeString> getBestLargeNodeStringsOrdered(){
		return bestLargeNodeStringsOrdered;
	}
	
	public List<ChemicalNodeString> getBestSmallNodeStringsOrdered(){
		return bestSmallNodeStringsOrdered;
	}
	
	public EditTranscript getBestEditTranscript(){
		return bestEditTranscript;
	}
	
	public double getBestScore(){
		return bestScore;
	}
	
	/**
	 * Buildes a reordered nodestring
	 * @param original to be reordered
	 * @param order reorders the original, must contain the same number of elements as original
	 * @return reorder of original
	 */
	private static List<ChemicalNodeString> reorderNodeString(List<ChemicalNodeString> original, int[] order){
		List<ChemicalNodeString> ordered = new ArrayList<ChemicalNodeString>();
		for(int j = 0; j < order.length; j++) {
			ordered.add(original.get(order[j]));
		}
		return(ordered);
	}
	
	private static double max(double[] scores) {
		double max = Double.NEGATIVE_INFINITY;
		for(double score : scores){
			if(score > max){
				max = score;
			}
		}
		return max;
	}
	
	private int[][] toArray(List<int[]> reorderedSamples) {
		int[][] array = new int[reorderedSamples.size()][reorderedSamples.get(0).length];
		for(int i = 0; reorderedSamples.size() > i; i++){
			for(int j = 0; reorderedSamples.get(i).length > j; j++){
				array[i][j] = reorderedSamples.get(i)[j];
			}
		}
		return array;
	}

	public static int[] maxKIndex(double[] array, int top_k) {
	    double[] max = new double[top_k];
	    int[] maxIndex = new int[top_k];
	    Arrays.fill(max, Double.NEGATIVE_INFINITY);
	    Arrays.fill(maxIndex, -1);

	    top: for(int i = 0; i < array.length; i++) {
	        for(int j = 0; j < top_k; j++) {
	            if(array[i] > max[j]) {
	                for(int x = top_k - 1; x > j; x--) {
	                    maxIndex[x] = maxIndex[x-1]; max[x] = max[x-1];
	                }
	                maxIndex[j] = i; max[j] = array[i];
	                continue top;
	            }
	        }
	    }
	    return maxIndex;
	}
}
