# NoobSQL
## Android version must be lower than 13!
NoobSQL is a simple Android application to manage .db files.
Please, read the "How To Use" section.

## Dependencies
This application is written in Kotlin and C++ using the C++17 standards.

Those libs must be installed in the "~NoobSQL/app/src/main/cpp" folder.
* [sqlite3pp](https://github.com/iwongu/sqlite3pp/tree/master)
* [SQLiteCpp](https://github.com/SRombauts/SQLiteCpp)

Check [CMakeLists.txt](https://github.com/DangeL187/NoobSQL/blob/main/app/src/main/cpp/CMakeLists.txt) for more information.

## How It Works
The application uses [native-lib.cpp](https://github.com/DangeL187/NoobSQL/blob/main/app/src/main/cpp/native-lib.cpp) file in order to connect the Kotlin code with my [C++ library](https://github.com/DangeL187/NoobSQL/blob/main/app/src/main/cpp/include/Database/Database.hpp).

My C++ library is a wrapper that uses sqlite3pp and SQLiteCpp in order to manage a .db file.

## [Get The APK File](https://github.com/DangeL187/NoobSQL/tree/main/APK_FILE)

## How To Use

### First, you need to grant the application permissions to access the storage:
<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/allow1.png" width="30%">	<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/allow2.png" width="30%">	<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/allow3.png" width="30%">

### To create a new table press a big "+" button:
<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/start.png" width="30%">	<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/created.png" width="30%">

### INSERT ROW
#### Use the first edit field to specify the column names
#### Use the second edit field to specify the values you want to add
#### SQLite command: "INSERT INTO table (column1,column2 ,..) VALUES( value1,	value2 ,...);"
<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/insert1.png" width="30%">	<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/insert2.png" width="30%">

#### It is not necessary to specify the first column's name:
<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/insert3.png" width="30%">	<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/insert4.png" width="30%">

### ADD COLUMN
#### Use the edit field to specify a new column's name
#### SQLite command: "ALTER TABLE table_name ADD COLUMN column_name;"
<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/add1.png" width="30%">	<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/add2.png" width="30%">

### UPDATE
#### Modifies values that satisfy the condition
#### Use the first edit field to specify the column names
#### Use the second edit field to specify the values you want to update(edit)
#### Use the third edit field to specify the column's name
#### Use the fourth edit field to specify the value you are looking for
#### SQLite command: "UPDATE table SET column_1 = new_value_1, column_2 = new_value_2 WHERE column_name='value';"
<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/update1.png" width="30%">	<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/update2.png" width="30%">

### SELECT
#### Selects values that satisfy the condition
#### Use the first edit field to specify the column's name
#### Use the second edit field to specify the value
#### SQLite command: "SELECT * FROM table WHERE column='value';"
<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/select1.png" width="30%">	<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/select2.png" width="30%">

#### If you do not fill in the edit fields, all rows will be selected
#### SQLite command: "SELECT * FROM table;"
<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/select3.png" width="30%">	<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/select4.png" width="30%">

### RENAME COLUMN
#### Use the first edit field to specify the old column's name
#### Use the second edit field to specify a new column's name
#### SQLite command: "ALTER TABLE table_name RENAME COLUMN current_name TO new_name;"
<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/rename1.png" width="30%">	<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/rename2.png" width="30%">

### DELETE ROW
#### Deletes row that satisfies the condition
#### Use the first edit field to specify the column's name
#### Use the second edit field to specify the value
#### SQLite command: "DELETE FROM table WHERE column='value';"
<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/deleteRow1.png" width="30%">	<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/deleteRow2.png" width="30%">

### DELETE COLUMN
#### Use the edit field to specify the column's name to delete
#### SQLite command: "ALTER TABLE table DROP COLUMN column_name;"
<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/deleteCol1.png" width="30%">	<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/deleteCol2.png" width="30%">

### TABLES
#### Use arrows to change current table
#### Click small pink button in the upper right corner to delete current table
<img src="https://github.com/DangeL187/NoobSQL/blob/main/img/drop2.png" width="30%">
