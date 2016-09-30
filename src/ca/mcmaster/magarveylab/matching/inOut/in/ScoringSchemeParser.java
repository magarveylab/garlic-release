package ca.mcmaster.magarveylab.matching.inOut.in;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import ca.mcmaster.magarveylab.matching.algorithm.scoring.ScoringScheme;
import ca.mcmaster.magarveylab.matching.algorithm.scoring.ScoringScheme.AlignmentType;

public class ScoringSchemeParser {
	ScoringScheme ss = new ScoringScheme();
	public ScoringSchemeParser(String scoringSchemeFile) throws ScoringSchemeParsingException{
		try {
			Scanner scanner = new Scanner(new File(scoringSchemeFile));
			String json = scanner.useDelimiter("\\Z").next();
			scanner.close();
			readJSON(json);
		} catch (Exception e) {
			throw new ScoringSchemeParsingException(e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	private void readJSON(String json) throws JsonParseException, JsonMappingException, IOException, InvalidAlignmentTypeException {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> map = mapper.readValue(json, new TypeReference<HashMap<String,Object>>(){});
		Map<String, String> scheme = (Map<String, String>)map.get("Scoring_scheme");
		ss.alignmentType = getAlignmentType(scheme.get("Alignment_type"));
		ss.useNonspecificTailoring = Boolean.parseBoolean(scheme.get("Use_nonspecific_tailoring"));
		ss.useExactSugarMatching = Boolean.parseBoolean(scheme.get("Use_exact_sugar_matching"));
		ss.fattyAcidOrPKGapPenalty = Double.parseDouble(scheme.get("Fatty_acid_or_pk_gap_penalty"));
		ss.betweenPrismSequenceGapPenalty = Double.parseDouble(scheme.get("between_prism_sequence_gap_penalty"));
		ss.withinPrismSequenceGapPenalty = Double.parseDouble(scheme.get("Within_prism_sequence_gap_penalty"));
		ss.repeatedPrismPKUnitGapPenalty = Double.parseDouble(scheme.get("Repeated_prism_pk_unit_gap_penalty")); //not used, schedule for deletion
		ss.betweenGrapeSequenceGapPenalty = Double.parseDouble(scheme.get("Between_grape_sequence_gap_penalty"));
		ss.withinGrapeSequenceGapPenalty = Double.parseDouble(scheme.get("Within_grape_sequence_gap_penalty"));
		ss.aminoAcidMatchScore = Double.parseDouble(scheme.get("Amino_acid_match_score"));
		ss.aminoAcidPartialMatchScore = Double.parseDouble(scheme.get("Amino_acid_partial_match_score"));
		ss.pkOxidationMatchScore = Double.parseDouble(scheme.get("PK_oxidation_match_score"));
		ss.pkPossiblePrMatchScore = Double.parseDouble(scheme.get("PK_possible_Pr_match_score"));
		ss.pkMalMatchScore = Double.parseDouble(scheme.get("PK_Mal_match_score"));
		ss.pkMeMMatchScore = Double.parseDouble(scheme.get("PK_MeM_match_score"));
		ss.pkUnusualSubstrateMatchScore = Double.parseDouble(scheme.get("PK_unusual_substrate_match_score"));
		ss.nonProteinogenicAminoAcidBonus = Double.parseDouble(scheme.get("Nonproteinogenic_amino_acid_bonus"));
		ss.aromaticAminoAcidBonus = Double.parseDouble(scheme.get("Aromatic_amino_acid_bonus"));
		ss.pkCompleteMatchBonus = Double.parseDouble(scheme.get("PK_complete_match_bonus"));
		ss.pkMaxScoreForMultiplePossibleOxidationStates = Double.parseDouble(scheme.get("PK_max_score_for_multiple_possible_oxidation_states"));
		ss.siteSpecificTailoringScore = Double.parseDouble(scheme.get("Site_specific_tailoring_score"));
		ss.aminoAcidSubstitutionPenalty = Double.parseDouble(scheme.get("Amino_acid_substitution_penalty"));
		ss.pkSubstrateSubstitutionPenalty = Double.parseDouble(scheme.get("PK_substrate_substitution_penalty"));
		ss.pkOxidationSubstitutionPenalty = Double.parseDouble(scheme.get("PK_oxidation_substitution_penalty"));
		ss.aminoAcidPKPenalty = Double.parseDouble(scheme.get("Amino_acid_pk_penalty"));
		ss.aminoAcidDifferencePenalty = Double.parseDouble(scheme.get("Amino_acid_difference_penalty"));
		ss.nonSpecificTailoringBonus = Double.parseDouble(scheme.get("Nonspecific_tailoring_bonus"));
		ss.sugarMissingPenalty = Double.parseDouble(scheme.get("Sugar_missing_penalty"));
		ss.sulfurBetaLactamBonus = Double.parseDouble(scheme.get("Sulfur_beta_lactam_bonus"));
		ss.cyclizationBonus = Double.parseDouble(scheme.get("Cyclization_bonus"));
		ss.sugarBonus = Double.parseDouble(scheme.get("Sugar_bonus"));
		ss.sugarPrismGeneDifferenceAdjustment = Double.parseDouble(scheme.get("Sugar_prism_gene_difference_adjustment"));
		ss.sugarTypeBonus = Double.parseDouble(scheme.get("Sugar_type_bonus"));
		ss.sugarNumberBonus = Double.parseDouble(scheme.get("Sugar_number_bonus"));
		ss.sugarTypePenalty = Double.parseDouble(scheme.get("Sugar_type_penalty"));
		ss.sugarNumberPenalty = Double.parseDouble(scheme.get("Sugar_number_penalty"));
		ss.starterBonus = Double.parseDouble(scheme.get("Starter_bonus"));
		ss.acylAdenylatingBonus = Double.parseDouble(scheme.get("Acyl_adenylating_bonus"));
		ss.chemicalTypeBonus = Double.parseDouble(scheme.get("Chemical_type_bonus"));
		ss.scaffoldBonus = Double.parseDouble(scheme.get("Scaffold_bonus"));
	}

	private AlignmentType getAlignmentType(String string) throws InvalidAlignmentTypeException {
		for(AlignmentType alignmentType : AlignmentType.values()){
			if(string.equals(alignmentType.toString())){
				return alignmentType;
			}
		}
		throw new InvalidAlignmentTypeException(string);
	}

	public ScoringScheme getScoringScheme(){
		return ss;
	}
}
