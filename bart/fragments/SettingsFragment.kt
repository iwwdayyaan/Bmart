package com.bart.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bart.R
import com.bart.api.AppController
import com.bart.api.interfaces.ApiService
import com.bart.interfaces.NavigationHost
import javax.inject.Inject

class SettingsFragment : Fragment() , View.OnClickListener{
    @Inject
    internal lateinit var apiService: ApiService
    private var ctx : Context? = null

    companion object {
        fun newInstance(user_type: Int): SettingsFragment {
            val fragment = SettingsFragment()
            val args = Bundle()
            args.putInt("user_type", user_type)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.tvForgotPwd->   ((context!! as Activity as NavigationHost).navigateTo(RegisterFragment.newInstance(0),false,false))
            R.id.btnSignin-> {

            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        return  inflater.inflate(R.layout.home_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.ctx = context
        ((context!! as Activity).application as AppController).component.inject(this@SettingsFragment)
    }

}