package com.bart.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bart.R
import com.bart.api.AppConstants
import com.bart.api.AppController
import com.bart.api.Responce
import com.bart.api.interfaces.ApiService
import com.bart.interfaces.NavigationHost
import com.bart.recyclerview.Kadapter
import com.bart.recyclerview.setUp
import com.bart.utility.GlideApp
import com.bart.utility.Utility
import kotlinx.android.synthetic.main.history_fragment.*
import kotlinx.android.synthetic.main.history_fragment_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class HistoryFragment  : Fragment(){
    @Inject
    internal lateinit var apiService: ApiService
    var rr : MutableList<Responce.History.Data>? = null
    var ctx : Context? = null
    var section_number : Int? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.ctx = context
        ((context!! as Activity).application as AppController).component.inject(this@HistoryFragment)
    }

    companion object {
        fun newInstance(sectionNumber: Int): HistoryFragment {
            val fragment = HistoryFragment()
            val args = Bundle()
            args.putInt("section_number", sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        return  inflater.inflate(R.layout.history_fragment, container, false)
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        section_number = arguments!!.getInt("section_number")
        if(Utility.isConnected()){
            history()
        }else{
            tvNoData.visibility = View.VISIBLE
            tvNoData.text = "No internet Connect!!"
            rv.visibility = View.GONE
            swiperefresh.isRefreshing = false
        }

        rr = mutableListOf()

        rv!!.setUp(rr!!, R.layout.history_fragment_item, { it1 ->
            this.tvWalletId.text = it1.walletid
            this.tvArtGallery.text = it1.art_gallery_name
            this.tvVal.text = it1.value
            this.tvDop.text = Utility.dateFormat1(it1.purchase_date)
            this.tvPurchaser.text = it1.purchasename
            this.tvPurchageValue.text = it1.purchaseValue
            val circularProgressDrawable = CircularProgressDrawable(ctx as Activity)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            GlideApp.with(this@HistoryFragment).load(Uri.parse(AppConstants.BASE_URL+"uploads/artpic/"+it1.artpic)) .placeholder(circularProgressDrawable).error(R.drawable.no_image).into(ivArt)
            this.ivArt.setOnClickListener { Utility.showDailog(ctx as Activity, Uri.parse(AppConstants.BASE_URL+"uploads/artpic/"+it1.artpic)) }

        }, { view1: View, i: Int ->

        })

        val linearLayoutManager = LinearLayoutManager(ctx as Activity)
        rv.layoutManager = linearLayoutManager

       swiperefresh.setOnRefreshListener { history() }
    }
    @SuppressLint("SetTextI18n")
    private fun history() {
        try {
            rv.visibility = View.VISIBLE
            tvNoData.visibility = View.GONE
            swiperefresh.isRefreshing = true
            apiService.history("history", AppController.getSharedPref().getString("user_id","")).enqueue(object : Callback<Responce.History> {
                override fun onResponse(call: Call<Responce.History>, response: Response<Responce.History>) {
                    if (response.isSuccessful && response.body()!!.status) {
                        rr!!.clear()
                        rr!!.addAll(response.body()!!.data)
                        rv!!.adapter!!.notifyDataSetChanged()

                        tvNoData.visibility = View.GONE
                        rv.visibility = View.VISIBLE
                        swiperefresh.isRefreshing = false
                    } else {
                        tvNoData.visibility = View.VISIBLE
                        tvNoData.text = "No History"
                        rv.visibility = View.GONE
                        swiperefresh.isRefreshing = false

                    }
                }
                override fun onFailure(call: Call<Responce.History>, t: Throwable) {
                    t.printStackTrace()
                    tvNoData.visibility = View.VISIBLE
                    tvNoData.text = "Exception onFailure(): $t"
                    rv.visibility = View.GONE
                    swiperefresh.isRefreshing = false
                }
            })
        } catch(e : Exception){
            tvNoData.text = "Exception in try: $e"
            Log.d("",""+e)
        }

    }
}