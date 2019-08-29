package com.bart.activities

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.bart.R
import com.vistrav.ask.Ask
import com.bart.fragments.RegisterFragment
import com.bart.fragments.SigninFragment
import com.bart.interfaces.NavigationHost
import kotlinx.android.synthetic.main.content_main_login_reg.*
import kotlinx.android.synthetic.main.login_signup_activity.*

class SignRegisterActivity : AppCompatActivity() , NavigationHost,View.OnClickListener {
    private var doubleBackToExitPressedOnce = false
    val INT_ID_OF_YOUR_REQUEST : Int = 20
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_signup_activity)

        Ask.on(this)
                .id(INT_ID_OF_YOUR_REQUEST) // in case you are invoking multiple time Ask from same activity or fragment
                .forPermissions(Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withRationales("Access Network State permission need for app to work properly") //optional
                .go()
        btnReg.setOnClickListener(this)
        btnSignin.setOnClickListener(this)

    }

    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean, addOrReplace: Boolean) {
        var transaction : FragmentTransaction? = null

        if(addOrReplace){
            transaction = supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, fragment)
        }else{
            transaction = supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment)
        }
      if (addToBackstack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            if(supportFragmentManager.backStackEntryCount ==1){
                if (doubleBackToExitPressedOnce) {
                    finish()
                    return
                }
                this.doubleBackToExitPressedOnce = true
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

                Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)

            }else{
                supportFragmentManager.popBackStack()
            }
         } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)

        }
      }
    override fun onClick(p0: View?) {
        when(p0!!.id){
           R.id.btnReg->   navigateTo(RegisterFragment.newInstance(0),true,false)
           R.id.btnSignin -> navigateTo(SigninFragment.newInstance(0),false,false)
        }
    }

}
