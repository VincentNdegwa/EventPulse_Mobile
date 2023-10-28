package com.example.eventpulse.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventpulse.Data.tickets.TicketsData
import com.example.eventpulse.Data.tickets.UserTickets
import com.example.eventpulse.Modules.Variables
import com.example.eventpulse.R

class TicketsAdapter(var Data:List<TicketsData>, var context: Context):RecyclerView.Adapter<TicketsAdapter.ViewHolder>() {
    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        var image = itemView.findViewById<ImageView>(R.id.image_ticket)
        var date = itemView.findViewById<TextView>(R.id.ticket_event_date)
        var title = itemView.findViewById<TextView>(R.id.ticket_event_name)
        var status = itemView.findViewById<TextView>(R.id.ticket_status)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var card = LayoutInflater.from(context).inflate(R.layout.card_ticktet, parent, false)
        return ViewHolder(card)
    }

    override fun getItemCount(): Int {
        return  Data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.text = Data[position].event.event_date
        holder.title.text = Data[position].event.title
        holder.status.text = Data[position].status
        Glide.with(context).load(Variables().Url+Data[position].event.event_image).into(holder.image)

    }
}