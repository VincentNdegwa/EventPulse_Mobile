package com.example.eventpulse.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.eventpulse.Data.login.UserLogin
import com.example.eventpulse.Data.mainResponse.MainResPonse
import com.example.eventpulse.Request.DataRequest
import com.example.eventpulse.databinding.ActivityRegisterBinding
import com.google.gson.Gson

class Register : AppCompatActivity() {
    private lateinit var bind:ActivityRegisterBinding
    var params = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.login.setOnClickListener{
            this.navigateToLogin()
        }
        bind.signupButton.setOnClickListener{
            this.registerUser()
        }
    }

    private fun registerUser() {
        val email = bind.emailInput.text.toString()
        val passInput = bind.passwordInput.text.toString()
        val passConf = bind.passwordConfInput.text.toString()
        val username = bind.usernameInput.text.toString()

        if (email.isEmpty() || passInput.isEmpty() || passConf.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Please fill all inputs", Toast.LENGTH_SHORT).show()
            return
        }

        if (passConf != passInput) {
            Toast.makeText(this, "Password and Password Confirmation do not match", Toast.LENGTH_SHORT).show()
            return
        }


        val params = hashMapOf(
            "username" to username,
            "email" to email,
            "password" to passInput,
            "passwordConf" to passConf
        )

        this.navigateToDash(params)
    }




    private fun navigateToLogin(){
        var loginIntent = Intent(this, Login::class.java)
        startActivity(loginIntent)
    }

    private fun navigateToDash(params: HashMap<String, String>) {

        DataRequest(this).post(params, "api/user/register", onSuccess = {

            res-> var data = Gson().fromJson(res, MainResPonse::class.java)
            if (!data.error){
                Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
            }else{
                this.loginUser(params["email"].toString(), params["password"].toString())
            }
        }, onError = {
            err-> Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
        })
    }

    private fun loginUser(email: String, pass:String){
        var params = HashMap<String, String>()

        params["email"] = email
        params["password"] = pass

        DataRequest(this).
        post(
            params,
            "api/user/login",
            onSuccess = { data ->
                var userData = Gson().fromJson(data, UserLogin::class.java)
                if ( userData.error){
                    Toast.makeText(this,userData.message, Toast.LENGTH_LONG).show()
                }else{
                    params["profile_image"] = userData.data.profile.profile_image
                    params["first_name"] = userData.data.profile.first_name

                    var dashIntent = Intent(this, Home::class.java)
                    getSharedPreferences("user", Context.MODE_PRIVATE)
                        .edit()
                        .putString("credentials", Gson().toJson(params))
                        .putString("userData",data)
                        .apply()
                    startActivity(dashIntent)
                }
            },
            onError = { error ->
                println(error)
            } )


    }
}