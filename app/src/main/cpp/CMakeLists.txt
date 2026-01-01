# 目录标记：app/src/main/cpp/CMakeLists.txt
cmake_minimum_required(VERSION 3.22.1)
project("stealth_root")

add_library(stealth_bridge SHARED stealth_bridge.c)

find_library(log-lib log)
target_link_libraries(stealth_bridge ${log-lib})