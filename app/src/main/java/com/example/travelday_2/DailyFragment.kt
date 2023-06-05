package com.example.travelday_2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelday_2.databinding.FragmentDailyBinding
import java.util.ArrayList
import kotlin.properties.ReadWriteProperty


class DailyFragment : Fragment() {
    lateinit var binding:FragmentDailyBinding
    var dailyList: ArrayList<DailyItem> = ArrayList()
    lateinit var adapter:DailyScheduleAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDailyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        sharedViewModel.countryList.observe(viewLifecycleOwner) { countryList ->
            updateRecyclerView()
        }
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

    private fun updateRecyclerView() {
        adapter.notifyDataSetChanged()
    }


    private fun initRecyclerView() {
        var selectedCountry= arguments?.getSerializable("클릭된 국가") as SharedViewModel.Country
        var selectedDate= arguments?.getSerializable("클릭된 날짜") as SharedViewModel.Date
        val countryList = sharedViewModel.countryList.value
        val countryIndex = countryList?.indexOf(selectedCountry)
        val dateIndex = countryIndex?.let { selectedCountry.dateList.indexOf(selectedDate) }

        if (countryIndex != null && dateIndex != null && countryIndex >= 0 && dateIndex >= 0) {
            val dailyScheduleList = countryList[countryIndex].dateList[dateIndex].dailyScheduleList

            adapter = DailyScheduleAdapter(dailyScheduleList)
            binding.scheduleRecyclerView.adapter = adapter
            binding.scheduleRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        //제목 바 설정

        binding.dateTextView.text=selectedDate.date
        //move 와 remove 기능 추가
        val simpleCallback=object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition)
            }

        }
        val itemTouchHelper= ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.scheduleRecyclerView)


        binding.addButton.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("클릭된 국가", selectedCountry)
                putSerializable("클릭된 날짜", selectedDate)
            }
            val dailyAddFragment=DailyAddFragment().apply {
                arguments=bundle
            }
            parentFragmentManager.beginTransaction().apply{
                add(R.id.frag_container, dailyAddFragment)
                hide(this@DailyFragment)
                addToBackStack(null)
                commit()
            }
        }

    }

}


