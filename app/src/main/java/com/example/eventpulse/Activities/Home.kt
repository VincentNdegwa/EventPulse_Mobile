package com.example.eventpulse.Activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
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
        var data = getSharedPreferences("user", Context.MODE_PRIVATE).getString("userData",null)
        if (loading == true && data != null){
            userData =  gson.fromJson(data, UserLogin::class.java)
            this.renderData(data)
            this.eventListeners()
        }else{
            setContentView(R.layout.activity_login)
            Toast.makeText(this,"An Error Occurred", Toast.LENGTH_SHORT).show()
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
    private fun renderData(data: String) {
        var userData = gson.fromJson(data, UserLogin::class.java)
        Toast.makeText(this, "You are logged in as ${userData.data.email}", Toast.LENGTH_SHORT).show()
        setContentView(R.layout.main_loading)
        var params = HashMap<String, String>()
        params["user_id"]= userData.data.id.toString()
        DataRequest(this).post(params, "api/retrieve", onSuccess = {data->
            var resData = gson.fromJson(data, DashData::class.java)
            if (!resData.error){
                setContentView(bind.root)
                var userProfile =   bind.topFragment.findViewById<ImageView>(R.id.user_profile_image)
                if (!userData.data.profile.profile_image.isNullOrEmpty()){
                    Glide.with(this)
                        .load(Variables().Url+userData.data.profile.profile_image)
                        .into(userProfile)
                }else{
                    Glide.with(this).load(Variables().ImageUrl).into(userProfile)
                }
                this.renderLatestEvents(resData)
                this.renderRecommenenData(resData)
                this.renderUpcoming(resData)
                this.renderTrending(resData)
                var searchInput = findViewById<EditText>(R.id.search_input)
                searchInput.setOnEditorActionListener{v, actionId, event->
                    when(actionId){
                        EditorInfo.IME_ACTION_DONE,EditorInfo.IME_ACTION_SEARCH->{
                            this.searchEvents(searchInput)
                            true
                        }
                        else->false
                    }
                }

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
    }

    private fun searchEvents(searchInput: EditText) {
        if (searchInput.text.trim().isNotEmpty()){
            var search = searchInput.text.trim()
            var params = HashMap<String,String>()
            params["search"] = search.toString()
            DataRequest(this).post(params,"api/events/search", onSuccess = {data->
                println(data)
                var resData = gson.fromJson(data,searchData::class.java)
                if (!resData.error){
                    var trans = this.supportFragmentManager.beginTransaction()
                    trans.replace(R.id.home_frame, Events.instance(data))
                    trans.addToBackStack(null)
                    trans.commit()
                }
            }, onError = {
                err -> println(err)
            })
        }

    }

}