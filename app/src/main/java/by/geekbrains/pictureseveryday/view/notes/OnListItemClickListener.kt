package by.geekbrains.pictureseveryday.view.notes

import by.geekbrains.pictureseveryday.domain.NoteEntity

interface OnListItemClickListener {

    fun onItemClick(note: NoteEntity)
}