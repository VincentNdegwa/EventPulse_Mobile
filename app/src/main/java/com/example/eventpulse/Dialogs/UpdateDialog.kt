package com.example.eventpulse.Dialogs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.eventpulse.Activities.MainActivity
import com.example.eventpulse.R
import com.example.eventpulse.databinding.FragmentUpdateDialogBinding

class UpdateDialog : DialogFragment() {

    private lateinit var bind: FragmentUpdateDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentUpdateDialogBinding.inflate(layoutInflater)
        this.eventListeners()
        return  bind.root
    }

    private fun eventListeners() {
        bind.cancelButton.setOnClickListener{
            dialog?.dismiss()
        }
        bind.restartButton.setOnClickListener{
            var intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        var width = (resources.displayMetrics.widthPixels* 0.90).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.setCancelable(false)
    }

}