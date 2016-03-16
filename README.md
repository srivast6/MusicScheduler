# About
MusicSchedule was developed by the members of Team 15 for the CS 408 class of Spring 2016. This project was developed in a Linux/OSX based environment. Currently, this project can be build and run in this type of environmet.

# Source Files
All of the source files are in the following folders

- ./src:  	This folder contains the .java files for the project
- ./images: This folder contains all images needed for the project
- ./lib:	This folder contains all library files needed for the project

# Compiling
1. Run ````$ make```` in the same directory as ````makefile````.

# Running
A script, ````run.sh````, was created to compile and run the MusicScheduler project. First, ensure that the script is capabile of executing on your system. Then execute the script.  

1. Execute ````$ chmod +x run.sh````
2. Execute ````$ ./run.sh````

# Notes
- Before committing to the repo remove all class files from the classes directory.
-- Execute ````$ make clean````

- If a new .java file is added to the sources folder, make sure to add it to the CLASSES macro in the makefile.
