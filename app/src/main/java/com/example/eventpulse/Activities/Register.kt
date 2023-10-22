package com.example.eventpulse.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventpulse.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {
    private lateinit var bind:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.login.setOnClickListener{
            this.navigateToLogin()
        }
        bind.signupButton.setOnClickListener{
            this.navigateToDash()
        }
    }

    private fun navigateToLogin(){
        var loginIntent = Intent(this, Login::class.java)
        startActivity(loginIntent)
    }

    private fun navigateToDash(){
        var dashIntent = Intent(this, Home::class.java)
        startActivity(dashIntent)
    }
}