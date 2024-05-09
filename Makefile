SRC_DIR := src
OUT_DIR := bin

SRCS := $(wildcard $(SRC_DIR)/*/*/*/*.java) $(wildcard $(SRC_DIR)/*/*/*/*/*.java) $(wildcard $(SRC_DIR)/*/*/*/*/*/*.java)
#CLS := $(SRCS:$(SRC_DIR)/%.java=$(OUT_DIR)/%.class)

JC := javac
JCFLAGS := -encoding iso-8859-1 -d $(OUT_DIR)/ -cp $(SRC_DIR)/

.SUFFIXES: .java .class

.PHONY: all clean build run

all: build run

build: .done

run:
	java -cp $(OUT_DIR) fr.svedel.fod.MainFOD

.done: $(SRCS)
	$(JC) $(JCFLAGS) $?
	touch .done

clean:
	rm -rf $(OUT_DIR)
	rm -f .done
	rm -f *~ $(SRC_DIR)/*/*/*/*~ $(SRC_DIR)/*/*/*/*/*~ $(SRC_DIR)/*/*/*/*/*/*~
