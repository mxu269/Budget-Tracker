run: run-server #only runs the server

#before run-all please find and free port 8000 & 3000
#sudo lsof -i :8000
#sudo kill -9 PID
run-all: run-server-daemon run-client #only works in unix & linux GUI (eg. macOS ubuntu)

run-server: compile
	java -cp bin:java/lib/* Main

run-server-daemon: compile #only works in unix & linux GUI (eg. macOS ubuntu)
	java -cp bin:java/lib/* Main &

run-client:
	npm install --prefix reactjs/budget-tracker
	npm start --prefix reactjs/budget-tracker

compile: fe


test: te
	java -jar junit5.jar -cp bin:java/lib/* --scan-classpath --details tree

clean:
	$(RM) bin/*.class


frontend = mxu269_Front_End_2
datawrangler = sstay2_Data_Wrangler_1
backend = zyang484_Back_End_1
tester = tpang4_Test_Engineer_1

dw: #datawrangler target
	javac -cp bin -d bin ./java/$(datawrangler)/*.java ./java/interface/DataWranglerInterface.java

be: dw #backend target
	javac -cp bin -d bin ./java/$(backend)/*.java ./java/interface/BackEndInterface.java ./java/interface/FilterInterface.java

fe: be
	javac -cp bin:java/lib/* -d bin ./java/src/*.java

te: fe
	javac -cp bin:junit5.jar -d bin ./java/$(tester)/*.java

copy: rm_others
	mkdir -p java/$(datawrangler)
	mkdir -p java/$(backend)
	mkdir -p java/$(tester)
	cp ../$(datawrangler)/*.java ./java/$(datawrangler)/
	cp ../$(backend)/*.java ./java/$(backend)/
	#cp ../$(frontend)/*.java ./$(frontend)/
	cp ../$(tester)/*.java ./java/$(tester)/

	cp -r ../interface ./java/
	cp -r ../res ./
	#cp -r ../lib ./java/

rm_others:
	rm -f -r java/$(datawrangler)
	rm -f -r java/$(backend)
	rm -f -r java/$(tester)

	rm -f -r java/interface
	rm -f -r res
	#rm -f -r java/lib