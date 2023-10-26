package com.example.eventpulse.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventpulse.Adapter.ResultsEventAdapter
import com.example.eventpulse.Data.search.searchData
import com.example.eventpulse.Request.DataRequest
import com.example.eventpulse.databinding.FragmentEventsBinding
import com.google.gson.Gson

class Events : Fragment() {

    private var gson = Gson()
    lateinit var bind:FragmentEventsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentEventsBinding.inflate(layoutInflater)
        var view = bind.root
        this.sortData(view)
        return view
    }
    fun sortData(view: FrameLayout) {
        var data = arguments?.getString("searchDataString")
        if (!data.isNullOrEmpty()){
            var search = gson.fromJson(data, searchData::class.java)
            if (search.data.isNotEmpty()){
                this.renderData(view,search)
            }else{
                bind.resultsNotFound.visibility = View.VISIBLE
            }
        }else{
            var params = HashMap<String,String>()
            params["search"] = "All"
            DataRequest(requireContext()).post(params,"api/events/search", onSuccess = {data->
                var search = gson.fromJson(data, searchData::class.java)
                if (search.data.isNotEmpty()){
                    this.renderData(view,search)
                }else{
                    bind.resultsNotFound.visibility = View.VISIBLE
                }
            }, onError = {
                    err -> println(err)
            })
        }
    }
    fun renderData(view: View, search: searchData){
        var adapter = ResultsEventAdapter(requireContext(), search.data)
        var recyclerView = bind.resultsRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = adapter
    }


    companion object{
        fun instance(data:String): Events {
            var events = Events()
            var bundle = Bundle()
            bundle.putSerializable("searchDataString", data)
            events.arguments = bundle
            return events
        }
    }

}