package ca.mcmaster.magarveylab.matching.algorithm.scoring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import ca.mcmaster.magarveylab.matching.algorithm.alignment.Alignment;
import ca.mcmaster.magarveylab.matching.algorithm.alignment.AlignmentObject;
import ca.mcmaster.magarveylab.matching.algorithm.alignment.EditTranscript;
import ca.mcmaster.magarveylab.matching.algorithm.alignment.Permutation;
import ca.mcmaster.magarveylab.matching.algorithm.alignment.PermutationAnalyzer;
import ca.mcmaster.magarveylab.matching.algorithm.scoring.ScoringScheme.AlignmentType;
import ca.mcmaster.magarveylab.matching.algorithm.scoring.ScoringScheme.ScoringType;
import ca.mcmaster.magarveylab.matching.breakdowns.Breakdown;
import ca.mcmaster.magarveylab.matching.breakdowns.GrapeBreakdown;
import ca.mcmaster.magarveylab.matching.breakdowns.PrismBreakdown;
import ca.mcmaster.magarveylab.matching.chem.ChemicalAbstraction;
import ca.mcmaster.magarveylab.matching.enums.ChemicalClassifications.ChemicalSubtype;
import ca.mcmaster.magarveylab.matching.enums.ChemicalClassifications.ChemicalType;
import ca.mcmaster.magarveylab.matching.chem.ChemicalAbstraction.Presence;
import ca.mcmaster.magarveylab.matching.chem.ChemicalNode;
import ca.mcmaster.magarveylab.matching.chem.ChemicalNodeString;
import ca.mcmaster.magarveylab.matching.enums.AcylAdenylatingSubstrates;
import ca.mcmaster.magarveylab.matching.enums.Monomers.CStarterEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.NonSpecificTailoringEnum;
import ca.mcmaster.magarveylab.matching.enums.Sugars.SugarGeneEnum;
import ca.mcmaster.magarveylab.matching.enums.Type2Cyclases.Type2CyclaseGene;
import ca.mcmaster.magarveylab.matching.enums.Type2Cyclases.Type2Scaffold;

public class ScoreCalculator {
	
	ScoringScheme scoringScheme;
	private static int maxPermutationSize = 6;  //TODO: make variable  
	public ScoreCalculator() {
		scoringScheme = new ScoringScheme(AlignmentType.GLOBAL, ScoringType.BASIC, true, true);
	}
	/**
	 * @param alignmentType
	 * @param scoringType
	 * @param useNonSpecificTailoring
	 * @param useAuxil Turns on long chain bonus and missing glycotransferase penalty
	 */
	public ScoreCalculator(ScoringScheme scoringScheme) {
		this.scoringScheme = scoringScheme;
	}
	
	public AlignmentObject getUnpermutedAlignment(Breakdown queryBreakdown, Breakdown subjectBreakdown){
		EditTranscript editTranscript = new EditTranscript();
		double rawScore = Double.NEGATIVE_INFINITY;
		double scaledScore = Double.NEGATIVE_INFINITY;
		ChemicalAbstraction queryAbstraction = queryBreakdown.getChemicalAbstraction();
		ChemicalAbstraction subjectAbstraction = subjectBreakdown.getChemicalAbstraction();	
		boolean queryIsPrism = queryBreakdown instanceof PrismBreakdown;
		
		double sugarScore = 0.00;
		if(scoringScheme.useExactSugarMatching){
			sugarScore = getSugarBonusScore(queryBreakdown, subjectBreakdown);
		}
		
		double regulatorScore = 0.00;
		if(queryBreakdown instanceof PrismBreakdown && subjectBreakdown instanceof PrismBreakdown){
			PrismBreakdown queryPrism = (PrismBreakdown)queryBreakdown;
			PrismBreakdown subjectPrism = (PrismBreakdown)subjectBreakdown;
			if(queryPrism.getRegulators().size() > 0){
				double queryRegulatorScore = RegulatorScoring.getRegulatoryMatchScore(queryPrism.getRegulators(), queryPrism.getRegulators());
				double subjectRegulatorScore = RegulatorScoring.getRegulatoryMatchScore(subjectPrism.getRegulators(), subjectPrism.getRegulators());
				double rawRegulatorScore = RegulatorScoring.getRegulatoryMatchScore(queryPrism.getRegulators(), subjectPrism.getRegulators());
				if(rawRegulatorScore/queryRegulatorScore > rawRegulatorScore/subjectRegulatorScore){
					regulatorScore = rawRegulatorScore/subjectRegulatorScore;
				}else {
					regulatorScore = rawRegulatorScore/queryRegulatorScore;
				}
			}
		}
		
		switch(scoringScheme.alignmentType) {
		case GLOBAL:
			rawScore = Alignment.performGlobalAlignment(queryAbstraction.getNodeStrings(),
					subjectAbstraction.getNodeStrings(),
					editTranscript,
					scoringScheme,
					queryIsPrism);
			break;
		case LOCAL:
			rawScore = Alignment.performLocalAlignment(queryAbstraction.getNodeStrings(), 
					subjectAbstraction.getNodeStrings(),
					editTranscript,
					scoringScheme,
					queryIsPrism);
			break;
		}
				
		//scaledScore = rawScore / (Math.sqrt(queryAbstraction.getNumNodes()+ 1) * Math.sqrt(subjectAbstraction.getNumNodes() + 1));
		scaledScore = rawScore;
		scaledScore += addExtraScores(queryAbstraction.getNodeStrings(), subjectAbstraction.getNodeStrings(), queryBreakdown, subjectBreakdown);
		
		String alignmentString = AlignmentObject.getAlignmentString(
			queryAbstraction.getNodeStrings(), 
			queryAbstraction,
			subjectAbstraction.getNodeStrings(), 
			subjectAbstraction,
			editTranscript);
		
		return new AlignmentObject(
			scaledScore, 
			rawScore,
			sugarScore,
			regulatorScore,
			editTranscript,
			alignmentString,
			queryBreakdown,
			subjectBreakdown);
	}
	
