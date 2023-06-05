package com.example.travelday_2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.travelday_2.databinding.FragmentTravellistParentBinding


class TravelListParentFragment : Fragment() {

    lateinit var binding:FragmentTravellistParentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTravellistParentBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
    }



    @SuppressLint("SuspiciousIndentation")
    fun initLayout() {
        val travelAdd = TraveladdFragment()
            childFragmentManager.beginTransaction()
                .replace(R.id.frag_container, travelAdd)
                .commit()
        }



//        private fun initBundle() {
//            val startDate = arguments?.getString("startDate")
//            val endDate = arguments?.getString("endDate")
//            selectedCountry = arguments?.getString("여행국가") ?: ""  // selectedCountry 초기화
//            tripList.add(TravelListItem(selectedCountry, startDate!!, endDate!!))
//            adapter.notifyDataSetChanged()
//        }
//
//        fun onCountrySelected(country: String) {
//            // 국가 데이터 처리
//            // 예시로 콘솔에 출력하는 동작을 수행합니다.
//            Toast.makeText(context, "데이터 전송완료", Toast.LENGTH_SHORT).show()
//        }


        //    private fun showCountryInputDialog() {
//
//        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_country_input, null)
//        val countryEditText: EditText = dialogView.findViewById(R.id.countryEditText)
//
//        val dialogBuilder = AlertDialog.Builder(context)
//            .setView(dialogView)
//            .setTitle("여행할 국가")
//            .setPositiveButton("저장", null)
//            .setNegativeButton("취소") { dialog, _ ->
//                dialog.dismiss()
//            }
//
//        val dialog = dialogBuilder.create()
//        dialog.show()
//
//        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
//            val countryName = countryEditText.text.toString().trim()
//            if (countryName.isNotEmpty()) {
//                selectedCountry=countryName
//                dialog.dismiss()
//                val bundle = Bundle().apply{
//                    putString("여행국가",selectedCountry)
//                }
//                val newFragment=DatePickDialogFragment().apply{
//                    arguments=bundle
//                }
//                parentFragmentManager.beginTransaction()
//                    .replace(R.id.viewPager2,newFragment).commit()
//            } else {
//                countryEditText.error = "국가 이름을 입력하세요"
//            }
//        }
//    }



    }
