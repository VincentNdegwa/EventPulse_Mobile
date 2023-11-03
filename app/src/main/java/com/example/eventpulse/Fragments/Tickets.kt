package com.example.eventpulse.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eventpulse.Adapter.ViewPagerAdapter
import com.example.eventpulse.Data.login.UserLogin
import com.example.eventpulse.Data.tickets.TicketsData
import com.example.eventpulse.Data.tickets.UserTickets
import com.example.eventpulse.Request.DataRequest
import com.example.eventpulse.databinding.FragmentTicketsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class Tickets : Fragment() {

    private lateinit var bind: FragmentTicketsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentTicketsBinding.inflate(layoutInflater)
        this.requestData()
        return bind.root

    }

    private fun renderViewPager(data: List<TicketsData>) {
        var adapter = ViewPagerAdapter(requireActivity(),data)
        bind.myViewpage.adapter = adapter
        TabLayoutMediator(bind.myTablayout,bind.myViewpage){
            tab,position->
            when(position){
                0 -> tab.text = "Pending"
                1 -> tab.text = "Approved"
                2 -> tab.text = "Attended"
            }
        }.attach()
        bind.myTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    bind.myViewpage.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

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
                        this.renderViewPager(dataRes.data)
//                        this.renderData(dataRes.data)
                    }else{
                        this.renderMessage(dataRes.message)
                    }
                }
            }, onError = {
                    error->
                this.renderMessage(error.toString())
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

    private fun renderMessage(message:String) {
        bind.infoText.text = message
        bind.infoText.visibility = View.VISIBLE
    }


    companion object {

    }
}