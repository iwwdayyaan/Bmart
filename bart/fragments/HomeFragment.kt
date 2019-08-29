package com.bart.fragments

import android.annotation.SuppressLint
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
import com.bart.activities.MainActivity
import com.bart.api.AppController
import com.bart.api.Responce
import com.bart.api.interfaces.ApiService
import com.bart.interfaces.NavigationHost
import com.bart.utility.Utility
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.sign_in_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class HomeFragment : Fragment() , View.OnClickListener{
    @Inject
    internal lateinit var apiService: ApiService
    private var ctx : Context? = null

    companion object {
        fun newInstance(user_type: Int): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putInt("user_type", user_type)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btnAdd->  {
               /* (ctx as MainActivity). getToggle()?.isDrawerIndicatorEnabled = false
                (ctx as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
                (ctx as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

                (ctx as MainActivity). getToggle()?.toolbarNavigationClickListener = this*/
                ((context!! as Activity as NavigationHost).navigateTo(RegisterArtFragment.newInstance(0),true,false))
            }
            R.id.btnTransfer-> {
               /* (ctx as MainActivity). getToggle()?.isDrawerIndicatorEnabled = false
                (ctx as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
                (ctx as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

                (ctx as MainActivity). getToggle()?.toolbarNavigationClickListener = this*/
                ((context!! as Activity as NavigationHost).navigateTo(TransferWalletFragment.newInstance("HomeFragment&"),true,false))
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        return  inflater.inflate(R.layout.home_fragment, container, false)
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvName.text = "Welcome ${AppController.getSharedPref().getString("user_name", "")!!} ..!"
        btnAdd.setOnClickListener(this)
        btnTransfer.setOnClickListener(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.ctx = context
        ((context!! as Activity).application as AppController).component.inject(this@HomeFragment)
    }

}