	/**
	 * Get the best alignment of two chemical abstractions using a Needleman-Wunsch - like global alignment algorithm
	 * @param grapeAbstraction
	 * @param prismAbstraction
	 * @return
	 */
	public AlignmentObject getBestAlignment(Breakdown queryBreakdown, Breakdown subjectBreakdown) {
		
		ChemicalAbstraction queryAbstraction = queryBreakdown.getChemicalAbstraction();
		ChemicalAbstraction subjectAbstraction = subjectBreakdown.getChemicalAbstraction();
		boolean queryIsPrism = queryBreakdown instanceof PrismBreakdown;
		
		int numQueryStrings = queryAbstraction.getNodeStrings().size();
		int numSubjectStrings = subjectAbstraction.getNodeStrings().size();
		
		//no need to permute if either query or subject is empty
		if (numQueryStrings < 1 || numSubjectStrings < 1) {
			return getUnpermutedAlignment(queryBreakdown, subjectBreakdown);
		}
		
		AlignmentObject alignmentObj = null;
		double sugarScore = 0.00;
		if(scoringScheme.useExactSugarMatching){
			sugarScore = getSugarBonusScore(queryBreakdown, subjectBreakdown);
		}
		
		double regulatorScore = 0.00;
		if(queryBreakdown instanceof PrismBreakdown && subjectBreakdown instanceof PrismBreakdown){
			PrismBreakdown queryPrism = (PrismBreakdown)queryBreakdown;
			PrismBreakdown subjectPrism = (PrismBreakdown)subjectBreakdown;
			if(queryPrism.getRegulators().size() > 0){
				double queryRegulatorScore = RegulatorScoring.getRegulatoryMatchScore(queryPrism.getRegulators(), queryPrism.getRegulators());
				double subjectRegulatorScore = RegulatorScoring.getRegulatoryMatchScore(subjectPrism.getRegulators(), subjectPrism.getRegulators());
				double rawRegulatorScore = RegulatorScoring.getRegulatoryMatchScore(queryPrism.getRegulators(), subjectPrism.getRegulators());
				if(rawRegulatorScore/queryRegulatorScore > rawRegulatorScore/subjectRegulatorScore){
					regulatorScore = rawRegulatorScore/subjectRegulatorScore;
				}else {
					regulatorScore = rawRegulatorScore/queryRegulatorScore;
				}
			}
		}
		
		boolean allStartsAreKnown = true;
		for(ChemicalNodeString cns : queryAbstraction.getNodeStrings()){
			if(!cns.isKnownStart()) {
				allStartsAreKnown = false;
			}
		}
		for(ChemicalNodeString cns : subjectAbstraction.getNodeStrings()){
			if(!cns.isKnownStart()) {
				allStartsAreKnown = false;
			}
		}
		
		EditTranscript editTranscript = new EditTranscript();
		//negation of: there is exactly one that is > maxPermutationSize and all starts are known (can run PermutationAnalyzer)
		if(!allStartsAreKnown 
				|| (numQueryStrings > maxPermutationSize && numSubjectStrings > maxPermutationSize) 
				|| (numQueryStrings <= maxPermutationSize && numSubjectStrings <= maxPermutationSize)) {
			double maxScore = Double.NEGATIVE_INFINITY;
			double maxScoreRaw = Double.NEGATIVE_INFINITY;
			int[][]  queryPermutations = Permutation.getPermutations(numQueryStrings, maxPermutationSize); 
			int[][]  subjectPermutations = Permutation.getPermutations(numSubjectStrings, maxPermutationSize);
			//Note that there are lots here with queries of length 1 or 0
			List<ChemicalNodeString> bestQueryPermutation = null;
			List<ChemicalNodeString> bestSubjectPermutation = null;
			EditTranscript bestEditTranscript = null;
			
			for(int queryIndex = 0; queryIndex < queryPermutations.length; queryIndex++){
				List<List<ChemicalNodeString>> queryNodeStringsCyclicCombinations = new ArrayList<List<ChemicalNodeString>>();
				List<ChemicalNodeString> singleQueryNodeStrings = new ArrayList<ChemicalNodeString>(); 
				for(int i = 0; i < queryPermutations[queryIndex].length; i++) {
					singleQueryNodeStrings.add(queryAbstraction.getNodeStrings().get(queryPermutations[queryIndex][i]));
				}
				if(!queryAbstraction.hasOnlyKnownStarts()){ //Check if the compound has a pure cyclic string, this means that every monomer needs to have a chance at first
					queryNodeStringsCyclicCombinations = rearrangeCyclicNodeStrings(singleQueryNodeStrings);					
				}else{
					queryNodeStringsCyclicCombinations.add(singleQueryNodeStrings);
				}
				if(subjectPermutations == null) {
					continue;
				}
				for(List<ChemicalNodeString> queryNodeStrings : queryNodeStringsCyclicCombinations){
					for(int subjectIndex = 0; subjectIndex < subjectPermutations.length; subjectIndex++) {
						List<List<ChemicalNodeString>> subjectNodeStringsCyclicCombinations = new ArrayList<List<ChemicalNodeString>>();
						List<ChemicalNodeString> singleSubjectNodeStrings = new ArrayList<ChemicalNodeString>(); 
						for(int i = 0; i < subjectPermutations[subjectIndex].length; i++) {
							singleSubjectNodeStrings.add(subjectAbstraction.getNodeStrings().get(subjectPermutations[subjectIndex][i]));
						}
						if(!subjectAbstraction.hasOnlyKnownStarts()){
							subjectNodeStringsCyclicCombinations = rearrangeCyclicNodeStrings(singleSubjectNodeStrings);					
						}else{
							subjectNodeStringsCyclicCombinations.add(singleSubjectNodeStrings);
						}
						
						for(List<ChemicalNodeString> subjectNodeStrings : subjectNodeStringsCyclicCombinations){

							editTranscript = new EditTranscript();
							double rawScore = Double.NEGATIVE_INFINITY;
							double scaledScore = Double.NEGATIVE_INFINITY;
							
							switch(scoringScheme.alignmentType) {
							case GLOBAL:
								rawScore = Alignment.performGlobalAlignment(queryNodeStrings,
										subjectNodeStrings,
										editTranscript,
										scoringScheme,
										queryIsPrism);
								break;
							case LOCAL:
								rawScore = Alignment.performLocalAlignment(queryNodeStrings, 
										subjectNodeStrings,
										editTranscript,
										scoringScheme,
										queryIsPrism);
								break;
							}
							
							// Scale the score
							//scaledScore = rawScore / Math.min(queryAbstraction.getNumNodes(), subjectAbstraction.getNumNodes());
//							if(scoringScheme.scoringType == ScoringType.GRAPEPRISM){
//							scaledScore = rawScore / queryAbstraction.getNumNodes();
//						}else{
							//scaledScore = rawScore / (Math.sqrt(queryAbstraction.getNumNodes()+1) * Math.sqrt(subjectAbstraction.getNumNodes()+1));
							scaledScore = rawScore;
//						}
							scaledScore += addExtraScores(queryNodeStrings, subjectNodeStrings, queryBreakdown, subjectBreakdown);
							
							if(scaledScore > maxScore) {
								maxScore = scaledScore;
								maxScoreRaw = rawScore;
								bestQueryPermutation = queryNodeStrings;
								bestSubjectPermutation = subjectNodeStrings;
								bestEditTranscript = editTranscript;
							}
						}
					}
				}
			}
			
			String alignmentString = AlignmentObject.getAlignmentString(
					bestQueryPermutation, 
					queryAbstraction,
					bestSubjectPermutation, 
					subjectAbstraction,
					bestEditTranscript);
			
			 alignmentObj = new AlignmentObject(
					maxScore, 
					maxScoreRaw,
					sugarScore,
					regulatorScore,
					bestEditTranscript,
					alignmentString,
					queryBreakdown,
					subjectBreakdown,
					bestQueryPermutation, 
					bestSubjectPermutation);
		}else{
			PermutationAnalyzer permutationAnalyzer;
			List<ChemicalNodeString> queryNodeStrings;
			List<ChemicalNodeString> subjectNodeStrings;
			if(numSubjectStrings > numQueryStrings){ //one must be greater than other to pass the previous if statement.
				permutationAnalyzer = new PermutationAnalyzer(subjectAbstraction, queryAbstraction, scoringScheme, queryIsPrism);
				subjectNodeStrings = permutationAnalyzer.getBestLargeNodeStringsOrdered();
				queryNodeStrings = permutationAnalyzer.getBestSmallNodeStringsOrdered();
			}else{
				permutationAnalyzer = new PermutationAnalyzer(queryAbstraction, subjectAbstraction, scoringScheme, queryIsPrism);
				subjectNodeStrings = permutationAnalyzer.getBestSmallNodeStringsOrdered();
				queryNodeStrings = permutationAnalyzer.getBestLargeNodeStringsOrdered();
				permutationAnalyzer.getBestEditTranscript().flip();
			}
			if (permutationAnalyzer.getBestEditTranscript() == null) {
				System.out.println("BestEditTranscript is null!");
			}
			double scaledScore;
			//if(scoringScheme.scoringType == ScoringType.GRAPEPRISM){
				//scaledScore = permutationAnalyzer.getBestScore() / queryAbstraction.getNumNodes();
			//}else{
			//scaledScore = permutationAnalyzer.getBestScore() / (Math.sqrt(queryAbstraction.getNumNodes()+1) * Math.sqrt(subjectAbstraction.getNumNodes()+1));
			scaledScore = permutationAnalyzer.getBestScore();
			//}
			scaledScore += addExtraScores(queryNodeStrings, subjectNodeStrings, queryBreakdown, subjectBreakdown);
			
			String alignmentString = AlignmentObject.getAlignmentString(
					queryNodeStrings, 
					queryAbstraction,
					subjectNodeStrings,
					subjectAbstraction,
					permutationAnalyzer.getBestEditTranscript());
			
			alignmentObj = new AlignmentObject(
					scaledScore, 
					permutationAnalyzer.getBestScore(),
					sugarScore,
					regulatorScore,
					permutationAnalyzer.getBestEditTranscript(),
					alignmentString,
					queryBreakdown,
					subjectBreakdown,
					queryNodeStrings,
					subjectNodeStrings);
		}
		return alignmentObj;
	}
	
	
	/**
	 * This method returns the full list of chemicalNodeStrings for a chemicalAbstraction with the cyclized fragment being changed in
	 * each list. The cyclized fragment has each monomer as first, since the start is unknown.
	 * @param singleGrapeNodeStrings
	 * @return
	 */
	private List<List<ChemicalNodeString>> rearrangeCyclicNodeStrings(List<ChemicalNodeString> singleGrapeNodeStrings) {
		List<List<ChemicalNodeString>> nodeStringsCyclicCombinations = new ArrayList<List<ChemicalNodeString>>();
		for(ChemicalNodeString nodeString : singleGrapeNodeStrings){
			if(!nodeString.isKnownStart()){
				for(int i = 0; nodeString.getChemicalNodes().size() -1 > i; i++){
					List<ChemicalNodeString> cyclicNodeStrings = new ArrayList<ChemicalNodeString>();
					for(int j = 0; singleGrapeNodeStrings.size() > j; j++) {
						ChemicalNodeString singleNodeString = singleGrapeNodeStrings.get(j);
						if(singleNodeString.equals(nodeString) && i != 0){
							List<ChemicalNode> topNodeString = singleNodeString.getChemicalNodes().subList(0, i);
							List<ChemicalNode> bottomNodeString =  singleNodeString.getChemicalNodes().subList(i, singleNodeString.getChemicalNodes().size());
							ArrayList<ChemicalNode> rearrangedNodes =  new ArrayList<ChemicalNode>(bottomNodeString);
							rearrangedNodes.addAll(topNodeString);
							ChemicalNodeString rearrangedNodeString = new ChemicalNodeString(rearrangedNodes);
							cyclicNodeStrings.add(rearrangedNodeString);
						}else{
							cyclicNodeStrings.add(singleNodeString);
						}
					}
					nodeStringsCyclicCombinations.add(cyclicNodeStrings);
				}
			}
		}
		return nodeStringsCyclicCombinations;
	}
	
