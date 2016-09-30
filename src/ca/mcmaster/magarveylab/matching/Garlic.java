package ca.mcmaster.magarveylab.matching;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.node.ObjectNode;
import ca.mcmaster.magarveylab.matching.GarlicConfig.AnalysisType;
import ca.mcmaster.magarveylab.matching.algorithm.AnalysisObject;
import ca.mcmaster.magarveylab.matching.algorithm.Analyzer;
import ca.mcmaster.magarveylab.matching.algorithm.GarlicAnalyzerException;
import ca.mcmaster.magarveylab.matching.algorithm.alignment.AlignmentObject;
import ca.mcmaster.magarveylab.matching.algorithm.scoring.ScoringScheme;
import ca.mcmaster.magarveylab.matching.algorithm.scoring.ScoringScheme.AlignmentType;
import ca.mcmaster.magarveylab.matching.algorithm.scoring.ScoringScheme.ScoringType;
import ca.mcmaster.magarveylab.matching.breakdowns.Breakdown;
import ca.mcmaster.magarveylab.matching.breakdowns.GrapeBreakdown;
import ca.mcmaster.magarveylab.matching.breakdowns.PrismBreakdown;
import ca.mcmaster.magarveylab.matching.inOut.in.JSONin;
import ca.mcmaster.magarveylab.matching.inOut.in.ScoringSchemeParser;
import ca.mcmaster.magarveylab.matching.inOut.in.ScoringSchemeParsingException;
import ca.mcmaster.magarveylab.matching.inOut.out.CSVwriter;
import ca.mcmaster.magarveylab.matching.inOut.out.CouldNotGenerateResultStringException;
import ca.mcmaster.magarveylab.matching.inOut.out.JSONout;
import ca.mcmaster.magarveylab.matching.inOut.out.NoResultsException;

public class Garlic {

	private GarlicConfig gc;

	public Garlic(GarlicConfig gc) {
		this.gc = gc;
	}

	public void run() throws ScoringSchemeParsingException, GarlicAnalyzerException, IOException, CouldNotGenerateResultStringException, NoResultsException {
		
		//parse the input data
		List<Breakdown> queryBreakdowns = JSONin.readJSONPath(new File(gc.getQueryPath()), false);
		List<Breakdown> subjectBreakdowns = JSONin.readJSONPath(new File(gc.getSubjectPath()), false);
		//Add additional user data
		if(gc.getQueryID() != null){
			for(Breakdown b : queryBreakdowns){
				b.setID(gc.getQueryID());
			}
		}
		if(gc.getSubjectID() != null){
			for(Breakdown b : subjectBreakdowns){
				b.setID(gc.getSubjectID());
			}
		}
		
		//Set the scoring logic based on the types of data input can also have a config file at some point that will be parsed for all scoring
		ScoringScheme scoringScheme = setScoringScheme(queryBreakdowns, subjectBreakdowns);
		garlicRun(queryBreakdowns, subjectBreakdowns, scoringScheme);
	}

	private ScoringScheme setScoringScheme(List<Breakdown> queryBreakdowns, List<Breakdown> subjectBreakdowns) throws ScoringSchemeParsingException {
		ScoringScheme scoringScheme;
		if(gc.getScoringSchemeFile() != null){
			ScoringSchemeParser ssp = new ScoringSchemeParser(gc.getScoringSchemeFile());
			scoringScheme = ssp.getScoringScheme();
		}else{
			//set to default, refined (empirical, non-optimized) scoring
			if(queryBreakdowns.get(0) instanceof PrismBreakdown && subjectBreakdowns.get(0) instanceof PrismBreakdown){
				scoringScheme = new ScoringScheme(AlignmentType.GLOBAL, ScoringType.PRISMPRISM, true, true);
			}else if(queryBreakdowns.get(0) instanceof GrapeBreakdown && subjectBreakdowns.get(0) instanceof GrapeBreakdown){
				scoringScheme = new ScoringScheme(AlignmentType.GLOBAL, ScoringType.GRAPEGRAPE, true, true);
			}else{
				scoringScheme = new ScoringScheme(AlignmentType.GLOBAL, ScoringType.GRAPEPRISM, true, true);
			}
			//OPT: Set to final scoring scheme (comment to use refined instead)
			scoringScheme = new ScoringScheme(AlignmentType.GLOBAL, ScoringType.FINAL, true, true);
		}
		return scoringScheme;
	}

