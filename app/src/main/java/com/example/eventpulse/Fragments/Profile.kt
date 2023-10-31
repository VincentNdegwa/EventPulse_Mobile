package com.example.eventpulse.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
        this.eventListeners()
        return bind.root
    }

    private fun eventListeners() {
        bind.updateProfileButton.setOnClickListener{
            var trans = requireActivity().supportFragmentManager.beginTransaction()
            trans.replace(R.id.home_frame, ProfileUpdate())
            var topfragemnt = requireActivity().findViewById<FrameLayout>(R.id.top_fragment)
            topfragemnt.visibility = View.GONE
            trans.addToBackStack(null)
            trans.commit()
        }
    }

    private fun requestData() {
        var userData = requireContext().getSharedPreferences("user",Context.MODE_PRIVATE).getString("userData", null)
        if (userData != null){
            data = Gson().fromJson(userData, UserLogin::class.java)
            this.renderData(data)
//            var params = HashMap<String,String>()
//            params["user_id"]= data.data.id.toString()
        }
    }

    @SuppressLint("SetTextI18n")

    private fun renderData(data: UserLogin) {
        fun getStringOrFallback(value: String?, fallback: String): String {
            return value?.takeIf { it.isNotEmpty() } ?: fallback
        }

        val address = getStringOrFallback(data.data.profile.country, "null") +
                " " +
                getStringOrFallback(data.data.profile.state, "null")
        bind.profileAddress.text = address

        if (data.data.profile.profile_image.isNotEmpty()) {
            Glide.with(requireContext()).load(Variables().Url + data.data.profile.profile_image)
                .into(bind.imageProfile)
        }

        bind.profilePhoneNo.text = data.data.profile.phone_number?.toString()?: "null"
        bind.profileFirstName.text = getStringOrFallback(data.data.profile.first_name, "null")
        bind.profileEmailAddress.text = getStringOrFallback(data.data.email, "null")
        bind.profileEmailStatus.text = data.data.profile.email_status
        bind.profilePhoneStatus.text = data.data.profile.phone_status
        bind.profileLastName.text = getStringOrFallback(data.data.profile.last_name, "null")
    }


    companion object {

    }
}