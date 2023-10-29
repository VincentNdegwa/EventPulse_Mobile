package com.example.eventpulse.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventpulse.Adapter.TicketsAdapter
import com.example.eventpulse.Data.login.UserLogin
import com.example.eventpulse.Data.tickets.TicketsData
import com.example.eventpulse.Data.tickets.UserTickets
import com.example.eventpulse.R
import com.example.eventpulse.Request.DataRequest
import com.example.eventpulse.databinding.FragmentTicketsBinding
import com.google.gson.Gson


class Tickets : Fragment() {

    private lateinit var bind: FragmentTicketsBinding
    private lateinit var message: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentTicketsBinding.inflate(layoutInflater)
        return bind.root
        this.requestData()

    }

    private fun requestData() {
        var params = HashMap<String, String>()
        var userData = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("userData", null)
        if (userData != null){
            var jsonUserData = Gson().fromJson(userData, UserLogin::class.java)
            params["user_id"] = jsonUserData.data.id.toString()

            DataRequest(requireContext()).post(params,"api/applicant/tickets", onSuccess = {
                    data->
                if (!data.isNullOrEmpty()){
                    var dataRes= Gson().fromJson(data, UserTickets::class.java)
                    if (!dataRes.error){
                        this.renderData(dataRes.data)
                    }else{
                        message = dataRes.message
                        this.renderMessage()
                    }
                }
            }, onError = {
                    error->
                message = error
                this.renderMessage()
            })
        }
        this.eventListeners()

    }

    private fun eventListeners() {
//        bind.hamburgerButton.setOnClickListener{
//            var drawer = bind.homeNavDrawer
//            drawer.openDrawer(GravityCompat.START)
//        }
    }

    private fun renderMessage() {
        bind.infoText.text = message
        bind.infoText.visibility = View.VISIBLE
    }

    private fun renderData(data: List<TicketsData>) {
        if (data.size>0){
            var adapter = TicketsAdapter(data, requireContext())
            var recyclerview = bind.ticketsRecyclerview
            recyclerview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerview.adapter = adapter
        }else{
            bind.infoText.text = message
            bind.infoText.visibility = View.VISIBLE
        }
    }

    companion object {

    }
}