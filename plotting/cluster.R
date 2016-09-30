library(ape)

adjacency <- read.table("amino_acid_pairwise_similarity.txt", row.names=NULL, sep="\t")

# Resolve repeated row/column names
rownames(adjacency) <- make.names(adjacency[,1], unique=TRUE)
adjacency <- adjacency[,2:ncol(adjacency)]

adjacency.matrix <- as.matrix(adjacency)
adjacency.matrix <- -adjacency.matrix
#colnames(adjacency.matrix) <- strtrim(colnames(adjacency.matrix), 20)
#rownames(adjacency.matrix) <- strtrim(rownames(adjacency.matrix), 20)

distances <- as.dist(adjacency.matrix)

my.hclust <- hclust(distances)

#my.hclust.dendrogram <- as.dendrogram(my.hclust)
#
#cut.out <- cut(my.hclust.dendrogram, h=2.5)
#
##par(mfrow=c(5,1))
##pdf("mar5_main.pdf", width=40, height=15)
##plot(hcd, main="Main")
#dev.off()
#pdf("mar5_upper.pdf", width=40, height=15)
#plot(cut.out$upper, 
#     main="Upper tree of cut")
#dev.off()
#pdf("mar5_clust1.pdf", width=40, height=15)
#plot(cut.out$lower[[1]], 
#     main="First branch of lower tree with cut at h=75")
#dev.off()
#pdf("mar5_clust2.pdf", width=40, height=15)
#plot(cut.out$lower[[2]], 
#     main="Second branch of lower tree with cut at h=75")
#dev.off()
#pdf("mar5_clust3.pdf", width=40, height=15)
#plot(cut.out$lower[[3]], 
#     main="Third branch of lower tree with cut at h=75")
#dev.off()
#pdf("mar5_clust4.pdf", width=40, height=15)
#plot(cut.out$lower[[4]], 
#     main="Fourth branch of lower tree with cut at h=75")
#dev.off()
#
#enter image description here

my.tree <- as.phylo(my.hclust) 
write.tree(phy=my.tree, file="aminoacids.newick")

#png("mar5_hierarchy.png", width=8000, height=8000)
#plot(my.hclust)
#dev.off()
