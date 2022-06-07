package by.geekbrains.pictureseveryday.view.notes

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.RecyclerView
import by.geekbrains.pictureseveryday.R
import by.geekbrains.pictureseveryday.domain.NoteEntity

class NotesAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private var note: MutableList<Pair<NoteEntity, Boolean>>,
) : RecyclerView.Adapter<BaseViewHolder>(), ItemTouchHelperAdapter {

    companion object {
        private const val TYPE_NOTE = 0
        private const val TYPE_HEADER = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_NOTE) {
            NoteViewHolder(
                inflater.inflate(R.layout.recycler_item_note, parent, false) as View
            )
        } else {
            HeaderViewHolder(
                inflater.inflate(R.layout.recycler_header, parent, false) as View
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(note[position])
    }

    override fun getItemCount() = note.size


    override fun getItemViewType(position: Int) = when (position) {
        0 -> TYPE_HEADER
        else -> TYPE_NOTE
    }

    fun appendItem() {
        note.add(generateItem())
        notifyItemInserted(itemCount - 1)
    }

    private fun generateItem() = Pair(NoteEntity("New Note", ""), false)

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        note.removeAt(fromPosition).apply {
            note.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        note.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class NoteViewHolder(view: View) : BaseViewHolder(view), ItemTouchHelperViewHolder {
        override fun bind(note: Pair<NoteEntity, Boolean>) {
            itemView.apply {
                findViewById<TextView>(R.id.note_date_text_view).text = note.first.note
                setOnClickListener {
                    onListItemClickListener.onItemClick(note.first)
                }
                findViewById<TextView>(R.id.note_description_text_view).setOnClickListener {
                    toggleText()

                }
                findViewById<AppCompatEditText>(R.id.note_edit_text).visibility =
                    if (note.second) View.VISIBLE else View.GONE
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }

        private fun toggleText() {
            note[layoutPosition] = note[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }
    }

    inner class HeaderViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(note: Pair<NoteEntity, Boolean>) {
        }
    }
}