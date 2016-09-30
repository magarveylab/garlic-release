#!/bin/bash
set -e

QUEUE=high_priority.q

CLASSES=(PK NRP HYBRID)

if [ "$#" -ne 2 ]; then
	echo "Illegal number of params
Requires input directory, and output directory"
	exit 1
fi

#get absolute paths of arguments
SUBJECTDIR="$(readlink -f "$1")"
ERRDIR="$(readlink -f "$2")"

if [ ! -d "$SUBJECTDIR" ]; then
	echo "Subject directory "$SUBJECTDIR" does not exisst"
	exit 1
fi

#make output dirs if don't exist
if [ ! -d "$ERRDIR" ]; then
	mkdir "$ERRDIR"
fi

for CLASS in ${CLASSES[@]}
do

    FILESLIST="./${CLASS}biglist.txt"

    FILECOUNT="$(wc -l "$FILESLIST" | cut -f1 -d ' ')"

    OUTDIR=$CLASS

    if [ ! -d "$OUTDIR" ]; then
        mkdir "$OUTDIR"
    fi

    echo $FILECOUNT

    qsub -l h_vmem=38G -q $QUEUE -o $ERRDIR -t 1-$FILECOUNT ~/grid_scripts/bigrun.sub "$FILESLIST" "$SUBJECTDIR" "$OUTDIR" 


done


