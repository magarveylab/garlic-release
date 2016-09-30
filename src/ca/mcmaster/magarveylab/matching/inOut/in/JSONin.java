package ca.mcmaster.magarveylab.matching.inOut.in;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ca.mcmaster.magarveylab.matching.breakdowns.Breakdown;

public class JSONin {
	/**
	 * @param json
	 * @param prismOnPrism set to true if all adenylation domains should be used in alignmnet, this is typically only used in prism on prism analyses
	 * @return
	 * @throws IOException 
	 */
	public static List<Breakdown> readJSONString(String json, String fileName, boolean prismOnPrism) throws IOException{
		List<Breakdown> breakdowns = new ArrayList<Breakdown>();
		
		if(json.contains("[{\"grape_results\":")){
			breakdowns.addAll(GrapeJSONin.readMultiGrapeJSON(json));
		}else if(json.contains("grape_results")){
			breakdowns.add(GrapeJSONin.loadGrapeJSONString(json, fileName));
		}else if(json.contains("prism_results")){
			PrismJSONparser jo = new PrismJSONparser(prismOnPrism);
			breakdowns.addAll(jo.loadPrism(json));
		}else{
			throw new IOException("JSON not properly formatted");
		}
		return breakdowns;
	}
	/**
	 * @param file
	 * @param prismOnPrism set to true if all adenylation domains should be used in alignmnet, this is typically only used in prism on prism analyses
	 * @return
	 */
	public static List<Breakdown> readJSONPath(File file, boolean prismOnPrism){ // can also be directory
		List<Breakdown> breakdowns = new ArrayList<Breakdown>(); 
		
		if(file.isDirectory()){ //read all files in direcory as JSON
			for(File singleFile : file.listFiles()){
				breakdowns.addAll(readJSONPath(singleFile, prismOnPrism));
			}
		}else{ //read the file as JSON
			try{
				breakdowns.addAll(readJSONString(readFile(file), file.getName(), prismOnPrism));
			}catch(FileNotFoundException fileNotFoundException){
				System.err.println("File does not exist: " + file.getAbsolutePath());
			}catch(Exception e){
				System.err.println("Could not parse file: " + file.getAbsolutePath());
				e.printStackTrace();
			}
		}
		
		return breakdowns;
	}
	
	private static String readFile(File file) throws FileNotFoundException{
		Scanner scanner = new Scanner(file);
		String json = scanner.useDelimiter("\\Z").next();
		json = json.replace(" ", "");
		scanner.close();	
		return json;
	}
}
