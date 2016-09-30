package ca.mcmaster.magarveylab.matching.algorithm.scoring;

import ca.mcmaster.magarveylab.matching.enums.AcylAdenylatingSubstrates;
import ca.mcmaster.magarveylab.matching.enums.ChemicalClassifications.ChemicalSubtype;
import ca.mcmaster.magarveylab.matching.enums.Monomers.AminoAcidEnum;

public class ScoringScheme {
	
	public static enum ScoringType {BASIC, GRAPEPRISM, GRAPEGRAPE, PRISMPRISM, FINAL};
	public static enum AlignmentType {GLOBAL, LOCAL};
	public static AcylAdenylatingSubstrates[][] identicalAcylAdenylatingSubstrates = {
			{AcylAdenylatingSubstrates.PYRUVATE, AcylAdenylatingSubstrates.LACTATE, AcylAdenylatingSubstrates.VALERAIC_ACID, AcylAdenylatingSubstrates.ALPHA_KETOISOVALERATE, AcylAdenylatingSubstrates._3_HYDROXYLPATANOLIC_ACID},
			{AcylAdenylatingSubstrates.SALICYLIC_ACID, AcylAdenylatingSubstrates._3_FORMAMIDO_5_HYDROXYBENZOIC_ACID, AcylAdenylatingSubstrates.BENZOIC_ACID},
			{AcylAdenylatingSubstrates.ALPHA_KETOISOCAPROATE, AcylAdenylatingSubstrates.HYDROXY_3_METHYL_PENTANOIC_ACID}
			};
	public static AminoAcidEnum[] proteinogenicAA = {
				AminoAcidEnum.Histidine,
				AminoAcidEnum.Alanine,
				AminoAcidEnum.Isoleucine,
				AminoAcidEnum.Arginine,
				AminoAcidEnum.Leucine,
				AminoAcidEnum.Asparagine,
				AminoAcidEnum.Lysine,
				AminoAcidEnum.Aspartic_Acid,
				AminoAcidEnum.Methionine, // Note: Methionine is atypical of NRPs
				AminoAcidEnum.Cysteine,
				AminoAcidEnum.Phenylalanine,
				AminoAcidEnum.Glutamic_Acid,
				AminoAcidEnum.Threonine,
				AminoAcidEnum.Glutamine,
				AminoAcidEnum.Tryptophan,
				AminoAcidEnum.Glycine,
				AminoAcidEnum.Valine,
				AminoAcidEnum.Proline, // Note: Proline is not as common as other AAs
				AminoAcidEnum.Serine,
				AminoAcidEnum.Tyrosine,
		};
	public static AminoAcidEnum[][] identicalAminoAcidNames = { // When there is a tailoring on these such as NMT it no longer is considered a match.
			{AminoAcidEnum.Alpha_keto_isovalerate, AminoAcidEnum.Hydroxyisovalerate, AminoAcidEnum.Valeric_Acid},
			{AminoAcidEnum.Pyruvate, AminoAcidEnum.Lactate},
			{AminoAcidEnum.Oxopentoate, AminoAcidEnum.Lactate},
			{AminoAcidEnum.Valine, AminoAcidEnum._3_hydroxy_anthranilic_acid},
			{AminoAcidEnum.Dehydro_Aminobutyric_Acid, AminoAcidEnum.Threonine},
			{AminoAcidEnum.Pyruvate, AminoAcidEnum.Valeric_Acid},
			{AminoAcidEnum.HydroxyOrnithine, AminoAcidEnum.Ornithine},
			{AminoAcidEnum.SERORCYS, AminoAcidEnum.Serine},
			{AminoAcidEnum.SERORCYS, AminoAcidEnum.Cysteine},
			};
	public static AminoAcidEnum[][] similarAminoAcidNames = {
			{AminoAcidEnum.Valine, AminoAcidEnum.Isoleucine, AminoAcidEnum.Alanine, AminoAcidEnum.Leucine, AminoAcidEnum.Beta_hydroxy_Valine, AminoAcidEnum.Beta_Lysine, AminoAcidEnum.HydroxyLeucine},
			{AminoAcidEnum.Alanine, AminoAcidEnum.Beta_Alanine},
			{AminoAcidEnum.Glutamic_Acid, AminoAcidEnum.Glutamine, AminoAcidEnum.piperazic_acid},
			{AminoAcidEnum.Phenylalanine, AminoAcidEnum.Tryptophan, AminoAcidEnum.Tyrosine, AminoAcidEnum.Hydroxy_3_methylpentanoic_Acid, AminoAcidEnum.quinoxaline_carboxylic_acid, AminoAcidEnum.Beta_HydroxyPhenylalanine, AminoAcidEnum.HydroxyTyrosine, AminoAcidEnum.BetaMethylPhenylalanine},
			{AminoAcidEnum.Asparagine, AminoAcidEnum.Aspartic_Acid, AminoAcidEnum.Hydroxyasparagine, AminoAcidEnum.Beta_Methyl_Aspartic_Acid, AminoAcidEnum.Hydroxyaspartic_Acid},
			{AminoAcidEnum.MethylProline, AminoAcidEnum.Proline},
			};
	public static AminoAcidEnum[] aromaticAAs = {AminoAcidEnum.Phenylalanine, AminoAcidEnum.Tryptophan, AminoAcidEnum.Tyrosine, AminoAcidEnum.Histidine};
	
