package com.example.eventpulse.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.eventpulse.Adapter.LatestEventsAdapter
import com.example.eventpulse.Data.homeData.DashData
import com.example.eventpulse.Data.login.Data
import com.example.eventpulse.Data.login.Profile
import com.example.eventpulse.Data.login.UserLogin
import com.example.eventpulse.R
import com.example.eventpulse.Request.DataRequest
import com.example.eventpulse.databinding.ActivityHomeBinding
import com.google.gson.Gson

class Home : AppCompatActivity() {
    var gson = Gson()
    var loading:Boolean = true
    private lateinit var bind:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityHomeBinding.inflate(layoutInflater)
        loading = true
        var data = intent.getStringExtra("userData")
        if (loading == true && !data.isNullOrEmpty()){
            var userData = gson.fromJson(data, UserLogin::class.java)
            Toast.makeText(this, "You are logged in as ${userData.data.email}", Toast.LENGTH_SHORT).show()
            setContentView(R.layout.main_loading)
            var params = HashMap<String, String>()
            params["user_id"]= userData.data.id.toString()
            DataRequest(this).post(params, "api/retrieve", onSuccess = {data->
                    var resData = gson.fromJson(data, DashData::class.java)
                    if (!resData.error){
                        var adapter = LatestEventsAdapter(resData.latestEvents, this)
                        setContentView(bind.root)
                        var recyclerView = findViewById<RecyclerView>(R.id.latest_events_recycler)
                        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
                        recyclerView.adapter = adapter
                        println("adapter set")
                    }else{
                        setContentView(R.layout.activity_login)
                        Toast.makeText(this,"An Error Occurred", Toast.LENGTH_SHORT).show()
                    }
            }, onError = {err-> println(err) })
        }else{
            setContentView(R.layout.activity_login)
            Toast.makeText(this,"An Error Occurred", Toast.LENGTH_SHORT).show()
        }

    }
}