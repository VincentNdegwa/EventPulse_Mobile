package com.example.eventpulse.Modules

import android.content.Context
import android.content.Intent
import com.example.eventpulse.Activities.Home

class ActivityRender(var context: Context, var cls: Class<*>) {
    var activityIntent = Intent(context,cls)
    fun open(){
        context.startActivity(activityIntent)
    }
}