a<-read.table("R-avRelPer--slack--insert--sa--ils_off.dat")$V1
b<-read.table("R-avRelPer--slack--insert--first--ils_on.dat")$V1
results<-c(a,b)
results<-array ( results , dim=c (60, 2) )
test = wilcox.test (results[,1], results[,2], paired=T)$p.value
write(test, file = "p values wilco test", ncolumns = 1)