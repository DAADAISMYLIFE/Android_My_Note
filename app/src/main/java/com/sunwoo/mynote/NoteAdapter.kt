package com.sunwoo.mynote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunwoo.mynote.databinding.ItemNoteBinding

class NoteAdapter(
    private var noteList: MutableList<Note>,
    private val onDelete: (Int) -> Unit,
    private val onUpdate: (Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {


    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note, position: Int) {
            val titleMaxLength = 20
            val longTitle = if (note.title.length > titleMaxLength) {
                note.title.substring(0, titleMaxLength) + "..."
            } else {
                note.title
            }

            binding.tvTitle.text = longTitle
            binding.tvContent.text = note.content
            binding.tvDate.text = note.date
            binding.btnDelete.setOnClickListener { onDelete(position) }
            binding.btnEdit.setOnClickListener { onUpdate(position) }
        }
    }

    override fun getItemCount(): Int = noteList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.bind(note, position)
    }

    fun updateData(newNoteList: MutableList<Note>) {
        noteList = newNoteList
        notifyDataSetChanged()
    }
}