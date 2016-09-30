library(reshape2)
library(ggplot2)

# A value greater than the highest rank
infinity = 100000

## Load the files
# TODO: condense this with loops or apply statements
base.local.ranks <- read.table("may7_garlic_comparison/baseLocal/ranks.txt")
base.global.ranks <- read.table("may7_garlic_comparison/baseGlobal/ranks.txt")
global.refined.ranks <- read.table("may7_garlic_comparison/globalRefinedScores/ranks.txt")
global.refined.tailoring.ranks <- read.table("may7_garlic_comparison/globalRefinedScoresTailoring/ranks.txt")
global.refined.tailoring.lcs.ranks <- read.table("may7_garlic_comparison/globalRefinedScoresTailoringLCS/ranks.txt")
local.refined.ranks <- read.table("may7_garlic_comparison/localRefinedScores/ranks.txt")
local.refined.tailoring.ranks <- read.table("may7_garlic_comparison/localRefinedScoresTailoring/ranks.txt")
local.refined.tailoring.lcs.ranks <- read.table("may7_garlic_comparison/localRefinedScoresTailoringLCS/ranks.txt")
global.refined2.ranks <- read.table("may7_garlic_comparison/globalRefined2Scores/ranks.txt")
global.refined2.tailoring.ranks <- read.table("may7_garlic_comparison/globalRefined2ScoresTailoring/ranks.txt")
global.refined2.tailoring.lcs.ranks <- read.table("may7_garlic_comparison/globalRefined2ScoresTailoringLCS/ranks.txt")
global.refined3.ranks <- read.table("may7_garlic_comparison/globalRefined3Scores/ranks.txt")
global.refined3.tailoring.ranks <- read.table("may7_garlic_comparison/globalRefined3ScoresTailoring/ranks.txt")
global.refined3.tailoring.lcs.ranks <- read.table("may7_garlic_comparison/globalRefined3ScoresTailoringLCS/ranks.txt")

base.local.ranks <- base.local.ranks[,c(1,2)]
base.global.ranks <- base.global.ranks[,c(1,2)]
global.refined.ranks <- global.refined.ranks[,c(1,2)]
global.refined.tailoring.ranks <- global.refined.tailoring.ranks[,c(1,2)]
global.refined.tailoring.lcs.ranks <- global.refined.tailoring.lcs.ranks[,c(1,2)]
local.refined.ranks <- local.refined.ranks[,c(1,2)]
local.refined.tailoring.ranks <- local.refined.tailoring.ranks[,c(1,2)]
local.refined.tailoring.lcs.ranks <- local.refined.tailoring.lcs.ranks[,c(1,2)]
global.refined2.ranks <- global.refined2.ranks[,c(1,2)]
global.refined2.tailoring.ranks <- global.refined2.tailoring.ranks[,c(1,2)]
global.refined2.tailoring.lcs.ranks <- global.refined2.tailoring.lcs.ranks[,c(1,2)]
global.refined3.ranks <- global.refined3.ranks[,c(1,2)]
global.refined3.tailoring.ranks <- global.refined3.tailoring.ranks[,c(1,2)]
global.refined3.tailoring.lcs.ranks <- global.refined3.tailoring.lcs.ranks[,c(1,2)]

## Load the files
base.local.scores <- read.table("may7_garlic_comparison/baseLocal/scores.txt")
base.global.scores <- read.table("may7_garlic_comparison/baseGlobal/scores.txt")
global.refined.scores <- read.table("may7_garlic_comparison/globalRefinedScores/scores.txt")
global.refined.tailoring.scores <- read.table("may7_garlic_comparison/globalRefinedScoresTailoring/scores.txt")
global.refined.tailoring.lcs.scores <- read.table("may7_garlic_comparison/globalRefinedScoresTailoringLCS/scores.txt")
local.refined.scores <- read.table("may7_garlic_comparison/localRefinedScores/scores.txt")
local.refined.tailoring.scores <- read.table("may7_garlic_comparison/localRefinedScoresTailoring/scores.txt")
local.refined.tailoring.lcs.scores <- read.table("may7_garlic_comparison/localRefinedScoresTailoringLCS/scores.txt")
global.refined2.scores <- read.table("may7_garlic_comparison/globalRefined2Scores/scores.txt")
global.refined2.tailoring.scores <- read.table("may7_garlic_comparison/globalRefined2ScoresTailoring/scores.txt")
global.refined2.tailoring.lcs.scores <- read.table("may7_garlic_comparison/globalRefined2ScoresTailoringLCS/scores.txt")
global.refined3.scores <- read.table("may7_garlic_comparison/globalRefined3Scores/scores.txt")
global.refined3.tailoring.scores <- read.table("may7_garlic_comparison/globalRefined3ScoresTailoring/scores.txt")
global.refined3.tailoring.lcs.scores <- read.table("may7_garlic_comparison/globalRefined3ScoresTailoringLCS/scores.txt")


