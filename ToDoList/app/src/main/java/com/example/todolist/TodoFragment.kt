package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.adapter.TodoRecyclerAdapter
import com.example.todolist.databinding.TodoFragmentBinding
import com.example.todolist.vo.TodoItemVO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.navigation.fragment.navArgs

class TodoFragment : Fragment() {
    private lateinit var binding: TodoFragmentBinding

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mTodoRecyclerAdapter: TodoRecyclerAdapter
    private lateinit var mTodoList: ArrayList<TodoItemVO>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TodoFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        binding.inputTodo.requestFocus()
        mTodoList = arrayListOf<TodoItemVO>(
            TodoItemVO("Wash Dishes", false),
            TodoItemVO("Fold Laundry", false)
        )

        mTodoRecyclerAdapter = TodoRecyclerAdapter(mTodoList, object: ClickListener {
            override fun onClick(view: View?, position: Int) {
                deleteData(position)
            }
        })
        mRecyclerView = binding.recyclerviewTodo

        mRecyclerView.adapter = mTodoRecyclerAdapter

        binding.btnCreate.setOnClickListener{
            addData()
        }
    }

    private fun addData() {
        if(binding.inputTodo.text.toString()=="") return
        mTodoList.add(
            TodoItemVO(binding.inputTodo.text.toString(),false)
        )
        binding.inputTodo.setText("")

        CoroutineScope(Dispatchers.Main).launch {
            binding.tvNoData.visibility = View.GONE

            mTodoRecyclerAdapter.items = mTodoList
            mTodoRecyclerAdapter.notifyDataSetChanged()
        }
    }

    private fun deleteData(position: Int){
        mTodoList.removeAt(position)

        CoroutineScope(Dispatchers.Main).launch {
            if(mTodoList.size == 0) {
                binding.tvNoData.visibility = View.VISIBLE
            } else {
                binding.tvNoData.visibility = View.GONE
            }
            mTodoRecyclerAdapter.items = mTodoList
            mTodoRecyclerAdapter.notifyDataSetChanged()
        }
    }
}