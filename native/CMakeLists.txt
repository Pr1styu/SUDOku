cmake_minimum_required(VERSION 3.14)  # CMake version check
project(CAFFParser)               # Create project
set(CMAKE_CXX_STANDARD 14)            # Enable c++14 standard

set(SOURCE_FILES main.cpp)

# Add executable target with source files listed in SOURCE_FILES variable
add_executable(CAFFParser ${SOURCE_FILES} jpeg-9d/jpeglib.h jpeg-9d/jdatadst.c jpeg-9d/jcapistd.c jpeg-9d/jcparam.c jpeg-9d/jcapimin.c jpeg-9d/jcinit.c jpeg-9d/jcomapi.c
        jpeg-9d/jcmarker.c jpeg-9d/jmemmgr.c jpeg-9d/jpegint.h jpeg-9d/jmemnobs.c jpeg-9d/jcmaster.c jpeg-9d/jutils.c jpeg-9d/jcmainct.c jpeg-9d/jcprepct.c jpeg-9d/jccoefct.c
        jpeg-9d/jccolor.c jpeg-9d/jcsample.c jpeg-9d/jcdctmgr.c jpeg-9d/jfdctint.c jpeg-9d/jfdctfst.c jpeg-9d/jfdctflt.c jpeg-9d/jchuff.c jpeg-9d/jcarith.c jpeg-9d/jerror.c
        jpeg-9d/jerror.h jpeg-9d/jaricom.c)