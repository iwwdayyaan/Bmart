package com.bart.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bart.R
import com.bart.activities.MainActivity
import com.bart.api.AppController
import com.bart.api.Responce
import com.bart.api.interfaces.ApiService
import com.bart.interfaces.NavigationHost
import com.bart.utility.Utility
import kotlinx.android.synthetic.main.sign_in_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import javax.inject.Inject

class SigninFragment: Fragment() ,View.OnClickListener{
    @Inject
    internal lateinit var apiService: ApiService
    private var ctx : Context? = null

    companion object {
        fun newInstance(user_type: Int): SigninFragment {
            val fragment = SigninFragment()
            val args = Bundle()
            args.putInt("user_type", user_type)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onClick(p0: View?) {
       when(p0!!.id){
           R.id.tvReg-> {
               ((context!! as Activity as NavigationHost).navigateTo(RegisterFragment.newInstance(0),false,false))
           }
          R.id.tvForgotPwd->   ((context!! as Activity as NavigationHost).navigateTo(ForgetPasswordFragment.newInstance(0),false,false))
          R.id.btnSignin-> {
            validate()
           }
       }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        return  inflater.inflate(R.layout.sign_in_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvReg.setOnClickListener(this)
        tvForgotPwd.setOnClickListener(this)
        btnSignin.setOnClickListener(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.ctx = context
          ((context!! as Activity).application as AppController).component.inject(this@SigninFragment)
    }
    private fun validate(){
        if(!TextUtils.isEmpty(et_email.text)){
            if(!TextUtils.isEmpty(et_pwd.text)){
                if (Utility.isConnected()) {
                    userLogin()
                } else {
                    Toast.makeText(activity, "No internet connection!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(ctx as Activity, "Please enter password!", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(ctx as Activity, "Please enter email!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun userLogin() {
        progressBar.visibility = View.VISIBLE
        apiService.userLogin("userlogin",et_email.text.toString(),et_pwd.text.toString(),"xyz").enqueue(object : Callback<Responce.LoginUser> {
            override fun onResponse(call: Call<Responce.LoginUser>, response: Response<Responce.LoginUser>) {
                if (response.isSuccessful && response.body()!!.status) {
                    AppController.getSharedPref().edit().putBoolean("isLogin",true).apply()
                    AppController.getSharedPref().edit().putString("user_id",response.body()!!.data[0].userid).apply()
                    AppController.getSharedPref().edit().putString("user_name",response.body()!!.data[0].name).apply()
                    AppController.getSharedPref().edit().putString("email",response.body()!!.data[0].email).apply()
                    startActivity(Intent(ctx as Activity, MainActivity::class.java))
                    (ctx as Activity). finish()

                } else {
                    progressBar.visibility = View.GONE
                    Toast.makeText(ctx as Activity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Responce.LoginUser>, t: Throwable) {
                t.printStackTrace()

                progressBar.visibility = View.GONE
            }
        })
    }

}