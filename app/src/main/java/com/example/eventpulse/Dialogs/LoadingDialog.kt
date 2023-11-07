package com.example.eventpulse.Dialogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.eventpulse.R
import com.example.eventpulse.databinding.FragmentLoadingDialogBinding


class LoadingDialog : DialogFragment() {

    private lateinit var bind:FragmentLoadingDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentLoadingDialogBinding.inflate(layoutInflater)
        return bind.root
    }


    override fun onStart() {
        super.onStart()
        var width = (resources.displayMetrics.widthPixels* 0.90).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.setCancelable(false)
    }



    override fun onStop() {
        super.onStop()
        dialog?.dismiss()
    }
}