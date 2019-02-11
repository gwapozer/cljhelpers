.PHONY: run clean print

JNI_DIR=../../target/jni
CLASS_DIR=../../target/classes

CLASS_NAME=IpgeneratorJNI
CLASS_FILE=$(CLASS_DIR)/$(CLASS_NAME).class
JAVA_FILE=../../src_java/IpgeneratorJNI.java
JAR_FILE=../../target/out-standalone.jar

C_FILE=IpgeneratorJNI.c Ip_generator.cpp
C_HEADER=$(JNI_DIR)/IpgeneratorJNI.h
LIB_FILE=$(JNI_DIR)/lib$(CLASS_NAME).so

#INCLUDE_DIRS=$(shell find $(JAVA_HOME)/include -type d)
INCLUDE_DIRS=/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers
INCLUDE_ARGS=$(INCLUDE_DIRS:%=-I%) -I$(JNI_DIR)

run: $(LIB_FILE) $(JAR_FILE)
	#java -jar $(JAR_FILE)

jar: $(JAR_FILE)

$(JAR_FILE): $(CLASS_FILE) $(C_HEADER)
	#lein uberjar

$(CLASS_FILE): $(JAVA_FILE)
	lein javac

header: $(C_HEADER)

$(C_HEADER): $(CLASS_FILE)
	mkdir -p $(JNI_DIR)
	javah -o $(C_HEADER) -cp $(CLASS_DIR) $(CLASS_NAME)
	@touch $(C_HEADER)

lib: $(LIB_FILE)

$(LIB_FILE): $(C_FILE) $(C_HEADER)
	#$(CC) $(INCLUDE_ARGS) -shared $(C_FILE) -o $(LIB_FILE) -fPIC
	g++ -lstdc++ $(INCLUDE_ARGS) -shared $(C_FILE) -o $(LIB_FILE) -fPIC

clean:
	lein clean
	rm -rf $(JNI_DIR)

print:
    $(info $(JNI_DIR))
    #$(info $(CLASS_DIR))
    #$(info $(C_HEADER))
