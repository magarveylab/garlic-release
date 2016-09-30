package ca.mcmaster.magarveylab.matching.algorithm.alignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import ca.mcmaster.magarveylab.matching.algorithm.alignment.EditTranscript.EditOperations;
import ca.mcmaster.magarveylab.matching.algorithm.scoring.ScoringScheme;
import ca.mcmaster.magarveylab.matching.chem.ChemicalNode;
import ca.mcmaster.magarveylab.matching.chem.ChemicalNodeString;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.AminoAcidNode;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.PKPieceNode;
import ca.mcmaster.magarveylab.matching.enums.Monomers.AminoAcidEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKOxidationStateEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKSubstrateEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.SiteSpecificTailoringEnum;

public class Alignment {
		
	/**
	 * Perform Needleman-Wunsch - like global alignment
	 * @param queryNodeStrings
	 * @param grapeNonSpecificTailorings
	 * @param subjectNodeStrings
	 * @param prismNonSpecificTailorings
	 * @param editTranscript
	 * @param queryPrism for scoring purposes to check if the query is PRISM or GRAPE
	 * @return the similarity score between the two chemical node strings
	 */
	public static double performGlobalAlignment(List<ChemicalNodeString> queryNodeStrings,
			List<ChemicalNodeString> subjectNodeStrings,
			EditTranscript editTranscript,
			ScoringScheme scoringScheme,
			boolean queryPrism) {
		//TODO:  Bad use of queryPrism, 1) should at least have subjectPrism 2) better to be agnostic, have neither
		//TODO:  (cont) only used for between/within grape and prism scores, these should probably be merged anyhow
		// Keep track of the GRAPE and PRISM nodes that are the start of node strings
		List<ChemicalNode> queryEndingNodes = new ArrayList<ChemicalNode>();
		List<ChemicalNode> subjectEndingNodes = new ArrayList<ChemicalNode>();
		
		//boolean hasMultipleAminoAcidPiece = false;
		
		ArrayList<ChemicalNode> queryString = new ArrayList<ChemicalNode>();
		for(ChemicalNodeString nodeString : queryNodeStrings) {
			if(nodeString.getChemicalNodes().isEmpty()) {
				continue;
			}
			queryEndingNodes.add(nodeString.getChemicalNodes().get(nodeString.getChemicalNodes().size()-1));
			
			for(ChemicalNode queryNode : nodeString.getChemicalNodes()) {
		//		if(grapeNode.getClass() == MultipleAminoAcidNode.class) {
		//		hasMultipleAminoAcidPiece = true;
		//			continue;
		//		}
				queryString.add(queryNode);
			}
		}
		
		ArrayList<ChemicalNode> subjectString = new ArrayList<ChemicalNode>();
		for(ChemicalNodeString nodeString : subjectNodeStrings) {
			if(nodeString.getChemicalNodes().isEmpty()) {
				continue;
			}
			subjectEndingNodes.add(nodeString.getChemicalNodes().get(nodeString.getChemicalNodes().size()-1));
			for(ChemicalNode subjectNode : nodeString.getChemicalNodes()) {
				subjectString.add(subjectNode);
			}
		}
		
		// Create our dynamic programming lookup table. The element lookup[i][j] represents the min cost of the Grape string
		// of the first i elements (indices 0 to i-1 inclusive), and the Prism string of the first j elements (indices 0 to j-1 inclusive).
		
		EditOperations[][] editOperations = new EditOperations[queryString.size()+1][subjectString.size()+1];
		double[][] lookup = new double[queryString.size()+1][subjectString.size()+1];
		
		// Base cases
		// Base case for when j = 0 (the Prism string is empty, and the best match consists of j Prism gaps
		lookup[0][0] = 0;
		editOperations[0][0] = EditOperations.D;
		for(int i = 1; i <= queryString.size(); i++) {
			if(queryString.get(i-1) instanceof PKPieceNode && ((PKPieceNode) queryString.get(i-1)).isPossibleNonPK()) {
				lookup[i][0] = lookup[i-1][0] + scoringScheme.fattyAcidOrPKGapPenalty;
			} else {
				if(queryPrism){
					lookup[i][0] = lookup[i-1][0] + scoringScheme.betweenGrapeSequenceGapPenalty;
				}else{
					lookup[i][0] = lookup[i-1][0] + scoringScheme.betweenPrismSequenceGapPenalty;
				}
			}
			editOperations[i][0] = EditOperations.D;
		}
		// Base case for when i = 0 (the Grape string is empty, and the best match consists of i Grape gaps
		for(int j = 1; j <= subjectString.size(); j++) {
			if(subjectString.get(j-1) instanceof PKPieceNode && ((PKPieceNode) subjectString.get(j-1)).isPossibleNonPK()) {
				lookup[0][j] = lookup[0][j-1] + scoringScheme.fattyAcidOrPKGapPenalty;
			} else {
				if(queryPrism){
					lookup[0][j] = lookup[0][j-1] + scoringScheme.betweenPrismSequenceGapPenalty;
				}else{
					lookup[0][j] = lookup[0][j-1] + scoringScheme.betweenGrapeSequenceGapPenalty;
				}
			}
			editOperations[0][j] = EditOperations.I;
		}
		
		//Prep calcs for inner loop below to determine if within or between subjects
		boolean fromWithinQuery;
		boolean fattyAcidOrPKGap;
		boolean[] fromWithinSubjects = new boolean[subjectString.size()];
		for(int j = 0; j < subjectString.size(); j++) {
			fromWithinSubjects[j] = subjectEndingNodes.contains(subjectString.get(j));
		}
		
		// Fill the rest of the table.
		for(int i = 1; i < queryString.size()+1; i++) {
			fromWithinQuery = queryEndingNodes.contains(queryString.get(i-1));
			fattyAcidOrPKGap = (queryString.get(i-1) instanceof PKPieceNode && ((PKPieceNode) queryString.get(i-1)).isPossibleNonPK());
			for(int j = 1; j < subjectString.size()+1; j++) {
				//{I, D, M}
				// Insertion: this corresponds to a Query gap
				double insertionScore = lookup[i][j-1];
				if(subjectString.get(j-1) instanceof PKPieceNode && ((PKPieceNode) subjectString.get(j-1)).isPossibleNonPK()) {
					insertionScore += scoringScheme.fattyAcidOrPKGapPenalty;
				} else {
					if(!fromWithinQuery) {
						if(queryPrism){
							insertionScore += scoringScheme.betweenPrismSequenceGapPenalty;
						}else{
							insertionScore += scoringScheme.betweenGrapeSequenceGapPenalty;
						}
					}
					else {
						if(queryPrism){
							insertionScore += scoringScheme.withinPrismSequenceGapPenalty;
						}else{
							insertionScore += scoringScheme.withinGrapeSequenceGapPenalty;
						}
					}
				}
				// Deletion: this corresponds to a Subject gap
				double deletionScore = lookup[i-1][j];
				if (fattyAcidOrPKGap) {
					deletionScore += scoringScheme.fattyAcidOrPKGapPenalty;
				}
				else if(!fromWithinSubjects[j-1]) {
					//TODO:  should instead test if the subject is Prism or not
					if(queryPrism){
						deletionScore += scoringScheme.betweenGrapeSequenceGapPenalty;
					}else{
						deletionScore += scoringScheme.betweenPrismSequenceGapPenalty;
					}
				}
				else {
					if(queryPrism){
						deletionScore += scoringScheme.withinGrapeSequenceGapPenalty;
					}else{
						deletionScore += scoringScheme.withinPrismSequenceGapPenalty;
					}
				}
				// Match: this is either a correct match or a substitution
				double substitutionScore = lookup[i-1][j-1];
				substitutionScore += getMatchScore(queryString.get(i-1), subjectString.get(j-1), scoringScheme);
				
				if(insertionScore > deletionScore && insertionScore > substitutionScore) {
					lookup[i][j] = insertionScore;
					editOperations[i][j] = EditOperations.I;
				}
				else if(deletionScore > substitutionScore) {
					lookup[i][j] = deletionScore;
					editOperations[i][j] = EditOperations.D;
				}
				else {
					lookup[i][j] = substitutionScore;
					editOperations[i][j] = EditOperations.M;
				}
			}
		}
		
		// Backtrack to populate the edit transcript
		int i = queryString.size();
		int j = subjectString.size();
		while(i != 0 || j != 0) {
			editTranscript.addEditOperation(editOperations[i][j], lookup[i][j]);
			switch(editOperations[i][j]) {
			case I:
				j--;
				break;
			case D:
				i--;
				break;
			case M:
				i--;
				j--;
				break;
			}
		}
		editTranscript.reverse();
		//System.out.println(editTranscript);
		double score = 0;
		score +=  lookup[queryString.size()][subjectString.size()];
		
		// Scale the score
		//score = score / Math.max(grapeString.size(), prismString.size());

		//score = score / prismString.size();
		//score = score * 5 / 2.0;
		//score = score - 8 / Math.sqrt(prismString.size());
		return score;
	}
	