	private double getStarterBonusScore(Breakdown queryBreakdown, Breakdown subjectBreakdown) {
		double starterBonusScore = 0;
		
		ChemicalAbstraction queryChemicalAbstraction = queryBreakdown.getChemicalAbstraction();
		ChemicalAbstraction subjectChemicalAbstraction = subjectBreakdown.getChemicalAbstraction();
		
		List<AcylAdenylatingSubstrates> querySubstrates = new ArrayList<AcylAdenylatingSubstrates>();
		for(AcylAdenylatingSubstrates queryStarter : queryChemicalAbstraction.getAcylAdenylatingSubstrates()){
			querySubstrates.add(queryStarter);
		}

		Presence queryFattyAcid = queryChemicalAbstraction.getFattyAcidStatus();
		Presence subjectFattyAcid = subjectChemicalAbstraction.getFattyAcidStatus();

		//Calculate Fatty Acid Scores (starterBonus)
		if (queryBreakdown instanceof PrismBreakdown && subjectBreakdown instanceof PrismBreakdown){
			if(queryFattyAcid == Presence.TRUE && subjectFattyAcid == Presence.TRUE){ //Check if both contain a C starter
				starterBonusScore += scoringScheme.starterBonus;
				//OPT:  unfair bonus to PRISM/PRISM
				/**
				for(CStarterEnum queryFA : queryChemicalAbstraction.getFattyAcids()){
					for(CStarterEnum subjectFA : subjectChemicalAbstraction.getFattyAcids()){
						if(queryFA == subjectFA){
							starterBonusScore += scoringScheme.starterBonus;
						}
					}
				}
				**/
			}else{ //If there was a C starter don't also check for presence of a fat since this was already added
				boolean subjectFA = false;
				boolean queryFA = false;
				for(AcylAdenylatingSubstrates starter : subjectChemicalAbstraction.getAcylAdenylatingSubstrates()){
					if(starter.name().endsWith("FATTY_ACID") || starter.name().equals("MYRISTATE")){
						subjectFA = true;
					}
				}
				for(AcylAdenylatingSubstrates starter : queryChemicalAbstraction.getAcylAdenylatingSubstrates()){
					if(starter.name().endsWith("FATTY_ACID") || starter.name().equals("MYRISTATE")){
						queryFA = true;
					}
				}
				if((subjectFA == true && queryFA == true) || (subjectFA == true && queryFattyAcid == Presence.TRUE) || (queryFA == true && subjectFattyAcid == Presence.TRUE)){
					starterBonusScore += scoringScheme.starterBonus;
				}
			}
		}else{ //GRAPEPRISM or PRISMGRAPE or GRAPEGRAPE
			if(queryFattyAcid == Presence.TRUE || queryFattyAcid == Presence.POSSIBLE){
				if(subjectFattyAcid == Presence.TRUE || subjectFattyAcid == Presence.POSSIBLE)	starterBonusScore += scoringScheme.starterBonus;
			}
		}

		//Calculate Acyl Adenylating Substrate Scores
		for(AcylAdenylatingSubstrates subjectStarter : subjectChemicalAbstraction.getAcylAdenylatingSubstrates()){
			for(int i = 0; querySubstrates.size() > i; i++){
				AcylAdenylatingSubstrates queryStarter = querySubstrates.get(i);
				if(subjectStarter == queryStarter){
					starterBonusScore += scoringScheme.acylAdenylatingBonus; 
					querySubstrates.remove(i);
					break;
				}else{
					boolean matched = false;
					for(AcylAdenylatingSubstrates[] identicalList : ScoringScheme.identicalAcylAdenylatingSubstrates) {
						if(Arrays.asList(identicalList).contains(queryStarter)
								&& Arrays.asList(identicalList).contains(subjectStarter)) {
							starterBonusScore += scoringScheme.acylAdenylatingBonus;
							querySubstrates.remove(i);
							matched = true;
							break;
						}
					}
					if(matched) break;
				}
			}
		}
		return starterBonusScore;
	}
	
