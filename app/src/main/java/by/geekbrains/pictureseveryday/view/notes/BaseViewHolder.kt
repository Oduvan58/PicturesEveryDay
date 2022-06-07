package by.geekbrains.pictureseveryday.view.notes

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.geekbrains.pictureseveryday.domain.NoteEntity

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(note: Pair<NoteEntity, Boolean>)
}