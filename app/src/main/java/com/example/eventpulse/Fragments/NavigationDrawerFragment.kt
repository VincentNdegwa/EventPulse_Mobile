package com.example.eventpulse.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.core.view.size
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.eventpulse.Activities.Home
import com.example.eventpulse.Data.ProfileData.ProfileData
import com.example.eventpulse.Modules.ActivityRender
import com.example.eventpulse.Modules.Variables
import com.example.eventpulse.R
import com.example.eventpulse.databinding.FragmentNavigationDrawerBinding
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NavigationDrawerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NavigationDrawerFragment : Fragment() {

    private lateinit var bind: FragmentNavigationDrawerBinding
    private lateinit var profileData: ProfileData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentNavigationDrawerBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        var sharedPref = requireActivity().getSharedPreferences("user",Context.MODE_PRIVATE).getString("credentials", null)
        if (!sharedPref.isNullOrEmpty()){
            profileData =  Gson().fromJson(sharedPref, ProfileData::class.java)
        }

        this.renderData()
        var sideNav = bind.drawerHolder.navView
        this.addEventListeners(sideNav)

        return bind.root


    }
    private fun addEventListeners(sideNav: NavigationView) {
        for (i in 0 until sideNav.menu.size()) {
            val menuItem = sideNav.menu.getItem(i)
            menuItem.setOnMenuItemClickListener {
                when (menuItem.itemId) {
                    R.id.home_nav -> {
                        ActivityRender(requireContext(),Home::class.java).open()
                        true
                    }
                    R.id.events_nav -> {
//                        ActivityRender(requireContext(),Home::class.java).open()
                        var drawer = requireActivity().findViewById<DrawerLayout>(R.id.home_nav_drawer)
                        drawer.closeDrawer(GravityCompat.START)

                        var trans = requireActivity().supportFragmentManager.beginTransaction()
                        trans.replace(R.id.home_frame, Events())
                        trans.commit()
                        true
                    }
                    R.id.tickets_nav -> {
                        println("Tickets menu item clicked")
                        true
                    }
                    R.id.profile_nav -> {
                        println("Profile menu item clicked")
                        true
                    }
                    else -> false
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