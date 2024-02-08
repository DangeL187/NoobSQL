#include <iostream>
#include <memory>
#include <vector>
#include <fstream>
#include <sqlite3pp.h>
#include <SQLiteCpp/SQLiteCpp.h>

#ifndef SQLITE3PPEXT_H
#define SQLITE3PPEXT_H
#include <sqlite3ppext.h>
#endif

class Database {
public:
    typedef std::vector<std::string>              v_str;
    typedef std::vector<std::vector<std::string>> vv_str;
    typedef const std::string&                    str_r;
    typedef const std::vector<std::string>&       v_str_r;

    explicit Database(const char* db_path) {
        try {
            db = std::make_shared<sqlite3pp::database>(db_path);
        } catch (std::exception& e) {
            throw std::runtime_error("Failed to load a database");
        }
    }

    v_str getTableNames() {
        v_str table_names;
        sqlite3pp::query query(*db, "SELECT name FROM sqlite_schema WHERE type = 'table' AND name NOT LIKE 'sqlite_%'");
        vv_str vec = getQuery(query);
        for (v_str& row: vec) {
            table_names.push_back(row[0]);
        }
        return table_names;
    }

    void   createTable(str_r table_name);
    void   deleteTable(str_r table_name);

    void   addColumn(str_r table_name, str_r column_name);
    void   deleteColumn(str_r table_name, str_r column_name);
    v_str  getColumns(str_r table_name);
    void   renameColumn(str_r table_name, str_r old_column_name, str_r new_column_name);

    void   insertRow(str_r table_name, v_str types, v_str values);
    void   deleteRow(str_r table_name, str_r column_name, str_r to_delete);
    vv_str selectRow(str_r table_name, str_r column_name = "", str_r to_find = "");
    void   updateRow(str_r table_name, v_str_r types, v_str_r values, str_r column_name = "", str_r to_find = "");
private:
    std::shared_ptr<sqlite3pp::database> db;

    static vv_str getQuery(sqlite3pp::query& query) {
        vv_str values;
        for (auto row: query) {
            v_str v;
            for (int j = 0; j < query.column_count(); j++) {
                std::string value;
                if (row.get<const char*>(j)) {
                    value = row.get<const char*>(j);
                } else {
                    value = "";
                }
                v.emplace_back(value);
            }
            values.emplace_back(v);
        }
        return values;
    }

    static std::string putInBraces(v_str_r vec, bool quoteWords = false) {
        std::string output = "(";
        for (int i = 0; i < vec.size()-1; i++) {
            if (quoteWords) {
                output += "\"" + vec[i] + "\", ";
            }
            else {
                output += vec[i] + ", ";
            }
        }
        if (quoteWords) {
            output += "\"" + vec[vec.size() - 1] + "\")";
        } else {
            output += vec[vec.size()-1] + ")";
        }
        return output;
    }
};
