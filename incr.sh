
pre=http://clerk.house.gov/evs

jarFile=target/repVoteCrawl-0.0.1-SNAPSHOT-jar-with-dependencies.jar


# see http://stackoverflow.com/questions/8789729/zero-padding-in-bash
# also http://stackoverflow.com/questions/169511/how-do-i-iterate-over-a-range-of-numbers-defined-by-variables-in-bash


# 2014
year=2014
start_roll_call=481
i=400

echo starting ${pre}/${year}/ROLL_${i}.asp 
# http://clerk.house.gov/evs/2014/ROLL_000.asp
java -jar ${jarFile} -y ${year}  -u ${pre}/${year}/ROLL_${i}.asp  -s ${start_roll_call}

echo finished ${pre}/${year}/ROLL_${i}.asp 


i=500
echo starting ${pre}/${year}/ROLL_${i}.asp 
# http://clerk.house.gov/evs/2014/ROLL_000.asp
java -jar ${jarFile} -y ${year}  -u ${pre}/${year}/ROLL_${i}.asp  

echo finished ${pre}/${year}/ROLL_${i}.asp 



