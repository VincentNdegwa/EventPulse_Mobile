package com.example.eventpulse.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventpulse.R

class MainActivity : AppCompatActivity() {
//    private lateinit var bind:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    if (2==2){
        var logingINtent = Intent(this, Login::class.java)
        startActivity(logingINtent)
    }else{
        setContentView(R.layout.main_loading)
    }
    }
}