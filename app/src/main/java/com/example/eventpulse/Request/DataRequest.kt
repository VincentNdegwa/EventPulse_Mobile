package com.example.eventpulse.Request

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.eventpulse.variables.Variables

class DataRequest(context: Context) {
    private var context = context


    fun post(data: HashMap<String, String>, url: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        try {
            val queue = Volley.newRequestQueue(context)

            var stringreq =object :StringRequest(Request.Method.POST, Variables().Url+url, {
                    res->
                onSuccess(res)
            }, {
                    error->
                onError(error.toString())
            }){
                override fun getParams(): Map<String, String>? {
                    return data
                }
            }
            queue.add(stringreq)
        }catch (error:VerifyError){
            println("error$error")
        }
    }
}