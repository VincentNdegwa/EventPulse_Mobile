package com.example.eventpulse.Request

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class DataRequest(context: Context) {
    private var context = context
    fun post(data:HashMap<String,String>,url: String){
        val queue = Volley.newRequestQueue(context)

        var stringreq =object :StringRequest(Request.Method.POST, url, {
            res->
            println(res)
        }, {
            error->
            println(error)
        }){
            override fun getParams(): Map<String, String>? {
                return data
            }
        }
        queue.add(stringreq)
    }
}