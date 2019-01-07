dir /s /B *.java > sources.txt
javac @sources.txt -d compiled
del "sources.txt"

dir /s /B *.class > classes.txt
cd ..
jar cfm Group26.jar phase3\MANIFEST.txt @classes.txt

cd phase3
del @classes.txt
del "classes.txt"

cd ..
java -jar Group26.jar