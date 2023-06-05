package com.example.travelday

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.travelday_2.SharedViewModel
import com.example.travelday_2.databinding.TravelListRowBinding


class TravelListAdapter(val items: ArrayList<SharedViewModel.Country>) :RecyclerView.Adapter<TravelListAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data: SharedViewModel.Country)
    }
    var itemClickListener:OnItemClickListener?=null
    inner class ViewHolder(val binding: TravelListRowBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.textView.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition])
            }

        }
    }
    fun moveItem(oldPos:Int, newPos:Int){
        val item = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos,item)
        notifyItemMoved(oldPos, newPos)
    }
    fun removeItem(pos:Int){
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = TravelListRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val startDate = item.dateList.firstOrNull()?.date
        val endDate = item.dateList.lastOrNull()?.date
        val travelPeriod = if (startDate != null && endDate != null) {
            "$startDate ~ $endDate"
        } else {
            ""
        }
        holder.binding.textView.text = item.name +"\n" +travelPeriod
        holder.binding.dDay.text="D-"+item.dDay
    }



}