	/**
	 * Perform Smith-Waterman - like local alignment
	 * @param queryNodeStrings
	 * @param grapeNonSpecificTailorings
	 * @param subjectNodeStrings
	 * @param prismNonSpecificTailorings
	 * @param editTranscript
	 * @param queryPrism whether the query is a PrismBreakdown or not for unnsymmetrical scoring
	 * @return the similarity score between the two chemical node strings
	 */	
	public static double performLocalAlignment(List<ChemicalNodeString> queryNodeStrings,
			List<ChemicalNodeString> subjectNodeStrings,
			EditTranscript editTranscript,
			ScoringScheme scoringScheme,
			boolean queryPrism) {
		
		// Check if Subject output is empty
		boolean subjectEmpty = true;
		for(ChemicalNodeString s : subjectNodeStrings) {
			if(s.getChemicalNodes().size() > 0) {
				subjectEmpty = false;
				break;
			}
		}
		if(subjectEmpty) {
			for(int i = 0; i < queryNodeStrings.size(); i++) {
				for(int j = 0; j < queryNodeStrings.get(i).getChemicalNodes().size(); j++) {
					editTranscript.addEditOperation(EditOperations.D, 0);
				}
			}
			return(0);
		}
		// Check if Query output is empty
		boolean queryEmpty = true;
		for(ChemicalNodeString s : queryNodeStrings) {
			if(s.getChemicalNodes().size() > 0) {
				queryEmpty = false;
				break;
			}
		}
		if(queryEmpty) {
			for(int i = 0; i < subjectNodeStrings.size(); i++) {
				for(int j = 0; j < subjectNodeStrings.get(i).getChemicalNodes().size(); j++) {
					editTranscript.addEditOperation(EditOperations.I, 0);
				}
			}
			return(0);
		}
		
		
		// Keep track of the Query and Subject nodes that are the start of node strings
		List<ChemicalNode> queryEndingNodes = new ArrayList<ChemicalNode>();
		
		List<ChemicalNode> subjectEndingNodes = new ArrayList<ChemicalNode>();
		
		
		//boolean hasMultipleAminoAcidPiece = false;
		
		List<ChemicalNode> queryString = new ArrayList<ChemicalNode>();
		for(ChemicalNodeString nodeString : queryNodeStrings) {
			queryEndingNodes.add(nodeString.getChemicalNodes().get(nodeString.getChemicalNodes().size()-1));
			for(ChemicalNode queryNode : nodeString.getChemicalNodes()) {
				queryString.add(queryNode);
			}
		}
		
		List<ChemicalNode> subjectString = new ArrayList<ChemicalNode>();
		for(ChemicalNodeString nodeString : subjectNodeStrings) {
			subjectEndingNodes.add(nodeString.getChemicalNodes().get(nodeString.getChemicalNodes().size()-1));
			for(ChemicalNode subjectNode : nodeString.getChemicalNodes()) {
				subjectString.add(subjectNode);
			}
		}
		
		// Create our dynamic programming lookup table. The element lookup[i][j] represents the min cost of the Grape substring
		// with element indices 0 to i inclusive (the first i+1 elements)
		// and the Prism substring with element indices 0 to j inclusive (the first j+1 elements)
		
		EditOperations[][] editOperations = new EditOperations[queryString.size()][subjectString.size()];
		double[][] lookup = new double[queryString.size()][subjectString.size()];
		
		
		// Base cases
		// Base case for when j = 0 (the first element of the Prism string)
		/*
		lookup[0][0] = 0;
		for(int i = 1; i <= grapeString.size(); i++) {
			lookup[i][0] = 0; //i * betweenPrismSequenceGapPenalty; // Note this difference from global alignment
			editOperations[i][0] = EditOperations.D;
		}
		// Base case for when i = 0 (the Grape string is empty, and the best match consists of i Grape gaps
		for(int j = 1; j <= prismString.size(); j++) {
			lookup[0][j] = 0; //j * betweenGrapeSequenceGapPenalty; // Note this difference from global alignment
			editOperations[0][j] = EditOperations.I;
		}
		*/
		
		
		// Fill the rest of the table.
		for(int i = 0; i < queryString.size(); i++) {
			for(int j = 0; j < subjectString.size(); j++) {
				//{I, D, M}
				// Insertion: this corresponds to a Grape gap
				double insertionScore = 0;
				double deletionScore = 0;
				double substitutionScore = 0;
				// j or j-1?
				if(j - 1 >= 0) {
					insertionScore = lookup[i][j-1];
					if(i - 1 >= 0 && queryEndingNodes.contains(queryString.get(i-1))) {
						if(queryPrism){
							insertionScore += scoringScheme.betweenPrismSequenceGapPenalty;
						}else{
							insertionScore += scoringScheme.betweenGrapeSequenceGapPenalty;
						}
					}
					else {
						if(queryPrism){
							insertionScore += scoringScheme.withinPrismSequenceGapPenalty;
						}else{
							insertionScore += scoringScheme.withinGrapeSequenceGapPenalty;
						}
					}
				}
				// Deletion: this corresponds to a Prism gap
				if(i - 1 >= 0) {
					deletionScore = lookup[i-1][j];
					if(j - 1 >= 0 &&subjectEndingNodes.contains(subjectString.get(j-1))) {
						if(queryPrism){
							deletionScore += scoringScheme.betweenGrapeSequenceGapPenalty;
						}else{
							deletionScore += scoringScheme.betweenPrismSequenceGapPenalty;
						}
					}
					else {
						if(queryPrism){
							deletionScore += scoringScheme.withinGrapeSequenceGapPenalty;
						}else{
							deletionScore += scoringScheme.withinPrismSequenceGapPenalty;
						}
					}
				}
				// Match: this is either a correct match or a substitution
				if(i - 1 >= 0 && j - 1 >= 0) {
					substitutionScore = lookup[i-1][j-1];
				}
				substitutionScore += getMatchScore(queryString.get(i), subjectString.get(j), scoringScheme);
				
				if(insertionScore <= 0 && deletionScore <= 0 && substitutionScore <= 0) {
					lookup[i][j] = 0;
					editOperations[i][j] = EditOperations.N;
				}
				else if(insertionScore > deletionScore && insertionScore > substitutionScore) {
					lookup[i][j] = insertionScore;
					editOperations[i][j] = EditOperations.I;
				}
				else if(deletionScore > substitutionScore) {
					lookup[i][j] = deletionScore;
					editOperations[i][j] = EditOperations.D;
				}
				else {
					lookup[i][j] = substitutionScore;
					editOperations[i][j] = EditOperations.M;
				}
			}
		}
		
		// Backtrack to populate the edit transcript
		int queryEnd = 0;
		int subjectEnd = 0;
		for(int i = 0; i < queryString.size(); i++) {
			for(int j = 0; j < subjectString.size(); j++) {
				if(lookup[i][j] > lookup[queryEnd][subjectEnd]) {
					queryEnd = i;
					subjectEnd = j;
				}
			}
		}
		
		int queryIndex = queryEnd;
		int subjectIndex = subjectEnd;
		int prevQueryIndex = queryEnd;
		int prevSubjectIndex = subjectEnd;
		// Since we are backtracking, fill the editOperations and then subsequently reverse
		boolean exitBacktrack = false;
		
		while(!exitBacktrack) {
			if(queryIndex == 0 && subjectIndex == 0) {
				exitBacktrack = true;
			}
			if( queryIndex < 0 || subjectIndex < 0 || editOperations[queryIndex][subjectIndex] == EditOperations.N) {
				// Revert the Query and Subject indices for the next step
				queryIndex = prevQueryIndex;
				subjectIndex = prevSubjectIndex;
				break;
			}
			
			prevQueryIndex = queryIndex;
			prevSubjectIndex = subjectIndex;
			
			editTranscript.addEditOperation(editOperations[queryIndex][subjectIndex], lookup[queryIndex][subjectIndex]);
			
			switch(editOperations[queryIndex][subjectIndex]) {
			case I:
				subjectIndex--;
				break;
			case D:
				queryIndex--;
				break;
			case M:
				//if(grapeIndex > 0) {
					queryIndex--;
				//}
				//if(prismIndex > 0) {
					subjectIndex--;
				//}
				break;
			}
		}
		
		// Fill in the leading end of the edit transcript with matches, insertions, deletions
		while(queryIndex > 0 || subjectIndex > 0) {
			if(queryIndex == 0 && subjectIndex > 0) {
				editTranscript.addEditOperation(EditOperations.I, 0);
				subjectIndex--;
			}
			else if(queryIndex > 0 && subjectIndex == 0) {
				editTranscript.addEditOperation(EditOperations.D, 0);
				queryIndex--;
			}
			else if(queryIndex > 0 && subjectIndex > 0) {
				editTranscript.addEditOperation(EditOperations.M, 0);
				queryIndex--;
				subjectIndex--;
			}
		}
		editTranscript.reverse();
		
		// Fill in the trailing end of the edit transcript
		queryIndex = queryEnd;
		subjectIndex = subjectEnd;
		
		while(queryIndex < queryString.size() - 1 || subjectIndex < subjectString.size() - 1) {
			if(queryIndex >= queryString.size() - 1 && subjectIndex < subjectString.size() - 1) {
				editTranscript.addEditOperation(EditOperations.I, 0);
				subjectIndex++;
			}
			else if(queryIndex < queryString.size() - 1 && subjectIndex >= subjectString.size() - 1) {
				editTranscript.addEditOperation(EditOperations.D, 0);
				queryIndex++;
			}
			else if(queryIndex < queryString.size() - 1 && subjectIndex < subjectString.size() - 1) {
				editTranscript.addEditOperation(EditOperations.M, 0);
				queryIndex++;
				subjectIndex++;
			}
		}
		
		double score = 0;
		if(lookup.length > 0 && lookup[0].length > 0) {
			score +=  lookup[queryEnd][subjectEnd];
		}
		
		///Main.print(editOperations);
		//Main.print(lookup);
		//System.out.println(editTranscript + " " + score);
		
		return score;
	}
	
