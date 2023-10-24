package com.example.eventpulse.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventpulse.Data.login.UserLogin
import com.example.eventpulse.R
import com.example.eventpulse.databinding.ActivityHomeBinding
import com.google.gson.Gson

class Home : AppCompatActivity() {
    var gson = Gson()
    var loading:Boolean = true
    private lateinit var bind:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loading = true
        var data = intent.getStringExtra("putExtra")
        var userData = gson.toJson(data, UserLogin::class.java)
//        if (loading == true){
//            setContentView(R.layout.main_loading)
//        }else{
            bind = ActivityHomeBinding.inflate(layoutInflater)
            setContentView(bind.root)
//        }

    }
}