package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.todolist.databinding.DogCatFragmentBinding
import com.example.todolist.service.http.DogCatService
import com.example.todolist.util.LoadingUtil
import com.example.todolist.vo.DogCatResVO
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DogCatFragment : Fragment() {
    private lateinit var binding: DogCatFragmentBinding

    private lateinit var mSwipeRefreshView: SwipeRefreshLayout
    private var dogOrCat : String = "dog"

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DogCatFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    fun init() {
        mSwipeRefreshView = binding.dogCatRefresh
        mSwipeRefreshView.setOnRefreshListener {
            fetchData(dogOrCat)
        }

        binding.btnGetCat.setOnClickListener {
            fetchData("cat")
        }
        binding.btnGetDog.setOnClickListener {
            fetchData("dog")
        }
    }

    private fun setData(data: DogCatResVO) {
        CoroutineScope(Dispatchers.Main).launch {
            var url = data.fileUrl
            url = url.replace("\\", "")
            println(url)
            Picasso.get()
                .load(url)
                .into(binding.imageIcons, object : Callback {
                    override fun onSuccess() {
                        binding.imageIcons.visibility = View.VISIBLE
                        stopRefresh()
                    }

                    override fun onError(e: java.lang.Exception?) {
                        println("Error")
                        if (e != null) {
                            println(e.stackTraceToString())
                        }
                        binding.imageIcons.visibility = View.INVISIBLE
                        binding.tvNoData.visibility = View.VISIBLE
                        stopRefresh()
                    }
                })
        }
    }

    private fun fetchData(s: String) {
        val context = requireContext()
        val dogCatService = DogCatService()

        binding.tvNoData.visibility = View.GONE

        dogOrCat = s

        CoroutineScope(Dispatchers.Main).launch {
            mSwipeRefreshView.isRefreshing = false
        }

        LoadingUtil.show(context, "Fetch data......")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result: DogCatResVO = dogCatService.getDogOrCat(s)

                setData(result)
            } catch (e: Exception) {
                println("Error")
                stopRefresh()
            }
        }
    }

    private fun stopRefresh() {
        LoadingUtil.hide()
        CoroutineScope(Dispatchers.Main).launch {
            mSwipeRefreshView.isRefreshing = false
        }
    }

}