## Do some renaming
colnames(base.local.ranks) <- c("Name", "base.local")
colnames(base.global.ranks) <- c("Name", "base.global")
colnames(global.refined.ranks) <- c("Name", "global.refined")
colnames(global.refined.tailoring.ranks) <- c("Name", "global.refined.tailoring")
colnames(global.refined.tailoring.lcs.ranks) <- c("Name", "global.refined.tailoring.lcs")
colnames(local.refined.ranks) <- c("Name", "local.refined")
colnames(local.refined.tailoring.ranks) <- c("Name", "local.refined.tailoring")
colnames(local.refined.tailoring.lcs.ranks) <- c("Name", "local.refined.tailoring.lcs")
colnames(global.refined2.ranks) <- c("Name", "global.refined2")
colnames(global.refined2.tailoring.ranks) <- c("Name", "global.refined2.tailoring")
colnames(global.refined2.tailoring.lcs.ranks) <- c("Name", "global.refined2.tailoring.lcs")
colnames(global.refined3.ranks) <- c("Name", "global.refined3")
colnames(global.refined3.tailoring.ranks) <- c("Name", "global.refined3.tailoring")
colnames(global.refined3.tailoring.lcs.ranks) <- c("Name", "global.refined3.tailoring.lcs")


## Do some renaming
colnames(base.local.scores) <- c("Name", "base.local")
colnames(base.global.scores) <- c("Name", "base.global")
colnames(global.refined.scores) <- c("Name", "global.refined")
colnames(global.refined.tailoring.scores) <- c("Name", "global.refined.tailoring")
colnames(global.refined.tailoring.lcs.scores) <- c("Name", "global.refined.tailoring.lcs")
colnames(local.refined.scores) <- c("Name", "local.refined")
colnames(local.refined.tailoring.scores) <- c("Name", "local.refined.tailoring")
colnames(local.refined.tailoring.lcs.scores) <- c("Name", "local.refined.tailoring.lcs")
colnames(global.refined2.scores) <- c("Name", "global.refined2")
colnames(global.refined2.tailoring.scores) <- c("Name", "global.refined2.tailoring")
colnames(global.refined2.tailoring.lcs.scores) <- c("Name", "global.refined2.tailoring.lcs")
colnames(global.refined3.scores) <- c("Name", "global.refined3")
colnames(global.refined3.tailoring.scores) <- c("Name", "global.refined3.tailoring")
colnames(global.refined3.tailoring.lcs.scores) <- c("Name", "global.refined3.tailoring.lcs")


## Merge into one data frame
all.data.ranks <- merge(base.local.ranks, base.global.ranks, by="Name")
all.data.ranks <- merge(all.data.ranks, global.refined.ranks, by="Name")
all.data.ranks <- merge(all.data.ranks, global.refined.tailoring.ranks, by="Name")
all.data.ranks <- merge(all.data.ranks, global.refined.tailoring.lcs.ranks, by="Name")
all.data.ranks <- merge(all.data.ranks, local.refined.ranks, by="Name")
all.data.ranks <- merge(all.data.ranks, local.refined.tailoring.ranks, by="Name")
all.data.ranks <- merge(all.data.ranks, local.refined.tailoring.lcs.ranks, by="Name")
all.data.ranks <- merge(all.data.ranks, global.refined2.ranks, by="Name")
all.data.ranks <- merge(all.data.ranks, global.refined2.tailoring.ranks, by="Name")
all.data.ranks <- merge(all.data.ranks, global.refined2.tailoring.lcs.ranks, by="Name")
all.data.ranks <- merge(all.data.ranks, global.refined3.ranks, by="Name")
all.data.ranks <- merge(all.data.ranks, global.refined3.tailoring.ranks, by="Name")
all.data.ranks <- merge(all.data.ranks, global.refined3.tailoring.lcs.ranks, by="Name")


