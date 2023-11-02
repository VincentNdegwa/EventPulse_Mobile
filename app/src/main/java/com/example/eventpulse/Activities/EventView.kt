package com.example.eventpulse.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventpulse.Adapter.HostAdapter
import com.example.eventpulse.Data.viewData.ViewData
import com.example.eventpulse.Modules.Variables
import com.example.eventpulse.R
import com.example.eventpulse.databinding.ActivityEventViewBinding
import com.google.gson.Gson

class EventView : AppCompatActivity() {
    private lateinit var Data:ViewData
    private lateinit var bind:ActivityEventViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityEventViewBinding.inflate(layoutInflater)
        var data = intent.getStringExtra("event")
        Data = Gson().fromJson(data, ViewData::class.java)
        println(Data)
        setContentView(bind.root)
        this.renderData()
        this.eventLister()
    }

    private fun eventLister() {
        bind.navbackButton.setOnClickListener{
            this.onBackPressed()
        }
    }

    fun renderData(){
        Glide.with(this).load(Variables().Url+Data.event_image).into(bind.selectedEventImage)
        bind.viewEventDate.text = Data.event_date
        bind.viewEventDescription.text = Data.description
        bind.viewEventName.text = Data.title

        var adapter = HostAdapter(Data.hosts,this)
        var recyclerview = findViewById<RecyclerView>(R.id.host_recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recyclerview.adapter = adapter
    }
}