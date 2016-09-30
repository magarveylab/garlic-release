package ca.mcmaster.magarveylab.matching.inOut.in;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import ca.mcmaster.magarveylab.matching.breakdowns.GrapeBreakdown;
import ca.mcmaster.magarveylab.matching.chem.ChemicalAbstraction;
import ca.mcmaster.magarveylab.matching.chem.ChemicalNodeString;
import ca.mcmaster.magarveylab.matching.chem.ChemicalAbstraction.Presence;
import ca.mcmaster.magarveylab.matching.chem.ExactPossibleSugar;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.AminoAcidNode;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.PKPieceNode;
import ca.mcmaster.magarveylab.matching.enums.AcylAdenylatingSubstrates;
import ca.mcmaster.magarveylab.matching.enums.ChemicalClassifications;
import ca.mcmaster.magarveylab.matching.enums.Monomers;
import ca.mcmaster.magarveylab.matching.enums.Monomers.KnownOther;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKOxidationStateEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKSubstrateEnum;
import ca.mcmaster.magarveylab.matching.enums.Sugars.SugarEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings;
import ca.mcmaster.magarveylab.matching.enums.ChemicalClassifications.ChemicalScaffold;
import ca.mcmaster.magarveylab.matching.enums.ChemicalClassifications.ChemicalSubtype;
import ca.mcmaster.magarveylab.matching.enums.ChemicalClassifications.ChemicalType;
import ca.mcmaster.magarveylab.matching.enums.Monomers.AminoAcidEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.NonSpecificTailoringEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.SiteSpecificTailoringEnum;
import ca.mcmaster.magarveylab.matching.inOut.Utility;

public class GrapeJSONin {

	public static List<GrapeBreakdown> readGrapeDirectoryJSON(File grapeDirectory) throws IOException{
		List<GrapeBreakdown> grapeBreakdowns = new ArrayList<GrapeBreakdown>();
		for (final File grapeFile : grapeDirectory.listFiles()) {
			try{
				grapeBreakdowns.add(loadGrape(grapeFile));
			}catch(Exception e){
				System.err.println("Couldn't get data for file: " + grapeFile.toString()); //Not fatal, just means one of the results are bad and gets skipped.
			}
		}
		return grapeBreakdowns;
	}
	
	public static GrapeBreakdown loadGrapeJSONString(String json, String fileName) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> map = mapper.readValue(json, new TypeReference<HashMap<String,Object>>(){});		
		
