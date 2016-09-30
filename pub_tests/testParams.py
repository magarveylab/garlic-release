#!/bin/python
#analyzeGarlic.py
#Wrapper for garlic, used to optimize parameters
#by Mclean Edwards
#created: Mon Feb  8 10:06:12 EST 2016

#DELETEME for PUBLIC_RELEASE
#Most ideas in analyzeGarlic/some ideas left to try here
#This is a development script, not to be released

import json
import subprocess
import scipy.optimize as opt

import analyzeScores


#DEFAULTS (folders, initializations)
QUERY_DIRECTORY = "../prism_167"
SUBJECT_DIRECTORY = "../grape_167"
SCORE_NAME = "_score.csv"  #one of ["_raw.csv", "_relative.csv", "_score.csv", "_sugarscore.csv"]
SCORING_METHOD = analyzeScores.secondScore

DEFAULT_PARAM_FILE = "../scoring_test.json" 
PARAM_FILE = "./aGparameters.json"  #TEMP file 
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


#A list of scoringDict keys that we are using for parameters (all numerical values), order is important
ORDERED_KEYS = [ 
    "Fatty_acid_or_pk_gap_penalty", #?? - good start for nelder-mead as first parameter +0.025
    "Between_grape_sequence_gap_penalty", 
    "Within_grape_sequence_gap_penalty", 
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

ORDERED_KEYS = [ 
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



def createParamFile(params, default_json=DEFAULT_JSON, param_file = PARAM_FILE):
    assert len(params) == len(ORDERED_KEYS)
    assert type(default_json) == type({})  #type dict

    param_json = default_json.copy()
    default_scores = default_json[ROOT_JSON_NAME]  #dictionary
    assert len(params) == len(ORDERED_KEYS)
    for i in range(len(ORDERED_KEYS)):
        keyname = ORDERED_KEYS[i]
        param_json[ROOT_JSON_NAME][keyname] = str(params[i])

    #merge params
    #TODO: maybe more later (dim reduction)
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
    scoring_method is just that, defined ???
    """

    #create parameter json file
    createParamFile(params)

    #run garlic
    subprocess.call(GARLIC_ARGS)

    #score based on output
    csvfilename = OUTPUT_DIRECTORY + "/" + OUTPUT_BASENAME + SCORE_NAME
    #querylist, subjectlist, matrix = analyzeScores.parseFile(csvfilename)
    #hack for now:  because faster to run prism as query
    subjectlist, querylist, matrix = analyzeScores.parseFile(csvfilename)
    matrix = matrix.T

    score = SCORING_METHOD(querylist, subjectlist, matrix)
    return score

def printAllScores(params):
    """wrapper around garlic
    params is an array of numbers, length of ORDERED_KEYS
    scoring_method is just that, defined ???
    """

    #create parameter json file
    createParamFile(params)

    #run garlic
    subprocess.call(GARLIC_ARGS)

    #score based on output
    csvfilename = OUTPUT_DIRECTORY + "/" + OUTPUT_BASENAME + SCORE_NAME
    #querylist, subjectlist, matrix = analyzeScores.parseFile(csvfilename)
    #hack for now:  because faster to run prism as query
    subjectlist, querylist, matrix = analyzeScores.parseFile(csvfilename)
    matrix = matrix.T

    analyzeScores.printAllScores(querylist, subjectlist, matrix)

def getAllScores(params):
    """wrapper around garlic
    params is an array of numbers, length of ORDERED_KEYS
    scoring_method is just that, defined ???
    """

    #create parameter json file
    createParamFile(params)

    #run garlic
    subprocess.call(GARLIC_ARGS)

    #score based on output
    csvfilename = OUTPUT_DIRECTORY + "/" + OUTPUT_BASENAME + SCORE_NAME
    #querylist, subjectlist, matrix = analyzeScores.parseFile(csvfilename)
    #hack for now:  because faster to run prism as query
    subjectlist, querylist, matrix = analyzeScores.parseFile(csvfilename)
    matrix = matrix.T

    return analyzeScores.getAllScores(querylist, subjectlist, matrix)


def setupFiles():
    """Initialize parameter files, directories"""
    subprocess.call(["mkdir", OUTPUT_DIRECTORY])

def initParams(default_json=DEFAULT_JSON):
    params = []
    for i in range(len(ORDERED_KEYS)):
        keyname = ORDERED_KEYS[i]
        params.append(float(default_json[ROOT_JSON_NAME][keyname]))
    return params


#NOTE:  both outputConvex, outputLinear based on 3 by 3 values
def outputConvex(xvalues, fvalues):
    """Given list or tuple of xvalues and fvalues,
    return list of errors
    positive if convex (more so if more convex), negative if not, 0 if linear
    """
    assert len(xvalues) == len(fvalues)
    assert len(xvalues) >= 3
    convexdiff = []
    for i in range(len(xvalues)-2):
        x, y, z = xvalues[i:i+3]
        a, b, c = fvalues[i:i+3]
        #h is like lambda
        h = 1.0*(z - y)/(z - x)
        convexdiff.append(h*a + (1-h)*c - b)
    return convexdiff


def outputLinear(xvalues, fvalues):
    """
    returns 
    list of differences
    list of slopes
    """
    assert len(xvalues) == len(fvalues)
    assert len(xvalues) >= 3
    lineardiff = 0
    slopes = []
    for i in range(len(xvalues)-2):
        x, y, z = xvalues[i:i+3]
        a, b, c = fvalues[i:i+3]
        #h is like lambda
        h = 1.0*(z - y)/(z - x)
        lineardiff += abs(h*a + (1-h)*c - b)
        slopes.append(1.0*(c-a)/(z-x))
    return lineardiff, slopes

def test():
    setupFiles()
    params = initParams()
    print "STARTING SCORES:"
    start_counter, start_counter_better, start_basicscore, \
            start_secondscore, start_flatscore, start_ls2score = getAllScores(params)
    print start_basicscore, start_secondscore, start_flatscore, start_ls2score

    basicscores = [0.0,]*9
    secondscores = [0.0,]*9
    flatscores = [0.0,]*9
    ls2scores = [0.0,]*9
    scorearray = [basicscores, secondscores, flatscores, ls2scores]
    scorenames = ["basic", "second", "flat", "ls2"]
    startscorearray = [start_basicscore, start_secondscore, start_flatscore, start_ls2score]

    paramvalues = [0.0,]*9

    MULTIPLIERS = [0.5, 0.95, 1.0, 1.05, 1.5, 10, 100, 1000, 10000]
    for i in range(len(params)):
        initial_param = params[i]
        for j in range(len(MULTIPLIERS)):
            multiplier = MULTIPLIERS[j]
            paramvalues[j] = initial_param * multiplier
            if multiplier == 1.0:
                assert j == 2
                #fill scores of each type with starting values
                for typeindex in range(len(scorearray)):
                    scorearray[typeindex][j] = startscorearray[typeindex]
            else:
                params[i] = initial_param*multiplier
                these_scores = getAllScores(params)
                #fill scores of each type with proper scores
                for typeindex in range(len(scorearray)):
                    scorearray[typeindex][j] = these_scores[2:][typeindex]

                print "For "+ORDERED_KEYS[i]+": "+str(initial_param*multiplier)
                print basicscores[j], secondscores[j], flatscores[j], ls2scores[j]
    
        #h = lambda
        h = 1.0*(MULTIPLIERS[-1] - MULTIPLIERS[-2])/(MULTIPLIERS[-1] - MULTIPLIERS[-3])
        print
        print "RESULTS for :" + ORDERED_KEYS[i]
        #below should be positive if convex
        for typeindex in range(len(scorearray)):
            convexdiff = outputConvex(paramvalues, scorearray[typeindex])
            lineardiff, slopes = outputLinear(paramvalues, scorearray[typeindex])
            print "For "+scorenames[typeindex] +" (convex, linear, slopes): "
            print ["%.3f" % val for val in convexdiff]
            print lineardiff
            print ["%.3f" % val for val in slopes]
        print
        params[i] = initial_param



if __name__=="__main__":
    test()


