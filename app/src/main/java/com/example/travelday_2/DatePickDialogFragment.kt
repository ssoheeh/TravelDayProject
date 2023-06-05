package com.example.travelday_2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.example.travelday_2.databinding.FragmentDatePickDialogBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


class DatePickDialogFragment : Fragment() {
    lateinit var binding:FragmentDatePickDialogBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDatePickDialogBinding.inflate(layoutInflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDataRangePicker()
        initBackStack()
    }

    private fun initBackStack() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로가기 버튼이 눌렸을 때 처리할 동작 구현
                parentFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    private fun showDataRangePicker() {
        val selectedCountry=arguments?.getString("country")

        val dateRangePicker =
            MaterialDatePicker
                .Builder.dateRangePicker()
                .setTitleText("Select Date")
                .build()

        dateRangePicker.show(
            activity?.supportFragmentManager!!,
            "date_range_picker"
        )

        dateRangePicker.addOnPositiveButtonClickListener { dateSelected ->

            val startDate = dateSelected.first
            val endDate = dateSelected.second
            val bundle=Bundle().apply {
                putString("startDate",convertLongToTime(startDate))
                putString("endDate",convertLongToTime(endDate))
                putString("country",selectedCountry)
            }
            val traveladdFragment=TraveladdFragment().apply {
                arguments=bundle
            }
            parentFragmentManager.beginTransaction().apply {
                add(R.id.frag_container,traveladdFragment)
                hide(this@DatePickDialogFragment)
                show(traveladdFragment)
                addToBackStack(null)
                commit()
            }

        }

    }


    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat(
            "yyyy.MM.dd",
            Locale.getDefault())
        return format.format(date)
    }

}