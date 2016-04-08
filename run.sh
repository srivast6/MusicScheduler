#Compile the program
make clean
make

#Create a jar of the file
jar -cfmv MusicScheduler.jar MANIFEST.MF -C classes . -C lib . -C images .

#Execute the program
#java -cp .:./lib/jl1.0.1.jar:./classes MusicHome

#run the jar
java -jar MusicScheduler.jar
