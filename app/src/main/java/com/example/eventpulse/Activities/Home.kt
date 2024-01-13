package com.example.eventpulse.Activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventpulse.Adapter.LatestEventsAdapter
import com.example.eventpulse.Adapter.RecomendendEventsAdapter
import com.example.eventpulse.Adapter.TrendingEventsAdapter
import com.example.eventpulse.Adapter.UpComingEventsAdapter
import com.example.eventpulse.Data.homeData.DashData
import com.example.eventpulse.Data.login.UserLogin
import com.example.eventpulse.Data.search.searchData
import com.example.eventpulse.Fragments.Events
import com.example.eventpulse.Fragments.HomeEvents
import com.example.eventpulse.R
import com.example.eventpulse.Request.DataRequest
import com.example.eventpulse.databinding.ActivityHomeBinding
import com.example.eventpulse.Modules.Variables
import com.google.gson.Gson

class Home : AppCompatActivity() {
    var gson = Gson()
    var loading:Boolean = true
    private lateinit var bind:ActivityHomeBinding
//    private lateinit var data:String
    private lateinit var userData:UserLogin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityHomeBinding.inflate(layoutInflater)
        loading = true
        setContentView(bind.root)
        var data = getSharedPreferences("user", Context.MODE_PRIVATE).getString("userData",null)
        if (loading == true && data != null){
            userData =  gson.fromJson(data, UserLogin::class.java)
            this.eventListeners()
            this.renderData()
            this.navigate()
        }else{
            setContentView(R.layout.activity_login)
            Toast.makeText(this,"An Error Occurred", Toast.LENGTH_SHORT).show()
        }


    }

    private fun navigate() {
        var trans = this.supportFragmentManager.beginTransaction()
        trans.replace(R.id.home_frame, HomeEvents())
        trans.commit()
    }

    override fun onBackPressed() {
        finishAffinity()
    }
    private fun renderData() {
        var userProfile =   bind.topFragment.findViewById<ImageView>(R.id.user_profile_image)
        if (!userData.data.profile.profile_image.isNullOrEmpty()){
            Glide.with(this)
                .load(Variables().Url+userData.data.profile.profile_image)
                .into(userProfile)
        }else{
            Glide.with(this).load(Variables().ImageUrl).into(userProfile)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        getSharedPreferences("user",Context.MODE_PRIVATE).edit().remove("userData").apply()
    }

    private fun eventListeners(){
        var menuButton = bind.topFragment.findViewById<ImageView>(R.id.hamburger_button)
        menuButton.setOnClickListener{
            var drawer = findViewById<DrawerLayout>(R.id.home_nav_drawer)
            drawer.openDrawer(GravityCompat.START)
        }
    }
}