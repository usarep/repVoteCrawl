
pre=http://clerk.house.gov/evs

jarFile=target/repVoteCrawl-0.0.1-SNAPSHOT-jar-with-dependencies.jar


# START=0
# END=4



# see http://stackoverflow.com/questions/8789729/zero-padding-in-bash
# also http://stackoverflow.com/questions/169511/how-do-i-iterate-over-a-range-of-numbers-defined-by-variables-in-bash

# 2014
# for i in 000 100 200 300 ; do

# 2013
# for i in 000 100 200 300 400 500 600 ; do

# 2012
year=2012
for i in 000 100 200 300 400 500 600 ; do
 echo starting ${pre}/${year}/ROLL_${i}.asp 
 # http://clerk.house.gov/evs/2014/ROLL_000.asp
 java -jar ${jarFile} -y ${year}  -u ${pre}/${year}/ROLL_${i}.asp 

 echo finished ${pre}/${year}/ROLL_${i}.asp 

done

# 2011
year=2011
for i in 000 100 200 300 400 500 600 700 800 900 ; do
 echo starting ${pre}/${year}/ROLL_${i}.asp 
 java -jar ${jarFile} -y ${year}  -u ${pre}/${year}/ROLL_${i}.asp 
 echo finished ${pre}/${year}/ROLL_${i}.asp 
done

# 2010
year=2010
for i in 000 100 200 300 400 500 600 ; do
 echo starting ${pre}/${year}/ROLL_${i}.asp 
 java -jar ${jarFile} -y ${year}  -u ${pre}/${year}/ROLL_${i}.asp 
 echo finished ${pre}/${year}/ROLL_${i}.asp 
done

# 2009
year=2009
for i in 000 100 200 300 400 500 600 700 800 900 ; do
 echo starting ${pre}/${year}/ROLL_${i}.asp 
 java -jar ${jarFile} -y ${year}  -u ${pre}/${year}/ROLL_${i}.asp 
 echo finished ${pre}/${year}/ROLL_${i}.asp 
done