	static double getMatchScore(ChemicalNode queryNode, ChemicalNode subjectNode, ScoringScheme scoringScheme) {
		if(queryNode.getClass() != subjectNode.getClass()){
			//deleted use of fattyAcidOrPKGapPenalty here:  a gap is a gap, not a match
			return scoringScheme.aminoAcidPKPenalty;
		}
		if(queryNode.getClass() == AminoAcidNode.class) {
			double score = 0;
			AminoAcidNode queryAANode = (AminoAcidNode) queryNode;
			AminoAcidNode subjectAANode = (AminoAcidNode) subjectNode;
			boolean hit = false;
			if(queryAANode.getAminoAcidType() == null || subjectAANode.getAminoAcidType() == null) {
				//score += aminoAcidSubstitutionPenalty;
			}
			else if(queryAANode.getAminoAcidType() != subjectAANode.getAminoAcidType()) {
				// check if they are identical based on list
				for(AminoAcidEnum[] identicalList : ScoringScheme.identicalAminoAcidNames) {
					if(Arrays.asList(identicalList).contains(queryAANode.getAminoAcidType())
							&& Arrays.asList(identicalList).contains(subjectAANode.getAminoAcidType())) {
						score += scoringScheme.aminoAcidMatchScore;
						hit = true;
						break;
						
//						if(!Arrays.asList(scoringScheme.proteinogenicAA).contains(grapeAANode.getAminoAcidType())) {
//							score += scoringScheme.nonProteinogenicAminoAcidBonus;
//						}
					}
				}
				if(hit == false) {
					for(AminoAcidEnum[] similarList : ScoringScheme.similarAminoAcidNames) {
						if(Arrays.asList(similarList).contains(queryAANode.getAminoAcidType())
								&& Arrays.asList(similarList).contains(subjectAANode.getAminoAcidType())) {
							score += scoringScheme.aminoAcidPartialMatchScore;
							hit = true;
							break; 
						}
					}
				}
				if(hit == false) {
					score += scoringScheme.aminoAcidSubstitutionPenalty;
				}
			}
			else {
				// Amino acid match
				score += scoringScheme.aminoAcidMatchScore;
				hit = true;
			}
			if(hit == true) {
				//Bonus scores
				// If it is aromatic
				if(Arrays.asList(ScoringScheme.aromaticAAs).contains(queryAANode.getAminoAcidType())
						&& (Arrays.asList(ScoringScheme.aromaticAAs).contains(subjectAANode.getAminoAcidType()))) {
					score += scoringScheme.aromaticAminoAcidBonus;
				}
				//If it is not Proteinogenic, add bonus
				if(!Arrays.asList(ScoringScheme.proteinogenicAA).contains(queryAANode.getAminoAcidType())
						&& !Arrays.asList(ScoringScheme.proteinogenicAA).contains(subjectAANode.getAminoAcidType())) {
					score += scoringScheme.nonProteinogenicAminoAcidBonus;
				}
			}
			score += getSiteSpecificTailoringScore(queryAANode.getSiteSpecificTailoring(), subjectAANode.getSiteSpecificTailoring(), scoringScheme);
			
			/*
			if(!grapeAANode.getSiteSpecificTailoring().equals(prismAANode.getSiteSpecificTailoring())){
				//TODO: consider if this is worthwhile
				score += aminoAcidSiteSpecificTailoringPenalty;
			}
			*/
			return score;
		}
		
		// grapeNode.getClass() must be equal to PKPieceNode.class
		assert queryNode.getClass() == PKPieceNode.class;
		double score = 0;
		PKPieceNode queryPKNode = (PKPieceNode) queryNode;
		PKPieceNode subjectPKNode = (PKPieceNode) subjectNode;
		boolean substrateMatch = false;
		boolean oxidationStateMatch = false;
		boolean atleastOneNullAtEachPosition = false;
		if(queryPKNode.getSubstrate() == null || subjectPKNode.getSubstrate() == null){
			if(queryPKNode.getSubstrate() == PKSubstrateEnum.Pr || subjectPKNode.getSubstrate() == PKSubstrateEnum.Pr){
				score += scoringScheme.pkPossiblePrMatchScore;
			}
			atleastOneNullAtEachPosition = true;
        }else if(queryPKNode.getSubstrate().equals(PKSubstrateEnum.MeM) && queryPKNode.getSiteSpecificTailoring().contains(SiteSpecificTailoringEnum.C_METHYLTRANSFERASE)) {
        	if (subjectPKNode.getSubstrate().equals(PKSubstrateEnum.IBu)) {
                    score += scoringScheme.pkUnusualSubstrateMatchScore;
                    substrateMatch = true;
			}else if(subjectPKNode.getSubstrate().equals(PKSubstrateEnum.MeM) && subjectPKNode.getSiteSpecificTailoring().contains(SiteSpecificTailoringEnum.C_METHYLTRANSFERASE)){
                    score += scoringScheme.pkUnusualSubstrateMatchScore;
                    substrateMatch = true;
        	} else { //non-matches with CMT should count as penalties
					score += scoringScheme.pkSubstrateSubstitutionPenalty;
        	}
		}else if(subjectPKNode.getSubstrate().equals(PKSubstrateEnum.Mal) && subjectPKNode.getSiteSpecificTailoring().contains(SiteSpecificTailoringEnum.C_METHYLTRANSFERASE)){
			if (queryPKNode.getSubstrate().equals(PKSubstrateEnum.MeM)) {
                    score += scoringScheme.pkMeMMatchScore;
                    substrateMatch = true;
			}else if(queryPKNode.getSubstrate().equals(PKSubstrateEnum.Mal) && queryPKNode.getSiteSpecificTailoring().contains(SiteSpecificTailoringEnum.C_METHYLTRANSFERASE)){
                    score += scoringScheme.pkMeMMatchScore;
                    substrateMatch = true;
        	} else { //non-matches with CMT should count as penalties
					score += scoringScheme.pkSubstrateSubstitutionPenalty;
        	}
		}else if(subjectPKNode.getSubstrate().equals(PKSubstrateEnum.MeM) && subjectPKNode.getSiteSpecificTailoring().contains(SiteSpecificTailoringEnum.C_METHYLTRANSFERASE)){
			if (queryPKNode.getSubstrate().equals(PKSubstrateEnum.IBu)) {
						score += scoringScheme.pkUnusualSubstrateMatchScore;
						substrateMatch = true;
        	} else {
					score += scoringScheme.pkSubstrateSubstitutionPenalty;
        	}
		}else if(queryPKNode.getSubstrate().equals(PKSubstrateEnum.Mal) && queryPKNode.getSiteSpecificTailoring().contains(SiteSpecificTailoringEnum.C_METHYLTRANSFERASE)){
			if (subjectPKNode.getSubstrate().equals(PKSubstrateEnum.MeM)) {
						score += scoringScheme.pkMeMMatchScore;
						substrateMatch = true;
        	} else {
				score += scoringScheme.pkSubstrateSubstitutionPenalty;
        	}
		} else if(queryPKNode.getSubstrate() != subjectPKNode.getSubstrate()) {
			score += scoringScheme.pkSubstrateSubstitutionPenalty;
		}
		else {
			substrateMatch = true;
			if (queryPKNode.getSubstrate() == PKSubstrateEnum.Mal) {
				score += scoringScheme.pkMalMatchScore;
			} else if (queryPKNode.getSubstrate() == PKSubstrateEnum.MeM) {
				score += scoringScheme.pkMeMMatchScore;
			} else {
				score += scoringScheme.pkUnusualSubstrateMatchScore;
			}
		}
		
		if(queryPKNode.getPossibleOxidationStates().isEmpty() || subjectPKNode.getPossibleOxidationStates().isEmpty()){
			atleastOneNullAtEachPosition = true;
		}else if(!queryPKNode.getPossibleOxidationStates().isEmpty() || !subjectPKNode.getPossibleOxidationStates().isEmpty()) {
			boolean hasMatch = false;
			int countMatches = 0;
			//counting matches 
			for(PKOxidationStateEnum oxidationState : queryPKNode.getPossibleOxidationStates()) {
				if(subjectPKNode.getPossibleOxidationStates().contains(oxidationState)) {
					countMatches++;
					hasMatch = true;
				}
			}
			for(PKOxidationStateEnum oxidationState : subjectPKNode.getPossibleOxidationStates()) {
				if(queryPKNode.getPossibleOxidationStates().contains(oxidationState)) {
					countMatches++;
				}
			}
			if(countMatches == 0) {
				score += scoringScheme.pkOxidationSubstitutionPenalty;
			}
			else {
				double oxyScoreModifier = 1.0*countMatches/(subjectPKNode.getPossibleOxidationStates().size() + queryPKNode.getPossibleOxidationStates().size());
				score += oxyScoreModifier * scoringScheme.pkOxidationMatchScore;
				oxidationStateMatch = true;
			}
		}
		
		// bonus for matching both substrate and oxidation state
		if(substrateMatch && oxidationStateMatch) {
			score += scoringScheme.pkCompleteMatchBonus;
		}
		
		return score;
	}
	
	static int getSiteSpecificTailoringScore(Set<SiteSpecificTailoringEnum> querySiteSpecificTailoring, Set<SiteSpecificTailoringEnum> subjectSiteSpecificTailoring, ScoringScheme scoringScheme) {
		int score = 0;
		for(SiteSpecificTailoringEnum siteSpecificTailoring : querySiteSpecificTailoring) {
			if(subjectSiteSpecificTailoring.contains(siteSpecificTailoring)) {
				score += scoringScheme.siteSpecificTailoringScore;
				if(subjectSiteSpecificTailoring.contains(SiteSpecificTailoringEnum.CYCLIZATION) 
						&& siteSpecificTailoring == SiteSpecificTailoringEnum.CYCLIZATION) {
					score += scoringScheme.cyclizationBonus;
				}
				// || prismSiteSpecificTailoring.contains(SiteSpecificTailoringEnum.OXAZOLE) 
				else if(subjectSiteSpecificTailoring.contains(SiteSpecificTailoringEnum.THIAZOLE)
						&& siteSpecificTailoring == SiteSpecificTailoringEnum.THIAZOLE){
					score += scoringScheme.cyclizationBonus;
				}
			}
		}
		return score;
	}
	
}
