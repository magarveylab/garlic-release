#!/bin/python
#analyzeScores.py
#Analyze score csv outputs from garlic
#Use:  python analyzeScores.py <filepath_to_csv_scores>
#created by Mclean Edwards for Magarvey Lab
#Created Wed Jan 27 13:37:42 EST 2016

FILE_NAME = "output/output_relative.csv"
FILE_PATH = "output/"
FILE_BASE = "output"
FILE_ENDING_RAW = "_raw.csv"
FILE_ENDING_RELATIVE = "_relative.csv"
FILE_ENDING_SUGAR = "_sugarscore.csv"
FILE_ENDINGS = [FILE_ENDING_RAW, FILE_ENDING_RELATIVE, FILE_ENDING_SUGAR]

#TO PREPARE FOR PUBLIC_RELEASE: DELETE ALL SCORES THAT ARE NOT RANK-BASED
SECOND_SCORE_MAX = 50 #chosen since this is a high non-relative score


import sys
import numpy as np

BAD_NUMBERS = [np.nan, np.inf, -np.inf]

def main(filepath):
    """main(filepath)
    filepath - filepath to csv output from garlic
    this csv file is assumed to follow the format:
    1st line:
    csv seperated list of strings for subject filenames
    the first entry is ignored
    nth line:
    name of query, followed by score for each subject in the first line

    for filenames, any extension is removed (everything after the first '.')
    once the file extension is removed, it is assumed that matches will be
    identically named (grape and prism)
    """
    querylist, subjectlist, matrix = parseFile(filepath)
    #Uncomment below to swap query/subject:
    #temp = querylist
    #querylist = subjectlist
    #subjectlist = temp
    #matrix = matrix.T
    hits, basicscore, secondscore, flatscore, ls2score = printResults(querylist, subjectlist, matrix)
    printAllScores(querylist, subjectlist, matrix)

def parseFile(filepath):
    file = open(filepath, 'r')
    subjectlist = file.readline().split(',')[1:]
    subjectlist[-1] = subjectlist[-1][:-1] #remove \n character
    csvMatrix = np.loadtxt(file, delimiter=',', dtype = 'string')
    file.close()
    querylist = csvMatrix.T[0].tolist()
    matrix = np.array(csvMatrix.T[1:].T, dtype='d')
    for namelist in [subjectlist, querylist]:
        for i in range(len(namelist)):
            #YCli:  comment this next line out
            namelist[i] = namelist[i].split('.')[0]
    #For testing:  delete or comment for speed
    #for subjectname in subjectlist:
    #    assert subjectname in querylist
    for n in BAD_NUMBERS:
        assert n not in matrix
    return querylist, subjectlist, matrix

def getEquivalent(querylist, subjectlist, matrix):
    """Returns a 2D array of matching sets
    Only use for relative scores (match = 1.0).
    Built for grape on grape or prism on prism (so 1.0 happens)
    Easy way to find what garlic cannot tell apart.
    NOT MEANT FOR GRAPE/PRISM
    """
    mA = np.sum(matrix >= 1.0, axis = 1)
    mInd = np.nonzero(matrix[mA > 1] >= 1.0)
    equiv_clusters = []
    lastindex = -1
    for i in range(len(mInd[0])):
        if mInd[0][i] == lastindex:
            #ICIHERE
            equiv_clusters[mInd[0][i]].append(querylist[mInd[1][i]])
        else:
            equiv_clusters.append([querylist[mInd[1][i]],])
        lastindex = mInd[0][i]
    return equiv_clusters

def secondScore(querylist, subjectlist, matrix):
    secondscore = 0.0 #add max(n_score - matchscore)

    for j in range(len(subjectlist)):
        subjectname = subjectlist[j]
        jscores = matrix[:,j]  #scores for this subject against each query
        assert subjectname in querylist
        i = querylist.index(subjectname)
        matchscore = matrix[i,j]
        scores_without_target = np.concatenate((jscores[:i],jscores[i+1:]))
        maxsecond = scores_without_target.max()
        secondscore += matchscore - maxsecond

    return -secondscore  #for using minimizing

def secondScore2(querylist, subjectlist, matrix):
    secondscore = 0.0 #add max(n_score - matchscore)

    for j in range(len(subjectlist)):
        subjectname = subjectlist[j]
        jscores = matrix[:,j]  #scores for this subject against each query
        assert subjectname in querylist
        i = querylist.index(subjectname)
        matchscore = matrix[i,j]
        scores_without_target = np.concatenate((jscores[:i],jscores[i+1:]))
        maxsecond = scores_without_target.max()
        secondscore += np.min((matchscore,SECOND_SCORE_MAX)) - maxsecond

    return -secondscore  #for using minimizing

