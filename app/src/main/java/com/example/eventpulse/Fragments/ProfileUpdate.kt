package com.example.eventpulse.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Display.Mode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.eventpulse.Data.login.UserLogin
import com.example.eventpulse.Data.mainResponse.MainResPonse
import com.example.eventpulse.Modules.Variables
import com.example.eventpulse.R
import com.example.eventpulse.Request.DataRequest
import com.example.eventpulse.databinding.FragmentProfileUpdateBinding
import com.google.gson.Gson

class ProfileUpdate : Fragment() {

    private lateinit var bind:FragmentProfileUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentProfileUpdateBinding.inflate(layoutInflater)
        this.renderData()
        this.eventListeners()
        return bind.root
    }

    private fun eventListeners() {


        bind.buttonUpdate.setOnClickListener{
            var params = HashMap<String, String>()
            val firstName = bind.firstName.text.toString()
            val lastName = bind.lastName.text.toString()
            val phoneNumber = bind.PhoneNumber.text.toString()
            val country = bind.country.text.toString()
            val state = bind.county.text.toString()

            var sharedPrefData = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("userData", null)
            params["first_name"] = firstName
            params["last_name"] = lastName
            params["phone_number"] = phoneNumber
            params["country"] = country
            params["state"] = state
            if (
                firstName.isNotEmpty() &&
                lastName.isNotEmpty() &&
                phoneNumber.isNotEmpty() &&
                country.isNotEmpty() &&
                state.isNotEmpty() &&
                sharedPrefData != null
            ) {
                var userData = Gson().fromJson(sharedPrefData, UserLogin::class.java)
                params["user_id"] = userData.data.id.toString()

               DataRequest(requireContext()).post(params,"api/profile/update", onSuccess = {
                   res->
                       var resData = Gson().fromJson(res,MainResPonse::class.java)
                   if (resData.error){
                       Toast.makeText(requireContext(), resData.message, Toast.LENGTH_SHORT).show()
                   }else{
                       Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
                   }
                   this.navigateBack()
               }, onError = {
                   err-> Toast.makeText(requireContext(), err, Toast.LENGTH_SHORT).show()

               })
            }

        }

        bind.buttonCancel.setOnClickListener{
            this.navigateBack()
        }
    }

    private fun navigateBack() {
       requireActivity().onBackPressed()
    }


    private fun renderData() {
        fun handleEmpty(string: String, fallback:String): String {

            if (string.isEmpty()){
                return fallback
            }else{
                return  string
            }
        }
        var sharedpref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("userData", null)

        if (!sharedpref.isNullOrEmpty()){
            var userData = Gson().fromJson(sharedpref, UserLogin::class.java)
            var profileData = userData.data.profile
            bind.PhoneNumber.setText(profileData.phone_number?.toString())
            bind.country.setText(handleEmpty(profileData.country, ""))
            bind.county.setText(handleEmpty(profileData.state, ""))
            bind.firstName.setText(handleEmpty(profileData.first_name, ""))
            bind.lastName.setText(handleEmpty(profileData.last_name, ""))

            if (!profileData.profile_image.isEmpty()){
                Glide.with(requireContext()).load(Variables().Url+profileData.profile_image).into(bind.updateProfileImage)
            }else{
                Glide.with(requireContext()).load(Variables().ImageUrl).into(bind.updateProfileImage)
            }
        }
    }

    companion object {


    }
}