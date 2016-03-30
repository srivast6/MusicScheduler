#Compile the program
make clean
make

#Execute the program
#java -cp .:./lib/jl1.0.1.jar:./classes MusicHome

#Create a jar of the file
jar -cfve MusicScheduler.jar MusicHome -C classes . -C lib .

#run the jar
java -jar MusicScheduler.jar
