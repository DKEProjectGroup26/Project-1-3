cd phase3
javac *.java

cd ..
jar cfm Group26.jar phase3\MANIFEST.txt phase3\*.class

cd phase3
del *.class

cd ..
java -jar Group26.jar