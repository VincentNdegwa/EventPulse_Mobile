package com.example.eventpulse.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Toast
import com.example.eventpulse.Data.login.UserLogin
import com.example.eventpulse.R
import com.example.eventpulse.Request.DataRequest
import com.example.eventpulse.databinding.ActivityLoginBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Objects

class Login : AppCompatActivity() {
    private lateinit var bind: ActivityLoginBinding
    var gson= Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       bind = ActivityLoginBinding.inflate(layoutInflater)
        var sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE).getString("credentials", "no data")
        if (sharedPref != "no data"){
            var dataType = object : TypeToken<HashMap<String,String>>(){}.type
            var dataHashMap = gson.fromJson<HashMap<String,String>>(sharedPref, dataType)
            println(dataHashMap)
            if (!dataHashMap["email"].isNullOrEmpty() && !dataHashMap["password"].isNullOrEmpty()){
                this.navigateToDash(dataHashMap["email"].toString(), dataHashMap["password"].toString())
            }
        }else{
            setContentView(bind.root)
            bind.loginButton.setOnClickListener {
                var email = bind.emailInput.text.toString().trim()
                var pass = bind.passwordInput.text.toString().trim()

                this.navigateToDash(email,pass)
            }
            bind.createAcc.setOnClickListener {
                this.navigateCreateAcc()
            }
        }

    }

//    override fun onResume() {
//        super.onResume()
//        finishAffinity()
//    }

    private fun navigateToDash(email: String, pass:String){
        var params = HashMap<String, String>()

        params["email"] = email
        params["password"] = pass

        Log.d("navDash", params.toString())
        DataRequest(this).
        post(
            params,
            "api/user/login",
            onSuccess = { data ->
               var userData = gson.fromJson(data,UserLogin::class.java)
               if ( userData.error){
                   Toast.makeText(this,userData.message, Toast.LENGTH_LONG).show()
               }else{
                   params["profile_image"] = userData.data.profile.profile_image
                   params["first_name"] = userData.data.profile.first_name

                   var dashIntent = Intent(this, Home::class.java)
                   getSharedPreferences("user", Context.MODE_PRIVATE)
                       .edit()
                       .putString("credentials", gson.toJson(params))
                       .putString("userData",data)
                       .apply()
                   startActivity(dashIntent)
               }
                        },
            onError = { error ->
              println(error)
            } )


    }
    private  fun navigateCreateAcc(){
        var createAccIntent = Intent(this, Register::class.java)
        startActivity((createAccIntent))
    }
}