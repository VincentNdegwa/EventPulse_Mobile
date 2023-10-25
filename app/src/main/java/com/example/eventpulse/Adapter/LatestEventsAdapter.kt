package com.example.eventpulse.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventpulse.Data.homeData.LatestEvent
import com.example.eventpulse.Modules.Redirect
import com.example.eventpulse.R
import com.example.eventpulse.Modules.Variables
import com.google.gson.Gson

class LatestEventsAdapter(private var data:List<LatestEvent>,private var context: Context): RecyclerView.Adapter<LatestEventsAdapter.ViewHolder>() {
    class  ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var event_image = itemView.findViewById<ImageView>(R.id.event_image)
        var event_title = itemView.findViewById<TextView>(R.id.event_title)
        var event_description = itemView.findViewById<TextView>(R.id.event_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var card = LayoutInflater.from(context).inflate(R.layout.latest_event_card, parent, false)
        return  ViewHolder(card)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.event_title.text = data[position].title
        holder.event_description.text = data[position].description
        Glide.with(context).load(Variables().Url+data[position].event_image).into(holder.event_image)

        holder.itemView.setOnClickListener{
            var dataString = Gson().toJson(data[position])
            Redirect(context).openEventView(dataString)
        }
    }
    override fun getItemCount(): Int {
        return this.data.size
    }

}