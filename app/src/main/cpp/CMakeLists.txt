# !WARNING!
# YOU MUST PUT sqlite3pp AND SQLiteCpp FOLDERS INSIDE "~NoobSQL/app/src/main/cpp" FOLDER!
# IF YOU HAVE THESE INSTALLED IN A DIFFERENT LOCATION, PLEASE CHANGE THE CMakeLists.txt

cmake_minimum_required(VERSION 3.22.1)
project(NoobSQL)

set(CMAKE_CXX_FLAGS "-std=c++17 -static-libstdc++ -static-libgcc -fpermissive")

add_subdirectory(SQLiteCpp)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/sqlite3pp/headeronly_src ${CMAKE_CURRENT_SOURCE_DIR}/include)

set(SOURCE_FILES "src/DatabaseTable.cpp" "src/DatabaseRows.cpp" "src/DatabaseColumns.cpp" "native-lib.cpp")
file(GLOB HEADER_FILES "include/*.hpp" "include/**/*.hpp")

add_library(${CMAKE_PROJECT_NAME} SHARED ${SOURCE_FILES})

target_include_directories(${CMAKE_PROJECT_NAME} PRIVATE ${HEADER_FILES})

target_link_libraries(${CMAKE_PROJECT_NAME} SQLiteCpp android log)