package com.lucas.contactlist.ui

import android.icu.text.Transliterator.Position

sealed interface OnContactClickListener {
    fun onContactClick(position: Int)
    fun onRemoveContactMenuItemClick(position: Int)
    fun onEditContactMenuItemClick(position: Int)
}