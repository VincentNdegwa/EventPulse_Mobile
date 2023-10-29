package com.example.eventpulse.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.eventpulse.Activities.Home
import com.example.eventpulse.Data.ProfileData.ProfileData
import com.example.eventpulse.Modules.ActivityRender
import com.example.eventpulse.Modules.Variables
import com.example.eventpulse.R
import com.example.eventpulse.databinding.FragmentNavigationDrawerBinding
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson

class NavigationDrawerFragment : Fragment() {

    private lateinit var bind: FragmentNavigationDrawerBinding
    private lateinit var profileData: ProfileData
    private  lateinit var sideNav: NavigationView;
//    private lateinit var drawer: DrawerLayout
    private lateinit var fr:FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentNavigationDrawerBinding.inflate(layoutInflater)
        var sharedPref = requireActivity().getSharedPreferences("user",Context.MODE_PRIVATE).getString("credentials", null)
        if (!sharedPref.isNullOrEmpty()){
            profileData =  Gson().fromJson(sharedPref, ProfileData::class.java)
        }

        this.renderData()
        sideNav = bind.drawerHolder.navView
        this.addEventListeners(sideNav)
        fr =  requireActivity().supportFragmentManager
        return bind.root
    }

    private fun addEventListeners(sideNav: NavigationView) {
        println("menu clicked")
        for (i in 0 until sideNav.menu.size()) {
            val menuItem = sideNav.menu.getItem(i)
            menuItem.setOnMenuItemClickListener {
                var  drawer = requireActivity().findViewById<DrawerLayout>(R.id.home_nav_drawer)
                drawer.closeDrawer(GravityCompat.START)
                when (menuItem.itemId) {
                    R.id.home_nav -> {
                        ActivityRender(requireContext(),Home::class.java).open()
                        true
                    }
                    R.id.events_nav -> {
                        var trans = fr.beginTransaction()
                        trans.replace(R.id.home_frame, Events())
                        trans.commit()
                        true
                    }
                    R.id.tickets_nav -> {
                        var trans = fr.beginTransaction()
                        trans.replace(R.id.home_frame,Tickets())
                        trans.commit()
                        true
                    }
                    R.id.profile_nav -> {
                        true
                    }
                    else -> {
                        false
                    }
                }
            }

            if (menuItem.hasSubMenu()) {
                val subMenu = menuItem.subMenu
                for (j in 0 until subMenu?.size()!!) {
                    val subMenuItem = subMenu.getItem(j)
                    subMenuItem.setOnMenuItemClickListener {
                        when (subMenuItem.itemId) {
                            R.id.logout_nav -> {
                                println("Logout menu item clicked")
                                true
                            }
                            else -> false
                        }
                    }
                }
            }
        }
    }



    private fun renderData(){
        if (profileData.profile_image != ""){
            Glide.with(requireContext()).load(Variables().Url+profileData.profile_image).into(bind.profileImage)
        }else{
            Glide.with(requireContext()).load(Variables().ImageUrl).into(bind.profileImage)
        }
        if (profileData.first_name != ""){
            bind.userName.text = profileData.first_name
        }else{
            bind.userName.visibility = View.GONE
        }
        if (profileData.email != ""){
            bind.userEmail.text = profileData.email
        }else{
            bind.userEmail.visibility = View.GONE
        }
    }
    companion object {

        fun newInstance(): NavigationDrawerFragment {
            return NavigationDrawerFragment()
        }
    }
}