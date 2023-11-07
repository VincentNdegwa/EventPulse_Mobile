package com.example.eventpulse.Modules

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.eventpulse.R

class CustomToast(context: Context) {
    var context:Context
    init {
        this.context = context
    }
    fun make(message:String,root:ViewGroup){
        var layout = LayoutInflater.from(context).inflate(R.layout.layout_custom_toast,root, false)
        val text = layout.findViewById<TextView>(R.id.text)
        text.text = message

        var toast = Toast(context)
        toast.setGravity(Gravity.BOTTOM, 10, 16)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }
}