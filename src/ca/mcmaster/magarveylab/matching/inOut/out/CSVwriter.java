package ca.mcmaster.magarveylab.matching.inOut.out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import ca.mcmaster.magarveylab.matching.algorithm.AnalysisObject;
import ca.mcmaster.magarveylab.matching.algorithm.alignment.AlignmentObject;

public class CSVwriter {
	
	private List<AnalysisObject> results;
	private String fileBase = "output";
	private String outputDir = "";
	
	/**
	 * 
	 * @param results the full list of Analysis objects for the run to be written to files
	 * @throws NoResultsException if there are no queries or no subjects throws this error
	 */
	public CSVwriter(List<AnalysisObject> results) throws NoResultsException{
		this.results = results;
		if(results.size() == 0){
			throw new NoResultsException("No queries");
		}
		if(results.get(0).getAlignmentObjects().size() == 0){
			throw new NoResultsException("No subjects");
		}
	}
	
	/**
	 * Sets the base output file name, will get prepended with a postfixes (extension and score type). 
	 * @param fileBase
	 */
	public void setOutputFile(String fileBase){
		this.fileBase = fileBase;
	}
	
	/**
	 * Set the output directory, can be relative or absolute
	 * @param outputDir
	 */
	public void setOutputDirectory(String outputDir){
		this.outputDir = outputDir;
	}

	private void writeTheScores(ScoreType scoretype) throws FileNotFoundException, UnsupportedEncodingException{
		String fileName = fileBase + "_" + scoretype + ".csv";
		if(outputDir != ""){
			fileName = outputDir + File.separator + fileName;
		}
		PrintWriter writer = getWriter(fileName);
		writeCSV(writer, scoretype);
		writer.close();
	}
	
	/**
	 * Writes a csv of the raw scores for all queries on all subjects.
	 * If no outputFile or outputDirectory were set it will write to
	 * the present working directory with the prefix named "output"
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void writeRawScores() throws FileNotFoundException, UnsupportedEncodingException{
		writeTheScores(ScoreType.raw);
	}


	/**
	 * Writes a csv of the base scores for all queries on all subjects.
	 * If no outputFile or outputDirectory were set it will write to
	 * the present working directory with the prefix named "output"
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void writeScores() throws FileNotFoundException, UnsupportedEncodingException{
		writeTheScores(ScoreType.score);
	}

	/**
	 * Writes a csv of the sugar scores for all queries on all subjects.
	 * If no outputFile or outputDirectory were set it will write to
	 * the present working directory with the prefix named "output"
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void writeSugarScores() throws FileNotFoundException, UnsupportedEncodingException{
		writeTheScores(ScoreType.sugarscore);
	}

	/**
	 * Writes a csv of the relative scores for all queries on all subjects.
	 * If no outputFile or outputDirectory were set it will write to
	 * the present working directory with the prefix named "output"
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void writeRelativeScores() throws FileNotFoundException, UnsupportedEncodingException{
		writeTheScores(ScoreType.relative);
	}
	
	private void writeCSV(PrintWriter writer, ScoreType scoreType) {
		//write header, use the first analysisObject
		writer.write("Row names");
		for(AlignmentObject alo : results.get(0).getAlignmentObjects()){
			String name = cleanName(alo.getPrismBreakdown().getFileName());
			writer.write("," + name);
		}
		//write results for a line
		for(AnalysisObject ano : results){
			writer.write(System.getProperty("line.separator"));
			String name = cleanName(ano.getAlignmentObjects().get(0).getGrapeBreakdown().getFileName());
			writer.write(name);
			for(AlignmentObject alo : ano.getAlignmentObjects()){
				writer.write("," + getScore(alo, scoreType));
			}
		}
	}

	private Double getScore(AlignmentObject alo, ScoreType scoreType) {
		Double score = null;
		switch (scoreType){
			case score:
				score = alo.getScore();
				break;
			case raw:
				score = alo.getRawScore();
				break;
			case relative:
				score = alo.getRelativeScore();
				break;
				//not implemented:
			case regulator:
				score = alo.getRegulatorScore();
				break;
			case resistance:
				//score = alo.getResistanceScore();
				break;
			case sugarscore:
				score = alo.getSugarScore();
		}
		return score;
	}

	private String cleanName(String name) {
		name = name.replace(",", "_");
		if(name.length() > 100){
			name = name.substring(0, 100);
		}
		return name;
	}

	private PrintWriter getWriter(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		return writer;
	}
	
	private enum ScoreType{
		raw, relative, regulator, resistance, score, sugarscore;
	}
}
