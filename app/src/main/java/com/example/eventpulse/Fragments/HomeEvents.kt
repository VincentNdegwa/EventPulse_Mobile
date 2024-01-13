package com.example.eventpulse.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.eventpulse.Modules.Variables
import com.example.eventpulse.R
import com.example.eventpulse.Request.DataRequest
import com.example.eventpulse.databinding.FragmentHomeEventsBinding
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class HomeEvents : Fragment() {

    private lateinit var bind: FragmentHomeEventsBinding
    private lateinit var userData:UserLogin
    private lateinit var errorType: String
    var gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentHomeEventsBinding.inflate(layoutInflater)
        var loading = true
        var data = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
            .getString("userData", null)
        if (loading == true && data != null) {
            userData = gson.fromJson(data, UserLogin::class.java)

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val results: DashData? = makeDataRequest(data)
                    Log.d("dashData2", results.toString())

                    withContext(Dispatchers.Main) {
                        if (results != null) {
                            renderData(results)
                        } else {
                            println("data is null")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("Network Error", e.message ?: "Unknown error")
                }
            }
        } else {
            Toast.makeText(requireContext(), "An Error Occurred", Toast.LENGTH_SHORT).show()
        }
        return bind.root
    }

    private suspend fun makeDataRequest(data: String): DashData = suspendCoroutine { continuation ->
        val userData = gson.fromJson(data, UserLogin::class.java)
        val params = HashMap<String, String>()

        params["user_id"] = userData.data.id.toString()
        DataRequest(requireContext()).post(params, "api/retrieve", onSuccess = { responseData ->
            val resData = gson.fromJson(responseData, DashData::class.java)
            if (!resData.error) {
                Log.d("dashData1", resData.toString())
                continuation.resume(resData)
            } else {
                continuation.resumeWithException(Exception("Error in network response"))
            }
        }, onError = { err ->
            Log.e("Network Error", err)
            continuation.resumeWithException(Exception("Network Error"))
        })
    }


    fun renderData(resData: DashData?) {
        if (resData== null){
            bind.eventsAvaillable.visibility = View.GONE
            bind.noEvents.visibility = View.VISIBLE

        }else{
            bind.eventsAvaillable.visibility = View.VISIBLE
            bind.noEvents.visibility = View.GONE
            if (resData != null) {
                this.renderLatestEvents(resData)
            }
            if (resData != null) {
                this.renderRecommenenData(resData)
            }
            if (resData != null) {
                this.renderUpcoming(resData)
            }
            if (resData != null) {
                this.renderTrending(resData)
            }
            var searchInput = bind.searchInput
            searchInput.setOnEditorActionListener{v, actionId, event->
                when(actionId){
                    EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_SEARCH->{
                        this.searchEvents(searchInput)
                        true
                    }
                    else->false
                }
            }
        }

    }
    private fun renderLatestEvents(resData: DashData) {
        var adapter = LatestEventsAdapter(resData.latestEvents, requireContext())
        var recyclerView = bind.latestEventsRecycler
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter = adapter
    }
    private fun renderRecommenenData(resData: DashData) {
        val adapter = RecomendendEventsAdapter(requireContext(), resData.recomendedEvents)
        val recyclerView = bind.recommendedEventsRecycler
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

    }
    private fun renderUpcoming(resData: DashData){
        if (resData.upCommingEvents.isEmpty()){
            bind.invisibleHolder.visibility = View.VISIBLE
        }else{
            val adapter = UpComingEventsAdapter(requireContext(), resData.upCommingEvents)
            val recyclerView = bind.upcomingEventsRecycler
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapter
        }

    }

    private fun renderTrending(resData: DashData){
        val adapter = TrendingEventsAdapter(requireContext(), resData.trendingEvents)
        val recyclerView = bind.trendingEventsRecycler
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun searchEvents(searchInput: EditText) {
        if (searchInput.text.trim().isNotEmpty()){
            var search = searchInput.text.trim()
            var params = HashMap<String,String>()
            params["search"] = search.toString()
            DataRequest(requireContext()).post(params,"api/events/search", onSuccess = { data->
                println(data)
                var resData = gson.fromJson(data, searchData::class.java)
                if (!resData.error){
                    var trans = requireActivity().supportFragmentManager.beginTransaction()
                    trans.replace(R.id.home_frame, Events.instance(data))
                    trans.addToBackStack(null)
                    trans.commit()
                }
            }, onError = {
                    err -> println(err)
            })
        }

    }
    companion object {
      fun instance(data:String): HomeEvents {
          var app = HomeEvents()
          var args = Bundle()
          args.putString("data", data)
          app.arguments = args
          return app
      }
    }
}