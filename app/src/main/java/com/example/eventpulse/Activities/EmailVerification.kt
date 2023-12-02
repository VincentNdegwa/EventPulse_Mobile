package com.example.eventpulse.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.eventpulse.Data.mainResponse.MainResPonse
import com.example.eventpulse.Dialogs.EmailCodeDialog
import com.example.eventpulse.Dialogs.ErrorDialog
import com.example.eventpulse.Dialogs.LoadingDialog
import com.example.eventpulse.R
import com.example.eventpulse.Request.DataRequest
import com.example.eventpulse.databinding.ActivityEmailVerificationBinding
import com.google.gson.Gson

class EmailVerification : AppCompatActivity() {
    private lateinit var bind: ActivityEmailVerificationBinding
    var gson = Gson()
    private lateinit var loadingDialog: LoadingDialog
    private var errorDialog: ErrorDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityEmailVerificationBinding.inflate(layoutInflater)
        setContentView(bind.root)
        this.eventListerner()
        loadingDialog = LoadingDialog()
    }

    private fun eventListerner() {
        bind.login.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, Login::class.java)
            startActivity(intent)
        })
        bind.signupButton.setOnClickListener{
            this.validateData()
        }
    }

    private fun validateData() {
        if (!bind.emailInput.text?.trim().isNullOrEmpty()){
            var params = HashMap<String, String>()
            params["email"] = bind.emailInput.text.toString()

            this.hideElement(true)

            DataRequest(this).post(params,"api/email/verify", onSuccess = { data->
                println(data)
                this.hideElement(false)
                if (!data.isNullOrEmpty()){
                    var errorDialog:ErrorDialog
                    var emailCode:EmailCodeDialog
                    var resData = gson.fromJson(data, MainResPonse::class.java)
                    if (resData.error){
                        errorDialog = ErrorDialog(resData.message)
                        errorDialog.show(supportFragmentManager, "errorDialog")
//                        Toast.makeText(this, resData.message, Toast.LENGTH_SHORT).show()
                    }else{
                        emailCode = EmailCodeDialog(params["email"])
                        emailCode.show(supportFragmentManager, "emailCodeDialog")
//                        Toast.makeText(this, resData.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }, onError = {
                error->
                this.hideElement(false)
                if (!isFinishing && !isDestroyed) {
                    errorDialog = ErrorDialog(error)
                    println("error in sending data")
                    errorDialog?.show(supportFragmentManager, "errorFragment")
                }

                println(error)
            })
        }
    }

    private fun hideElement(status: Boolean) {

        if (status){
            loadingDialog.show(this.supportFragmentManager, "fr")
        }else{
            loadingDialog.dismiss()
        }
    }
}