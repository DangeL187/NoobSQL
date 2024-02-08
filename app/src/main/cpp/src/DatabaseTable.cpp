#define SQLITE3PPEXT_H
#include "Database/Database.hpp"

using v_str   = Database::v_str;
using vv_str  = Database::vv_str;
using str_r   = Database::str_r;
using v_str_r = Database::v_str_r;

void Database::createTable(str_r table_name) {
    std::string line = "CREATE TABLE " + table_name + " (ID varchar(255), Name varchar(255))";
    sqlite3pp::command cmd(*db, line.c_str());
    cmd.execute();
}

void Database::deleteTable(str_r table_name) {
    std::string line = "DROP TABLE " + table_name;
    sqlite3pp::command cmd(*db, line.c_str());
    cmd.execute();
}
