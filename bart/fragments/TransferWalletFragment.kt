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
import com.bart.interfaces.SelectViewPager
import kotlinx.android.synthetic.main.transfer_wallet_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class TransferWalletFragment : Fragment() , View.OnClickListener{
    @Inject
    internal lateinit var apiService: ApiService
    private var ctx : Context? = null

    companion object {
        fun newInstance(fr: String): TransferWalletFragment {
            val fragment = TransferWalletFragment()
            val args = Bundle()
            args.putString("fr", fr)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onClick(p0: View?) {
        when(p0!!.id){
        //    R.id.btnAdd->   ((context!! as Activity as NavigationHost).navigateTo(RegisterArtFragment.newInstance(0),false,false))
            R.id.btnTransfer-> validate()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        return  inflater.inflate(R.layout.transfer_wallet_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments != null){
           val fr =  arguments!!.getString("fr","").split("&")[0]
           if(fr == "MyArtFragment"){
              et_wallet_id.isEnabled = false
              et_wallet_id.setText(arguments!!.getString("fr","").split("&")[1])
          }
        }
        btnTransfer.setOnClickListener(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.ctx = context
        ((context!! as Activity).application as AppController).component.inject(this@TransferWalletFragment)
    }
    private fun validate(){
        if(!TextUtils.isEmpty(et_wallet_id.text)){
            if(!TextUtils.isEmpty(et_mobile.text)){
                if(et_mobile.text!!.length == 10){
              if(!TextUtils.isEmpty(et_wallet_name.text)){
                if(!TextUtils.isEmpty(et_value.text)){
                    wallet()
                }else{
                    Toast.makeText(ctx as Activity, "Please enter value!", Toast.LENGTH_SHORT).show()
                }
                }else{
                    Toast.makeText(ctx as Activity, "Please enter wallet name!", Toast.LENGTH_SHORT).show()
                }
                }else{
                    Toast.makeText(ctx as Activity, "Please enter 10 digit mobile number!", Toast.LENGTH_SHORT).show()
                }
                }else{
                Toast.makeText(ctx as Activity, "Please enter mobile number!", Toast.LENGTH_SHORT).show()
              }

            }else{
            Toast.makeText(ctx as Activity, "Please enter wallet id!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun wallet() {
        progressBar.visibility = View.VISIBLE
        apiService.wallet("wallet",AppController.getSharedPref().getString("user_id",""),et_wallet_id.text.toString(),et_wallet_name.text.toString(),
                et_mobile.text.toString(),et_value.text.toString()).enqueue(object : Callback<Responce.Status> {
            override fun onResponse(call: Call<Responce.Status>, response: Response<Responce.Status>) {
                if (response.isSuccessful && response.body()!!.status) {
                  //  Toast.makeText(ctx as Activity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    (context!! as Activity as SelectViewPager).currentItem(0)
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