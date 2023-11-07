package com.example.eventpulse.Activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.eventpulse.Data.login.UserLogin
import com.example.eventpulse.Data.viewData.ViewData
import com.example.eventpulse.R
import com.example.eventpulse.Request.DataRequest
import com.example.eventpulse.databinding.ActivityEventApplicationBinding
import com.google.gson.Gson

class EventApplication : AppCompatActivity() {
    private lateinit var bind:ActivityEventApplicationBinding
    private lateinit var event_id:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityEventApplicationBinding.inflate(layoutInflater)
        var data = intent.getStringExtra("data")
        setContentView(bind.root)

        this.getData(data)

    }

    private fun getData(data: String?) {
        if (!data.isNullOrEmpty()){
            var eventData = Gson().fromJson(data, ViewData::class.java)
            event_id = eventData.id.toString()
            this.eventListeners()
        }else{
            Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            super.getOnBackPressedDispatcher().onBackPressed()
        }
    }

    private fun eventListeners() {
        bind.backButton.setOnClickListener{
            super.getOnBackPressedDispatcher().onBackPressed()
        }
        bind.applicationButton.setOnClickListener{
            this.startApplication()
        }
    }

    private fun startApplication() {
        var params = HashMap<String, String>()

        var event_agenda = findViewById<EditText>(R.id.reason).text.toString()
        var expectation = findViewById<EditText>(R.id.expectations).text.toString()
        var similar_event = findViewById<EditText>(R.id.similar_event).text.toString()
        var learning_objective = findViewById<EditText>(R.id.objectives).text.toString()
        var suggestions = findViewById<EditText>(R.id.suggestions).text.toString()

        params["event_agenda"] = event_agenda
        params["expectation"] = expectation
        params["similar_event"] = similar_event
        params["learning_objective"] = learning_objective
        params["suggestions"] = suggestions


        var sharedref = getSharedPreferences("user", Context.MODE_PRIVATE).getString("userData", null)
        if (!sharedref.isNullOrEmpty()){
           var userData = Gson().fromJson(sharedref, UserLogin::class.java)
            var id = userData.data.id.toString()
            params["event_id"] =event_id
            params["user_id"] = id

            if (this.validate(params)){
                Log.d("params", params.toString())
                DataRequest(this).post(params,"api/event/apply",
                    onSuccess = {
                            data->print(data)
                    },
                    onError = {
                        err->print(err)
                    })
            }else{
                Toast.makeText(this, "Please fill the required fields", Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(this, "Failed to get user details, Logout then you Login back", Toast.LENGTH_SHORT).show()
        }

    }

    private fun validate(params: HashMap<String, String>): Boolean {
        return true
    }
}