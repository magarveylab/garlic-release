#!/bin/python
#grabTestSet.py
#by Mclean Edwards for magarvey lab
#Created: Wed Mar 16 14:57:58 EDT 2016

"""Uses a previous garlic output of alignments such as:
    java -jar garlic.jar -k -o output -q prism_167 -s grape_48K -i scoring_test.json
and creates a test set directory of those subjects that best match each query,
about 10 per query, and deduplicating results
also includes corresponding subjects if they correspond to the query's name

Assumes unix-type os (linux tested, maybe mac/android)
"""

import os
import subprocess

SUBJECT_ENDING = ".json"
QUERY_ENDING = ".fasta.json"

ALIGN_ENDING = ".fasta_garlic.txt"

ALIGN_DIRECTORY = "./GF/output"
SUBJECT_DIRECTORY = "./grape_48K_nospaces"
QUERY_DIRECTORY = "./prism_167"
NEW_DIRECTORY = "./grape_GF"
PREV_DIRECTORY = "./grape_463" #not used

SCORE_THRESHOLD = 0.05 #relative score threshold for grouping results
HIT_THRESHOLD = 5 #number of hits to take from each alignment file

def main():
    align_filenames = os.listdir(ALIGN_DIRECTORY)
    subject_filenames = os.listdir(SUBJECT_DIRECTORY)

    includelist = os.listdir(QUERY_DIRECTORY)
    for i in range(len(includelist)):
        includelist[i] = includelist[i].split(QUERY_ENDING)[0]+SUBJECT_ENDING  #basename
        assert includelist[i] in subject_filenames

    totalhits = 0
    for filename in align_filenames:
        file = open(ALIGN_DIRECTORY+"/"+filename, 'r')
        line = file.readline()
        name = filename.split(ALIGN_ENDING)[0]

        hitcounter = 0
        lastscore = None
        highscore = None
        similar_subjects = []

        while(line != "" and hitcounter < HIT_THRESHOLD):
            line = file.readline() #never first line

            if "Subject name:" in line:
                subjectname = line[14:-1]  #between Subject name:_ and newline
                discard = file.readline()  #assumes three lines of alignment scoring
                discard = file.readline()
                discard = file.readline()
                scoreline = file.readline()
                assert "Raw Score:" in scoreline
                relscore_string = scoreline.split(' ')[-1]  #assumes last score on this line
                relscore = float(relscore_string[:-1]) #remove newline

                if subjectname != "5": #fixing broken file
                    if subjectname+SUBJECT_ENDING not in subject_filenames:
                        print line
                        print scoreline
                        print "Not in subject listing:  "+subjectname
                    if lastscore == None or highscore - relscore < SCORE_THRESHOLD:
                        lastscore = relscore
                        if highscore == None or highscore < relscore:
                            highscore = relscore
                        similar_subjects.append(subjectname)
                    else: #finished similar_subjects
                        hitcounter += 1
                        assert len(similar_subjects) > 0

                        #Determine if something in the simliar list is already in the group
                        already_included = False
                        bestmatch = similar_subjects[0]
                        for ss in similar_subjects:
                            if ss+SUBJECT_ENDING in includelist:
                                already_included = True
                                break
                            if len(bestmatch) > len(ss): #prefer files with shorter names
                                bestmatch = ss

                        if not already_included:
                            print bestmatch
                            totalhits += 1
                            includelist.append(bestmatch + SUBJECT_ENDING)

                        similar_subjects = [subjectname,]
                        lastscore = relscore
                        highscore = relscore
                    

        file.close()

    print totalhits

    subprocess.call(["mkdir", NEW_DIRECTORY])
    for filename in includelist:
        subprocess.call(["cp", SUBJECT_DIRECTORY+"/"+filename, NEW_DIRECTORY+"/"+filename])


if __name__=="__main__":
    main()
