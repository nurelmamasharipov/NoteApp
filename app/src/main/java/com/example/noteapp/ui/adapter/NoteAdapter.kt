package com.example.noteapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.models.NoteModel
import com.example.noteapp.databinding.ItemNoteBinding
import com.example.noteapp.ui.intetface.OnClickItem

class NoteAdapter(
    private val onLongClick: OnClickItem,
    private val onClick: OnClickItem
) : ListAdapter<NoteModel, NoteAdapter.ViewHolder>(DiffCallback()) {
    class ViewHolder (private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(item: NoteModel) {
            binding.txtName.text = item.title
            binding.txtAbout.text = item.description
            binding.tvData.text = item.date
            itemView.setBackgroundColor(item.color)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))

        holder.itemView.setOnLongClickListener {
            onLongClick.OnLongClick(getItem(position))
            true
        }
        holder.itemView.setOnClickListener {
            onClick.onClick(getItem(position))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemNoteBinding.inflate(
        (    LayoutInflater.from(parent.context)),
            parent,
            false))
    }

    class DiffCallback: DiffUtil.ItemCallback<NoteModel>() {
        override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
            return oldItem == newItem
        }
    }
}