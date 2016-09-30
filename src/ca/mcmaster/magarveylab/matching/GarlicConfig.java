package ca.mcmaster.magarveylab.matching;
import java.lang.NumberFormatException;

public class GarlicConfig {
	
	private static GarlicConfig config = new GarlicConfig();
	
	private String version = "1.0.2";
	private String queryFile = null;
	private String queryID = null;
	private String subjectID = null;
	private String subjectFile = null;
	private String outputDirectory = null;
	private boolean allAdenylation = false;
	private AnalysisType analysisType = AnalysisType.FULL_ALIGNMENTS;
	private String scoringSchemeFile = null;
	private String outputBaseFileName = null;
	private int threadCount = 1;
	
	private GarlicConfig() {}

	enum AnalysisType {
		FULL_ALIGNMENTS, ONLY_SCORES, PAIRWISE_MATRIX, STRING_OUTPUT;
	}
	
	//getters
	static GarlicConfig getConfig() {
		return config;
	}
	String getVersion(){
		return version;
	}
	String getQueryPath(){
		return queryFile;
	}
	String getQueryID(){
		return queryID;
	}
	String getSubjectPath(){
		return subjectFile;
	}
	String getSubjectID(){
		return subjectID;
	}
	String getOutputDirectory(){
		return outputDirectory;
	}
	boolean getAllAdenylation(){
		return allAdenylation;
	}
	AnalysisType getAnalysisType(){
		return analysisType;
	}
	String getScoringSchemePath(){
		return scoringSchemeFile;
	}
	String getScoringSchemeFile(){
		return scoringSchemeFile;
	}
	String getOutputBaseFileName(){
		return outputBaseFileName;
	}
	int getThreadCount(){
		return threadCount;
	}
	
	//setters
	void setQuery(String optionValue) {
		queryFile = optionValue;
	}
	void setQueryID(String id){
		queryID = id;
	}
	void setSubject(String optionValue) {
		subjectFile = optionValue;
	}
	void setSubjectID(String id){
		subjectID = id;
	}
	void setOutputDirectory(String optionValue) {
		outputDirectory = optionValue;
	}
	void allAdenylation(){
		allAdenylation = true;
	}
	void setAnalysisType(AnalysisType optionValue) {
		analysisType = optionValue;
	}
	void setScoringSchemePath(String scheme){
		scoringSchemeFile = scheme;
	}
	void setThreadCount(int number_of_threads) throws NumberFormatException{
		if(number_of_threads > 0){
			threadCount = number_of_threads;
		} else {
			throw new NumberFormatException("Thread Count must be a positive integer");
		}
	}
	void setThreadCount(String number_of_threads) throws NumberFormatException{
		threadCount = Integer.valueOf(number_of_threads);
		if(threadCount < 1){
			threadCount = 1;
			throw new NumberFormatException("Thread Count must be a positive integer");
		}
	}
	
	void setOutputBaseFileName(String optionValue) {
		outputBaseFileName = optionValue;
	}
}
