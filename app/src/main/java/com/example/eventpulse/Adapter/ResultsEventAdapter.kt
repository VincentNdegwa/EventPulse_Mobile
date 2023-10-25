package com.example.eventpulse.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventpulse.Data.search.SearchEvents
import com.example.eventpulse.Modules.Redirect
import com.example.eventpulse.R
import com.example.eventpulse.Modules.Variables
import com.google.gson.Gson

class ResultsEventAdapter(var context: Context, var data: List<SearchEvents>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    class ViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){
        var event_image = itemView.findViewById<ImageView>(R.id.event_results_image)
        var event_title = itemView.findViewById<TextView>(R.id.event_title)
        var event_desc = itemView.findViewById<TextView>(R.id.event_description)
        var event_data = itemView.findViewById<TextView>(R.id.event_date)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var card = LayoutInflater.from(context).inflate(R.layout.card_results_events, parent, false)
        return  ViewHolder(card)
    }

    override fun getItemCount(): Int {
        return  data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder){
            holder.event_data.text = data[position].event_date
            holder.event_title.text = data[position].title
            holder.event_desc.text = data[position].description
            Glide.with(context).load(Variables().Url+data[position].event_image).into(holder.event_image)

            holder.itemView.setOnClickListener{
                var dataString = Gson().toJson(data[position])
                Redirect(context).openEventView(dataString)
            }
        }
    }

}