package com.example.noteapp.ui.intetface

import com.example.noteapp.data.models.NoteModel

interface OnClickItem {
    fun OnLongClick(noteModel: NoteModel)

    fun onClick(noteModel: NoteModel)
}