	private double getNonSpecificTailoringScore(List<NonSpecificTailoringEnum> queryNonSpecificTailorings, List<NonSpecificTailoringEnum> subjectNonSpecificTailorings) {
		double cost = 0;
		
		boolean[] subjectNonSpecificTailoringVisited = new boolean[subjectNonSpecificTailorings.size()];
		for(NonSpecificTailoringEnum queryTailoring : queryNonSpecificTailorings) {
			for(int i = 0; i < subjectNonSpecificTailorings.size(); i++) {
				if(subjectNonSpecificTailoringVisited[i]) {
					continue;
				}				
				if(subjectNonSpecificTailorings.get(i).equals(queryTailoring)) {
					subjectNonSpecificTailoringVisited[i] = true;
					
					if(queryTailoring.equals(NonSpecificTailoringEnum.CHLORINATION)) {
						cost += scoringScheme.nonSpecificTailoringBonus*2; // double for chloro
					}
					else if(queryTailoring.equals(NonSpecificTailoringEnum.GLYCOSYLTRANSFERASE)) {
						cost += scoringScheme.nonSpecificTailoringBonus;
					}
					else if(queryTailoring.equals(NonSpecificTailoringEnum.SULFOTRANSFERASE)) {
						cost += scoringScheme.nonSpecificTailoringBonus;
					}
					else if (queryTailoring.equals(NonSpecificTailoringEnum.SULFUR_BETA_LACTAM)) {
						cost += scoringScheme.sulfurBetaLactamBonus;
					}
					if(!scoringScheme.useExactSugarMatching) {
						if(queryTailoring.equals(NonSpecificTailoringEnum.SUGAR_AMINOTRANSFER)) {
							cost += scoringScheme.nonSpecificTailoringBonus;
						}
						else if(queryTailoring.equals(NonSpecificTailoringEnum.SUGAR_NMETHYLTRANSFER)) {
							cost += scoringScheme.nonSpecificTailoringBonus;
						}
						else if(queryTailoring.equals(NonSpecificTailoringEnum.SUGAR_OMETHYLTRNASFER)) {
							cost += scoringScheme.nonSpecificTailoringBonus;
						}
					}
				}
			}
		}
		
		//TODO: think about whether costs are necessary. For now, just add for matches.
		// Check if chlorination status is not identical
		//if(queryNonSpecificTailorings.contains(NonSpecificTailoringEnum.CHLORINATION) && subjectNonSpecificTailorings.contains(NonSpecificTailoringEnum.CHLORINATION)) {
			//cost += nonSpecificTailoringBonus;
		//}
		if(!scoringScheme.useExactSugarMatching) {
			if(queryNonSpecificTailorings.contains(NonSpecificTailoringEnum.GLYCOSYLTRANSFERASE) != subjectNonSpecificTailorings.contains(NonSpecificTailoringEnum.GLYCOSYLTRANSFERASE)) {
				cost += scoringScheme.sugarMissingPenalty;
			}
		}
		/*
		if(queryNonSpecificTailorings.contains(NonSpecificTailoringEnum.FATTY_ACID) != subjectNonSpecificTailorings.contains(NonSpecificTailoringEnum.FATTY_ACID)) {
			cost += nonSpecificTailoringPenalty;
		}
		*/
		return cost;
	}
	
