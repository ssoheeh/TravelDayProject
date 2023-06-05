package com.example.travelday_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.travelday_2.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var tabLayout:TabLayout
    lateinit var viewPager:ViewPager2
    private val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTabLayout()

    }






    private fun setTabLayout() {
        val tabCount=3
        val viewPagerAdapter=ViewPagerAdapter(supportFragmentManager, lifecycle, tabCount)
        binding.viewPager2.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout,binding.viewPager2){tab, position->
            when(position){
                0-> {
                    tab.text = "준비물"
                    tab.setIcon(R.drawable.checklist)
                }
                1-> {
                    tab.text = "일정"
                    tab.setIcon(R.drawable.timeline)
                }
                2->{
                    tab.text="여행기"
                    tab.setIcon(R.drawable.diary)
                }

        }
    }.attach()
}}