package ca.mcmaster.magarveylab.matching;

import java.io.IOException;
import java.lang.NumberFormatException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import ca.mcmaster.magarveylab.matching.GarlicConfig.AnalysisType;
import ca.mcmaster.magarveylab.matching.algorithm.GarlicAnalyzerException;
import ca.mcmaster.magarveylab.matching.inOut.in.ScoringSchemeParsingException;
import ca.mcmaster.magarveylab.matching.inOut.out.CouldNotGenerateResultStringException;
import ca.mcmaster.magarveylab.matching.inOut.out.NoResultsException;

/**
 * Main method for grape for command line interface.
 * @author prees cdejong
 * 
 */
 public class CliMain {
	/**
	 * Execute a GARLIC analysis.
	 * @param args	command line arguments
	 */
	public static void main(String[] args) {
		Options options = createOptions();
		String header = "GARLIC - A tool to align and score between natural chemistries.";
		String footer = "Magarvey Lab 2015. Written by Chris Dejong and Greg Chen.";
		
		try {
			CommandLineParser parser = new GnuParser();
			CommandLine line = parser.parse(options, args, true);
			for (String opt : line.getArgs()){
				System.out.println(opt);
			}
			if (line.hasOption("h") || line.getOptions().length == 0){
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("garlic", header, options, footer, true);
			} else {
				GarlicConfig gc = GarlicConfig.getConfig();
				parseCommandLine(line);
				if (line.hasOption("v")) { 
					System.out.println(header);
					System.out.println("Garlic: version " + gc.getVersion());
					System.out.println(footer);
				} else {
					if(gc.getQueryPath() == null || gc.getSubjectPath() == null){
						throw new IllegalArgumentException();
					}
					//run grape here
					Garlic garlic = new Garlic(gc);
					garlic.run();
				}
			} 
		} catch (ParseException e) {
			System.err.println("Error: parsing command line arguments");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("garlic", header, options, footer, true);			
		} catch (NumberFormatException e){  //currently disabled
			System.err.println("Please enter an integer > 0 for thread count.\n");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.err.println("Error: must specify input query and subject");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("garlic", header, options, footer, true);
		} catch (ScoringSchemeParsingException e) {
			System.err.println("Error parsing scoringScheme JSON file, error:\n" + e.getLocalizedMessage() + "\nSee default scoringScheme file for formating");
		} catch (GarlicAnalyzerException e) {
			System.err.println("Error running the GARLIC algorithm:\n" + e.getLocalizedMessage() + "\nPlease contact maintainer, and share the error stack");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Could not write output file:\n" + e.getLocalizedMessage() + "\nEnsure you are trying to write in a writable directory");
			e.printStackTrace();
		} catch (CouldNotGenerateResultStringException e) {
			System.err.println("Error creating Results:\n" + e.getLocalizedMessage() + "\nPlease contact maintainer, and share the error stack");
			e.printStackTrace();
		} catch (NoResultsException e) {
			System.err.println("Error creating Results:\n" + e.getLocalizedMessage() + "\nPlease contact mainterner, and share the error stack");
		} catch (Exception e) {
			System.err.println("Error unknown stack:");
			e.printStackTrace();
		} 
	}

	/**
	 * Create command line options. disabled
	 * @return	newly created options
	 */
	@SuppressWarnings("static-access")
	public static Options createOptions() {
		Options options = new Options();
		
		// construct options with values
		//IO
		Option query = OptionBuilder.withLongOpt("query").hasArg().withArgName("QFILE")
				.withDescription("Set the path to the query input file.").create("q");
		
		Option subject = OptionBuilder.withLongOpt("subject").hasArg().withArgName("SFILE")
				.withDescription("Set the path to the subject input file.").create("s");
		
		Option scoringScheme = OptionBuilder.withLongOpt("scoringScheme").hasArg().withArgName("SSCHEME")
				.withDescription("Set the path to the scoring scheme json file.").create("i");
		
		Option output = OptionBuilder.withLongOpt("output").hasArg().withArgName("OUTPUT")
				.withDescription("Set an output directory.").create("o");
		
		//set ID
		Option queryID = OptionBuilder.withLongOpt("queryid").hasArg().withArgName("QID")
						.withDescription("Sets the queries' ID.").create("qi");
				
		Option subjectID = OptionBuilder.withLongOpt("subjectid").hasArg().withArgName("SID")
						.withDescription("Sets the subjects' ID.").create("si");
		
		//Data to take from input
		Option aden = new Option("a", "aden", false, "Consider all adenylation domains for scoring, including those not in modules.");
		
		//Output type pick only one
		Option onlyScore = new Option("z", "onlyscore", false, "Only gives score with output");
		
		//Output type only string
		Option string = new Option("k", "string", false, "Only gives alignment string as output");
		
		//Output type only string
		Option csv = new Option("c", "csv", false, "Gives a matrix of scores for subjects and queries");
		Option csvOut = OptionBuilder.withLongOpt("csvOut").hasArg().withArgName("QID")
				.withDescription("Sets the base file name for csv output.").create("d");
		
		//TODO: consider enabling: requires *careful* use of java threading
		//Option threads = OptionBuilder.withLongOpt("threads").hasArg().withArgName("TCOUNT")
		//		.withDescription("Number of threads to use.").create("t");
		
		//Help and version info
		Option help = new Option("h", "help", false, "Print this message");
		Option version = new Option("v", "version", false, "Print the current version and exit");
	
		options.addOption(query);
		options.addOption(subject);
		options.addOption(scoringScheme);
		options.addOption(output);
		
		options.addOption(queryID);
		options.addOption(subjectID);
		
		options.addOption(aden);
		options.addOption(onlyScore);
		options.addOption(string);
		options.addOption(csv);
		options.addOption(csvOut);
		
		//TODO: consider enabling
		//options.addOption(threads);
	
		
		options.addOption(help);
		options.addOption(version);

		return options;
	}
	
	/**
	 * Parse the command line input. 
	 * @param line	line to parse
	 * @throws ParseException
	 */
	private static void parseCommandLine(CommandLine line) throws ParseException, NumberFormatException {
		GarlicConfig gc = GarlicConfig.getConfig();
		//parse all other options here  and set them to GrapeConfig object.
		if (line.hasOption("q")) {
			gc.setQuery(line.getOptionValue("q"));
		}
		if(line.hasOption("qi")){
			gc.setQueryID(line.getOptionValue("qi"));
		}
		if(line.hasOption("s")){
			gc.setSubject(line.getOptionValue("s"));
		}
		if(line.hasOption("si")){
			gc.setSubjectID(line.getOptionValue("si"));
		}
		if(line.hasOption("i")){
			gc.setScoringSchemePath(line.getOptionValue("i"));
		}
		if(line.hasOption("o")){
			gc.setOutputDirectory(line.getOptionValue("o"));
		}
		if(line.hasOption("a")){
			gc.allAdenylation();
		}
		int analysisTypes = 0;
		if(line.hasOption("z")){
			gc.setAnalysisType(AnalysisType.ONLY_SCORES);
			analysisTypes ++;
		}
		if(line.hasOption("k")){
			gc.setAnalysisType(AnalysisType.STRING_OUTPUT);
			analysisTypes ++;
		}
		if(line.hasOption("c")){
			gc.setAnalysisType(AnalysisType.PAIRWISE_MATRIX);
			analysisTypes ++;
		}
		if(analysisTypes > 1){
			throw new ParseException("More than one analysis type selected");
		}
		if(line.hasOption("d")){
			if(!gc.getAnalysisType().equals(AnalysisType.PAIRWISE_MATRIX)){
				throw new ParseException("CSV file name given when not doing csv output");
			}
			gc.setOutputBaseFileName(line.getOptionValue("d"));
		}
		if(line.hasOption("t")){
			System.out.println("Threading currently disabled - using one thread.");
			gc.setThreadCount(line.getOptionValue("t"));
		}
		else{
			gc.setThreadCount(1);
		}
	}
}