	public static ChemicalSubtype[] rareChemicalSubtypes = {ChemicalSubtype.ENEDYINE, ChemicalSubtype.MACROLIDE, ChemicalSubtype.TERPENE};
	
	public ScoringType scoringType;
	public AlignmentType alignmentType;
	
	public boolean useNonspecificTailoring = true;
	// Exact sugar matching: if false, use sugar feature matching
	public boolean useExactSugarMatching = true;
	
	public double fattyAcidOrPKGapPenalty = 0;
	public double betweenPrismSequenceGapPenalty = 0;
	public double withinPrismSequenceGapPenalty = 0;
	public double repeatedPrismPKUnitGapPenalty = 0;
	
	// This penalty only applies if there is a multiple amino acid piece present. 
	public double betweenGrapeSequenceGapPenalty = 0;
	public double withinGrapeSequenceGapPenalty = 0;
	
	public double aminoAcidMatchScore = 0;
	public double aminoAcidPartialMatchScore = 0;
	public double pkOxidationMatchScore = 0;
	
	public double nonProteinogenicAminoAcidBonus = 0;
	public double aromaticAminoAcidBonus = 0;
	public double pkCompleteMatchBonus = 0;
	public double pkMaxScoreForMultiplePossibleOxidationStates = 0;
	
	public double siteSpecificTailoringScore = 0;
	
	public double aminoAcidSubstitutionPenalty = 0;
	public double pkSubstrateSubstitutionPenalty = 0;
	public double pkOxidationSubstitutionPenalty = 0;
	
	public double aminoAcidPKPenalty = 0;
	public double aminoAcidDifferencePenalty = 0;
	
	public double nonSpecificTailoringBonus = 0;
	public double sugarMissingPenalty = 0;
	public double sulfurBetaLactamBonus = 0;
	public double cyclizationBonus = 0;
	public double sugarBonus = 0;
	public double sugarPrismGeneDifferenceAdjustment = 0;
	
	public double starterBonus = 0;
	public double acylAdenylatingBonus = 0;
	public double chemicalTypeBonus = 0;
	public double scaffoldBonus = 0;
	
	public double orfNumberBonus = 0;
	
	//newer scorings
	public double pkPossiblePrMatchScore = 0;
	public double pkUnusualSubstrateMatchScore = 0;
	public double pkMeMMatchScore = 0;
	public double pkMalMatchScore = 0;
	
	//newer sugar scorings
	public double sugarTypeBonus = 0;
	public double sugarNumberBonus = 0;
	public double sugarTypePenalty = 0;
	public double sugarNumberPenalty = 0;

	
	public ScoringScheme(){
	}
	
	public ScoringScheme(AlignmentType alignmentType, ScoringType scoringType, boolean useNonSpecificTailoring, boolean useExactSugarMatching){
		this.alignmentType = alignmentType;
		this.scoringType = scoringType;
		this.useNonspecificTailoring = useNonSpecificTailoring;
		this.useExactSugarMatching = useExactSugarMatching;
		initializeScoringScheme(scoringType);
	}
	
	public ScoringScheme(AlignmentType alignmentType, ScoringType scoringType){
		this.alignmentType = alignmentType;
		this.scoringType = scoringType;
		initializeScoringScheme(scoringType);
	}

