package com.bart.utility

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.support.v4.widget.CircularProgressDrawable
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.view.*
import android.widget.*
import com.jsibbold.zoomage.ZoomageView
import com.bart.R
import com.bart.activities.SignRegisterActivity
import com.bart.api.AppController
import okhttp3.MultipartBody
import java.io.File

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utility {
    @SuppressLint("SetTextI18n")
    fun alertDialog(activity : Activity, msg : String) {
        val dialog =   Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_alert)
        val tvMsg = dialog.findViewById<TextView>(R.id.tvMsg)
        tvMsg.text = msg

        val btnYes = dialog.findViewById<View>(R.id.btnYes) as Button
        val btnNo = dialog.findViewById<View>(R.id.btnNo) as Button

        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        btnYes.setOnClickListener {
            dialog.dismiss()
            AppController.getSharedPref().edit().putBoolean("isLogin",false).apply()
            AppController.getSharedPref().edit().putString("user_id","").apply()
            AppController.getSharedPref().edit().putString("user_name","").apply()
            AppController.getSharedPref().edit().putString("email","").apply()

            activity.startActivity(Intent(activity, SignRegisterActivity::class.java))
            activity.finish()
        }
        dialog.show()
    }
    @SuppressLint("SetTextI18n")
    fun alertDialogOtp(activity : Activity, msg : String) {
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

        }
        dialog.show()
    }
    fun dateTimeFormat( date : String): String{
        var outputDateStr = ""
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH)

        try {
               if(date != ""){
                   val date2 = inputFormat.parse(date)
                   outputDateStr = outputFormat.format(date2)
               }
        }
        catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputDateStr
    }

   fun dateFormat1( date : String): String{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        var outputDateStr = ""
        try {
            val date2 = inputFormat.parse(date)
            outputDateStr = outputFormat.format(date2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputDateStr
    }
    fun dateFormat2( date : String): String{
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        var outputDateStr = ""
        try {
            val date2 = inputFormat.parse(date)
            outputDateStr = outputFormat.format(date2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputDateStr
    }
    fun timeFormat( date : String): String{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        var outputDateStr = ""
        try {
            val date2 = inputFormat.parse(date)
            outputDateStr = outputFormat.format(date2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputDateStr
    }


   /* fun showDailog(activity : Activity,uri :Uri) {

        val dialog = object : Dialog(activity,android.R.style.Theme_Light){
            override fun onTouchEvent(event: MotionEvent): Boolean {
                // Tap anywhere to close dialog.
                // this.dismiss()
                return true
            }
        }
        *//* val dialog = object : Dialog(DoctorsActivity.doctorsActivity) {
             override fun onTouchEvent(event: MotionEvent): Boolean {
                 // Tap anywhere to close dialog.
                // this.dismiss()
                 return true
             }
         }*//*
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN

        *//*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               dialog.window!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(MoreActivity.self, R.color.translucent_black)))
           } else {
               dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.translucent_black)))
           }*//*


        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_view_image)
        val window = dialog.window
        val wlp = window!!.attributes

        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val zoom = dialog.findViewById<ZoomageView>(R.id.ivZoomImage)
        val circularProgressDrawable = CircularProgressDrawable(zoom.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        GlideApp.with(zoom).load(uri) .placeholder(circularProgressDrawable).error(R.drawable.no_image).into(zoom)

        zoom.setImageURI(uri)
        val ivCancel = dialog.findViewById<ImageView>(R.id.ivCancel)
        ivCancel.setOnClickListener{  dialog.dismiss() }


        dialog.show()

    }*/
    fun isValidEmail(email: String): Boolean {
       return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
   }
   fun isConnected():Boolean {
        val cm = (AppController.getInstance().applicationContext
          .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
             val activeNetwork = cm.activeNetworkInfo
          return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
    fun setTextHTML(html: String): Spanned
    {
        val result: Spanned = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
        return result
    }
    fun toast(ctx : Context){

    }
    public fun showDailog(activity : Activity,uri : Uri) {

        val dialog = object : Dialog(activity,android.R.style.Theme_Light){
            override fun onTouchEvent(event: MotionEvent): Boolean {
                // Tap anywhere to close dialog.
                // this.dismiss()
                return true
            }
        }
        /* val dialog = object : Dialog(DoctorsActivity.doctorsActivity) {
             override fun onTouchEvent(event: MotionEvent): Boolean {
                 // Tap anywhere to close dialog.
                // this.dismiss()
                 return true
             }
         }*/
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN

        /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               dialog.window!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(MoreActivity.self, R.color.translucent_black)))
           } else {
               dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.translucent_black)))
           }*/


        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_view_image)
        val window = dialog.window
        val wlp = window!!.attributes

        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val zoom = dialog.findViewById<ZoomageView>(R.id.ivZoomImage)
        val circularProgressDrawable = CircularProgressDrawable(zoom.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        GlideApp.with(zoom).load(uri) .placeholder(circularProgressDrawable).error(R.drawable.no_image).into(zoom)

        zoom.setImageURI(uri)
        val ivCancel = dialog.findViewById<ImageView>(R.id.ivCancel)
        ivCancel.setOnClickListener{  dialog.dismiss() }


        dialog.show()

    }
 }