	/**
	 * @param queryBreakdown
	 * @param subjectBreakdown
	 * @return
	 */
	private double getSugarBonusScore(Breakdown queryBreakdown, Breakdown subjectBreakdown) { 
		double bestScore = Double.NEGATIVE_INFINITY; 
		
		List<HashMap<SugarGeneEnum, Integer>> querySugarGenesCombos;
		List<HashMap<SugarGeneEnum, Integer>> subjectSugarGenesCombos;
		List<HashMap<SugarGeneEnum, Integer>> nonemptySugarGenesCombos;

		querySugarGenesCombos = queryBreakdown.getChemicalAbstraction().getSugarGenes();
		subjectSugarGenesCombos = subjectBreakdown.getChemicalAbstraction().getSugarGenes();
		
		if((querySugarGenesCombos.size() == 0 && subjectSugarGenesCombos.size() != 0)
				|| (querySugarGenesCombos.size() != 0 && subjectSugarGenesCombos.size() == 0)){
			if(querySugarGenesCombos.size() == 0) {
				nonemptySugarGenesCombos = subjectSugarGenesCombos;
			} else {
				nonemptySugarGenesCombos = querySugarGenesCombos;
			}
			double combo_value;
			for(HashMap<SugarGeneEnum, Integer> nonemptySugarGenes : nonemptySugarGenesCombos){
				combo_value = 0;
				for(Entry<SugarGeneEnum, Integer> nonemptyEntry : nonemptySugarGenes.entrySet()){
					combo_value += scoringScheme.sugarTypePenalty;
					combo_value += nonemptyEntry.getValue() * scoringScheme.sugarNumberPenalty;
				}
				if(combo_value > bestScore) bestScore = combo_value;
			}
		}else if(querySugarGenesCombos.size() != 0 && subjectSugarGenesCombos.size() != 0){
			for(HashMap<SugarGeneEnum, Integer> querySugarGenes : querySugarGenesCombos){
				for(HashMap<SugarGeneEnum, Integer> subjectSugarGenes : subjectSugarGenesCombos){
					double score = 0.0;
					List<SugarGeneEnum> sugarMatches = new ArrayList<SugarGeneEnum>();
					for(Entry<SugarGeneEnum, Integer> queryEntry : querySugarGenes.entrySet()){
						boolean match = false;
						for(Entry<SugarGeneEnum, Integer> subjectEntry : subjectSugarGenes.entrySet()){
							if(queryEntry.getKey() != subjectEntry.getKey()) continue;
							match = true;
							sugarMatches.add(subjectEntry.getKey());
							score += scoringScheme.sugarTypeBonus;
							int diff = queryEntry.getValue() - subjectEntry.getValue();
							int number_matched = Math.min(queryEntry.getValue(), subjectEntry.getValue());
							score += number_matched * scoringScheme.sugarNumberBonus;
							score += Math.abs(diff) * scoringScheme.sugarNumberPenalty;
							break;
						}
						if(!match) score += scoringScheme.sugarTypePenalty + scoringScheme.sugarNumberPenalty*queryEntry.getValue();
					}
					//Check if there were unmatched subject sugars and penalize
					for(Entry<SugarGeneEnum, Integer> subjectEntry : subjectSugarGenes.entrySet()){
						if(!sugarMatches.contains(subjectEntry.getKey())){
							score += scoringScheme.sugarTypePenalty;
							score += scoringScheme.sugarNumberPenalty*subjectEntry.getValue();
						}
					}
					if(bestScore < score) bestScore = score;
				}
			}
		} else { //both do not have combos
			bestScore = scoringScheme.sugarTypeBonus;  //score a bit extra if they both do not have sugars
		}
		assert (bestScore != (double) Double.NEGATIVE_INFINITY); 
		return bestScore;
	}
	
