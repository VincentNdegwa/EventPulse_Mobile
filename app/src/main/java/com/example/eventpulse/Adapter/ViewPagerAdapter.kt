package com.example.eventpulse.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eventpulse.Data.tickets.TicketsData
import com.example.eventpulse.Fragments.EventApproved
import com.example.eventpulse.Fragments.EventAttended
import com.example.eventpulse.Fragments.EventPending
import com.google.gson.Gson

class ViewPagerAdapter(fragmentActivity: FragmentActivity, data: List<TicketsData>): FragmentStateAdapter(fragmentActivity) {

    private var data: List<TicketsData>
    private lateinit var eventAttending:String
    private lateinit var eventPending:String
    private lateinit var eventApproved:String
    private var gson= Gson()

    init {
        this.data =data
         sortData()
    }
   fun sortData(){

      var eventPendingArray = data.filter { it.status == "pending" }
      var eventApprovedArray = data.filter { it.status == "approved" }
      var eventAttendingArray = data.filter { it.status == "attended" }

       eventAttending = gson.toJson(eventAttendingArray)
       eventApproved = gson.toJson(eventApprovedArray)
       eventPending = gson.toJson(eventPendingArray)
   }
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
      return when(position){
           0->EventPending.instance(eventPending)
           1->EventApproved.instance(eventApproved)
           2->EventAttended.instance(eventAttending)
          else -> EventPending.instance(eventPending)
      }
    }
}