library(reshape2)
library(ggplot2)
library(xlsx)

# A value greater than the highest rank
infinity = 100000

## Load the files
# TODO: condense this with loops or apply statements
global.refined4.ranks <- read.table("june4_garlic_comparison_knowns_vs_knowns/globalRefined4Scores/ranks.txt")
global.refined4.tailoring.ranks <- read.table("june4_garlic_comparison_knowns_vs_knowns/globalRefined4ScoresTailoring/ranks.txt")
global.refined4.exactsugars.ranks <- read.table("june4_garlic_comparison_knowns_vs_knowns/globalRefined4ScoresExactSugars/ranks.txt")
global.refined4.tailoring.lcs.ranks <- read.table("june4_garlic_comparison_knowns_vs_knowns/globalRefined4ScoresTailoringLCS/ranks.txt")

rank.list <- list(
	global.refined4.ranks=global.refined4.ranks,
	global.refined4.tailoring.ranks=global.refined4.tailoring.ranks,
	global.refined4.exactsugars.ranks=global.refined4.exactsugars.ranks,
	global.refined4.tailoring.lcs.ranks=global.refined4.tailoring.lcs.ranks)

rank.list <- lapply(rank.list, function(x) x[,c(1,2)])

## Do some renaming
rank.list.names <- names(rank.list)
rank.list <- lapply(names(rank.list), function(x) {
	colnames(rank.list[[x]]) <- c("Name", sub(".ranks", "", x))
	return(rank.list[[x]])
	} )
names(rank.list) <- rank.list.names

## Merge into one data frame
all.data.ranks <- Reduce(function(x,y) merge(x,y,by="Name"), rank.list)

# Exclude compounds
#compound.annotations <- read.xlsx("june4_annotations.xlsx", sheetIndex=1)
#compounds.to.exclude <- as.character(compound.annotations[ !is.na(compound.annotations$Include...Y.N.) & compound.annotations$Include...Y.N. == "N",][,1])
#all.data.ranks <- all.data.ranks[!(all.data.ranks$Name %in% compounds.to.exclude),]

# Determine the subset / order of columns. 
# All alignments, reordered
#all.data.scores <- all.data.scores[,c(1,2,7,8,9,3:6,10:15)]

# Set the rank of values not in the top 50 to "infinity" - this way they are always higher than the cutoff
all.data.ranks[all.data.ranks==-1] <- infinity

#all.data <- all.data[rev(order(all.data$global.refined3.tailoring.lcs)),]

barplot.data <- data.frame(Cutoff=seq(1,10))

barplot.data <- cbind(barplot.data, sapply(all.data.ranks[,-1], function(x) sapply(barplot.data$Cutoff, function(y) sum(x <= y))))

barplot.data$Cutoff <- as.factor(barplot.data$Cutoff)
barplot.data.m <- melt(barplot.data, )
my.barplot <- ggplot(barplot.data.m, aes(Cutoff, value)) +
	geom_bar(aes(fill=variable),position="dodge", stat='identity') +
	coord_cartesian(ylim = c(0, nrow(all.data.ranks))) +
	scale_y_continuous(breaks=seq(0,nrow(all.data.ranks),60)) +
	xlab("Rank cutoff (out of ~300 known compounds)") +
	ylab("Number of correct cluster / compound matches") +
	scale_fill_discrete(name="Algorithm Configuration") +
	ggtitle("Matching Known Clusters to Known Compounds")


ggsave(my.barplot, filename="Barplot_GARLIC_performance_by_algorithm_june4.png", width=12, height=8, units='in')

cutoff <- 1
all.data.hits <- all.data.ranks
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
	global.refined4.hit=all.data.hits$global.refined4,
	global.refined4.tailoring.hit=all.data.hits$global.refined4.tailoring,
	global.refined4.exactsugars.hit=all.data.hits$global.refined4.exactsugars,
	global.refined4.tailoring.lcs.hit=all.data.hits$global.refined4.tailoring.lcs
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
	ggtitle(paste("Top", cutoff, "Hit(s):\n Cases That Differ\n By Algorithm")) +
	theme(panel.grid.major = element_blank(),panel.grid.minor = element_blank(), panel.background = element_blank())


ggsave(data.to.plot.plot, filename=paste0("Heatmap_Top", cutoff, "_cases_that_differ_june4.png"), width=3.5, height=7.5, units='in')
#	theme(axis.text.y = element_text(size=5))
# 	theme(axis.text.y=element_blank())
