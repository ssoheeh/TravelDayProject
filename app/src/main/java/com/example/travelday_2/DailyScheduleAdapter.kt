package com.example.travelday_2

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelday_2.databinding.DailyListRowBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DailyScheduleAdapter(var dailyList:ArrayList<SharedViewModel.DailySchedule>) : RecyclerView.Adapter<DailyScheduleAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data:SharedViewModel.DailySchedule)
    }
    var itemClickListener:OnItemClickListener?=null
    inner class ViewHolder(val binding: DailyListRowBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.scheduleTextView.setOnClickListener{
                itemClickListener?.OnItemClick(dailyList[adapterPosition])

            }

        }

    }
    fun moveItem(oldPos:Int, newPos:Int){
        val item = dailyList[oldPos]
        dailyList.removeAt(oldPos)
        dailyList.add(newPos,item)
        notifyItemMoved(oldPos, newPos)
    }
    fun removeItem(pos:Int){
        dailyList.removeAt(pos)
        notifyItemRemoved(pos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DailyListRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: DailyScheduleAdapter.ViewHolder, position: Int) {
        val item = dailyList[position]
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault()) //00:00 방식으로 시간표기
        val time = timeFormat.format(Date().apply { hours = item.hour; minutes = item.minute })
        holder.binding.timeTextView.text = time
        holder.binding.scheduleTextView.text = item.task
    }

    override fun getItemCount(): Int {
        return dailyList.size
    }
}

