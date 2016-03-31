#Compiler flags
RM = rm -rf
JFLAGS = -g -cp .:./lib/jl1.0.1.jar -d ./classes -sourcepath ./src
JC = javac

#clear default targets
.SUFFIXES = .java .class

#target entry to create class files
%.class: %.java
	$(JC) $(JFLAGS) $^

#classes macro that lists all java files required for compilation
CLASSES = \
	src/MusicHome.java \
	src/Alarm.java \
	src/Audio.java \
	src/BooleanEventListener.java \
	src/MusicPlayer.java \
	src/Playlist.java \
	src/saveData.java \
	src/ScheduledAlarms.java \
	src/ScheduledPlay.java

#default make target
default: classes

#target to make all java files into class files
classes: $(CLASSES:.java=.class)

#target for make clean
clean:
	$(RM) ./classes/*.class MusicScheduler.jar
