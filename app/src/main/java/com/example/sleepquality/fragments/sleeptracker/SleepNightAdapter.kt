package com.example.sleepquality.fragments.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sleepquality.R
import com.example.sleepquality.database.SleepNight
import com.example.sleepquality.convertDurationToFormatted
import com.example.sleepquality.convertNumericQualityToString

class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {
    var data = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sleepLength: TextView = itemView.findViewById(R.id.sleep_length)
        val quality: TextView = itemView.findViewById(R.id.quality_string)
        val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.sleepQuality.toString()
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
        return TextItemViewHolder(view)
    }

    override fun getItemCount() = data.size

    fun bind(item: SleepNight) {
        val res = itemView.context.resources
        sleepLength.text = convertDurationToFormatted(
            item.startTime, item.endTime, res)
        quality.text = convertNumericQualityToString(
            item.sleepQuality, res)
        qualityImage.setImageResource(when (item.sleepQuality) {
            0 -> R.drawable.ic_sleep_0
            1 -> R.drawable.ic_sleep_1
            2 -> R.drawable.ic_sleep_2
            3 -> R.drawable.ic_sleep_3
            4 -> R.drawable.ic_sleep_4
            5 -> R.drawable.ic_sleep_5
            else -> R.drawable.ic_sleep_active
        })
    }

}

