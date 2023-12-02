package com.example.eventpulse.Dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.eventpulse.databinding.FragmentEmailCodeDialogBinding


class EmailCodeDialog(message: String?) : DialogFragment() {

    private lateinit var bind: FragmentEmailCodeDialogBinding
    var message = message;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        var email = arguments?.getString("data");
//        if (!email.isNullOrEmpty()){
//
//        }
        bind = FragmentEmailCodeDialogBinding.inflate(layoutInflater)
        bind.tvEmail.text = "Email: $message"
        return  bind.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCancelable(false)
        dialog?.window?.setLayout((resources.displayMetrics.widthPixels*0.95).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onStop() {
        super.onStop()
    }
    companion object {
        fun instance(data:String):EmailCodeDialog{
            var fr = EmailCodeDialog(null)
            var arg = Bundle()
            arg.putString(data, "data")
            fr.arguments = arg
            return  fr
        }
    }
}