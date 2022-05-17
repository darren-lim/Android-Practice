package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.todolist.databinding.CalculatorFragmentBinding
import kotlinx.android.synthetic.main.calculator_fragment.*

class CalculatorFragment : Fragment(){
    private lateinit var binding: CalculatorFragmentBinding

    var isOperatorPressed : Boolean = false
    var outString: String? = ""
    var decimalPressed : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CalculatorFragmentBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(savedInstanceState != null){
            outString = savedInstanceState.getString("output")
            tv_result.text = outString
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("output", outString)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindButtons()
    }

    fun bindButtons() {
        binding.button0.setOnClickListener{ onDigitPressed("0") }
        binding.button1.setOnClickListener{ onDigitPressed("1") }
        binding.button2.setOnClickListener{ onDigitPressed("2") }
        binding.button3.setOnClickListener{ onDigitPressed("3") }
        binding.button4.setOnClickListener{ onDigitPressed("4") }
        binding.button5.setOnClickListener{ onDigitPressed("5") }
        binding.button6.setOnClickListener{ onDigitPressed("6") }
        binding.button7.setOnClickListener{ onDigitPressed("7") }
        binding.button8.setOnClickListener{ onDigitPressed("8") }
        binding.button9.setOnClickListener{ onDigitPressed("9") }
        binding.buttonClear.setOnClickListener{ onClear() }
        binding.buttonAdd.setOnClickListener{ onOperatorPressed(binding.buttonAdd) }
        binding.buttonSub.setOnClickListener{ onOperatorPressed(binding.buttonSub) }
        binding.buttonMult.setOnClickListener{ onOperatorPressed(binding.buttonMult) }
        binding.buttonDivide.setOnClickListener{ onOperatorPressed(binding.buttonDivide) }
        binding.buttonDot.setOnClickListener{ onDecimalPressed(binding.buttonDot) }
        binding.buttonEqual.setOnClickListener{ onEqual(binding.buttonEqual) }
    }

    fun onDigitPressed(digit: String) {
        outString += digit
        tv_result.text = outString
    }

    fun onClear() {
        tv_result.text = ""
        outString = ""
        isOperatorPressed = false
        decimalPressed = false
    }

    fun onOperatorPressed(view: View) {
        if(!isOperatorPressed) {
            outString += (view as Button).text
            tv_result.text = outString
            isOperatorPressed = true
            decimalPressed = false
        }
    }

    fun onDecimalPressed(view: View){
        if(!decimalPressed) {
            outString += (view as Button).text
            tv_result.text = outString
            decimalPressed = true
        }
    }

    fun onEqual(view: View) {
        var output = tv_result.text.toString()
        var negative = ""
        if(output == ""){
            return
        }
        if(output.startsWith("-")){
            negative = "-"
            output = output.substring(1)
        }
        if(output.contains("/")) {
            val splitValue = output.split("/")
            outString = removeZeroDecimal(((negative+splitValue[0]).toDouble() / splitValue[1].toDouble()).toString())
            tv_result.text = outString

        } else if(output.contains("X")) {
            val splitValue = output.split("X")
            outString = removeZeroDecimal(((negative+splitValue[0]).toDouble() * splitValue[1].toDouble()).toString())
            tv_result.text = outString

        } else if(output.contains("-")) {
            val splitValue = output.split("-")
            outString = removeZeroDecimal(((negative+splitValue[0]).toDouble() - splitValue[1].toDouble()).toString())
            tv_result.text = outString

        } else if(output.contains("+")) {
            val splitValue = output.split("+")
            outString = removeZeroDecimal(((negative+splitValue[0]).toDouble() + splitValue[1].toDouble()).toString())
            tv_result.text = outString
        }
        isOperatorPressed = false
        decimalPressed = tv_result.text.toString().contains(".")

    }

    fun removeZeroDecimal(result: String) : String {
        var value = result
        if(result.contains(".0")){
            value = result.substring(0, result.length-2)
        }
        return value
    }
}