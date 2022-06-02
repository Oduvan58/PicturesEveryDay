package by.geekbrains.pictureseveryday.view.notes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import by.geekbrains.pictureseveryday.databinding.ActivityNotesBinding
import by.geekbrains.pictureseveryday.domain.NoteEntity

class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val note = mutableListOf(
            Pair(NoteEntity("New Note", ""), false))
        note.add(0, Pair(NoteEntity("Header", ""), false))

        adapter = NotesAdapter(
            object : OnListItemClickListener {
                override fun onItemClick(note: NoteEntity) {
                    Toast.makeText(
                        this@NotesActivity,
                        note.note,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            note
        )
        binding.recyclerView.adapter = adapter
        binding.addNoteFAB.setOnClickListener {
            adapter.appendItem()
        }
        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(binding.recyclerView)
    }
}