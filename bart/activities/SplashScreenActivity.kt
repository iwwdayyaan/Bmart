package com.bart.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.bart.R
import com.bart.api.AppController

import kotlinx.android.synthetic.main.splash_screen_activity.*


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.splash_screen_activity)
        startAnimations()

    }
    override fun onBackPressed() {
      super.onBackPressed()
      finish()
  }

    private fun startAnimations() {
        var anim = AnimationUtils.loadAnimation(this, R.anim.alpha)
        anim.reset()

      flSplash.clearAnimation()
      flSplash.startAnimation(anim)

        anim = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        anim.reset()

        ivSplash.clearAnimation()
        ivSplash.startAnimation(anim)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }
            override fun onAnimationEnd(animation: Animation) {
                try {

                    if(AppController.getSharedPref().getBoolean("isLogin", false)){
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    }else{
                        startActivity(Intent(applicationContext, SignRegisterActivity::class.java))
                        finish()
                    }

                } catch (e : InterruptedException ) {
                    e.printStackTrace()
                }
            }
            override fun onAnimationRepeat(animation: Animation) {
            }
        })

    }
}