		GrapeBreakdown grapeBreakdown = readMap(map);
		grapeBreakdown.setFileName(fileName);
		return grapeBreakdown;
	}
	
	public static GrapeBreakdown loadGrape(File file) throws IOException{
		Scanner scanner = new Scanner(file);
		String json = scanner.useDelimiter("\\Z").next();
		scanner.close();
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> map = mapper.readValue(json, new TypeReference<HashMap<String,Object>>(){});		
		
		GrapeBreakdown grapeBreakdown = readMap(map);
		grapeBreakdown.setFileName(file.getName());
		return grapeBreakdown;
	}
	
	public static List<GrapeBreakdown> readMultiGrapeJSON(String grapeJson) throws IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Object>> mapResults = mapper.readValue(grapeJson, new TypeReference<List<Map<String, Object>>>(){});
		
		List<GrapeBreakdown> grapeBreakdowns = new ArrayList<GrapeBreakdown>(); 
		for(Map<String, Object> map : mapResults){
			try{
				GrapeBreakdown gBreakdown = readMap(map);
				if(gBreakdown != null){
					grapeBreakdowns.add(gBreakdown);
				}
			}catch(Exception e){
				System.err.println("Couldn't get GRAPE data for an entry");
			}
		}
		return grapeBreakdowns;
	}

	@SuppressWarnings("unchecked")
	private static GrapeBreakdown readMap(Map<String, Object> map){
		Map<String, Object> grapeResult = (Map<String, Object>)map.get("grape_results");
		
		//Check if there are any errors, if so return null
		if(grapeResult.get("error") != null){
			return null;
		}		
		//meta data
		GrapeBreakdown grapeBreakdown = new GrapeBreakdown();
		grapeBreakdown.setName((String)grapeResult.get("name"));
		grapeBreakdown.setSmiles((String)grapeResult.get("SMILES"));
		if(grapeResult.get("id") instanceof Integer){
			grapeBreakdown.setID(Integer.toString((Integer)grapeResult.get("id")));
		}else{
			grapeBreakdown.setID((String)grapeResult.get("id"));
		}
		
		ChemicalAbstraction chemicalAbstraction = new ChemicalAbstraction();
		ChemicalType chemicalType = ChemicalType.getChemicalType((String)grapeResult.get("chemical_type"));
		chemicalAbstraction.setChemicalType(chemicalType);
		ChemicalSubtype chemicalSubtype = ChemicalSubtype.getChemicalSubtype((String)grapeResult.get("chemical_subtype"));
		chemicalAbstraction.setChemicalSubtype(chemicalSubtype);
		ChemicalScaffold chemicalScaffold = ChemicalClassifications.getChemicalScaffold((String)grapeResult.get("chemical_scaffold"));
		chemicalAbstraction.setChemicalScaffold(chemicalScaffold);
		
		grapeBreakdown.setChemicalAbstraction(chemicalAbstraction);
		//ChemicalBreakdown information
		List<Object> fragments = (List<Object>)grapeResult.get("fragments");
		parseFragments(fragments, grapeBreakdown);
		return grapeBreakdown;
	}

	@SuppressWarnings("unchecked")
	private static void parseFragments(List<Object> fragments, GrapeBreakdown grapeBreakdown){
		ChemicalAbstraction chemicalAbstraction = grapeBreakdown.getChemicalAbstraction();
		for(Object fragment : fragments){
			ChemicalNodeString chemicalNodeString = new ChemicalNodeString();
			for(Map<String, Object> piece : (ArrayList<Map<String, Object>>)fragment){
				String type = (String)piece.get("type");
				Boolean knownStart = (Boolean)piece.get("known_start");
				if(knownStart != null){
					if(!knownStart){
						chemicalNodeString.setKnownStart(knownStart);
					}
				}
				Map<String, Object> modifications = (Map<String, Object>)piece.get("modifications");
				if(type.equals("AMINO_ACID")){
					AminoAcidNode aan = new AminoAcidNode();
					AminoAcidEnum aminoAcid = Monomers.getAminoAcidEnumFromAbbreviation((String)piece.get("name"));
					if(aminoAcid != null){
						aan.setAminoAcidType(aminoAcid);
					}
					if(modifications != null){
						List<String> tailors = (ArrayList<String>)modifications.get("tailors");
						if(tailors != null){
							for(String tailor : tailors){
								SiteSpecificTailoringEnum siteSpecificEnum = Tailorings.getSiteSpecificTailoringFromName(tailor);
								NonSpecificTailoringEnum nonSpecificEnum = Tailorings.getNonSpecificTailoringFromName(tailor);
								if(siteSpecificEnum == null && nonSpecificEnum == null) {
									System.err.println("Warning: Found tailoring value " + tailor + " that does not match any tailoring domains");
								}
								if(siteSpecificEnum != null) 
									aan.addSiteSpecificTailoring(siteSpecificEnum);
								else if(nonSpecificEnum != null) //make sure that it wasn't added as a specific
									chemicalAbstraction.addNonspecificTailoringEnum(nonSpecificEnum);
							}
						}
					}
					chemicalNodeString.addNode(aan);
				}else if(type.equals("MULTIPLE_AMINO_ACID_PIECE")){
					AminoAcidNode aan = new AminoAcidNode();
					String name = (String)piece.get("name");
					if(name != null){
						AminoAcidEnum aminoAcid = Monomers.getAminoAcidEnumFromAbbreviation(name);
						if(aminoAcid != null){
							aan.setAminoAcidType(aminoAcid);
						}
					}
					chemicalNodeString.addNode(aan);
				}else if(type.equals("ACYL_ADENYLATING")){
					String name = (String)piece.get("name");
					AcylAdenylatingSubstrates substrate = AcylAdenylatingSubstrates.getAcylAdenylatingSubstrateFromString(name);
					if(substrate != null){
						chemicalAbstraction.addStarter(substrate);
					}
				}else if(type.equals("POLYKETIDE") || type.equals("FA_OR_PK")){
					PKPieceNode pkPiece = new PKPieceNode();
					if(type.equals("FA_OR_PK")){
						chemicalAbstraction.setFattyAcid(Presence.POSSIBLE);
						pkPiece.setPossibleNonPK(true);
					}
					String name = (String)piece.get("name");
					if(name != null){ //polyketide monomer
						PKSubstrateEnum substrateEnum = PKSubstrateEnum.getPKSubstrateEnumFromName(name);
						pkPiece.setSubstrate(substrateEnum);
						List<String> possibleStates = (ArrayList<String>)piece.get("unit_states");
						for(String state : possibleStates){
							PKOxidationStateEnum oxiEnum = PKOxidationStateEnum.getPKOxidationStateEnumFromName(state);
							pkPiece.addPossibleOxidationState(oxiEnum);
						}
						chemicalNodeString.addNode(pkPiece);
					}else if(modifications != null){ //modifications
						List<String> tailors = (ArrayList<String>)modifications.get("tailors");
						List<String> acylAdenylatingUnits = (ArrayList<String>)modifications.get("acyl_adenylating_units");
						if(tailors != null){
							for(String tailor : tailors){
								NonSpecificTailoringEnum nonSpecificEnum = Tailorings.getNonSpecificTailoringFromName(tailor);
								if(nonSpecificEnum != null){
									chemicalAbstraction.addNonspecificTailoringEnum(nonSpecificEnum);
								}
							}
						}
						if(acylAdenylatingUnits != null){
							for(String acylAdenylatingUnit : acylAdenylatingUnits){
								AcylAdenylatingSubstrates substrate = AcylAdenylatingSubstrates.getAcylAdenylatingSubstrateFromString(acylAdenylatingUnit);
								if(substrate != null) chemicalAbstraction.addStarter(substrate);
							}
						}
					}	
				}else if(type.equals("FATTY_ACID")){ //Currently not adding type of fat matched here
					chemicalAbstraction.setFattyAcid(Presence.TRUE);
				}else if(type.equals("SUGAR")){
					ExactPossibleSugar sugars = new ExactPossibleSugar();
					List<String> sugarNames = (List<String>)piece.get("sugar_names");
					if(sugarNames.size() > 0){
						for(String sugarName : sugarNames){
							SugarEnum sugarMatch = Utility.getSugar(sugarName); 
							if(sugarMatch != null){
								sugars.add(sugarMatch);
							}else{
								sugars.add(SugarEnum.DEOXY); //If it didn't match a sugar it is almost definitely not a hexose, and thus is deoxy.
							}
						}
					}else{
						sugars.add(SugarEnum.DEOXY);
					}
					chemicalAbstraction.addExactPossibleSugars(sugars);
				}else if(type.equals("CYLOHEXANE")){
					//Nothing right now, not used in GARLIC
				}else{ //unknown_other and known_other, check for tailorings
					if(modifications != null){ //modifications
						List<String> tailors = (ArrayList<String>)modifications.get("tailors");
						List<String> acylAdenylatingUnits = (ArrayList<String>)modifications.get("acyl_adenylating_units");
						if(tailors != null){
							for(String tailor : tailors){
								NonSpecificTailoringEnum nonSpecificEnum = Tailorings.getNonSpecificTailoringFromName(tailor);
								if(nonSpecificEnum != null) chemicalAbstraction.addNonspecificTailoringEnum(nonSpecificEnum);
								else{
									System.out.println("Warning: Found tailoring value " + tailor + " that does not match any tailoring domains");
								}
							}
						}
						if(acylAdenylatingUnits != null){
							for(String acylAdenylatingUnit : acylAdenylatingUnits){
								AcylAdenylatingSubstrates substrate = AcylAdenylatingSubstrates.getAcylAdenylatingSubstrateFromString(acylAdenylatingUnit);
								if(substrate != null) chemicalAbstraction.addStarter(substrate);
							}
						}
					}
					if(type.equals("KNOWN_OTHER")){
						String name = (String)piece.get("name");
						if(name != null){
							KnownOther knownOther = KnownOther.getKnownOtherMatch(name);
							if(knownOther != null){
								grapeBreakdown.addKnownOther(knownOther);
							}
						}
					}
				}
			}
			if(chemicalNodeString.getChemicalNodes().size() == 1 && chemicalNodeString.isKnownStart() == false){ //fix for older versions where known start was being set to false for single size
				chemicalNodeString.setKnownStart(true);
			}
			if(chemicalNodeString.getChemicalNodes().size() > 0){
				chemicalAbstraction.addNodeString(chemicalNodeString);
			}
		}
	}
}
