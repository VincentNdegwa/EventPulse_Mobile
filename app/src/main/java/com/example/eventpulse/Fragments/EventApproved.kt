package com.example.eventpulse.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventpulse.Adapter.TicketsAdapter
import com.example.eventpulse.Data.tickets.TicketsData
import com.example.eventpulse.R
import com.example.eventpulse.databinding.FragmentEventApprovedBinding
import com.example.eventpulse.databinding.FragmentEventPendingBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EventApproved : Fragment() {

    private lateinit var bind: FragmentEventApprovedBinding
    private var gson= Gson()
    private lateinit var ticketsData: List<TicketsData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentEventApprovedBinding.inflate(layoutInflater)
        var stringData = arguments?.getString("data")
        ticketsData = gson.fromJson<List<TicketsData>>(stringData,object : TypeToken<List<TicketsData>>() {}.type)
        renderData(ticketsData)
        return bind.root
    }


        private fun renderData(data: List<TicketsData>) {
        if (data.size>0){
            var adapter = TicketsAdapter(data, requireContext())
            var recyclerview = bind.ticketsRecyclerview
            recyclerview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerview.adapter = adapter
        }else{
            bind.infoText.visibility = View.VISIBLE
        }
    }
    companion object {
        fun instance(data:String): EventApproved{
            var fr = EventApproved()
            var arg = Bundle()
            arg.putString("data", data)
            fr.arguments = arg
            return  fr
        }

    }
}