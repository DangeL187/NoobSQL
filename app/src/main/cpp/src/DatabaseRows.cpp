#define SQLITE3PPEXT_H
#include "Database/Database.hpp"

using v_str   = Database::v_str;
using vv_str  = Database::vv_str;
using str_r   = Database::str_r;
using v_str_r = Database::v_str_r;

int getQuerySize(sqlite3pp::query& query_) {
    int query_size = 0;
    for (auto i: query_) {
        query_size++;
    }
    return query_size;
}

void Database::insertRow(str_r table_name, v_str types, v_str values) { // list of types
    std::string line_q = "SELECT * FROM " + table_name;
    sqlite3pp::query query(*db, line_q.c_str());

    if (types[0] != query.column_name(0)) {
        types.insert(types.begin(), query.column_name(0));
        values.insert(values.begin(), std::to_string(getQuerySize(query)+1));
    }

    std::string str_types = putInBraces(types);
    std::string str_values = putInBraces(values, true);
    std::string line = "INSERT INTO " + table_name + " " + str_types + " VALUES " + str_values;
    sqlite3pp::command cmd(*db, line.c_str());
    cmd.execute();
}

void Database::deleteRow(str_r table_name, str_r column_name, str_r to_delete) {
    std::string line = "DELETE FROM " + table_name + " WHERE " + column_name + "='" + to_delete + "'";
    sqlite3pp::command cmd(*db, line.c_str());
    cmd.execute();
}

vv_str Database::selectRow(str_r table_name, str_r column_name, str_r to_find) {
    std::string line;
    if (column_name.empty() && to_find.empty()) {
        line = "SELECT * FROM " + table_name;
    } else if (!column_name.empty() && !to_find.empty()) {
        line = "SELECT * FROM " + table_name + " WHERE " + column_name + "=\"" + to_find + "\"";
    } else {
        throw std::runtime_error("Fields must be either filled in or empty!");
    }
    sqlite3pp::query query(*db, line.c_str());
    return getQuery(query);
}

void Database::updateRow(str_r table_name, v_str_r types, v_str_r values, str_r column_name, str_r to_find) {
    std::string line = "UPDATE " + table_name + " SET ";
    for (int i = 0; i < types.size()-1; i++) {
        line += types[i] + " = " + "'" + values[i] + "', ";
    } line += types[types.size()-1] + " = " + "'" + values[types.size()-1] + "'";
    if (!column_name.empty() && !to_find.empty()) {
        line += " WHERE " + column_name + " = " + "'" + to_find + "'";
    }
    sqlite3pp::command cmd(*db, line.c_str());
    cmd.execute();
}
