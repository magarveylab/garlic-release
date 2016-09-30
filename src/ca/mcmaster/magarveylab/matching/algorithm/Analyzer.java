package ca.mcmaster.magarveylab.matching.algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.mcmaster.magarveylab.matching.algorithm.scoring.ScoreCalculator;
import ca.mcmaster.magarveylab.matching.algorithm.scoring.ScoringScheme;
import ca.mcmaster.magarveylab.matching.breakdowns.Breakdown;
import ca.mcmaster.magarveylab.matching.breakdowns.GrapeBreakdown;

public class Analyzer {
	private ScoreCalculator scoreCalculator;
	private List<AnalysisObject> analysisObjects = new ArrayList<AnalysisObject>();
	private int resultIndex = 0;
	
	/**
	 * @param queryBreakdowns
	 * @param subjectBreakdowns
	 * @param scoringScheme with parameters chosen by the user
	 * @throws CDKException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public Analyzer(List<Breakdown> queryBreakdowns, List<Breakdown> subjectBreakdowns, ScoringScheme scoringScheme, int threadMax) throws IOException, InterruptedException{
		
		scoreCalculator = new ScoreCalculator(scoringScheme);
		//OPT: uncomment below for faster PRISMGRAPE/GRAPEPRISM, but less detailed GRAPEGRAPE analysis
		//NOTE:  was uncommented for testing for GARLIC paper
		/**
		if(queryBreakdowns.get(0) instanceof GrapeBreakdown){
			for(Breakdown breakdown : queryBreakdowns){
				breakdown.getChemicalAbstraction().removeIterativeNodeStrings();
			}
			
		}
		if(subjectBreakdowns.get(0) instanceof GrapeBreakdown){
			for(Breakdown breakdown : subjectBreakdowns){
				breakdown.getChemicalAbstraction().removeIterativeNodeStrings();
			}
			
		}
		**/
		for(Breakdown breakdown : queryBreakdowns){
			//System.out.println(breakdown.getName());
			analysisObjects.add(new AnalysisObject(breakdown, subjectBreakdowns, scoreCalculator));
//			resultsJSON.add(analysisObject.getSortedJSON());
//			resultsString.add(analysisObject.getSortedString());			
		}
		/**Threading breaks, NOT concurrent safe, disabled for NOW
		ThreadGroup tg = new ThreadGroup("breakdown_analysis_group");
		final List<Breakdown> subjectBreakdownsT = subjectBreakdowns;
		Thread[] threads = new Thread[queryBreakdowns.size()];
		final Object LOCK = new Object();
		for(int i=0;i<queryBreakdowns.size();i++){
			final Breakdown breakdown = queryBreakdowns.get(i);
			Thread t = new Thread(tg, new Runnable() {
				public void run() {
					System.out.println(breakdown.getName());
					try {
						AnalysisObject newAnal = new AnalysisObject(breakdown, subjectBreakdownsT, scoreCalculator);
						synchronized(LOCK) {
							analysisObjects.add(newAnal);
						}
					} catch (CDKException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		//			resultsJSON.add(analysisObject.getSortedJSON());
		//			resultsString.add(analysisObject.getSortedString());			
				}
			});
			threads[i] = t;
			t.start();
			// Wait if number of active threads equal or greater than limit
			while (tg.activeCount() >= threadMax) {
				Thread.sleep(500);
			}
		}		
		for(Thread thread : threads) {
			thread.join();
		}
		**/
	}
	
	public List<AnalysisObject> getAnalysisObjects(){
		return analysisObjects;
	}
	
	//Below are for the webapp
	/**
	 * @return true if there is another prism cluster to get JSON results for from the prismJson input
	 */
	public boolean hasNextPrismResult(){
		if(resultIndex < analysisObjects.size()){
			return true;
		}
		return false;
	}
	/**
	 * @return the next list of Alignments for the next prism cluster in JSON format, ordered by score
	 * @throws ArrayIndexOutOfBoundsException when there is no next prism result, use hasNextPrismResult to check if there is another
	 * @throws IOException when JSON generator fails
	 */
	public List<String> getNextResult() throws ArrayIndexOutOfBoundsException, IOException{ 
		resultIndex ++;
		return analysisObjects.get(resultIndex - 1).getSortedJSON();
	}
	
	/**
	 * @return the current Alignment's output string
	 * @throws ArrayIndexOutOfBoundsException when there is no next prism result, use hasNextPrismResult to check if there is another
	 */
	public List<String> getCurrentResultString() throws ArrayIndexOutOfBoundsException{
		return analysisObjects.get(resultIndex - 1).getSortedString();
	}
}
