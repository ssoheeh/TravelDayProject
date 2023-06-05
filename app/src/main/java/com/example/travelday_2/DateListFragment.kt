package com.example.travelday_2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.travelday_2.databinding.FragmentDateListBinding
import org.json.JSONObject
import java.util.*


class DateListFragment : Fragment() {
    val country = arguments?.getSerializable("클릭된 국가") as SharedViewModel.Country

    lateinit var binding:FragmentDateListBinding
    var city = country.name
    lateinit var adapter: DateListAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentDateListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initBackStack()
        init()
    }

    private fun init() {
        binding.weatherLayout.setOnClickListener {
            CurrentCall()
        }
    }

    private fun CurrentCall() {
        val requestQueue = Volley.newRequestQueue(requireContext())
        val url = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+"d74c3bbee7a3c497383271ff0d494542"

        val stringRequest = StringRequest(
            Request.Method.GET,url,
            Response.Listener<String>{
                    response ->
                val jsonObject = JSONObject(response)

                val weatherJson = jsonObject.getJSONArray("weather")
                val weatherObj = weatherJson.getJSONObject(0)

                var weather = weatherObj.getString("description")
                val tempK = JSONObject(jsonObject.getString("main"))
                val tempDo = (Math.round((tempK.getDouble("temp")-273.15)*100)/100.0)
                weather = weather + tempDo + "°C"



            },
            Response.ErrorListener {

            })

        requestQueue.add(stringRequest)
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




    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    private fun initRecyclerView() {

        if (country != null) {
                adapter = DateListAdapter(country.dateList)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL,false)}

                adapter.itemClickListener=object:DateListAdapter.OnItemClickListener{
                    override fun onItemClick(data: SharedViewModel.Date) {

                        val bundle = Bundle().apply {
                            putSerializable("클릭된 국가", country)
                            putSerializable("클릭된 날짜", data)
                        }
                        val dailyFragment=DailyFragment().apply {
                            arguments=bundle
                        }
                        parentFragmentManager.beginTransaction().apply {
                            add(R.id.frag_container, dailyFragment)
                            hide(this@DateListFragment)
                            addToBackStack(null)
                            commit()
                        }
                    }
                }
        //국가이름, 날짜 데이터 및 디데이 표시
        val startDate = country.dateList.firstOrNull()?.date
        val endDate = country.dateList.lastOrNull()?.date
        val travelPeriod = if (startDate != null && endDate != null) {
            "$startDate ~ $endDate"
        } else {
            ""
        }
        binding.travelData.text=country.name +"\n " +travelPeriod
        binding.dDayDateList.text="D-"+country.dDay

        }

}

