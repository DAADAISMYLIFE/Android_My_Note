package com.sunwoo.mynote

import android.content.ContentValues
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
            ), null, null, null, null, NoteDatabaseHelper.COLUMN_ID + " DESC"
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

    fun loadNotesByDate(date: String): MutableList<Note> {
        val noteList = mutableListOf<Note>()
        val db = dbHelper.readableDatabase

        val cursor: Cursor = db.query(
            NoteDatabaseHelper.TABLE_NAME,
            arrayOf(
                NoteDatabaseHelper.COLUMN_ID,
                NoteDatabaseHelper.COLUMN_TITLE,
                NoteDatabaseHelper.COLUMN_CONTENT,
                NoteDatabaseHelper.COLUMN_DATE
            ),
            "${NoteDatabaseHelper.COLUMN_DATE} = ?",
            arrayOf(date),
            null,
            null,
            null
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
                    val noteDate =
                        cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_DATE))
                    noteList.add(Note(id, title, content, noteDate))
                } while (cursor.moveToNext())
            }
        }

        db.close()
        return noteList
    }

    fun addNote(note: Note): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(NoteDatabaseHelper.COLUMN_TITLE, note.title)
            put(NoteDatabaseHelper.COLUMN_CONTENT, note.content)
            put(NoteDatabaseHelper.COLUMN_DATE, note.date)
        }

        val id = db.insert(
            NoteDatabaseHelper.TABLE_NAME,
            null,
            values
        )
        db.close()
        return id
    }

    fun deleteNote(id: Int): Int {
        val db = dbHelper.writableDatabase
        val rowsDeleted = db.delete(
            NoteDatabaseHelper.TABLE_NAME,
            "${NoteDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
        return rowsDeleted
    }

    fun updateNote(note: Note): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(NoteDatabaseHelper.COLUMN_TITLE, note.title)
            put(NoteDatabaseHelper.COLUMN_CONTENT, note.content)
            put(NoteDatabaseHelper.COLUMN_DATE, note.date)
        }

        val rowsAffected = db.update(
            NoteDatabaseHelper.TABLE_NAME,
            values,
            "${NoteDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(note.id.toString())
        )

        db.close()
        return rowsAffected
    }
}

