
pre=http://clerk.house.gov/evs

jarFile=target/repVoteCrawl-0.0.1-SNAPSHOT-jar-with-dependencies.jar

year=2014

# START=0
# END=4



# see http://stackoverflow.com/questions/8789729/zero-padding-in-bash
# also http://stackoverflow.com/questions/169511/how-do-i-iterate-over-a-range-of-numbers-defined-by-variables-in-bash


# for i in 000 100 200 300 ; do
for i in 000 ; do
 echo starting ${pre}/${year}/ROLL_${i}.asp 
 # http://clerk.house.gov/evs/2014/ROLL_000.asp
 java -jar ${jarFile} -y ${year}  -u ${pre}/${year}/ROLL_${i}.asp 

 echo finished ${pre}/${year}/ROLL_${i}.asp 

done
