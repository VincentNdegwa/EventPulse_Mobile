package com.example.eventpulse.Activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventpulse.Adapter.TicketsAdapter
import com.example.eventpulse.Data.login.UserLogin
import com.example.eventpulse.Data.tickets.TicketsData
import com.example.eventpulse.Data.tickets.UserTickets
import com.example.eventpulse.R
import com.example.eventpulse.Request.DataRequest
import com.example.eventpulse.databinding.ActivityTicketsBinding
import com.google.gson.Gson
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class TicketsAct : AppCompatActivity() {
    private lateinit var bind:ActivityTicketsBinding
    private lateinit var message: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityTicketsBinding.inflate(layoutInflater)
        setContentView(bind.root)
        this.requestData()
    }

    private fun requestData() {
        var params = HashMap<String, String>()
        var userData = getSharedPreferences("user", Context.MODE_PRIVATE).getString("userData", null)
        if (userData != null){
            var jsonUserData = Gson().fromJson(userData, UserLogin::class.java)
            params["user_id"] = jsonUserData.data.id.toString()

            DataRequest(this).post(params,"api/applicant/tickets", onSuccess = {
                    data->
                if (!data.isNullOrEmpty()){
                   var dataRes= Gson().fromJson(data,UserTickets::class.java)
                    if (!dataRes.error){
                        this.renderData(dataRes.data)
                    }else{
                        message = dataRes.message
                        this.renderMessage()
                    }
                }
            }, onError = {
                    error->
                message = error
                this.renderMessage()
            })
        }
        this.eventListeners()

    }

    private fun eventListeners() {
        bind.hamburgerButton.setOnClickListener{
            var drawer = findViewById<DrawerLayout>(R.id.home_nav_drawer)
            drawer.openDrawer(GravityCompat.START)
        }
    }

    private fun renderMessage() {
        bind.infoText.text = message
        bind.infoText.visibility = View.VISIBLE
    }

    private fun renderData(data: List<TicketsData>) {
        if (data.size>0){
            var adapter = TicketsAdapter(data, this)
            var recyclerview = findViewById<RecyclerView>(R.id.tickets_recyclerview)
            recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recyclerview.adapter = adapter
        }else{
            bind.infoText.text = message
            bind.infoText.visibility = View.VISIBLE
        }
    }
}