package com.example.eventpulse.Request

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.eventpulse.Dialogs.NetworkErrorDialog
import com.example.eventpulse.Modules.Variables

class DataRequest(context: Context) {
    private var context = context
    private var inprogress = false


    fun post(data: HashMap<String, String>, url: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        if (!inprogress){
            inprogress = true
            try {
                val queue = Volley.newRequestQueue(context)

                var stringreq =object :StringRequest(Request.Method.POST, Variables().Url+"/"+url, {
                        res->
                    onSuccess(res)
                }, {
                        error->
                    onError(error.toString())
                }){
                    override fun getParams(): Map<String, String>? {
                        return data
                    }

                    override fun getRetryPolicy(): RetryPolicy {
                        return DefaultRetryPolicy(
                            30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        )
                    }
                }
                queue.add(stringreq)
            }catch (error:VerifyError){
              if (context is AppCompatActivity){
                  NetworkErrorDialog().show((context as AppCompatActivity).supportFragmentManager, "network")
              }else{
                  Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show()
              }

            }
        }

    }
}
