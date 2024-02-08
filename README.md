# NoobSQL
NoobSQL is a simple Android application to manage .db files.

## Dependencies
This application is written in Kotlin and C++ using the C++17 standards.

Those libs must be installed in the "~NoobSQL/app/src/main/cpp" folder.
* sqlite3pp
* SQLiteCpp

Check [CMakeLists.txt](https://github.com/DangeL187/NoobSQL/blob/main/app/src/main/cpp/CMakeLists.txt) for more information.

## How It Works
The application uses [native-lib.cpp](https://github.com/DangeL187/NoobSQL/blob/main/app/src/main/cpp/native-lib.cpp) file in order to connect the Kotlin code with my [C++ library](https://github.com/DangeL187/NoobSQL/blob/main/app/src/main/cpp/include/Database/Database.hpp).

My C++ library is a wrapper that uses sqlite3pp and SQLiteCpp in order to manage a .db file.

## [Get The APK File](https://github.com/DangeL187/NoobSQL/tree/main/APK_FILE)

## How To Use

### First, you need to grant the application permissions to access the storage:
<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/start.png" width="30%">	<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/start.png" width="30%">	<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/start.png" width="30%">
