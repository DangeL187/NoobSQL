#define SQLITE3PPEXT_H
#include "Database/Database.hpp"

using v_str   = Database::v_str;
using vv_str  = Database::vv_str;
using str_r   = Database::str_r;
using v_str_r = Database::v_str_r;

v_str Database::getColumns(str_r table_name) {
    std::string line = "SELECT * FROM " + table_name;
    sqlite3pp::query query(*db, line.c_str());
    v_str columns;
    for (int i = 0; i < query.column_count(); i++) {
        columns.emplace_back(query.column_name(i));
    }
    return columns;
}

void Database::addColumn(str_r table_name, str_r column_name) {
    std::string line = "ALTER TABLE " + table_name + " ADD " + column_name + " varchar(255)";
    sqlite3pp::command cmd(*db, line.c_str());
    cmd.execute();
}

void Database::deleteColumn(str_r table_name, str_r column_name) {
    std::string line = "ALTER TABLE " + table_name + " DROP COLUMN " + column_name;
    sqlite3pp::command cmd(*db, line.c_str());
    cmd.execute();
}

void Database::renameColumn(str_r table_name, str_r old_column_name, str_r new_column_name) {
    std::string line = "ALTER TABLE " + table_name + " RENAME " + old_column_name + " to " + new_column_name;
    sqlite3pp::command cmd(*db, line.c_str());
    cmd.execute();
}