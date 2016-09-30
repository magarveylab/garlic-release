library(reshape2)
library(ggplot2)

heatmap.hits <- TRUE

## Load the files
#base.local <- read.table("may7_garlic_comparison/baseLocal/ranks.txt")
base.global <- read.table("may7_garlic_comparison/baseGlobal/ranks.txt")
global.refined <- read.table("may7_garlic_comparison/globalRefinedScores/ranks.txt")
global.refined.tailoring <- read.table("may7_garlic_comparison/globalRefinedScoresTailoring/ranks.txt")
global.refined.tailoring.lcs <- read.table("may7_garlic_comparison/globalRefinedScoresTailoringLCS/ranks.txt")
local.refined <- read.table("may7_garlic_comparison/localRefinedScores/ranks.txt")
local.refined.tailoring <- read.table("may7_garlic_comparison/localRefinedScoresTailoring/ranks.txt")
local.refined.tailoring.lcs <- read.table("may7_garlic_comparison/localRefinedScoresTailoringLCS/ranks.txt")
global.refined2 <- read.table("may7_garlic_comparison/globalRefined2Scores/ranks.txt")
global.refined2.tailoring <- read.table("may7_garlic_comparison/globalRefined2ScoresTailoring/ranks.txt")
global.refined2.tailoring.lcs <- read.table("may7_garlic_comparison/globalRefined2ScoresTailoringLCS/ranks.txt")
global.refined3 <- read.table("may7_garlic_comparison/globalRefined3Scores/ranks.txt")
global.refined3.tailoring <- read.table("may7_garlic_comparison/globalRefined3ScoresTailoring/ranks.txt")
global.refined3.tailoring.lcs <- read.table("may7_garlic_comparison/globalRefined3ScoresTailoringLCS/ranks.txt")

if(heatmap.hits) {
	#base.local <- base.local[,c(1,2)]
	base.global <- base.global[,c(1,2)]
	global.refined <- global.refined[,c(1,2)]
	global.refined.tailoring <- global.refined.tailoring[,c(1,2)]
	global.refined.tailoring.lcs <- global.refined.tailoring.lcs[,c(1,2)]
	local.refined <- local.refined[,c(1,2)]
	local.refined.tailoring <- local.refined.tailoring[,c(1,2)]
	local.refined.tailoring.lcs <- local.refined.tailoring.lcs[,c(1,2)]
	global.refined2 <- global.refined2[,c(1,2)]
	global.refined2.tailoring <- global.refined2.tailoring[,c(1,2)]
	global.refined2.tailoring.lcs <- global.refined2.tailoring.lcs[,c(1,2)]
	global.refined3 <- global.refined3[,c(1,2)]
	global.refined3.tailoring <- global.refined3.tailoring[,c(1,2)]
	global.refined3.tailoring.lcs <- global.refined3.tailoring.lcs[,c(1,2)]
} else {
	#base.local <- base.local[,c(1,3)]
	base.global <- base.global[,c(1,3)]
	global.refined <- global.refined[,c(1,3)]
	global.refined.tailoring <- global.refined.tailoring[,c(1,3)]
	global.refined.tailoring.lcs <- global.refined.tailoring.lcs[,c(1,3)]
	local.refined <- local.refined[,c(1,3)]
	local.refined.tailoring <- local.refined.tailoring[,c(1,3)]
	local.refined.tailoring.lcs <- local.refined.tailoring.lcs[,c(1,3)]
	global.refined2 <- global.refined2[,c(1,3)]
	global.refined2.tailoring <- global.refined2.tailoring[,c(1,3)]
	global.refined2.tailoring.lcs <- global.refined2.tailoring.lcs[,c(1,3)]
	global.refined3 <- global.refined3[,c(1,3)]
	global.refined3.tailoring <- global.refined3.tailoring[,c(1,3)]
	global.refined3.tailoring.lcs <- global.refined3.tailoring.lcs[,c(1,3)]
}


