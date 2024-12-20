package com.sunwoo.mynote

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunwoo.mynote.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var noteList: MutableList<Note> = mutableListOf()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NoteAdapter
    private lateinit var noteStorage: NoteStorage
    private var lastSelectedDate: String? = null

    private val addEditActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            noteList = noteStorage.loadNotes()
            adapter.updateData(noteList)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lastSelectedDate = null
        noteStorage = NoteStorage(this)
        noteList = noteStorage.loadNotes()

        loadTodosFromDatabase()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter(noteList, ::onDelete, ::onUpdate)
        binding.recyclerView.adapter = adapter


        binding.mainCalendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = "${year}년 ${month + 1}월 ${dayOfMonth}일"

            if (selectedDate == lastSelectedDate) {
                loadTodosFromDatabase()
                lastSelectedDate = null
            } else {
                val notes = noteStorage.loadNotesByDate(selectedDate)
                noteList.clear()
                noteList.addAll(notes)
                adapter.updateData(noteList)
                lastSelectedDate = selectedDate
            }
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            addEditActivityResultLauncher.launch(intent)
        }
    }


    private fun loadTodosFromDatabase() {
        noteList.clear()
        noteList.addAll(noteStorage.loadNotes())
        if (::adapter.isInitialized) {
            adapter.updateData(noteList) // Adapter에 데이터 갱신
        }
    }

    private fun onDelete(position: Int) {
        val note = noteList[position]
        noteStorage.deleteNote(note.id!!)
        noteList.removeAt(position)
        adapter.updateData(noteList)
    }

    private fun onUpdate(position: Int) {
        val note = noteList[position]

        val intent = Intent(this, AddEditActivity::class.java).apply {
            putExtra("id", note.id)
            putExtra("title", note.title)
            putExtra("content", note.content)
            putExtra("date", note.date)
        }
        addEditActivityResultLauncher.launch(intent)
    }
}