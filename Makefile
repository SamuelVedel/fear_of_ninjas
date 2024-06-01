SRC_DIR := src
OUT_DIR := bin

VC_PATH ?= /home/saam/javalib/vcomponent.jar

SRCS := $(wildcard $(SRC_DIR)/*/*/*/*.java) $(wildcard $(SRC_DIR)/*/*/*/*/*.java) $(wildcard $(SRC_DIR)/*/*/*/*/*/*.java)
#CLS := $(SRCS:$(SRC_DIR)/%.java=$(OUT_DIR)/%.class)

JC := javac
JCFLAGS := -encoding iso-8859-1 -d $(OUT_DIR)/ -cp $(SRC_DIR):$(VC_PATH)

.SUFFIXES: .java .class

.PHONY: all clean build run jar

all: build run

build: .done

jar: Fear_of_ninja.jar

run:
	java -cp $(VC_PATH):$(OUT_DIR) fr.svedel.fod.MainFOD

.done: $(SRCS)
	$(JC) $(JCFLAGS) $?
	touch .done

Fear_of_ninja.jar: .done
	jar cfe Fear_of_ninja.jar fr.svedel.fod.MainFOD -C $(OUT_DIR) .

clean:
	rm -rf $(OUT_DIR)
	rm -f .done
	rm -f *~ $(SRC_DIR)/*/*/*/*~ $(SRC_DIR)/*/*/*/*/*~ $(SRC_DIR)/*/*/*/*/*/*~
	rm -f Fear_of_ninja.jar