## Do some renaming
#colnames(base.local) <- c("Name", "base.local")
colnames(base.global) <- c("Name", "base.global")
colnames(global.refined) <- c("Name", "global.refined")
colnames(global.refined.tailoring) <- c("Name", "global.refined.tailoring")
colnames(global.refined.tailoring.lcs) <- c("Name", "global.refined.tailoring.lcs")
colnames(local.refined) <- c("Name", "local.refined")
colnames(local.refined.tailoring) <- c("Name", "local.refined.tailoring")
colnames(local.refined.tailoring.lcs) <- c("Name", "local.refined.tailoring.lcs")
colnames(global.refined2) <- c("Name", "global.refined2")
colnames(global.refined2.tailoring) <- c("Name", "global.refined2.tailoring")
colnames(global.refined2.tailoring.lcs) <- c("Name", "global.refined2.tailoring.lcs")
colnames(global.refined3) <- c("Name", "global.refined3")
colnames(global.refined3.tailoring) <- c("Name", "global.refined3.tailoring")
colnames(global.refined3.tailoring.lcs) <- c("Name", "global.refined3.tailoring.lcs")


## Merge into one data frame
all.data <- base.global
#all.data <- merge(base.local, base.global, by="Name")
all.data <- merge(all.data, global.refined, by="Name")
all.data <- merge(all.data, global.refined.tailoring, by="Name")
all.data <- merge(all.data, global.refined.tailoring.lcs, by="Name")
all.data <- merge(all.data, local.refined, by="Name")
all.data <- merge(all.data, local.refined.tailoring, by="Name")
all.data <- merge(all.data, local.refined.tailoring.lcs, by="Name")
all.data <- merge(all.data, global.refined2, by="Name")
all.data <- merge(all.data, global.refined2.tailoring, by="Name")
all.data <- merge(all.data, global.refined2.tailoring.lcs, by="Name")
all.data <- merge(all.data, global.refined3, by="Name")
all.data <- merge(all.data, global.refined3.tailoring, by="Name")
all.data <- merge(all.data, global.refined3.tailoring.lcs, by="Name")


all.data$Name = as.character(all.data$Name)

cutoff <- 5

# Exclude compounds
compound.annotations <- read.table("may1_annotated.txt", sep="\t", header=TRUE)
compounds.to.exclude <- as.character(compound.annotations[ compound.annotations$Include...Y.N. == "N",][,1])

all.data <- all.data[!(all.data$Name %in% compounds.to.exclude),]

#### Determine the subset / order of columns. Exactly one line should be uncommented.
# Local alignments
#all.data <- all.data[,c(1,2,7:9)]

# Global alignments
#all.data <- all.data[,c(1,3:6)]

# All alignments, reordered
all.data <- all.data[,c(1,6,7,8,2:5, 9:14)]
#all.data <- all.data[,c(1,2,7,8,9,3:6, 10:12)]
####


if(heatmap.hits) {
	all.data[,2:ncol(all.data)][all.data[,2:ncol(all.data)] > cutoff] <- -1
	#all.data <- all.data[rowSums(as.matrix(all.data[,2:ncol(all.data)])) > -(ncol(all.data)-1),]
	all.data[all.data==-1] <- 10000
	all.data[,2:ncol(all.data)] <- as.integer(all.data[,2:ncol(all.data)] <= cutoff)
	#all.data <- all.data[rowSums(as.matrix(all.data[,2:ncol(all.data)])) < (ncol(all.data)-1),]
} else {
	all.data[,2:ncol(all.data)] <- sapply(all.data[,2:ncol(all.data)], scale)
}

#### Ordering of rows
# Order by rowsums
all.data <- all.data[rev(order(rowSums(all.data[,2:ncol(all.data)]))),]
# Order by hierarchical clustering
#all.data <- all.data[hclust(dist(all.data[,2:ncol(all.data)]))$order, ]
# Order by file
ordered.names <- scan("may7_ordered_names.txt", what=character(0))
all.data <- all.data[match(ordered.names, all.data$Name),]
#### Ordering of columns

all.data.m <- melt(all.data)
colnames(all.data.m)[2] <- "alignment.configuration"
all.data.m <- transform(all.data.m, Name = factor(Name, levels = as.character(all.data$Name)))
all.data.m <- transform(all.data.m, alignment.configuration = factor(alignment.configuration, levels = as.character(colnames(all.data))))

(p <- ggplot(all.data.m, aes(alignment.configuration, Name)) + geom_tile(aes(fill = value), colour = "white") + scale_fill_gradient(low = "white", high = "steelblue"))
p <- p + theme(axis.text.x = element_text(angle = 90, hjust = 1))
# Adjust y axis label text size
p <- p + theme(axis.text.y = element_text(size=5))
q <- p+theme(axis.text.y=element_blank())
p

colSums(all.data[,2:ncol(all.data)])
