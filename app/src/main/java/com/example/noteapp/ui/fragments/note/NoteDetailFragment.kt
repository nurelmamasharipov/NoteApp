package com.example.noteapp.ui.fragments.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.noteapp.App
import com.example.noteapp.R
import com.example.noteapp.data.models.NoteModel
import com.example.noteapp.databinding.FragmentNoteDetailBinding


class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (binding.titleEditText.text != null && binding.titleEditText.text != null) {
            binding.tvSave.visibility = View.VISIBLE
        }
        setupListener()

    }
    private fun setupListener() = with(binding) {
            tvSave.setOnClickListener {
                val title = titleEditText.text.toString()
                val text = textEditText.text.toString()
                val data = tvDate.text.toString()
                App.appDataBase?.noteDao()?.insertNote(NoteModel(title, text, data))
                findNavController().navigateUp()
            }
        }
    }