	private void garlicRun(List<Breakdown> queryBreakdowns, List<Breakdown> subjectBreakdowns, ScoringScheme scoringScheme) throws GarlicAnalyzerException, IOException, CouldNotGenerateResultStringException, NoResultsException{
		Analyzer ana;
		int threadMax = gc.getThreadCount();
		try {
			ana = new Analyzer(queryBreakdowns, subjectBreakdowns, scoringScheme, threadMax);
		} catch (Exception e){
			throw new GarlicAnalyzerException(e.getLocalizedMessage());
		}
		
		if(gc.getAnalysisType().equals(AnalysisType.PAIRWISE_MATRIX)){
			writePairwise(ana);
		}else{
			writeSeparate(ana);
		}
	}

	private void writePairwise(Analyzer ana) throws IOException, NoResultsException {
		try {
			CSVwriter writer = new CSVwriter(ana.getAnalysisObjects());
			if(gc.getOutputDirectory() != null){
				writer.setOutputDirectory(gc.getOutputDirectory());
			}
			if(gc.getOutputBaseFileName() != null){
				writer.setOutputFile(gc.getOutputBaseFileName());
			}
			writer.writeRawScores(); //alignment scores only
			writer.writeScores(); //alignment plus extra scores
			writer.writeRelativeScores(); //score divided by query on query score
			//writer.writeSugarScores();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			throw new IOException(e.getLocalizedMessage());
		}
	}

	private void writeSeparate(Analyzer ana) throws IOException, CouldNotGenerateResultStringException {
		boolean fullJSON = true;
		if(gc.getAnalysisType().equals(AnalysisType.ONLY_SCORES)){
			fullJSON = false;
		}
		
		for(AnalysisObject ano : ana.getAnalysisObjects()){
			ano.sort();
			Breakdown query = ano.getAlignmentObjects().get(0).getGrapeBreakdown();
			String outputString = "";
			String outputExtension = "";
			if(gc.getAnalysisType().equals(AnalysisType.STRING_OUTPUT)){
				outputExtension = ".txt";
				StringBuilder sb = new StringBuilder();
				sb.append("Query: " + query.getName() + "\n\n");
				for(AlignmentObject alo : ano.getAlignmentObjects()){
					sb.append("Subject name: " + alo.getPrismBreakdown().getName() + "\n" );
					sb.append(alo.toString() + "\n" + "Raw Score: " + alo.getScore() + "\tRelative Score: " + alo.getRelativeScore() + "\n\n");
				}
				outputString = sb.toString();
			}else{
				outputExtension = ".json";
				List<ObjectNode> nodes = new ArrayList<ObjectNode>();
				for(AlignmentObject alo : ano.getAlignmentObjects()){
					try {
						nodes.add(JSONout.toJSON(alo.getGrapeBreakdown(), alo.getPrismBreakdown(), alo, fullJSON));
					} catch (IOException e) {
						throw new CouldNotGenerateResultStringException(e.getLocalizedMessage());
					}
				}
				outputString = JSONout.groupJSON(nodes);
			}
			if(gc.getOutputDirectory() != null){
					String name;
					if(query.getID() != null){
						name = query.getID();
					}else{
						name = query.getFileName().replace(" ", "-");
					}
					PrintWriter writer;
					try {
						if(gc.getOutputDirectory() != null){
							
						}
						writer = new PrintWriter(gc.getOutputDirectory() + File.separator + name + "_garlic" + outputExtension, "UTF-8");
					} catch (FileNotFoundException | UnsupportedEncodingException e) {
						throw new IOException(e.getLocalizedMessage());
					}
					writer.append(outputString);
					writer.close();
			}else{
				System.out.println(outputString);
			}
		}		
	}
}
