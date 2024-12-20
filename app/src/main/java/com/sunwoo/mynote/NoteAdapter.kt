package com.sunwoo.mynote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunwoo.mynote.databinding.ItemNoteBinding

class NoteAdapter(
    private var noteList: MutableList<Note>,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {


    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note, position: Int) {
            binding.tvTitle.text = note.title
            binding.tvContent.text = note.content
            binding.btnDelete.setOnClickListener { onDelete(position) }
        }
    }

    override fun getItemCount(): Int = noteList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val todo = noteList[position]
        holder.bind(todo, position)
    }

    fun updateData(newTodoList: MutableList<Note>) {
        noteList = newTodoList
        notifyDataSetChanged()
    }
}