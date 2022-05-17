package com.example.todolist

import android.view.View

interface ClickListener {
    fun onClick(view: View?, position: Int)
}