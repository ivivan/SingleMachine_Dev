####### Compiler, tools and options
#you need change CXX and LINK to according to your environment

CXX      = g++
CXXFLAGS = -c -pipe -Wall -DUNIX_PLATFORM  -D_GCC_VER
INCPATH  = -I. -I../johnlib/include -I../ -I../pccts/antlr -I../pccts/h 
LINK     = g++
LINKFLAGS= 
AR       = ar cqs
COPY     = cp -f
COPY_FILE= $(COPY)
COPY_DIR = $(COPY) -r
DEL_FILE = rm -f
SYMLINK  = ln -sf
DEL_DIR  = rmdir
MOVE     = mv -f
CHK_DIR_EXISTS= test -d
MKDIR    = mkdir -p
AUTO_MAKE_CORE = ./AutoMakeCore

####### Output directory

OBJECTS_DIR = release

### build type
BUILD = $(MAKECMDGOALS)
ifeq ($(BUILD),)
BUILD  = release
endif
ifeq ($(BUILD),debug)
CXXFLAGS += -pg -g
LINKFLAGS+= -pg -g
OBJECTS_DIR = debug
endif

ifeq ($(BUILD),release)
CXXFLAGS += -O3
OBJECTS_DIR = release
endif

####### Files

SOURCES = $(shell echo *.cpp)
OBJECTS = $(SOURCES:%.cpp=$(OBJECTS_DIR)/%.o)
DEPENDS = $(SOURCES:%.cpp=$(OBJECTS_DIR)/%.d)
TARGET  = $(OBJECTS_DIR)/libcpp.a

####### Build rules
all: obj_dir  Makefile $(TARGET)
	$(LINK) $(LINKFLAGS) -o cvtree $(OBJECTS_DIR)/seq.o $(OBJECTS_DIR)/cvtree.o $(OBJECTS_DIR)/count.o
	$(LINK) $(LINKFLAGS) -o dist $(OBJECTS_DIR)/dist.o $(OBJECTS_DIR)/count.o $(OBJECTS_DIR)/seq.o
	chmod 755 *.pl


obj_dir:
	$(MKDIR) $(OBJECTS_DIR)

$(TARGET):  Makefile $(AUTO_MAKE_CORE) $(DEPENDS) $(OBJECTS)
	-$(DEL_FILE) $(TARGET)
	$(AR) $(TARGET) $(OBJECTS)

clean:
	-$(DEL_FILE) debug/*
	-$(DEL_FILE) release/*
	-$(DEL_FILE) *~ core *.core

 

####### Compile

include $(AUTO_MAKE_CORE)

#######
.PHONY: debug release clean
debug:all
release:all

