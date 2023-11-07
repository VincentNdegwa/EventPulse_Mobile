package com.example.eventpulse.Fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.eventpulse.Activities.MainActivity
import com.example.eventpulse.Data.login.UserLogin
import com.example.eventpulse.Data.mainResponse.MainResPonse
import com.example.eventpulse.Dialogs.UpdateDialog
import com.example.eventpulse.Modules.Variables
import com.example.eventpulse.R
import com.example.eventpulse.Request.DataRequest
import com.example.eventpulse.databinding.FragmentProfileUpdateBinding
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.File

class ProfileUpdate : Fragment() {

    private lateinit var bind:FragmentProfileUpdateBinding
    private  var profileImageUrl:String = ""


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
            if (profileImageUrl.isNotEmpty()){
                params["profile_image"] = profileImageUrl
            }

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
                       UpdateDialog().show(parentFragmentManager, "fragmentManager")
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
        bind.editImage.setOnClickListener{
            this.checkFilePermission()
        }
    }



    private fun checkFilePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED){
            this.imagePicker()
        }else{
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_MEDIA_IMAGES),read_request_code)
            var permissionReq = registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted->
                if (isGranted){
                    this.imagePicker()
                }
            }

            permissionReq.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }

    private fun imagePicker() {
        var intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        myResultsLauncher.launch(intent)

    }

    var myResultsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        results->
        if (results.resultCode == Activity.RESULT_OK){
            if (results.data != null){
                bind.updateProfileImage.setImageURI(results.data?.data)
                var imageFileBase64 = results.data?.data?.let { this.fileEncoder(it) }
                profileImageUrl = imageFileBase64.toString()
            }
        }
    }

    private fun fileEncoder(data: Uri): String {
        try {
            val inputStream = requireActivity().contentResolver.openInputStream(data)
            if (inputStream != null) {
                val byteArrayOutputStream = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var len: Int
                while (inputStream.read(buffer).also { len = it } != -1) {
                    byteArrayOutputStream.write(buffer, 0, len)
                }
                val imageBytes = byteArrayOutputStream.toByteArray()
                val imageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                return imageBase64
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
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