#!/bin/bash (not sh)
#mtGarlic.sh
#Multi Threaded Garlic Bash script

#TODO:  allow for command arguments

TEMP_PREFIX=HYBRID
TEMP_DIR=HYBRID

endings=(_raw.csv _relative.csv _score.csv _sugarscore.csv)

#bring them together
for ending in ${endings[@]}
do
    started=false
    for i in $TEMP_DIR/*$ending
    do
        if ! ($started)
        then
            sed -n '1p' "$i" > $TEMP_PREFIX$ending
            started=true
        fi
        sed -n '2p' "$i" >> $TEMP_PREFIX$ending
        echo >> $TEMP_PREFIX$ending
    done
done

