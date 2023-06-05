package com.example.travelday_2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelday.TravelListAdapter
import com.example.travelday_2.databinding.FragmentTraveladdBinding
import java.text.SimpleDateFormat
import java.util.*


class TraveladdFragment : Fragment() {
    lateinit var binding:FragmentTraveladdBinding
    lateinit var adapter: TravelListAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val currentDate: Date = Date()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentTraveladdBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initButton()
        setBundle()
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


    private fun calculateDday(date: Date): Int {
        val currentDate = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) }.time
        val diff = date.time - currentDate.time
        return (diff / (24 * 60 * 60 * 1000)).toInt()
    }
    private fun setBundle() {
        val startDate = arguments?.getString("startDate")

        val endDate = arguments?.getString("endDate")
        val selectedCountry = arguments?.getString("country")

        if (selectedCountry != null) {
            Log.i("체크",selectedCountry)
        }
        if (selectedCountry != null && startDate != null && endDate != null) {
            sharedViewModel.addCountry(selectedCountry)

            val selectedCountryIndex = sharedViewModel.countryList.value?.indexOfFirst { it.name == selectedCountry } ?: -1
            val startDateObj = convertStringToDate(startDate)
            val endDateObj = convertStringToDate(endDate)
            getDatesBetween(startDateObj, endDateObj, selectedCountryIndex)
            val dDay= calculateDday(startDateObj).toString()
            sharedViewModel.addDDay(selectedCountryIndex,dDay)
        }
        adapter.notifyDataSetChanged()
        }



    private fun initButton() {
        binding.addTripButton.setOnClickListener {
            val CountryFragment=CountryFragment()
            parentFragmentManager.beginTransaction().apply{
                replace(R.id.frag_container,CountryFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun initRecyclerView() {
        adapter = TravelListAdapter(sharedViewModel.countryList.value ?: arrayListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerView.adapter = adapter
        adapter.itemClickListener = object : TravelListAdapter.OnItemClickListener {
            override fun OnItemClick(data: SharedViewModel.Country) {
                val selectedCountry = arguments?.getString("country")
                val selectedCountryIndex = sharedViewModel.countryList.value?.indexOfFirst { it.name == selectedCountry } ?: -1
                val bundle = Bundle().apply {
                    putSerializable("클릭된 국가", sharedViewModel.countryList.value?.get(selectedCountryIndex))
                }
                val dateListFragment = DateListFragment().apply {
                    arguments = bundle
                }
                parentFragmentManager.beginTransaction().apply {

                    add(R.id.frag_container, dateListFragment)
                    hide(this@TraveladdFragment)
                    addToBackStack(null)
                    commit()
                }
            }}
        //remove, move 기능 추가
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
            itemTouchHelper.attachToRecyclerView(binding.recyclerView)


            }


    private fun convertStringToDate(dateString: String): Date {
        val format = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        return format.parse(dateString) ?: Date()
    }

    private fun getDatesBetween(startDate: Date, endDate: Date, selectedCountryIndex:Int) {
        val dates = mutableListOf<Date>()
        val calendar = Calendar.getInstance()
        calendar.time = startDate

        while (calendar.time.before(endDate) || calendar.time.equals(endDate)) {
            val result = calendar.time
            val format = SimpleDateFormat("MM.dd", Locale.getDefault())
            val formattedDate = format.format(result)
            sharedViewModel.addDate(selectedCountryIndex, formattedDate)
            calendar.add(Calendar.DATE, 1)
        }


    }
}