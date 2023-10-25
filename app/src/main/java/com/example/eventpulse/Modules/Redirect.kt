package com.example.eventpulse.Modules

import android.content.Context
import android.content.Intent
import com.example.eventpulse.Activities.EventView

class Redirect(var context: Context) {
    fun openEventView(data:String){
        var viewIntent = Intent(context,EventView::class.java)
        viewIntent.putExtra("event", data)
        context.startActivity(viewIntent)
    }
}