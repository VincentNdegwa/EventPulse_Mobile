package com.example.eventpulse.Dialogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.eventpulse.R
import com.example.eventpulse.databinding.FragmentErrorDialogBinding

class ErrorDialog(message:String) : DialogFragment() {

    private lateinit var bind: FragmentErrorDialogBinding
    private var message:String = message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentErrorDialogBinding.inflate(layoutInflater)
        bind.textError.text = message
        return bind.root
    }

    override fun onStart() {
        super.onStart()
        var width = (resources.displayMetrics.widthPixels* 0.90).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.setCancelable(true)
    }



    override fun onStop() {
        super.onStop()
        dialog?.dismiss()
    }

}