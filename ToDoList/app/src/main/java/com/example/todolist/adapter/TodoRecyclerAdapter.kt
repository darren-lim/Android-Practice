package com.example.todolist.adapter

import android.app.Activity
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.ClickListener
import com.example.todolist.R
import com.example.todolist.vo.TodoItemVO

class TodoRecyclerAdapter(var items: ArrayList<TodoItemVO>, clickListener: ClickListener?): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    private val mClickListener: ClickListener? = clickListener

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        var cardView: CardView = v.findViewById(R.id.card_view_todo)
        var textView: TextView = v.findViewById(R.id.tv_todo_description)
        var buttonComplete: ImageButton = v.findViewById(R.id.button_todo_complete)
        var buttonDelete: ImageButton = v.findViewById(R.id.button_todo_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder){
            val item = items[position]

            holder.textView.text = item.description
            // if completed?
            holder.buttonComplete.setOnClickListener{
                // change
                println("Complete")
                if(item.completed){
                    holder.textView.paintFlags = Paint.ANTI_ALIAS_FLAG
                } else {
                    holder.textView.paintFlags = holder.textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                item.completed = !item.completed
            }
            holder.buttonDelete.setOnClickListener{
                // change
                println("Delete")
                mClickListener?.onClick(holder.cardView, position)
            }
        }

    }

    override fun getItemCount() = items.size
}