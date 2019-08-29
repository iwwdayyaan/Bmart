package com.bart.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import com.bart.R
import com.bart.activities.MainActivity
import com.bart.api.AppController
import com.bart.api.Responce
import com.bart.api.interfaces.ApiService
import com.bart.interfaces.NavigationHost
import com.bart.utility.Utility
import kotlinx.android.synthetic.main.forget_password_fragment.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ForgetPasswordFragment : Fragment() , View.OnClickListener{
    @Inject
    internal lateinit var apiService: ApiService
    private var ctx : Context? = null

    companion object {
        fun newInstance(user_type: Int): ForgetPasswordFragment {
            val fragment = ForgetPasswordFragment()
            val args = Bundle()
            args.putInt("user_type", user_type)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.tvAlreadyAcct->   ((context!! as Activity as NavigationHost).navigateTo(SigninFragment.newInstance(0),false,false))
            R.id.btnSubmit-> {
                validate()
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        return  inflater.inflate(R.layout.forget_password_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvAlreadyAcct.movementMethod = LinkMovementMethod.getInstance()
        btnSubmit.setOnClickListener(this)
        tvAlreadyAcct.setOnClickListener(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.ctx = context
        ((context!! as Activity).application as AppController).component.inject(this@ForgetPasswordFragment)
    }
    private fun validate(){
        if(!TextUtils.isEmpty(et_email.text)){
            if(!TextUtils.isEmpty(et_phone.text)){
                if(et_phone.text!!.length==10){
                if (Utility.isConnected()) {
                    forgotPassword()
                } else {
                    Toast.makeText(activity, "No internet connection!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(ctx as Activity, "Please enter 10 digit mobile number!", Toast.LENGTH_SHORT).show()
            }
            }else{
                Toast.makeText(ctx as Activity, "Please enter password!", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(ctx as Activity, "Please enter email!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun forgotPassword() {
        progressBar.visibility = View.VISIBLE
        apiService.forgotPassword("forgotpassword",et_email.text.toString(),et_phone.text.toString()).enqueue(object : Callback<Responce.Status> {
            override fun onResponse(call: Call<Responce.Status>, response: Response<Responce.Status>) {
                if (response.isSuccessful && response.body()!!.status) {
                    alertDialog(ctx as Activity,response.body()!!.message)
                } else {
                    progressBar.visibility = View.GONE
                    Toast.makeText(ctx as Activity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Responce.Status>, t: Throwable) {
                t.printStackTrace()
                progressBar.visibility = View.GONE
            }
        })
    }
    @SuppressLint("SetTextI18n")
    fun alertDialog(activity : Activity, msg : String) {
        val dialog =   Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_alert)
        val tvMsg = dialog.findViewById<TextView>(R.id.tvMsg)
        tvMsg.text = msg

        val btnYes = dialog.findViewById<View>(R.id.btnYes) as Button
        btnYes.text =  "OK"

        val space = dialog.findViewById<View>(R.id.space) as Space
        space.visibility= View.GONE
        val btnNo = dialog.findViewById<View>(R.id.btnNo) as Button
        btnNo.visibility= View.GONE
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        btnYes.setOnClickListener {
            dialog.dismiss()
            ((context!! as Activity as NavigationHost).navigateTo(SigninFragment.newInstance(0),false,false))
        }
        dialog.show()
    }
}