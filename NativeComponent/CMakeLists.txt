cmake_minimum_required(VERSION 3.20)  # CMake version check
project(CAFFParser)               # Create project "simple_example"
set(CMAKE_CXX_STANDARD 14)            # Enable c++14 standard

set(SOURCE_FILES main.cpp)

# Add executable target with source files listed in SOURCE_FILES variable
add_executable(CAFFParser ${SOURCE_FILES} jpeg-9d/jpeglib.h jpeg-9d/jdatadst.c jpeg-9d/jcapistd.c jpeg-9d/jcparam.c jpeg-9d/jcapimin.c jpeg-9d/jcinit.c jpeg-9d/jcomapi.c
        jpeg-9d/jcmarker.c jpeg-9d/jmemmgr.c jpeg-9d/jpegint.h jpeg-9d/jmemnobs.c jpeg-9d/jcmaster.c jpeg-9d/jutils.c jpeg-9d/jcmainct.c jpeg-9d/jcprepct.c jpeg-9d/jccoefct.c
        jpeg-9d/jccolor.c jpeg-9d/jcsample.c jpeg-9d/jcdctmgr.c jpeg-9d/jfdctint.c jpeg-9d/jfdctfst.c jpeg-9d/jfdctflt.c jpeg-9d/jchuff.c jpeg-9d/jcarith.c jpeg-9d/jerror.c
        jpeg-9d/jerror.h jpeg-9d/jaricom.c)

#add_executable(CAFFParser ${SOURCE_FILES} jpeg-9d/jaricom.c jpeg-9d/jcapimin.c jpeg-9d/jcapistd.c jpeg-9d/jcarith.c jpeg-9d/jccoefct.c jpeg-9d/jccolor.c jpeg-9d/jcdctmgr.c jpeg-9d/jchuff.c
#        jpeg-9d/jcinit.c jpeg-9d/jcmainct.c jpeg-9d/jcmarker.c jpeg-9d/jcmaster.c jpeg-9d/jcomapi.c jpeg-9d/jconfig.h jpeg-9d/jcparam.c jpeg-9d/jcprepct.c jpeg-9d/jcsample.c
#        jpeg-9d/jctrans.c jpeg-9d/jdapimin.c jpeg-9d/jdapistd.c jpeg-9d/jdarith.c jpeg-9d/jdatadst.c jpeg-9d/jdatasrc.c jpeg-9d/jdcoefct.c jpeg-9d/jdcolor.c jpeg-9d/jdct.h
#        jpeg-9d/jddctmgr.c jpeg-9d/jdhuff.c jpeg-9d/jdinput.c jpeg-9d/jdmainct.c jpeg-9d/jdmarker.c jpeg-9d/jdmaster.c jpeg-9d/jdmerge.c jpeg-9d/jdpostct.c jpeg-9d/jdsample.c
#        jpeg-9d/jdtrans.c jpeg-9d/jerror.c jpeg-9d/jerror.h jpeg-9d/jfdctflt.c jpeg-9d/jfdctfst.c jpeg-9d/jfdctint.c jpeg-9d/jidctflt.c jpeg-9d/jidctfst.c jpeg-9d/jidctint.c
#        jpeg-9d/jinclude.h jpeg-9d/jmemmgr.c jpeg-9d/jmemnobs.c jpeg-9d/jmemsys.h jpeg-9d/jmorecfg.h jpeg-9d/jpegint.h jpeg-9d/jpeglib.h jpeg-9d/jquant1.c jpeg-9d/jquant2.c
#        jpeg-9d/jutils.c jpeg-9d/jversion.h)