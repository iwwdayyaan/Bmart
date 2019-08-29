package com.bart.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ShareCompat
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bart.R
import com.bart.activities.MainActivity
import com.bart.api.AppConstants
import com.bart.api.AppController
import com.bart.api.Responce
import com.bart.api.interfaces.ApiService
import com.bart.recyclerview.setUp
import com.bart.utility.GlideApp
import com.bart.utility.Utility
import kotlinx.android.synthetic.main.my_art_fragment.*
import kotlinx.android.synthetic.main.my_art_fragment_item.view.*
import kotlinx.android.synthetic.main.register_your_art_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class MyArtFragment : Fragment(){
    @Inject
    internal lateinit var apiService: ApiService

    private var rr : MutableList<Responce.FeatchArt.Data>? = null

    private var ctx : Context? = null
    private var section_number : Int? = null

   override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.ctx = context
        ((context!! as Activity).application as AppController).component.inject(this@MyArtFragment)
    }

    companion object {
        fun newInstance(sectionNumber: Int): MyArtFragment {
            val fragment = MyArtFragment()
            val args = Bundle()
            args.putInt("section_number", sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        return  inflater.inflate(R.layout.my_art_fragment, container, false)
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        section_number = arguments!!.getInt("section_number")
        if(Utility.isConnected()){
            featchArt()
        }else{
            tvNoData.visibility = View.VISIBLE
            tvNoData.text = "No internet Connect!!"
            rv.visibility = View.GONE
            swiperefresh.isRefreshing = false
        }

        rr = mutableListOf()

        rv!!.setUp(rr!!, R.layout.my_art_fragment_item, { it1 ->
            this.tvWalletId.text = it1.artid
            this.tvArtGallery.text = it1.artGalleryName
            this.tvVal.text = it1.value
            this.tvDop.text = Utility.dateFormat1(it1.dop)
            this.tvOwner.text = it1.ownerName
            this.tvPainter.text = it1.painterName
            val circularProgressDrawable = CircularProgressDrawable(ctx as Activity)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            GlideApp.with(this@MyArtFragment).load(Uri.parse(AppConstants.BASE_URL+"uploads/artpic/"+it1.artpic)).placeholder(circularProgressDrawable).error(R.drawable.no_image).into(ivArt)
            this.ivArt.setOnClickListener { Utility.showDailog(ctx as Activity, Uri.parse(AppConstants.BASE_URL+"uploads/artpic/"+it1.artpic)) }

            val shareText = "Wallet Id: "+it1.artid+", Art Gallery: "+it1.artGalleryName+", Art Value: "+it1.value+", Purchase Date: "+Utility.dateFormat1(it1.dop)+
                    ", Owner: "+it1.ownerName+", Painter: "+it1.painterName

            this.tvTransfer.setOnClickListener {
                (ctx as MainActivity).navigateTo(TransferWalletFragment.newInstance("MyArtFragment&${it1.artid}"),true,false) }

            this.tvShare.setOnClickListener {
             try {
                ShareCompat.IntentBuilder.from(ctx as Activity)
                        .setType("text/plain")
                        .setChooserTitle("Share")
                        .setText(shareText)
                        .startChooser()
            } catch (e: Exception) {

            } }

        }, { view1: View, i: Int ->

        })

        val linearLayoutManager = LinearLayoutManager(ctx as Activity)
        rv.layoutManager = linearLayoutManager

        swiperefresh.setOnRefreshListener { featchArt() }
    }
    @SuppressLint("SetTextI18n")
    private fun featchArt() {

        try {
            rv.visibility = View.VISIBLE
            tvNoData.visibility = View.GONE
            swiperefresh.isRefreshing = true
            apiService.featchArt("featchart", AppController.getSharedPref().getString("user_id","")).enqueue(object : Callback<Responce.FeatchArt> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<Responce.FeatchArt>, response: Response<Responce.FeatchArt>) {
                    if (response.isSuccessful && response.body()!!.status) {
                        rr!!.clear()
                        rr!!.addAll(response.body()!!.data)
                        rv!!.adapter!!.notifyDataSetChanged()

                        tvNoData.visibility = View.GONE
                        rv.visibility = View.VISIBLE
                        swiperefresh.isRefreshing = false

                       } else {
                        tvNoData.visibility = View.VISIBLE
                        tvNoData.text = "No Art"
                        rv.visibility = View.GONE
                        swiperefresh.isRefreshing = false

                    }
                }

                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<Responce.FeatchArt>, t: Throwable) {
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