## Merge into one data frame
all.data.scores <- merge(base.local.scores, base.global.scores, by="Name")
all.data.scores <- merge(all.data.scores, global.refined.scores, by="Name")
all.data.scores <- merge(all.data.scores, global.refined.tailoring.scores, by="Name")
all.data.scores <- merge(all.data.scores, global.refined.tailoring.lcs.scores, by="Name")
all.data.scores <- merge(all.data.scores, local.refined.scores, by="Name")
all.data.scores <- merge(all.data.scores, local.refined.tailoring.scores, by="Name")
all.data.scores <- merge(all.data.scores, local.refined.tailoring.lcs.scores, by="Name")
all.data.scores <- merge(all.data.scores, global.refined2.scores, by="Name")
all.data.scores <- merge(all.data.scores, global.refined2.tailoring.scores, by="Name")
all.data.scores <- merge(all.data.scores, global.refined2.tailoring.lcs.scores, by="Name")
all.data.scores <- merge(all.data.scores, global.refined3.scores, by="Name")
all.data.scores <- merge(all.data.scores, global.refined3.tailoring.scores, by="Name")
all.data.scores <- merge(all.data.scores, global.refined3.tailoring.lcs.scores, by="Name")


all.data.ranks$Name = as.character(all.data.ranks$Name)
all.data.scores$Name = as.character(all.data.scores$Name)


# Exclude compounds
compound.annotations <- read.table("may9_annotations.txt", sep="\t", header=TRUE)

compounds.to.exclude <- as.character(compound.annotations[ compound.annotations$Include...Y.N. == "N",][,1])

# Note: for a small number of cases (around 3) there were missing scores (possibly because GRAPE hit an error), the default score was -100
missing.scores <- all.data.scores$Name[apply(all.data.scores[,2:ncol(all.data.scores)], 1, function(x) -100 %in% x)]

compounds.to.exclude <- c(compounds.to.exclude, missing.scores)

all.data.scores <- all.data.scores[!(all.data.scores$Name %in% compounds.to.exclude),]
all.data.ranks <- all.data.ranks[!(all.data.ranks$Name %in% compounds.to.exclude),]

# Determine the subset / order of columns. 
# All alignments, reordered
all.data.scores <- all.data.scores[,c(1,2,7,8,9,3:6,10:15)]
all.data.ranks <- all.data.ranks[,c(1,2,7,8,9,3:6,10:15)]






# Set the rank of values not in the top 50 to "infinity" - this way they are always higher than the cutoff
all.data.ranks[all.data.ranks==-1] <- infinity


all.data.scores[,2:ncol(all.data.scores)][all.data.scores[,2:ncol(all.data.scores)] < 0] <- 0

all.data.scores[,2:ncol(all.data.scores)] <- sapply(all.data.scores[,2:ncol(all.data.scores)], scale)

#all.data <- all.data[rev(order(all.data$global.refined3.tailoring.lcs)),]

barplot.data <- data.frame(Cutoff=seq(5,50,5))
barplot.data$base.local <- sapply(barplot.data$Cutoff, function(x) sum(all.data.ranks$base.local <= x))
barplot.data$base.global <- sapply(barplot.data$Cutoff, function(x) sum(all.data.ranks$base.global <= x))
barplot.data$global.refined3 <- sapply(barplot.data$Cutoff, function(x) sum(all.data.ranks$global.refined3 <= x))
barplot.data$global.refined3.tailoring <- sapply(barplot.data$Cutoff, function(x) sum(all.data.ranks$global.refined3.tailoring <= x))
barplot.data$global.refined3.tailoring.lcs <- sapply(barplot.data$Cutoff, function(x) sum(all.data.ranks$global.refined3.tailoring.lcs <= x))

