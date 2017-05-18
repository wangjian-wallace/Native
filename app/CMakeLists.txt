# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

cmake_minimum_required(VERSION 3.4.1)


add_library(
             native-lib
             SHARED
             src/main/cpp/native-lib.cpp )

find_library(
              log-lib
              log )


set(distribution_DIR ${CMAKE_SOURCE_DIR}/../../../../libs)

