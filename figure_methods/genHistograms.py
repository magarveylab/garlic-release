#!/bin/bash
#genHistograms.py
#by Mclean Edwards for MagarveyLab

#Generate histograms from relative score csv file outputs from garlic
#Also outputs lists of files within a set threshold CUTOFF_SCORE

CUTOFF_SCORE = 0.33

INPUT_FILE = "./HYBRID_relative.csv"
WHICH = 2 #index of ORDER/HIGHBAR

ORDER = ["PK", "NRP", "HYBRID"]
HIGHBAR = [0.87, 0.90, 0.74]

import analyzeScores
import numpy as np
import matplotlib as mpl
mpl.use("Cairo")
import matplotlib.pyplot as plt

def main():
    matched_names = set()
    top_scores = []
    unknown_names = set()
    querynames, subjectnames, matrix = analyzeScores.parseFile(INPUT_FILE)
    print len(querynames), len(subjectnames)
    print len(set(querynames)), len(set(subjectnames))
    assert len(querynames) == len(matrix)
    knownfilename = ORDER[WHICH] + "_KNOWN"
    unknownfilename = ORDER[WHICH] + "_UNKNOWN"
    knownfile = open(knownfilename, 'w')
    unknownfile = open(unknownfilename, 'w')

    subjectscores = {}

    counterhigh = 0
    counterlow = 0
    unknown_cluster_count = 0
    unknown_clusters = set()
    for i in range(len(querynames)):
	row = np.array(matrix[i])
        indsort = row.argsort()
        row.sort()
        topscore = row[-1]
        topscores = row[-10:]
        top_scores.append(topscore)
        #top_scores.extend(topscores)
	if topscore < CUTOFF_SCORE:
                unknown_cluster_count += 1
                unknown_clusters.add(subjectnames[indsort[-1]])
	for jj in range(len(topscores)):
                score = topscores[jj]
                subjectname = subjectnames[indsort[-10+jj]]
                if subjectname not in subjectscores:
                    subjectscores[subjectname] = score
                else:
                    if score > subjectscores[subjectname]:
                        subjectscores[subjectname] = score
                        if subjectname in unknown_names and score >= CUTOFF_SCORE:
                            unknown_names.remove(subjectname) 
		if score > HIGHBAR[WHICH]:
		    matched_names.add(subjectname)
		    knownfile.write(subjectname+"\n")
                    counterhigh += 1
		elif score < CUTOFF_SCORE:
		    unknown_names.add(subjectname)
		    unknownfile.write(subjectname+"\n")
                    counterlow += 1

    #TODO:  files need fixing since taking max value
    unknownfile.close()
    knownfile.close()

    """for subjectname in subjectnames:
        if subjectname in subjectscores:
            top_scores.append(subjectscores[subjectname])
    """

    print "KNOWN: "+str(len(matched_names))
    print "UNKNOWN: "+str(len(unknown_names))
    print "UNKNOWN CLUSTERS: "+str(unknown_cluster_count)
    print "Derep UNKNOWN CLUSTERS:  "+str(len(unknown_clusters))
    print "Number of clusters: "+ str(len(top_scores))
    print counterhigh
    print counterlow

    histresult = np.histogram(top_scores, 50, range=(0.0, 1.0))


    bigscores = np.array(top_scores)
    
    #plotit
    fig = plt.figure()
    ax = fig.add_subplot(111)
    ax.hist(top_scores, 50, range=(0.0, 1.0))
    fig.savefig(ORDER[WHICH]+"_hist.pdf")

    fig = plt.figure()
    ax = fig.add_subplot(111)
    ax.hist(top_scores, 100, range=(0.0, 2.0))
    fig.savefig(ORDER[WHICH]+"_histall.pdf")

    fig = plt.figure()
    ax = fig.add_subplot(111)
    cutscores = np.array(bigscores[bigscores <= 0.33])
    ax.hist(cutscores, 50)
    plt.xlim([0, 1.0])
    fig.savefig(ORDER[WHICH]+"_histcut.pdf")


if __name__=="__main__":
    main()
