package com.example.travelday_2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travelday_2.databinding.DateListRowBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DateListAdapter(val items: ArrayList<SharedViewModel.Date>) :
    RecyclerView.Adapter<DateListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(data: SharedViewModel.Date)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: DateListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.dateTextView.setOnClickListener {
                itemClickListener?.onItemClick(items[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DateListRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val dayNumber=position+1
        holder.binding.dateTextView.text = "Day $dayNumber  " + item.date




    }
}