	public void initializeScoringScheme(ScoringType scoringType) {
		if(scoringType == ScoringType.FINAL) {
			 acylAdenylatingBonus = 11.7;
			 aminoAcidDifferencePenalty= 0;
			 aminoAcidMatchScore= 5.31;
			 aminoAcidPartialMatchScore= 1.18;
			 aminoAcidPKPenalty= -10;
			 aminoAcidSubstitutionPenalty= -2.25;
			 aromaticAminoAcidBonus= 1.17;
			 betweenGrapeSequenceGapPenalty= -2.31;
			 chemicalTypeBonus= 2; //unused during publication work, as this did not include PK type II
			 cyclizationBonus= 0;
			 fattyAcidOrPKGapPenalty= -0.64;
			 nonProteinogenicAminoAcidBonus= 3.43;
			 nonSpecificTailoringBonus= 7.95;
			 pkMalMatchScore= 1.27;
			 pkMeMMatchScore= 3.28;
			 pkCompleteMatchBonus= 0.99;
			 pkMaxScoreForMultiplePossibleOxidationStates= 5;
			 pkOxidationMatchScore= 3.25;
			 pkOxidationSubstitutionPenalty= -1.00;
			 pkPossiblePrMatchScore= 1;
			 pkSubstrateSubstitutionPenalty= -1.93;
			 pkUnusualSubstrateMatchScore= 14.12;
			 repeatedPrismPKUnitGapPenalty= -2.5;
			 scaffoldBonus= 1; //unused during publication work, for PK type II only
			 siteSpecificTailoringScore= 5.44;
			 starterBonus= -4.71;
			 sugarBonus= 0.05;
			 sugarMissingPenalty= 0;
			 sugarNumberBonus= 0.36;
			 sugarNumberPenalty= -0.19;
			 sugarPrismGeneDifferenceAdjustment= 0;
			 sugarTypeBonus= 1.91;
			 sugarTypePenalty= -0.77;
			 sulfurBetaLactamBonus= 0;
			 withinGrapeSequenceGapPenalty= -5.37;
			 withinPrismSequenceGapPenalty= -5.37;
			 betweenPrismSequenceGapPenalty= -2.3;
		}
		if(scoringType == ScoringType.BASIC) {
			 fattyAcidOrPKGapPenalty = -0.001;
			 betweenPrismSequenceGapPenalty = -1;
			 withinPrismSequenceGapPenalty = -1;
			 repeatedPrismPKUnitGapPenalty = -1;
			
			// This penalty only applies if there is a multiple amino acid piece present. 
			 betweenGrapeSequenceGapPenalty = -1;
			 withinGrapeSequenceGapPenalty = -1;
			
			 aminoAcidMatchScore = 1;
			 aminoAcidPartialMatchScore = -1; // Without refinements, "partial" matches are mismatches
			 pkOxidationMatchScore = 0.5;
			
			 nonProteinogenicAminoAcidBonus = 0;
			 aromaticAminoAcidBonus = 0;
			 pkCompleteMatchBonus = 0;
			 pkMaxScoreForMultiplePossibleOxidationStates = Double.MAX_VALUE;
			
			 siteSpecificTailoringScore = 0.25;
			
			 aminoAcidSubstitutionPenalty = -1;
			 pkSubstrateSubstitutionPenalty = -0.5;
			 pkOxidationSubstitutionPenalty = -0.5;
			
			 aminoAcidPKPenalty = -1;
			 
			 
			 starterBonus = 0.25;
			 acylAdenylatingBonus = 0.25; // new
			 sugarBonus = 0.25; 
			 sugarPrismGeneDifferenceAdjustment = 0.0;
			
			 nonSpecificTailoringBonus = 0.25;
			 sulfurBetaLactamBonus = 0.25;
			 
			 //newer scores
			 pkPossiblePrMatchScore = 0.5;
			 pkUnusualSubstrateMatchScore = 0.5;
			 pkMeMMatchScore = 0.5;
			 pkMalMatchScore = 0.5;
			 
			 sugarTypeBonus = 0.25;
			 sugarNumberBonus = 0.25;
			 sugarTypePenalty = -0.20;
			 sugarNumberPenalty = -0.20;
		}
		if(scoringType == ScoringType.GRAPEPRISM) {
			 fattyAcidOrPKGapPenalty = -0.001;
			 betweenPrismSequenceGapPenalty = -2;
			 withinPrismSequenceGapPenalty = -5;
			 repeatedPrismPKUnitGapPenalty = -2.5;
			
			// This penalty only applies if there is a multiple amino acid piece present. 
			 betweenGrapeSequenceGapPenalty = -2;
			 withinGrapeSequenceGapPenalty = -5;
			
			 aminoAcidMatchScore = 5;
			 aminoAcidPartialMatchScore = 1;
			 pkOxidationMatchScore = 3;
			
			 nonProteinogenicAminoAcidBonus = 3;
			 aromaticAminoAcidBonus = 1;
			 pkCompleteMatchBonus = 1;
			 pkMaxScoreForMultiplePossibleOxidationStates = 5;
			
			 siteSpecificTailoringScore = 2;
			
			 aminoAcidSubstitutionPenalty = -2;
			 pkSubstrateSubstitutionPenalty = -1;
			 pkOxidationSubstitutionPenalty = -1;
			
			 aminoAcidPKPenalty = -10;
			 
			 sugarBonus = 0.05; 
			 sugarPrismGeneDifferenceAdjustment = 0.5;
			
			 nonSpecificTailoringBonus = 0.5;
			 
			 starterBonus = 0.5;
			 acylAdenylatingBonus = 1; // new
			 scaffoldBonus = 2; // new
			 chemicalTypeBonus = 2; // new
			 
			 sulfurBetaLactamBonus = 1.5;

			 //newer scores
			 pkPossiblePrMatchScore = 1;
			 pkUnusualSubstrateMatchScore = 4;
			 pkMeMMatchScore = 2;
			 pkMalMatchScore = 1;
			 
			 sugarTypeBonus = 0.1;
			 sugarNumberBonus = 0.05;
			 sugarTypePenalty = -0.05;
			 sugarNumberPenalty = -0.025;
		}
		if(scoringType == ScoringType.PRISMPRISM) {
			 fattyAcidOrPKGapPenalty = -0.001;
			 betweenPrismSequenceGapPenalty = -2;
			 withinPrismSequenceGapPenalty = -2;
			
			// This penalty only applies if there is a multiple amino acid piece present. 
			 betweenGrapeSequenceGapPenalty = -2;
			 withinGrapeSequenceGapPenalty = -2;
			
			 aminoAcidMatchScore = 5;
			 aminoAcidPartialMatchScore = 1;
			 pkOxidationMatchScore = 3;
			
			 nonProteinogenicAminoAcidBonus = 3;
			 aromaticAminoAcidBonus = 1;
			 pkCompleteMatchBonus = 1;
			 pkMaxScoreForMultiplePossibleOxidationStates = 5;
			
			 siteSpecificTailoringScore = 2;
			
			 aminoAcidSubstitutionPenalty = -2;
			 pkSubstrateSubstitutionPenalty = -1;
			 pkOxidationSubstitutionPenalty = -1;
			
			 aminoAcidPKPenalty = -10;
			 
			 sugarBonus = 0.05;
			 
			 starterBonus = 0.25;
			 acylAdenylatingBonus = 0.5; // new
			 scaffoldBonus = 1; // new
			 chemicalTypeBonus = 1; // new
			 orfNumberBonus = 0.1; // new Scores number of orfs
			
			 nonSpecificTailoringBonus = 0.5;
			 sulfurBetaLactamBonus = 1.5;

			 //newer scores
			 pkPossiblePrMatchScore = 1;
			 pkUnusualSubstrateMatchScore = 4;
			 pkMeMMatchScore = 2;
			 pkMalMatchScore = 1;
			 
			 sugarTypeBonus = 0.1;
			 sugarNumberBonus = 0.05;
			 sugarTypePenalty = -0.05;
			 sugarNumberPenalty = -0.025;
		}

		if(scoringType == ScoringType.GRAPEGRAPE){
			 fattyAcidOrPKGapPenalty = -0.001;
			 betweenPrismSequenceGapPenalty = -5;
			 withinPrismSequenceGapPenalty = -5;
			
			// This penalty only applies if there is a multiple amino acid piece present. 
			 betweenGrapeSequenceGapPenalty = -5;
			 withinGrapeSequenceGapPenalty = -5;
			
			 aminoAcidMatchScore = 5;
			 aminoAcidPartialMatchScore = 1;
			 pkOxidationMatchScore = 3;
			
			 nonProteinogenicAminoAcidBonus = 3;
			 aromaticAminoAcidBonus = 1;
			 pkCompleteMatchBonus = 1;
			 pkMaxScoreForMultiplePossibleOxidationStates = 5;
			
			 siteSpecificTailoringScore = 2;
			
			 aminoAcidSubstitutionPenalty = -2;
			 pkSubstrateSubstitutionPenalty = -1;
			 pkOxidationSubstitutionPenalty = -1;
			
			 aminoAcidPKPenalty = -10;
			
			 nonSpecificTailoringBonus = 0.05;
			 sulfurBetaLactamBonus = 9;
			 cyclizationBonus = 9;
			 
			 sugarBonus = 0.1;
			 
			 starterBonus = 0.5;
			 scaffoldBonus = 1;
			 
			 chemicalTypeBonus = 2;

			 //newer scores
			 acylAdenylatingBonus = 0.5; // new
			 pkPossiblePrMatchScore = 1;
			 pkUnusualSubstrateMatchScore = 4;
			 pkMeMMatchScore = 2;
			 pkMalMatchScore = 1;

			 sugarTypeBonus = 0.1;
			 sugarNumberBonus = 0.05;
			 sugarTypePenalty = -0.05;
			 sugarNumberPenalty = -0.025;
		}
	}
}
