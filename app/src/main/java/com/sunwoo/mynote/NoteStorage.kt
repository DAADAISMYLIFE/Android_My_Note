package com.sunwoo.mynote

import android.content.Context
import android.database.Cursor

class NoteStorage(context: Context) {
    private val dbHelper = NoteDatabaseHelper(context)

    fun loadNotes(): MutableList<Note> {
        val noteList = mutableListOf<Note>()
        val db = dbHelper.readableDatabase

        val cursor: Cursor = db.query(
            NoteDatabaseHelper.TABLE_NAME, arrayOf(
                NoteDatabaseHelper.COLUMN_ID,
                NoteDatabaseHelper.COLUMN_TITLE,
                NoteDatabaseHelper.COLUMN_CONTENT,
                NoteDatabaseHelper.COLUMN_DATE
            ), null, null, null, null, null
        )

        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val id =
                        cursor.getInt(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_ID))
                    val title =
                        cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_TITLE))
                    val content =
                        cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_CONTENT))
                    val date =
                        cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_DATE))
                    noteList.add(Note(id, title, content, date))
                } while (cursor.moveToNext())
            }
        }

        db.close()
        return noteList
    }
}

