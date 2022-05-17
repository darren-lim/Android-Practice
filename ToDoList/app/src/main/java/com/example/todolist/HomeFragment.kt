package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.todolist.databinding.AppMenuBinding

class HomeFragment : Fragment() {
    private lateinit var binding: AppMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AppMenuBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuButtonInit()
    }

    override fun onStart() {
        super.onStart()
    }

    fun menuButtonInit() {
        binding.todoLayout.setOnClickListener{
            findNavController().navigate(R.id.todoFragment)
        }

        binding.calculatorLayout.setOnClickListener{
            findNavController().navigate(R.id.calculatorFragment)
        }

        binding.weatherLayout.setOnClickListener{
            findNavController().navigate(R.id.weatherFragment)
        }

        binding.scannerLayout.setOnClickListener{
            findNavController().navigate(R.id.Scanner)
        }

        binding.mapLayout.setOnClickListener {
            findNavController().navigate(R.id.Map)
        }
    }

}