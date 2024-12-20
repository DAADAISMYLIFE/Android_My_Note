package com.sunwoo.mynote

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "note.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "notes"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
           CREATE TABLE $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_TITLE TEXT NOT NULL,
            $COLUMN_CONTENT TEXT NOT NULL,
            $COLUMN_DATE DATE DEFAULT (DATETIME('now'))
           )
        """
        db!!.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}