def thirdScore(querylist, subjectlist, matrix):
    secondscore = 0.0 #add max(n_score - matchscore)

    for j in range(len(subjectlist)):
        subjectname = subjectlist[j]
        jscores = np.array(matrix[:,j])  #scores for this subject against each query
        assert subjectname in querylist
        i = querylist.index(subjectname)
        matchscore = matrix[i,j]

        scores_without_target = np.concatenate((jscores[:i],jscores[i+1:]))
        maxsecond = scores_without_target.max()
        secondscore += np.min((matchscore,SECOND_SCORE_MAX)) - maxsecond

        #and again, subject to that query it should match
        iscores = np.array(matrix[i,:])
        scores_without_target = np.concatenate((iscores[:i],iscores[i+1:]))
        maxsecond = scores_without_target.max()
        secondscore += np.min((matchscore,SECOND_SCORE_MAX)) - maxsecond

    return -secondscore  #for using minimizing


def ls2Score(querylist, subjectlist, matrix):
    #Note, ls2maxmin should be 1.0/-1.0 for normalized, maybe +50/-50 for raw??
    #TODO: remove, place at top as option
    ls2maxmin = [1.0, -1.0]
    ls2score = 0.0

    for j in range(len(subjectlist)):
        subjectname = subjectlist[j]
        jscores = matrix[:,j]  #scores for this subject against each query
        assert subjectname in querylist
        i = querylist.index(subjectname)
        matchscore = matrix[i,j]

        scores_without_target = np.concatenate((jscores[:i],jscores[i+1:]))

        highscore = np.min((matchscore, ls2maxmin[0]))
        minlevel = ls2maxmin[1] * np.ones_like(scores_without_target)
        maxlevel = ls2maxmin[0] * np.ones_like(scores_without_target)
        capped_scores = np.min((maxlevel, scores_without_target), axis=0)
        sqlist = (minlevel \
            - np.max((minlevel, capped_scores), axis=0))**2
        ls2score_add = (ls2maxmin[0] - highscore)**2 + sqlist.sum()
        ls2score += ls2score_add

    return ls2score

def flatScore(querylist, subjectlist, matrix):
    flatscore = 0.0 #add target score, subtract all others

    target_scoring = [1.0*len(querylist), -1.0]

    for j in range(len(subjectlist)):
        subjectname = subjectlist[j]
        jscores = matrix[:,j]  #scores for this subject against each query
        assert subjectname in querylist
        i = querylist.index(subjectname)
        matchscore = matrix[i,j]

        scores_without_target = np.concatenate((jscores[:i],jscores[i+1:]))

        flatscore_add = matchscore * target_scoring[0] \
            + (target_scoring[1]*scores_without_target).sum()
        flatscore += flatscore_add

    return -flatscore #for minimization

def basicScore(querylist, subjectlist, matrix):
    counter = 0
    counter_better = 0 #only adds if target is match and is unique
    basicscore = 0.0 #adds 1/rank in scores

    for j in range(len(subjectlist)):
        subjectname = subjectlist[j]
        jscores = np.array(matrix[:,j])  #scores for this subject against each query
        assert subjectname in querylist
        i = querylist.index(subjectname)
        matchscore = matrix[i,j]

        maxscore = jscores.max()

        if matchscore == maxscore:
            number_matched_to = np.count_nonzero(jscores == matchscore)
            counter += 1
            if number_matched_to == 1:
                counter_better += 1
            basicscore += 1.0/number_matched_to
        else:
            jscores.sort()
            matchindex = np.searchsorted(jscores, matchscore)
            scorelist = jscores.argsort().tolist()
            basicscore += 1.0/(len(querylist) - matchindex)

    return basicscore, counter, counter_better

def rankScore(querylist, subjectlist, matrix):
    counter = 0
    counter_better = 0 #only adds if target is match and is unique
    basicscore = 0.0 #adds 1/rank in scores

    for j in range(len(subjectlist)):
        subjectname = subjectlist[j]
        jscores = np.array(matrix[:,j])  #scores for this subject against each query
        assert subjectname in querylist
        i = querylist.index(subjectname)
        matchscore = matrix[i,j]

        maxscore = jscores.max()

        if matchscore == maxscore:
            number_matched_to = np.count_nonzero(jscores == matchscore)
            counter += 1
            if number_matched_to == 1:
                counter_better += 1
            basicscore += number_matched_to
        else:
            jscores.sort()
            matchindex = np.searchsorted(jscores, matchscore)
            scorelist = jscores.argsort().tolist()
            basicscore += (len(querylist) - matchindex)

    return basicscore



def basicScoreOnly(querylist, subjectlist, matrix):
    basicscore, counter, counter_better = basicScore(querylist, subjectlist, matrix)
    return -basicscore #for minimization