barplot.data$Cutoff <- as.factor(barplot.data$Cutoff)
barplot.data.m <- melt(barplot.data)
my.barplot <- ggplot(barplot.data.m, aes(Cutoff, value)) +
	geom_bar(aes(fill=variable),position="dodge", stat='identity') +
	coord_cartesian(ylim = c(0, 240)) +
	scale_y_continuous(breaks=seq(0,240,60)) +
	xlab("Rank cutoff (out of >40000 database compounds)") +
	ylab("Number of correct cluster / compound matches") +
	scale_fill_discrete(name="Algorithm Configuration") +
	ggtitle("GARLIC Performance: Matching Known Clusters to Compounds in a Large Chemical Database")


ggsave(my.barplot, filename="Barplot_GARLIC_performance_by_algorithm.png", width=12, height=8, units='in')

cutoff <- 5
all.data.hits <- all.data.ranks[,colnames(all.data.ranks) %in% c("Name", "base.local", "base.global", "global.refined3", "global.refined3.tailoring", "global.refined3.tailoring.lcs")]
# Set everything below the cutoff to "infinity"
all.data.hits[,2:ncol(all.data.hits)][all.data.hits[,2:ncol(all.data.hits)] > cutoff] <- infinity
# Set to boolean
all.data.hits[,2:ncol(all.data.hits)] <- as.integer(all.data.hits[,2:ncol(all.data.hits)] <= cutoff)
# Only consider clusters that were not all hits
all.data.hits <- all.data.hits[rowSums(as.matrix(all.data.hits[,2:ncol(all.data.hits)])) < (ncol(all.data.hits)-1),]
# Only consider clusters that were not all misses
all.data.hits <- all.data.hits[rowSums(as.matrix(all.data.hits[,2:ncol(all.data.hits)])) > 0,]


data.to.plot <- data.frame(
	Name=all.data.hits$Name, 
#	base.local.score=all.data.hits$base.local, 
	base.local.hit=all.data.hits$base.local,
#	base.global.score=all.data.hits$base.global, 
	base.global.hit=all.data.hits$base.global,
#	global.refined3.score=all.data.hits$global.refined3, 
	global.refined3.hit=all.data.hits$global.refined3,
#	global.refined3.tailoring.score=all.data.hits$global.refined3.tailoring, 
	global.refined3.tailoring.hit=all.data.hits$global.refined3.tailoring,
#	global.refined3.tailoring.lcs.score=all.data.hits$global.refined3.tailoring.lcs, 
	global.refined3.tailoring.lcs.hit=all.data.hits$global.refined3.tailoring.lcs
	)

data.to.plot <- data.to.plot[rev(order(rowSums(data.to.plot[,2:ncol(data.to.plot)]))),]
#data.to.plot <- data.to.plot[rev(order(all.data.hits$global.refined3.tailoring)),]
data.to.plot.m <- melt(data.to.plot)
data.to.plot.m <- transform(data.to.plot.m, Name = factor(Name, levels = as.character(data.to.plot$Name)))
data.to.plot.m <- transform(data.to.plot.m, variable = factor(variable, levels = as.character(colnames(data.to.plot))))



data.to.plot.plot <- ggplot(data.to.plot.m, aes(variable, Name)) + 
	geom_tile(aes(fill = value), colour = "white") + 
	scale_fill_gradient(low = "white", high = "steelblue") +
	theme(axis.text.x = element_text(angle = 90, hjust = 1)) +
	theme(legend.position = "none") +
	xlab("Algorithm Configuration") +
	ylab("Gene Cluster / Compound Name") +
	ggtitle("Top 5 Hits:\n Cases That Differ\n By Algorithm") +
	theme(panel.grid.major = element_blank(),panel.grid.minor = element_blank(), panel.background = element_blank())


ggsave(data.to.plot.plot, filename="Heatmap_Top5_cases_that_differ.png", width=3, height=7.5, units='in')
#	theme(axis.text.y = element_text(size=5))
# 	theme(axis.text.y=element_blank())