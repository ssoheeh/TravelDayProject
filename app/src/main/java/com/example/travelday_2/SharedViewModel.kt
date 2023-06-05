package com.example.travelday_2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class SharedViewModel : ViewModel() {
    // 중첩 클래스로 데이터 구조 정의
    data class Country(val name: String) : Serializable {
        val dateList = arrayListOf<Date>()
        var dDay:String = ""

    }

    data class Date(val date: String): Serializable {
        val dailyScheduleList = arrayListOf<DailySchedule>()
    }

    data class DailySchedule(val hour: Int, val minute: Int,val task: String): Serializable

    // 데이터 저장
    val _countryList = MutableLiveData<ArrayList<Country>>()
    val countryList: LiveData<ArrayList<Country>>
        get() = _countryList

    init {
        _countryList.value = arrayListOf()
    }

    // 데이터 추가 메서드
    fun addCountry(name: String) {
        val updatedList = countryList.value ?: arrayListOf()
        updatedList.add(Country(name))
        _countryList.value = updatedList
    }

    fun addDate(countryIndex: Int, date: String) {
        val updatedList = countryList.value ?: arrayListOf()
        if (countryIndex in 0 until updatedList.size) {
            val country = updatedList[countryIndex]
            country.dateList.add(Date(date))
        }
        _countryList.value = updatedList
    }

    fun addDailySchedule(countryIndex: Int, dateIndex: Int, hour: Int, minute: Int, task: String) {
        val updatedList = countryList.value ?: arrayListOf()
        if (countryIndex in 0 until updatedList.size) {
            val country = updatedList[countryIndex]
            if (dateIndex in 0 until country.dateList.size) {
                val date = country.dateList[dateIndex]

                // Find the insertion index based on the hour and minute values
                var insertionIndex = 0
                for (index in 0 until date.dailyScheduleList.size) {
                    val schedule = date.dailyScheduleList[index]
                    if (hour < schedule.hour || (hour == schedule.hour && minute < schedule.minute)) {
                        insertionIndex = index
                        break
                    }
                    insertionIndex = index + 1
                }

                // Insert the new DailySchedule object at the insertion index
                date.dailyScheduleList.add(insertionIndex, DailySchedule(hour, minute, task))
            }
        }
        _countryList.value = updatedList
    }
    fun addDDay(countryIndex: Int, dDay: String) {
        val updatedList = countryList.value ?: arrayListOf()
        if (countryIndex in 0 until updatedList.size) {
            val country = updatedList[countryIndex]
            country.dDay = dDay
        }
        _countryList.value = updatedList
    }


}



