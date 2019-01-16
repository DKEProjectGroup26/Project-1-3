cd %~dp0

cd phase3
javac *.java everything\*.java algorithms\*.java

cd ..
jar cfm Group26.jar phase3\MANIFEST.txt phase3\*.class phase3\everything\*.class phase3\algorithms\*.class

cd phase3
del *.class everything\*.class algorithms\*.class

cd ..
java -jar Group26.jar