	private double addExtraScores(List<ChemicalNodeString> queryNodeStrings, List<ChemicalNodeString> subjectNodeStrings, Breakdown queryBreakdown, Breakdown subjectBreakdown){
		double scoreToAdd = 0.00;
		
		if(scoringScheme.orfNumberBonus != 0){
			if(subjectNodeStrings.size() == queryNodeStrings.size()){
				for(int i = 0; queryNodeStrings.size() > i; i++){
					if(queryNodeStrings.get(i).getChemicalNodes().size() == subjectNodeStrings.get(i).getChemicalNodes().size()){
						scoreToAdd += scoringScheme.orfNumberBonus;
					}
				}
			}else{
				scoreToAdd -= Math.abs(subjectNodeStrings.size() - queryNodeStrings.size()) * scoringScheme.orfNumberBonus;
			}
		}
		
		if(scoringScheme.starterBonus != 0 || scoringScheme.acylAdenylatingBonus != 0){
			scoreToAdd += getStarterBonusScore(queryBreakdown, subjectBreakdown);
		}
		
		ChemicalAbstraction queryAbstraction = queryBreakdown.getChemicalAbstraction();
		ChemicalAbstraction subjectAbstraction = subjectBreakdown.getChemicalAbstraction();
		
		if(scoringScheme.useNonspecificTailoring) {
			scoreToAdd += getNonSpecificTailoringScore(queryAbstraction.getNonSpecificTailorings(), subjectAbstraction.getNonSpecificTailorings());
		}
		
		if(scoringScheme.useExactSugarMatching){
			scoreToAdd += getSugarBonusScore(queryBreakdown, subjectBreakdown);
		}
		
		if(scoringScheme.aminoAcidDifferencePenalty != 0){
			Integer aaModDifference = Math.abs(queryAbstraction.numAminoAcidModules() - subjectAbstraction.numAminoAcidModules());
			if(aaModDifference > 1){
				scoreToAdd += scoringScheme.aminoAcidDifferencePenalty;
			}
		}
		
		if(scoringScheme.chemicalTypeBonus != 0){ 
			scoreToAdd += getChemicalTypeScore(queryBreakdown, subjectBreakdown);
		}
		return scoreToAdd;
	}
	
