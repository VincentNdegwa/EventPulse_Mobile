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
import com.example.eventpulse.Data.viewData.Host
import com.example.eventpulse.Data.viewData.ViewData
import com.example.eventpulse.Modules.Redirect
import com.example.eventpulse.Modules.Variables
import com.example.eventpulse.R
import com.google.gson.Gson

class HostAdapter(private var data:List<Host>, private var context: Context): RecyclerView.Adapter<HostAdapter.ViewHolder>() {
    class  ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var host_image = itemView.findViewById<ImageView>(R.id.host_image)
        var host_name = itemView.findViewById<TextView>(R.id.host_name)
        var host_occupation = itemView.findViewById<TextView>(R.id.host_occupation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var card = LayoutInflater.from(context).inflate(R.layout.host_card,parent,false)
        return ViewHolder(card)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ViewHolder){
            Glide.with(context).load(Variables().Url+data[position].profile_image).into(holder.host_image)
            holder.host_name.text = data[position].host_name
            holder.host_occupation.text = data[position].host_occupation
        }
    }
    override fun getItemCount(): Int {
        return this.data.size
    }

}