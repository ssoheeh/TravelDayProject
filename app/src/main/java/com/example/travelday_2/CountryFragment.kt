package com.example.travelday_2

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import java.util.Date


class CountryFragment : DialogFragment() {
    private lateinit var countryEditText: EditText
    lateinit var selectedCountry: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showCountryInputDialog()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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



    private fun showCountryInputDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.fragment_country, null)
        countryEditText = dialogView.findViewById(R.id.countryEditText)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("여행할 국가")
            .setPositiveButton("저장", null)
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val countryName = countryEditText.text.toString().trim()
            if (countryName.isNotEmpty()) {
                selectedCountry = countryName
                val bundle = Bundle()
                bundle.putString("country", selectedCountry)
                val datePickDialogFragment=DatePickDialogFragment()
                datePickDialogFragment.arguments = bundle
                parentFragmentManager.beginTransaction().apply {
                    add(R.id.frag_container,datePickDialogFragment)
                    hide(this@CountryFragment)
                    addToBackStack(null)
                    commit()
                }
                dialog.dismiss()


            } else {
                countryEditText.error = "국가 이름을 입력하세요"
            }
        }
    }
}