	/**
	 * Gives chemicalType scores for non type1 pk and non nrp molecules as a way to score these
	 * Also works with the chemical scaffold bonus score
	 * @param queryBreakdown
	 * @param subjectBreakdown
	 * @return
	 */
	private double getChemicalTypeScore(Breakdown queryBreakdown, Breakdown subjectBreakdown){
		double scoreToAdd = 0.0;
		ChemicalAbstraction queryAbstraction = queryBreakdown.getChemicalAbstraction();
		ChemicalAbstraction subjectAbstraction = subjectBreakdown.getChemicalAbstraction();
		
		//type 2 matching
		if(queryAbstraction.getChemicalSubtype() == ChemicalSubtype.TYPE_2 && subjectAbstraction.getChemicalSubtype() == ChemicalSubtype.TYPE_2){
			scoreToAdd += scoringScheme.chemicalTypeBonus;
			if(queryBreakdown instanceof PrismBreakdown && subjectBreakdown instanceof PrismBreakdown){ //prism match by genes
				List<Type2CyclaseGene> queryGenes = queryAbstraction.getType2CyclaseGenes();
				List<Type2CyclaseGene> subjectGenes = subjectAbstraction.getType2CyclaseGenes();
				if(queryGenes.size() == 0 && subjectGenes.size() == 0){ //is type 2, but no particular type 2 genes consider it as a match
					scoreToAdd += scoringScheme.scaffoldBonus;
				}else if(queryGenes.size() == subjectGenes.size()){ // if they both have the same number of genes, see if the genes are all the same, if so then give bonus
					int matches = 0;
					for(Type2CyclaseGene queryGene : queryGenes){
						for(Type2CyclaseGene subjectGene : subjectGenes){
							if(queryGene == subjectGene){
								matches += 1;
								break;	
							}
						}
					}
					if(matches == queryGenes.size()){
						scoreToAdd += scoringScheme.scaffoldBonus;
					}
				}
			} else if(queryBreakdown instanceof GrapeBreakdown && subjectBreakdown instanceof GrapeBreakdown){ //grape match by scaffold
				//if(queryAbstraction.getChemicalScaffold().equals(subjectAbstraction.getChemicalScaffold())){
				if(subjectAbstraction.getChemicalScaffold() != null && queryAbstraction.getChemicalScaffold() != null){
					scoreToAdd += getType2ScaffoldScore((Type2Scaffold)queryAbstraction.getChemicalScaffold(), (Type2Scaffold)subjectAbstraction.getChemicalScaffold());
				}
			} else{ //grape prism match by genes (combo of genes by scaffold is generated for grape)
				if(queryBreakdown instanceof PrismBreakdown){ //query is prism
					if(subjectAbstraction.getChemicalScaffold() != null){
						scoreToAdd += getType2ScaffoldScore(queryAbstraction.getType2CyclaseGenes(), (Type2Scaffold)subjectAbstraction.getChemicalScaffold());
					}
				}else{ //query is grape
					if(queryAbstraction.getChemicalScaffold() != null){
						scoreToAdd += getType2ScaffoldScore(subjectAbstraction.getType2CyclaseGenes(), (Type2Scaffold)queryAbstraction.getChemicalScaffold());
					}
				}
			}
		}else if(subjectAbstraction.getChemicalSubtype().equals(queryAbstraction.getChemicalSubtype())){
			if(Arrays.asList(ScoringScheme.rareChemicalSubtypes).contains(subjectAbstraction.getChemicalSubtype())){ //Make array of 'bonus' subtypes //Make array of '
					scoreToAdd += scoringScheme.chemicalTypeBonus;
			}
		}
		return scoreToAdd;
	}
	
