package com.example.eventpulse.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventpulse.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private lateinit var bind: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.loginButton.setOnClickListener { this.navigateToDash() }
        bind.createAcc.setOnClickListener {
            this.navigateCreateAcc()
        }
    }

    private fun navigateToDash(){
        var dashIntent = Intent(this, Home::class.java)
        startActivity(dashIntent)
    }
    private  fun navigateCreateAcc(){
        var createAccIntent = Intent(this, Register::class.java)
        startActivity((createAccIntent))
    }
}