## GARLIC  

GARLIC (Global Alignment for natuRaL-products chemInformatics) is a software package for the comparison of natural products and their clusters. This is typically done with GRAPE and PRISM results. The comparison can be in any of the three types: Clusters-Clusters, Clusters-Compounds, Comounds-Compounds.

## Usage

GARLIC is a Java package, which can be compiled then run from the command line. 

A sample command line run might look like: 

```
$ java -jar garlic.jar -q prism_clusters.json -s grape_breakdown.json -o output/
```

This runs GARLIC with the query prism clusters (prism_clusters.json) against the subject grape breakdown (grape_breakdown.json) and write the results to the output/ directory based on the current working directory.

To see a detailed description of all command line options, do:

```
$ java -jar garlic.jar -h 
``` 

## Dependencies

The dependant libraries are located in this repository. The neo4j library will soon be replaced as currently only the json constructor (ajax) is used in the release version of GARLIC and there are no connections to a neo4j database.

Garlic is also dependant on the [enums](https://github.com/magarveylab/enums-releases) package released by the magarveylab located here:

## Publication relevant files

The scripts used to generate figures and results from PRISM and GRAPE data is located in figure_methods, plotting, and pub_tests

## Citing GARLIC

Please cite: 

> XXXXXXXXX
