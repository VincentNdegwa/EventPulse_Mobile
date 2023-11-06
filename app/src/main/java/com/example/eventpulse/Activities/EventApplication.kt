package com.example.eventpulse.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.eventpulse.R
import com.example.eventpulse.databinding.ActivityEventApplicationBinding

class EventApplication : AppCompatActivity() {
    private lateinit var bind:ActivityEventApplicationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityEventApplicationBinding.inflate(layoutInflater)
        setContentView(bind.root)

        this.eventListeners()
    }

    private fun eventListeners() {
        bind.backButton.setOnClickListener{
            super.onBackPressed()
        }
        bind.applicationButton.setOnClickListener{
            this.startApplication()
        }
    }

    private fun startApplication() {
        if (this.validate()){

        }else{
            Toast.makeText(this, "Please fill the required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validate(): Boolean {
        return true
    }
}