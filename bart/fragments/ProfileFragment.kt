package com.bart.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bart.R
import com.bart.api.AppController
import com.bart.api.Responce
import com.bart.api.interfaces.ApiService
import com.bart.interfaces.NavigationHost
import com.bart.utility.Utility
import kotlinx.android.synthetic.main.profile_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ProfileFragment : Fragment() , View.OnClickListener{

    @Inject
    internal lateinit var apiService: ApiService
    private var ctx : Context? = null

    companion object {
        fun newInstance(user_type: Int): ProfileFragment {
            val fragment = ProfileFragment()
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
            R.id.btnReg -> {
                validate()
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        return  inflater.inflate(R.layout.profile_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchProfile()
    //    tvAlreadyAcct.setOnClickListener(this)
        btnReg.text = "Edit"
        btnReg.setOnClickListener(this)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.ctx = context
        ((context!! as Activity).application as AppController).component.inject(this@ProfileFragment)
    }
    private fun validate(){
        if(!TextUtils.isEmpty(et_name.text)){
            if(!TextUtils.isEmpty(et_mobile.text)){
                if (et_mobile.text!!.length ==10) {
                    if (!TextUtils.isEmpty(et_email.text)) {
                        if (Utility.isValidEmail(et_email.text.toString())) {
                            if (!TextUtils.isEmpty(et_pwd.text)) {
                                if (!TextUtils.isEmpty(et_otp.text)) {
                                    if (!TextUtils.isEmpty(et_passport.text)) {
                                        if (Utility.isConnected()) {
                                            updateUserProfile()
                                        } else {
                                            Toast.makeText(activity, "No internet connection!", Toast.LENGTH_SHORT).show()
                                        }

                                    } else {
                                        Toast.makeText(activity, "Please enter passport number!", Toast.LENGTH_SHORT).show()
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
    private fun updateUserProfile() {

        progressBar.visibility = View.VISIBLE
        apiService.updateUserProfile("updateUserProfile",et_name.text.toString(),et_mobile.text.toString(),et_email.text.toString(),et_pwd.text.toString(),
                et_otp.text.toString(),et_passport.text.toString(),AppController.getSharedPref().getString("user_id","")).enqueue(object : Callback<Responce.Status> {
            override fun onResponse(call: Call<Responce.Status>, response: Response<Responce.Status>) {
                if (response.isSuccessful &&  response.body()!!.status) {

                    AppController.getSharedPref().edit().putString("user_name",et_name.text.toString()).apply()
                    AppController.getSharedPref().edit().putString("email",et_email.text.toString()).apply()

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
    private fun fetchProfile() {

        progressBar.visibility = View.VISIBLE
        apiService.featProfile("featprofile",AppController.getSharedPref().getString("user_id","")).enqueue(object : Callback<Responce.FeatProfile> {
            override fun onResponse(call: Call<Responce.FeatProfile>, response: Response<Responce.FeatProfile>) {
                if (response.isSuccessful &&  response.body()!!.status) {
                    et_name.setText(response.body()!!.data[0].name)
                    et_mobile.setText(response.body()!!.data[0].phoneno)
                    et_email.setText(response.body()!!.data[0].email)

                    et_pwd.setText(response.body()!!.data[0].password)
                    et_otp.setText(response.body()!!.data[0].otp)
                    et_passport.setText(response.body()!!.data[0].paasport)


                    Toast.makeText(ctx as Activity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                } else {
                    progressBar.visibility = View.GONE
                    Toast.makeText(ctx as Activity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Responce.FeatProfile>, t: Throwable) {
                t.printStackTrace()

                progressBar.visibility = View.GONE
            }
        })
    }

}