package com.sunwoo.mynote

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sunwoo.mynote.databinding.ActivityAddEditBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private lateinit var noteStorage: NoteStorage
    private var editNoteId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteStorage = NoteStorage(this)

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val date = intent.getStringExtra("date")
        editNoteId = intent.getIntExtra("id", -1)

        if (title != null && content != null && editNoteId != -1) {
            binding.etTitle.setText(title)
            binding.etContent.setText(content)
            binding.etDate.setText(date)
        }

        if (date == null) {
            val today = SimpleDateFormat(
                "yyyy년 MM월 dd일",
                Locale.getDefault()
            ).format(Calendar.getInstance().time)
            binding.etDate.setText(today)
        }

        binding.edDateContainer.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    val formattedDate = String.format("%04d년 %02d월 %02d일", year, monthOfYear + 1, dayOfMonth)
                    binding.etDate.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        binding.btnSave.setOnClickListener {
            val newTitle = binding.etTitle.text.toString()
            val newContent = binding.etContent.text.toString()
            val newDate = binding.etDate.text.toString()

            if (newTitle.isNotBlank() && newContent.isNotBlank()) {
                if (editNoteId != null && editNoteId != -1) {
                    val updatedNote =
                        Note(
                            id = editNoteId,
                            title = newTitle,
                            content = newContent,
                            date = newDate
                        )
                    noteStorage.updateNote(updatedNote)
                } else {
                    val newNote = Note(title = newTitle, content = newContent, date = newDate)
                    noteStorage.addNote(newNote)
                }
                setResult(RESULT_OK)
                finish()
            }
        }
    }
}