#!/bin/python
#scoring_params.py
#by Mclean Edwards for MagarveyLab, 2016
#Scoring scheme setup for garlic - work in progress

import json

DEFAULT_SCORING_FILEPATH = "./scoring_scheme.json"

#load from file
file = open(DEFAULT_SCORING_FILEPATH, 'r')
scoringJSON = json.load(file)
file.close()
scoringDict = scoringJSON["Scoring_scheme"]

#A list of scoringDict keys that we are using for parameters (all numerical values), order is important
ORDERED_KEYS = [ 
    "Fatty_acid_or_pk_gap_penalty", #?? - good start for nelder-mead as first parameter +0.025
    "between_prism_sequence_gap_penalty", #needs to be merged with prism/grape
    "Within_prism_sequence_gap_penalty", #needs to be merged
    "Between_grape_sequence_gap_penalty", #needs to be merged
    "Within_grape_sequence_gap_penalty", #needs to be merged
    "Amino_acid_match_score",
    "Amino_acid_partial_match_score",
    "Aromatic_amino_acid_bonus",
    "Nonproteinogenic_amino_acid_bonus",
    "Amino_acid_substitution_penalty",
    "Amino_acid_pk_penalty",
    "PK_possible_Pr_match_score",
    "PK_Mal_match_score",
    "PK_MeM_match_score",
    "PK_unusual_substrate_match_score",
    "Site_specific_tailoring_score",
    "Cyclization_bonus", #can omit if needed - set to 0
    "PK_substrate_substitution_penalty",
    "PK_oxidation_match_score",
    "PK_oxidation_substitution_penalty",
    "PK_complete_match_bonus",
    "Nonspecific_tailoring_bonus",
    "Sulfur_beta_lactam_bonus",
    "Sugar_type_bonus",
    "Sugar_number_bonus",
    "Sugar_type_penalty",
    "Sugar_number_penalty",
    "Starter_bonus",
    "Acyl_adenylating_bonus",
    "Chemical_type_bonus",  #are we including this??
    "Scaffold_bonus", #probably omit, set to zero??
]


#can try hardcoding or loading
scoringJSON = {"Scoring_scheme":
  {
    "Alignment_type":"GLOBAL",
    "Use_nonspecific_tailoring":"true",
    "Use_exact_sugar_matching":"true",
    "Fatty_acid_or_pk_gap_penalty":"-0.001",
    "between_prism_sequence_gap_penalty":"-2",
    "Within_prism_sequence_gap_penalty":"-5",
    "Repeated_prism_pk_unit_gap_penalty":"-2.5",
    "Between_grape_sequence_gap_penalty":"-2",
    "Within_grape_sequence_gap_penalty":"-5",
    "Amino_acid_match_score":"5",
    "Amino_acid_partial_match_score":"1",
    "PK_possible_Pr_match_score":"1",
    "PK_Mal_match_score":"1",
    "PK_MeM_match_score":"2",
    "PK_unusual_substrate_match_score":"4",
    "Nonproteinogenic_amino_acid_bonus":"3",
    "Aromatic_amino_acid_bonus":"1",
    "Amino_acid_substitution_penalty":"-2",
    "Amino_acid_pk_penalty":"-10",
    "Site_specific_tailoring_score":"2",
    "Cyclization_bonus":"0",
    "PK_substrate_substitution_penalty":"-1",
    "PK_oxidation_match_score":"3",
    "PK_oxidation_substitution_penalty":"-1",
    "PK_complete_match_bonus":"1",
    "PK_max_score_for_multiple_possible_oxidation_states":"5",
    "Amino_acid_difference_penalty":"0",
    "Nonspecific_tailoring_bonus":"0.5",
    "Sulfur_beta_lactam_bonus":"1.5",
    "Sugar_missing_penalty":"0",
    "Sugar_bonus":"0.05",
    "Sugar_type_bonus":"0.1",
    "Sugar_number_bonus":"0.05",
    "Sugar_type_penalty":"-0.05",
    "Sugar_number_penalty":"-0.025",
    "Sugar_prism_gene_difference_adjustment":"0",
    "Starter_bonus":"0.5",
    "Acyl_adenylating_bonus":"1",
    "Chemical_type_bonus":"2",
    "Scaffold_bonus":"2"
  }
}