def printAllScores(querylist, subjectlist, matrix):
    basicscore, counter, counter_better = basicScore(querylist, subjectlist, matrix)
    flatscore = flatScore(querylist, subjectlist, matrix)
    secondscore = secondScore(querylist, subjectlist, matrix)
    ls2score = ls2Score(querylist, subjectlist, matrix)
    print counter, counter_better, basicscore, secondscore, flatscore, ls2score
    
def getAllScores(querylist, subjectlist, matrix):
    basicscore, counter, counter_better = basicScore(querylist, subjectlist, matrix)
    flatscore = flatScore(querylist, subjectlist, matrix)
    secondscore = secondScore(querylist, subjectlist, matrix)
    ls2score = ls2Score(querylist, subjectlist, matrix)
    return counter, counter_better, basicscore, secondscore, flatscore, ls2score

def printResults(querylist, subjectlist, matrix):
    #temp = querylist
    #querylist = subjectlist
    #subjectlist = temp
    #matrix = matrix.T
    counter = 0
    counter_better = 0 #only adds if target is match and is unique
    basicscore = 0.0 #adds 1/rank in scores
    secondscore = 0.0 #add max(n_score - matchscore)
    target_scoring = [1.0*len(querylist), -1.0]
    flatscore = 0.0 #add target score, subtract all others
    #Note, ls2maxmin should be 1.0/-1.0 for normalized, maybe +50/-50 for raw??
    ls2maxmin = [1.0, -1.0]
    ls2score = 0.0

    matchscores = []
    maxscores = []

    for j in range(len(subjectlist)):
        subjectname = subjectlist[j]
        jscores = np.array(matrix[:,j])  #scores for this subject against each query
        print "For subject: " + subjectname
        assert subjectname in querylist
        i = querylist.index(subjectname)
        matchscore = matrix[i,j]
        print "Match score:  " + str(matchscore)

        maxscore = jscores.max()
        matchscores.append(matchscore)
        maxscores.append(maxscore)
        scores_without_target = np.concatenate((jscores[:i],jscores[i+1:]))

        maxsecond = scores_without_target.max()
        secondscore += matchscore - maxsecond
        print "Second Score: " + str(matchscore - maxsecond)

        flatscore_add = matchscore * target_scoring[0] \
            + (target_scoring[1]*scores_without_target).sum()
        print "Flatscore: " + str(flatscore_add)
        flatscore += flatscore_add

        highscore = np.min((matchscore, ls2maxmin[0]))
        minlevel = ls2maxmin[1] * np.ones_like(scores_without_target)
        maxlevel = ls2maxmin[0] * np.ones_like(scores_without_target)
        capped_scores = np.min((maxlevel, scores_without_target), axis=0)
        sqlist = (minlevel \
            - np.max((minlevel, capped_scores), axis=0))**2
        ls2score_add = (ls2maxmin[0] - highscore)**2 + sqlist.sum()
        print "Sum Squares: " + str(ls2score_add)
        ls2score += ls2score_add

        if matchscore == maxscore:
            number_matched_to = np.count_nonzero(jscores == matchscore)
            print "Matched to " + str(number_matched_to)
            counter += 1
            if number_matched_to == 1:
                counter_better += 1
            basicscore += 1.0/number_matched_to
        else:
            scorelist = jscores.argsort().tolist()
            basicscore += 1.0/(len(querylist) - scorelist.index(i))
            print "Rank of true match:  " + str(len(querylist) \
                    - scorelist.index(i))
            print "Matched to " + querylist[jscores.argmax()]
            top5 = jscores.argsort()[-5:]
            top5names = np.array(querylist)[top5]
            top5scores = jscores[top5]
            for rank in range(1,6):
                print "Rank "+str(rank)+" matched to " + top5names[-rank] \
                        +" with score:  "+str(top5scores[-rank])
    print
    print "Out of " + str(len(subjectlist)) + " possible hits: " \
            + str(counter_better)
    print "Out of " + str(len(subjectlist)) + " best score: " + str(counter)
    print "Basic Score: " + str(basicscore)
    print "FlatScore: " + str(flatscore)
    print "Second Score: " + str(secondscore)
    print "SumSquaredError: " + str(ls2score)
    print "Normalized Flat: " \
        + str(flatscore/2.0/len(subjectlist)/len(querylist))
    print "Normalized Second: " + str(secondscore/len(subjectlist))
    print "Normalized SumSquared" \
        + str(np.sqrt(ls2score/len(subjectlist)/len(querylist)))
    return counter, basicscore, secondscore, flatscore, ls2score


if __name__ == "__main__": 
    if (len(sys.argv)) < 2:
        main(FILE_NAME)
    else:
        main(sys.argv[1]) 

