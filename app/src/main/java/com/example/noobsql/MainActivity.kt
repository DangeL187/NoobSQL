package com.example.noobsql

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.noobsql.databinding.ActivityMainBinding
import java.io.File

////////THIS CODE IS NOT PERFECT AT ALL, I'M NOT AN EXPERT ON Kotlin!////////

fun getScreenWidthInPixels(context: Context): Int {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var tableIndex: Int = -1
    private var pageName: String = "main"

    private val REQUEST_EXTERNAL_STORAGE = 1
    @RequiresApi(Build.VERSION_CODES.R)
    private val PERMISSIONS_STORAGE = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
    )

    @RequiresApi(Build.VERSION_CODES.R)
    fun verifyStoragePermissions(activity: Activity) {
        val permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE)
        } else {
            accessFile()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accessFile()
            }
        }
    }

    private fun accessFile() {
        val folderPath = "/storage/emulated/0/NoobSQL"
        val filePath = "/storage/emulated/0/NoobSQL/new.db"
        val tableName: TextView = findViewById(R.id.table_name)

        if (initDatabase(filePath) == -1) {
            println("Database file does not exist!")
            println("Creating new database file...")
            val isFolderCreated = File(folderPath).mkdirs()
            val isFileCreated = File(filePath).createNewFile()
            if (isFileCreated) println("File 'new.db' successfully created!")
            initDatabase(filePath)
        }

        val tables: Array<String> = getTableNames()
        if (tables.isNotEmpty()) {
            tableName.text = tables[0]
            tableIndex++
            refreshTable()
        } else {
            activatePlusButton()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainButtons: ArrayList<Button> = getButtons()
        val inputTexts: ArrayList<EditText> = getInputTexts()
        val inputTable: EditText = findViewById(R.id.inputText0)
        val okButton: Button = findViewById(R.id.btnOk)
        val plusButton: Button = findViewById(R.id.btnPlus)
        val prevButton: Button = findViewById(R.id.btnPrevTable)
        val nextButton: Button = findViewById(R.id.btnNextTable)
        val confirmButton: Button = findViewById(R.id.btnConfirm)
        val deleteTableButton: Button = findViewById(R.id.btnDeleteTable)

        for (inp in inputTexts) {
            inp.visibility = View.INVISIBLE
            inp.isClickable = false
        }
        okButton.visibility = View.INVISIBLE
        inputTable.visibility = View.INVISIBLE
        plusButton.visibility = View.INVISIBLE
        prevButton.visibility = View.INVISIBLE
        confirmButton.visibility = View.INVISIBLE
        okButton.isClickable = false
        inputTable.isClickable = false
        plusButton.isClickable = false
        prevButton.isClickable = false
        confirmButton.isClickable = false

        mainButtons[0].setOnClickListener(View.OnClickListener { //select
            pageName = "select"
            inputTexts[0].hint = "column name"
            inputTexts[1].hint = "value"
            for (inp in getInputTexts(true)) {
                inp.visibility = View.VISIBLE
                inp.isClickable = true
            }
            goToOkPage()
            moveUpOrDown(true)
        })

        mainButtons[1].setOnClickListener(View.OnClickListener { //insertRow
            pageName = "insert_row"
            inputTexts[0].hint = "column1, column2..."
            inputTexts[1].hint = "value1, value2..."
            for (inp in getInputTexts(true)) {
                inp.visibility = View.VISIBLE
                inp.isClickable = true
            }
            goToOkPage()
            moveUpOrDown(true)
        })

        mainButtons[2].setOnClickListener(View.OnClickListener { //addColumn
            pageName = "add_column"
            inputTexts[0].hint = "column name"
            inputTexts[0].visibility = View.VISIBLE
            inputTexts[0].isClickable = true
            goToOkPage()
            moveUpOrDown(true)
        })

        mainButtons[3].setOnClickListener(View.OnClickListener { //updateRow
            pageName = "update_row"
            inputTexts[0].hint = "column1, column2..."
            inputTexts[1].hint = "value1, value2..."
            inputTexts[2].hint = "WHERE {column} = "
            inputTexts[3].hint = "{value}"
            for (inp in getInputTexts()) {
                inp.visibility = View.VISIBLE
                inp.isClickable = true
            }
            goToOkPage()
            moveUpOrDown(true)
        })

        mainButtons[4].setOnClickListener(View.OnClickListener { //renameColumn
            pageName = "rename_column"
            inputTexts[0].hint = "old column name"
            inputTexts[1].hint = "new column name"
            for (inp in getInputTexts(true)) {
                inp.visibility = View.VISIBLE
                inp.isClickable = true
            }
            goToOkPage()
            moveUpOrDown(true)
        })

        mainButtons[5].setOnClickListener(View.OnClickListener { //deleteRow
            pageName = "delete_row"
            inputTexts[0].hint = "column name"
            inputTexts[1].hint = "value"
            for (inp in getInputTexts(true)) {
                inp.visibility = View.VISIBLE
                inp.isClickable = true
            }
            goToOkPage()
            moveUpOrDown(true)
        })

        mainButtons[6].setOnClickListener(View.OnClickListener { //deleteColumn
            pageName = "delete_column"
            inputTexts[0].hint = "column name"
            inputTexts[0].visibility = View.VISIBLE
            inputTexts[0].isClickable = true
            goToOkPage()
            moveUpOrDown(true)
        })

        okButton.setOnClickListener(View.OnClickListener {
            val tableName: TextView = findViewById(R.id.table_name)
            val edit1: EditText = findViewById(R.id.inputText1)
            val edit2: EditText = findViewById(R.id.inputText2)

            if (pageName == "select") {
                val rows: Array<Array<String>> = selectRow(
                    tableName.text.toString(),
                    edit1.text.toString(),
                    edit2.text.toString()
                )
                refreshTable(rows)
            }
            else if (pageName == "insert_row") {
                val e1: Array<String> = edit1.text.toString().split(", ").toTypedArray()
                val e2: Array<String> = edit2.text.toString().split(", ").toTypedArray()
                if (insertRow(tableName.text.toString(), e1, e2) == 0) refreshTable()
            }
            else if (pageName == "add_column") {
                if (addColumn(tableName.text.toString(), edit1.text.toString()) == 0) refreshTable()
            }
            else if (pageName == "update_row") {
                val e1: Array<String> = edit1.text.toString().split(", ").toTypedArray()
                val e2: Array<String> = edit2.text.toString().split(", ").toTypedArray()
                val e3: EditText = findViewById(R.id.inputText3)
                val e4: EditText = findViewById(R.id.inputText4)
                if (updateRow(tableName.text.toString(), e1, e2, e3.text.toString(), e4.text.toString()) == 0) refreshTable()
            }
            else if (pageName == "rename_column") {
                if (renameColumn(tableName.text.toString(), edit1.text.toString(), edit2.text.toString()) == 0) refreshTable()
            }
            else if (pageName == "delete_row") {
                if (deleteRow(tableName.text.toString(), edit1.text.toString(), edit2.text.toString()) == 0) refreshTable()
            }
            else if (pageName == "delete_column") {
                if (deleteColumn(tableName.text.toString(), edit1.text.toString()) == 0) refreshTable()
            }
            else if (pageName == "plus") {
                if (createTable(inputTable.text.toString()) == 0) {
                    val tables: Array<String> = getTableNames()
                    tableIndex = tables.size-1
                    tableName.text = tables[tableIndex]
                    refreshTable()
                }
            }
            onBackPressed()
        })

        deleteTableButton.setOnClickListener(View.OnClickListener {
            val tableName: TextView = findViewById(R.id.table_name)
            if (tableName.text != "") {
                pageName = "delete"
                prevButton.visibility = View.INVISIBLE
                nextButton.visibility = View.INVISIBLE
                deleteTableButton.visibility = View.INVISIBLE
                confirmButton.visibility = View.VISIBLE
                prevButton.isClickable = false
                nextButton.isClickable = false
                deleteTableButton.isClickable = false
                confirmButton.isClickable = true
            }
        })

        confirmButton.setOnClickListener(View.OnClickListener {
            if (pageName == "delete") {
                val tableName: TextView = findViewById(R.id.table_name)
                if (deleteTable(tableName.text.toString()) == 0) {
                    val tables: Array<String> = getTableNames()
                    if (tables.isNotEmpty()) {
                        if (tableIndex > 0) tableIndex--
                        tableName.text = tables[tableIndex]
                    } else {
                        tableName.text = ""
                    }
                    refreshTable()
                }
                onBackPressed()
            }
        })

        prevButton.setOnClickListener(View.OnClickListener {
            val tableName: TextView = findViewById(R.id.table_name)
            val tables: Array<String> = getTableNames()
            for (btn in getButtons()) {
                btn.visibility = View.VISIBLE
                btn.isClickable = true
            }
            if (tableIndex > 0) {
                if (nextButton.isClickable) tableIndex--
            }
            if (tableIndex == 0) {
                prevButton.visibility = View.INVISIBLE
                prevButton.isClickable = false
            }
            nextButton.visibility = View.VISIBLE
            plusButton.visibility = View.INVISIBLE
            nextButton.isClickable = true
            plusButton.isClickable = false
            tableName.text = tables[tableIndex]
            refreshTable()
            onBackPressed()
        })

        nextButton.setOnClickListener(View.OnClickListener {
            prevButton.visibility = View.VISIBLE
            prevButton.isClickable = true

            val tableName: TextView = findViewById(R.id.table_name)
            val tables: Array<String> = getTableNames()

            if (tableIndex+1 < tables.size) {
                tableIndex++
                tableName.text = tables[tableIndex]
                refreshTable()
            } else {
                nextButton.visibility = View.INVISIBLE
                nextButton.isClickable = false
                activatePlusButton()
            }
            onBackPressed()
        })

        plusButton.setOnClickListener(View.OnClickListener {
            pageName = "plus"
            okButton.visibility = View.VISIBLE
            inputTable.visibility = View.VISIBLE
            plusButton.visibility = View.INVISIBLE
            prevButton.visibility = View.INVISIBLE
            nextButton.visibility = View.INVISIBLE
            okButton.isClickable = true
            inputTable.isClickable = true
            plusButton.isClickable = false
            prevButton.isClickable = false
            nextButton.isClickable = false
        })

        verifyStoragePermissions(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val tableName: TextView = findViewById(R.id.table_name)
        if (pageName == "plus") {
            val tables: Array<String> = getTableNames()
            if (tables.isNotEmpty()) {
                tableName.text = tables[tableIndex]
                refreshTable()
            } else {
                activatePlusButton()
            }
        }
        if (pageName != "main") {
            val okButton: Button = findViewById(R.id.btnOk)
            val prevButton: Button = findViewById(R.id.btnPrevTable)
            val nextButton: Button = findViewById(R.id.btnNextTable)
            val confirmButton: Button = findViewById(R.id.btnConfirm)
            val deleteTableButton: Button = findViewById(R.id.btnDeleteTable)
            val inputTable: EditText = findViewById(R.id.inputText0)
            if (tableName.text != "") {
                for (btn in getButtons()) {
                    btn.visibility = View.VISIBLE
                    btn.isClickable = true
                }
                if (tableIndex > 0) {
                    prevButton.visibility = View.VISIBLE
                    prevButton.isClickable = true
                }
                nextButton.visibility = View.VISIBLE
                deleteTableButton.visibility = View.VISIBLE
                nextButton.isClickable = true
                deleteTableButton.isClickable = true
            }
            for (inp in getInputTexts()) {
                inp.setText("")
                inp.visibility = View.INVISIBLE
                inp.isClickable = false
            }
            inputTable.setText("")
            okButton.visibility = View.INVISIBLE
            inputTable.visibility = View.INVISIBLE
            confirmButton.visibility = View.INVISIBLE
            okButton.isClickable = false
            inputTable.isClickable = false
            confirmButton.isClickable = false
            moveUpOrDown(false)
            pageName = "main"
        }
    }

    private fun getButtons(): ArrayList<Button> {
        val mainPageButtons: ArrayList<Button> = ArrayList()
        mainPageButtons.add(findViewById(R.id.btnSelect))
        mainPageButtons.add(findViewById(R.id.btnInsertRow))
        mainPageButtons.add(findViewById(R.id.btnAddColumn))
        mainPageButtons.add(findViewById(R.id.btnUpdateRow))
        mainPageButtons.add(findViewById(R.id.btnRenameColumn))
        mainPageButtons.add(findViewById(R.id.btnDeleteRow))
        mainPageButtons.add(findViewById(R.id.btnDeleteColumn))
        return mainPageButtons
    }

    private fun getInputTexts(firstTwoOnly: Boolean = false): ArrayList<EditText> {
        val inputTexts: ArrayList<EditText> = ArrayList()
        inputTexts.add(findViewById(R.id.inputText1))
        inputTexts.add(findViewById(R.id.inputText2))
        if (!firstTwoOnly) {
            inputTexts.add(findViewById(R.id.inputText3))
            inputTexts.add(findViewById(R.id.inputText4))
        }
        return inputTexts
    }

    private fun moveUpOrDown(moveUp: Boolean) {
        if (moveUp) {
            val table: TableLayout = findViewById(R.id.tableLayout)
            val scroll: ScrollView = findViewById(R.id.scrollView)
            val layoutParams = table.layoutParams
            val layoutParams2 = scroll.layoutParams
            layoutParams.height = 458
            layoutParams2.height = 458
            table.layoutParams = layoutParams
            scroll.layoutParams = layoutParams
        } else {
            val table: TableLayout = findViewById(R.id.tableLayout)
            val scroll: ScrollView = findViewById(R.id.scrollView)
            val layoutParams = table.layoutParams
            val layoutParams2 = scroll.layoutParams
            layoutParams.height = 369*2
            layoutParams2.height = 369*2
            table.layoutParams = layoutParams
            scroll.layoutParams = layoutParams
        }
    }

    private fun fillTable(tableLayout: TableLayout, data: Array<Array<String>>, columnWidth: Int) {
        tableLayout.removeAllViews()
        for (row in data) {
            val tableRow = TableRow(this)
            val tableRowParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            tableRow.layoutParams = tableRowParams
            for (cell in row) {
                val textView = TextView(this)
                textView.text = " $cell "
                textView.setTextColor(resources.getColor(R.color.neon_cyan))
                val cellParams = TableRow.LayoutParams(
                    columnWidth,
                    TableRow.LayoutParams.WRAP_CONTENT)
                textView.layoutParams = cellParams
                textView.setBackgroundResource(R.drawable.border_table)
                tableRow.addView(textView)
            }
            tableLayout.addView(tableRow)
        }
    }

    private fun refreshTable(arr: Array<Array<String>> = emptyArray()) {
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        val tableColumns = findViewById<TableLayout>(R.id.tableColumns)
        val tableName: TextView = findViewById(R.id.table_name)
        val rows =
            if (arr.isEmpty() && tableName.text != "") {
                selectRow(tableName.text.toString(), "", "")
            } else {
                arr
            }
        val columns: Array<String> =
            if (tableName.text == "") {
                emptyArray()
            } else {
                getColumns(tableName.text.toString())
            }
        if (columns.isNotEmpty()) {
            val columnWidth = getScreenWidthInPixels(this)/columns.size
            fillTable(tableColumns, arrayOf(columns), columnWidth)
            fillTable(tableLayout, rows, columnWidth)
        } else {
            activatePlusButton()
        }
    }

    private fun activatePlusButton() {
        val nextButton: Button = findViewById(R.id.btnNextTable)
        val plusButton: Button = findViewById(R.id.btnPlus)
        val tableName: TextView = findViewById(R.id.table_name)
        val tableLayout: TableLayout = findViewById(R.id.tableLayout)
        val tableColumns: TableLayout = findViewById(R.id.tableColumns)
        for (btn in getButtons()) {
            btn.visibility = View.INVISIBLE
            btn.isClickable = false
        }
        nextButton.visibility = View.INVISIBLE
        plusButton.visibility = View.VISIBLE
        nextButton.isClickable = false
        plusButton.isClickable = true
        tableName.text = ""
        tableLayout.removeAllViews()
        tableColumns.removeAllViews()
    }

    private fun goToOkPage() {
        val okButton: Button = findViewById(R.id.btnOk)
        val prevButton: Button = findViewById(R.id.btnPrevTable)
        val nextButton: Button = findViewById(R.id.btnNextTable)

        for (btn in getButtons()) {
            btn.visibility = View.INVISIBLE
            btn.isClickable = false
        }
        okButton.visibility = View.VISIBLE
        prevButton.visibility = View.INVISIBLE
        nextButton.visibility = View.INVISIBLE
        okButton.isClickable = true
        prevButton.isClickable = false
        nextButton.isClickable = false
    }

    private external fun initDatabase(filePath: String): Int

    private external fun createTable(table: String): Int
    private external fun deleteTable(table: String): Int
    private external fun getTableNames(): Array<String>

    private external fun deleteRow(table: String, edit1: String, edit2: String): Int
    private external fun insertRow(table: String, edit1: Array<String>, edit2: Array<String>): Int
    private external fun selectRow(table: String, edit1: String, edit2: String): Array<Array<String>>
    private external fun updateRow(table: String, edit1: Array<String>, edit2: Array<String>, edit3: String, edit4: String): Int

    private external fun addColumn(table: String, edit1: String): Int
    private external fun deleteColumn(table: String, edit1: String): Int
    private external fun getColumns(table: String): Array<String>
    private external fun renameColumn(table: String, edit1: String, edit2: String): Int

    companion object {
        init {
            System.loadLibrary("NoobSQL")
        }
    }
}
