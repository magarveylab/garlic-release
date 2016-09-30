#!/bin/python
#analyzeGarlic.py
#Wrapper for garlic, used to optimize parameters
#by Mclean Edwards
#created: Mon Feb  8 10:06:12 EST 2016

#Python 2.7
#Usage:  Modify DEFAULT DEFINITIONS as appropriate, then run without arguments
#creates Final.json, a new scoring scheme optimized to fit the testing files

#This version was used to create GF (final scoring scheme in published paper)

import json
import subprocess
import scipy.optimize as opt

import analyzeScores


#*****************DEFAULT DEFINITIONS*******************
#Directory containing prism files to learn on
QUERY_DIRECTORY = "../prism_167"
#Directory containing grape files to learn on (those matching prism must be named the same except for extension)
SUBJECT_DIRECTORY = "../grape_463"
#Score from garlic to use for learning
SCORE_NAME = "_score.csv"  #one of ["_raw.csv", "_relative.csv", "_score.csv", "_sugarscore.csv"]
#Learning metric
SCORING_METHOD = analyzeScores.rankScore

#Scoring file, parameters at start of process
DEFAULT_PARAM_FILE = "../scoring_basic.json"
PARAM_FILE = "./aGparameters.json"  #TEMP file
#This does not change, fixed to PRISM version
ROOT_JSON_NAME = "Scoring_scheme"

initparamfile = open(DEFAULT_PARAM_FILE, 'r')
DEFAULT_JSON = json.load(initparamfile)
OUTPUT_DIRECTORY = "scoring_temp"
GARLIC_JARNAME = "../garlicCLI.jar"
OUTPUT_BASENAME = "analyzeTemp"

GARLIC_ARGS = ["java", "-jar", GARLIC_JARNAME, "-c"]
GARLIC_ARGS += ["-d", OUTPUT_BASENAME]
GARLIC_ARGS += ["-s", SUBJECT_DIRECTORY, "-q", QUERY_DIRECTORY]
GARLIC_ARGS += ["-i", PARAM_FILE]
GARLIC_ARGS += ["-o", OUTPUT_DIRECTORY]

#****************ARRAY KEYS******************************
#A list of scoringDict keys that we are using for parameters (all numerical values), order is important
ORDERED_KEYS = [ 
    "Amino_acid_match_score",
    "Amino_acid_partial_match_score",
    "Aromatic_amino_acid_bonus",
    "Nonproteinogenic_amino_acid_bonus",
    "Amino_acid_substitution_penalty",
    "PK_Mal_match_score",
    "PK_MeM_match_score",
    "PK_unusual_substrate_match_score",
    "PK_substrate_substitution_penalty",
    "PK_oxidation_match_score",
    "PK_oxidation_substitution_penalty",
    "PK_complete_match_bonus",
    "Site_specific_tailoring_score",
    "Nonspecific_tailoring_bonus",
    "Acyl_adenylating_bonus",
    "Sugar_type_bonus",
    "Sugar_number_bonus",
    "Sugar_type_penalty",
    "Sugar_number_penalty",
    "Starter_bonus",
    "Within_grape_sequence_gap_penalty", 
    "Between_grape_sequence_gap_penalty", 
]


#*****************UTILITY METHODS*************************
def createParamFile(params, default_json=DEFAULT_JSON, param_file = PARAM_FILE):
    """Creates a json parameter file to be used by garlic from 
    a list of parameters, using ORDERED_KEYS to label the values"""
    assert len(params) == len(ORDERED_KEYS)
    assert type(default_json) == type({})  #type dict

    param_json = default_json.copy()
    default_scores = default_json[ROOT_JSON_NAME]  #dictionary
    assert len(params) == len(ORDERED_KEYS)
    for i in range(len(ORDERED_KEYS)):
        keyname = ORDERED_KEYS[i]
        param_json[ROOT_JSON_NAME][keyname] = str(params[i])

    #merge parameters
    param_json[ROOT_JSON_NAME]["between_prism_sequence_gap_penalty"] \
        = param_json[ROOT_JSON_NAME]["Between_grape_sequence_gap_penalty"] 
    param_json[ROOT_JSON_NAME]["Within_prism_sequence_gap_penalty"] \
        = param_json[ROOT_JSON_NAME]["Within_grape_sequence_gap_penalty"]

    outfile = open(param_file, 'w')
    json.dump(param_json, outfile)
    outfile.close()

def scoreGarlic(params):
    """wrapper around garlic
    params is an array of numbers, length of ORDERED_KEYS
    """

    #create parameter json file
    createParamFile(params)

    #run garlic
    subprocess.call(GARLIC_ARGS)

    #score based on output
    csvfilename = OUTPUT_DIRECTORY + "/" + OUTPUT_BASENAME + SCORE_NAME
    #querylist, subjectlist, matrix = analyzeScores.parseFile(csvfilename)
    #because faster to run with prism as query:
    subjectlist, querylist, matrix = analyzeScores.parseFile(csvfilename)
    matrix = matrix.T

    score = SCORING_METHOD(querylist, subjectlist, matrix)
    return score

def setupFiles():
    """Initialize parameter files, directories"""
    subprocess.call(["mkdir", OUTPUT_DIRECTORY])

def initParams(default_json=DEFAULT_JSON):
    params = []
    for i in range(len(ORDERED_KEYS)):
        keyname = ORDERED_KEYS[i]
        params.append(float(default_json[ROOT_JSON_NAME][keyname]))
    return params

#*****************MAIN METHODS*****************
def test():
    setupFiles()
    params = initParams()
    paramOut = opt.fmin_powell(scoreGarlic, params, xtol=0.01, ftol = 0.001, maxfun=2000)
    createParamFile(paramOut, param_file = "Final.json")

if __name__=="__main__":
    test()


