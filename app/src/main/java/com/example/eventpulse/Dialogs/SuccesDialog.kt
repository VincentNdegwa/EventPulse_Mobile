package com.example.eventpulse.Dialogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.eventpulse.R
import com.example.eventpulse.databinding.FragmentSuccesDialogBinding


class SuccesDialog(message:String) : DialogFragment() {

    private  lateinit var bind: FragmentSuccesDialogBinding
    var message = message
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentSuccesDialogBinding.inflate(layoutInflater)
        bind.messageText.text = message
        return  bind.root
    }

    override fun onStop() {
        super.onStop()
        var width = (resources.displayMetrics.widthPixels* 0.9).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.setCancelable(true)
    }

    override fun onStart() {
        super.onStart()
        dialog?.dismiss()
    }
}