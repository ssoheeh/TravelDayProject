package com.example.travelday_2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.travelday_2.databinding.FragmentDailyAddBinding
import java.util.*

class DailyAddFragment : Fragment() {
    lateinit var binding: FragmentDailyAddBinding
    val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDailyAddBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLayout()

    }



    private fun initLayout() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.fragment_daily_add, null)

            val taskEditText = dialogView.findViewById<EditText>(R.id.taskEditText)

            builder.setView(dialogView)

            builder.setPositiveButton("OK") { _, _ ->
                val msg = "$selectedHour:${String.format("%02d", selectedMinute)} ${taskEditText.text} 추가되었습니다"
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                val task = taskEditText.text.toString()
                val selectedCountry = arguments?.getSerializable("클릭된 국가") as SharedViewModel.Country
                val selectedDate = arguments?.getSerializable("클릭된 날짜") as SharedViewModel.Date
                val countryList = sharedViewModel.countryList.value
                val countryIndex = countryList?.indexOf(selectedCountry)
                val dateIndex = selectedCountry.dateList.indexOf(selectedDate)
                if (countryIndex != null && dateIndex != -1) {
                    sharedViewModel.addDailySchedule(countryIndex, dateIndex, selectedHour, selectedMinute, task)
                }
                parentFragmentManager.popBackStack()
            }

            builder.setNegativeButton("Cancel", null)
            builder.show()
        }

        val timePickerDialog = TimePickerDialog(context, timeSetListener, hour, minute, true)
        timePickerDialog.show()
    }


}




