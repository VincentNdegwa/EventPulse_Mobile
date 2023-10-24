package com.example.eventpulse.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventpulse.Data.homeData.upCommingEvents
import com.example.eventpulse.R
import com.example.eventpulse.variables.Variables

class UpComingEventsAdapter( var context:Context, var data: List<upCommingEvents>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var event_image = itemView.findViewById<ImageView>(R.id.event_image)
        var event_title = itemView.findViewById<TextView>(R.id.event_title)
        var event_description = itemView.findViewById<TextView>(R.id.event_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var card = LayoutInflater.from(context).inflate(R.layout.card_upcoming_events,parent,false)
        return  ViewHolder(card)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder){
            holder.event_title.text = data[position].title
            holder.event_description.text = data[position].description
            Glide.with(context).load(Variables().Url+data[position].event_image).into(holder.event_image)
        }
    }
}