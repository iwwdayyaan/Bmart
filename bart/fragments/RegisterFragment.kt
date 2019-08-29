package com.bart.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import kotlinx.android.synthetic.main.register_member_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RegisterFragment : Fragment() , View.OnClickListener,TextWatcher{

    @Inject
    internal lateinit var apiService: ApiService
    private var ctx : Context? = null
    private var oTpVerifier : Boolean? = false

    companion object {
        fun newInstance(user_type: Int): RegisterFragment {
            val fragment = RegisterFragment()
            val args = Bundle()
            args.putInt("user_type", user_type)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onClick(p0: View?) {
         when(p0!!.id) {
             R.id.tvAlreadyAcct -> {
                 ((context!! as Activity as NavigationHost).navigateTo(SigninFragment.newInstance(0), false, false))
             }
             R.id.btnGetOtp -> {
                 addOtp()
             }
             R.id.btnVerifyOtp -> {
                 confirmOtp()
             }
             R.id.btnReg -> {
                   validate()
             }

         }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        return  inflater.inflate(R.layout.register_member_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        et_mobile.addTextChangedListener(this)
        et_otp.addTextChangedListener(this)

        tvAlreadyAcct.setOnClickListener(this)
        btnGetOtp.setOnClickListener(this)
        btnVerifyOtp.setOnClickListener(this)
        btnReg.setOnClickListener(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.ctx = context
        ((context!! as Activity).application as AppController).component.inject(this@RegisterFragment)
    }
    private fun validate(){
        if(!TextUtils.isEmpty(et_name.text)){
            if(!TextUtils.isEmpty(et_mobile.text)){
                if (et_mobile.text!!.length ==10) {
                    if (!TextUtils.isEmpty(et_email.text)) {
                        if (Utility.isValidEmail(et_email.text.toString())) {
                            if (!TextUtils.isEmpty(et_pwd.text)) {
                                if (!TextUtils.isEmpty(et_otp.text)) {
                                    if (oTpVerifier!!) {
                                    if (!TextUtils.isEmpty(et_passport.text)) {
                                        if (Utility.isConnected()) {
                                            register()
                                        } else {
                                            Toast.makeText(activity, "No internet connection!", Toast.LENGTH_SHORT).show()
                                        }

                                    } else {
                                        Toast.makeText(activity, "Please enter passport number!", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(activity, "Please verify otp!", Toast.LENGTH_SHORT).show()
                                }
                                } else {
                                    Toast.makeText(activity, "Please enter otp!", Toast.LENGTH_SHORT).show()
                                }

                            } else {
                                Toast.makeText(activity, "Please enter password!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(activity, "Please enter correct email!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(activity, "Please enter email!", Toast.LENGTH_SHORT).show()
                    }

                      } else {
                    Toast.makeText(activity, "Please enter correct mobile number!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(ctx as Activity, "Please enter mobile number!", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(ctx as Activity, "Please enter user name!", Toast.LENGTH_SHORT).show()
        }


    }
    private fun register() {
        progressBar.visibility = View.VISIBLE
        apiService.userSignUp("usersignup",et_name.text.toString(),et_mobile.text.toString(),et_email.text.toString(),et_pwd.text.toString(),
                et_otp.text.toString(),et_passport.text.toString(),"xyz").enqueue(object : Callback<Responce.UserSignUp> {
            override fun onResponse(call: Call<Responce.UserSignUp>, response: Response<Responce.UserSignUp>) {
                if (response.isSuccessful &&  response.body()!!.status) {
                    AppController.getSharedPref().edit().putBoolean("isLogin",true).apply()
                    AppController.getSharedPref().edit().putString("user_id",response.body()!!.data[0].userid).apply()
                    AppController.getSharedPref().edit().putString("user_name",response.body()!!.data[0].name).apply()
                    AppController.getSharedPref().edit().putString("email",response.body()!!.data[0].email).apply()

                    Toast.makeText(ctx as Activity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE

                    startActivity(Intent(ctx as Activity, MainActivity::class.java))
                    (ctx as Activity). finish()
                } else {
                    progressBar.visibility = View.GONE
                    Toast.makeText(ctx as Activity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Responce.UserSignUp>, t: Throwable) {
                t.printStackTrace()

                progressBar.visibility = View.GONE
            }
        })
    }
    override fun afterTextChanged(p0: Editable?) {
         if(et_mobile.text.toString().length==10){
             space.visibility= View.VISIBLE
             btnGetOtp.visibility = View.VISIBLE
             btnVerifyOtp.visibility = View.GONE
             et_otp.isEnabled = true
             if(p0!!.length==4) {
                 btnGetOtp.visibility = View.GONE
                 btnVerifyOtp.visibility = View.VISIBLE
             }else{
                 btnGetOtp.visibility = View.VISIBLE
                 btnVerifyOtp.visibility = View.GONE
             }
         }else{
             et_otp.isEnabled = false
             space.visibility= View.GONE
             btnGetOtp.visibility = View.GONE
             btnVerifyOtp.visibility = View.GONE
         }

    }override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    private fun addOtp() {
        progressBar.visibility = View.VISIBLE
        apiService.addOtp("addotp",et_mobile.text.toString()).enqueue(object : Callback<Responce.Status> {
            override fun onResponse(call: Call<Responce.Status>, response: Response<Responce.Status>) {
                if (response.isSuccessful &&  response.body()!!.status) {

                    progressBar.visibility = View.GONE
                    Utility.alertDialogOtp(ctx as Activity,"Otp send on mobile number ${et_mobile.text.toString()}.Please enter OTP & verify")
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
    private fun confirmOtp() {
        progressBar.visibility = View.VISIBLE
        apiService.confirmOtp("confirmotp",et_mobile.text.toString(),et_otp.text.toString()).enqueue(object : Callback<Responce.Status> {
            override fun onResponse(call: Call<Responce.Status>, response: Response<Responce.Status>) {
                if (response.isSuccessful &&  response.body()!!.status) {
                    et_mobile.isEnabled = false
                    et_otp.isEnabled = false
                    space.visibility = View.GONE
                    btnGetOtp.visibility = View.GONE
                    btnVerifyOtp.visibility = View.GONE
                    oTpVerifier= true
                    Toast.makeText(ctx as Activity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
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
}