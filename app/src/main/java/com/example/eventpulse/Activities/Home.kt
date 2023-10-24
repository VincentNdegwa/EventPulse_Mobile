package com.example.eventpulse.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventpulse.Adapter.LatestEventsAdapter
import com.example.eventpulse.Adapter.RecomendendEventsAdapter
import com.example.eventpulse.Adapter.TrendingEventsAdapter
import com.example.eventpulse.Adapter.UpComingEventsAdapter
import com.example.eventpulse.Data.homeData.DashData
import com.example.eventpulse.Data.login.UserLogin
import com.example.eventpulse.R
import com.example.eventpulse.Request.DataRequest
import com.example.eventpulse.databinding.ActivityHomeBinding
import com.google.gson.Gson

class Home : AppCompatActivity() {
    var gson = Gson()
    var loading:Boolean = true
    private lateinit var bind:ActivityHomeBinding
    private lateinit var data:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityHomeBinding.inflate(layoutInflater)
        loading = true
        data = intent.getStringExtra("userData").toString()
        if (loading == true && !data.isNullOrEmpty()){
            this.renderData()
        }else{
            setContentView(R.layout.activity_login)
            Toast.makeText(this,"An Error Occurred", Toast.LENGTH_SHORT).show()
        }

    }

    private fun renderData(){
        var userData = gson.fromJson(data, UserLogin::class.java)
        Toast.makeText(this, "You are logged in as ${userData.data.email}", Toast.LENGTH_SHORT).show()
        setContentView(R.layout.main_loading)
        var params = HashMap<String, String>()
        params["user_id"]= userData.data.id.toString()
        DataRequest(this).post(params, "api/retrieve", onSuccess = {data->
            var resData = gson.fromJson(data, DashData::class.java)
            if (!resData.error){
                setContentView(bind.root)
                this.renderLatestEvents(resData)
                this.renderRecommenenData(resData)
                this.renderUpcoming(resData)
                this.renderTrending(resData)
            }else{
                setContentView(R.layout.activity_login)
                Toast.makeText(this,"An Error Occurred", Toast.LENGTH_SHORT).show()
            }
        }, onError = {err-> println(err) })
    }
    private fun renderLatestEvents(resData: DashData) {
        var adapter = LatestEventsAdapter(resData.latestEvents, this)
        var recyclerView = findViewById<RecyclerView>(R.id.latest_events_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter = adapter
    }
    private fun renderRecommenenData(resData: DashData) {
        val adapter = RecomendendEventsAdapter(this, resData.recomendedEvents)
        val recyclerView = findViewById<RecyclerView>(R.id.recommended_events_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

    }
    private fun renderUpcoming(resData: DashData){
        if (resData.upCommingEvents.isEmpty()){
            bind.invisibleHolder.visibility = View.VISIBLE
        }else{
            val adapter = UpComingEventsAdapter(this, resData.upCommingEvents)
            val recyclerView = findViewById<RecyclerView>(R.id.upcoming_events_recycler)
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapter
        }

    }

    private fun renderTrending(resData: DashData){
        val adapter = TrendingEventsAdapter(this, resData.trendingEvents)
        val recyclerView = findViewById<RecyclerView>(R.id.trending_events_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        Log.d("Adapter", "Item count: ${adapter.itemCount}")
    }

}