	private double getType2ScaffoldScore(Type2Scaffold type2ScaffoldA, Type2Scaffold type2ScaffoldB) {
		double scoreToAdd = 0;
		Type2CyclaseGene[][] geneSetsA = type2ScaffoldA.genes();
		Type2CyclaseGene[][] geneSetsB = type2ScaffoldB.genes();
		
		int numGenesA = Type2Scaffold.count(geneSetsA);
		int numGenesB = Type2Scaffold.count(geneSetsB);
		if(numGenesA == 0 && numGenesB == 0){
			scoreToAdd += scoringScheme.scaffoldBonus;
		}else{
			boolean match = false;
			for(Type2CyclaseGene[] geneSetA : geneSetsA){
				for(Type2CyclaseGene[] geneSetB : geneSetsB){
					int matches = 0;
					if(geneSetA.length != geneSetB.length){
						continue;
					}
					for(Type2CyclaseGene geneA : geneSetA){
						if(Arrays.asList(geneSetB).contains(geneA)){
							matches++;
						}
					}
					if(matches == geneSetA.length){
						scoreToAdd += scoringScheme.scaffoldBonus;
						match = true;
						break;
					}
				}
				if(match){
					break;
				}
			}
		}
		return scoreToAdd;
	}
	
	private double getType2ScaffoldScore(List<Type2CyclaseGene> type2CyclaseGenes, Type2Scaffold type2Scaffold) {
		double scoreToAdd = 0;
		Type2CyclaseGene[][] genes = type2Scaffold.genes();
		int numGenes = Type2Scaffold.count(genes);
		if(numGenes == 0 && type2CyclaseGenes.size() == 0){
			scoreToAdd += scoringScheme.scaffoldBonus;
		}else if(numGenes != 0 && type2CyclaseGenes.size() != 0){
			int matches = 0;
			for(int i = 0; genes.length > i; i++){
				for(Type2CyclaseGene gene : genes[i]){
					for(Type2CyclaseGene other : type2CyclaseGenes){
						if(other == gene){
							matches += 1;
							break;	
						}
					}
				}
			}
			if(matches == genes.length){
				scoreToAdd += scoringScheme.scaffoldBonus;
			}
		}
		return scoreToAdd;
	}
}