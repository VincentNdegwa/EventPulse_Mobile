package com.example.eventpulse.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.eventpulse.Data.login.UserLogin
import com.example.eventpulse.Modules.Variables
import com.example.eventpulse.R
import com.example.eventpulse.databinding.FragmentProfileBinding
import com.google.gson.Gson


class Profile : Fragment() {

    private lateinit var bind:FragmentProfileBinding
    private lateinit var data: UserLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentProfileBinding.inflate(layoutInflater)
        this.requestData()
        return bind.root
    }

    private fun requestData() {
        var userData = requireContext().getSharedPreferences("user",Context.MODE_PRIVATE).getString("userData", null)
        if (userData != null){
            data = Gson().fromJson(userData, UserLogin::class.java)
            this.renderData()
//            var params = HashMap<String,String>()
//            params["user_id"]= data.data.id.toString()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderData() {
        bind.profileAddress.text = data.data.profile.country+data.data.profile.state
        if (data.data.profile.profile_image != ""){
            Glide.with(requireContext()).load(Variables().Url+data.data.profile.profile_image).into(bind.imageProfile)
        }
        bind.profilePhoneNo.text = data.data.profile.phone_number.toString()
        bind.profileFirstName.text = data.data.profile.first_name.toString()
        bind.profileEmailAddress.text = data.data.email
        bind.profileEmailStatus.text = data.data.profile.email_status
        bind.profilePhoneStatus.text = data.data.profile.phone_status
    }

    companion object {

    }
}