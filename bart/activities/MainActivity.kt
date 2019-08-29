 package com.bart.activities

 import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.bart.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.os.StrictMode
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.*
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bart.api.AppController
import com.bart.fragments.*
import com.bart.interfaces.NavigationHost
import com.bart.interfaces.SelectViewPager
import com.bart.utility.Utility
import kotlinx.android.synthetic.main.content_main.*

 class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener ,
         NavigationHost,ViewPager.OnPageChangeListener,SelectViewPager {
     private var doubleBackToExitPressedOnce = false
     private  var toggle : ActionBarDrawerToggle? = null
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         title=""
         setContentView(R.layout.activity_main)
         setSupportActionBar(toolbar)

         toggle = ActionBarDrawerToggle(
                 this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
         drawer_layout.addDrawerListener(toggle!!)
         toggle!!.syncState()

         nav_view.setNavigationItemSelectedListener(this)
         navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

         viewPager.adapter = PagerAdapter(supportFragmentManager )
         viewPager.offscreenPageLimit = 4
         viewPager.addOnPageChangeListener(this)
         val header = nav_view.getHeaderView(0)
         val name = header.findViewById<View>(R.id.tvName) as TextView
         name.text = AppController.getSharedPref().getString("user_name", "")!!

         val email = header.findViewById<View>(R.id.tvEmail) as TextView
         email.text = AppController.getSharedPref().getString("email", "")!!
         viewPager.addOnPageChangeListener(this)

     }

     override fun onBackPressed() {
         if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
             drawer_layout.closeDrawer(GravityCompat.START)
         } else {
             if(supportFragmentManager.backStackEntryCount==1){
                 toggle?.isDrawerIndicatorEnabled = true
                 supportActionBar?.setDisplayHomeAsUpEnabled(false)
                 supportActionBar?.setDisplayShowHomeEnabled(false)
                  toggle?.toolbarNavigationClickListener = toggle?.toolbarNavigationClickListener
             }
             if (doubleBackToExitPressedOnce) {
                 super.onBackPressed()
                 return
             }
             this.doubleBackToExitPressedOnce = true
             Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

             Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)

         }
     }

     override fun onCreateOptionsMenu(menu: Menu): Boolean {
         // Inflate the menu; this adds items to the action bar if it is present.
         menuInflater.inflate(R.menu.main, menu)
         return true
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         // Handle action bar item clicks here. The action bar will
         // automatically handle clicks on the Home/Up button, so long
         // as you specify a parent activity in AndroidManifest.xml.
         when (item.itemId) {
             R.id.action_share -> {
                 try {
                     ShareCompat.IntentBuilder.from(this)
                             .setType("text/plain")
                             .setChooserTitle("Share")
                             .setText("http://play.google.com/store/apps/details?id=$packageName")
                             .startChooser()
                 } catch (e: Exception) {
                     //e.toString();
                 }
                 return true
             }
             else -> return super.onOptionsItemSelected(item)
         }
     }

     override fun onNavigationItemSelected(item: MenuItem): Boolean {
         // Handle navigation view item clicks here.
           when (item.itemId) {

            R.id.nav_home -> {
                when(supportFragmentManager.backStackEntryCount){
                    1->  supportFragmentManager.popBackStack()
                }
                viewPager.currentItem = 0
                navigation.menu.getItem(0).isChecked = true
            }
            R.id.nav_my_art -> {
                when(supportFragmentManager.backStackEntryCount){
                    1->  supportFragmentManager.popBackStack()
                }
                viewPager.currentItem = 1
                navigation.menu.getItem(1).isChecked = true
            }
               R.id.nav_txn_history -> {
                   when(supportFragmentManager.backStackEntryCount){
                       1->  supportFragmentManager.popBackStack()
                   }
                   viewPager.currentItem = 2
                   navigation.menu.getItem(2).isChecked = true
               }
            R.id.nav_transfer -> {
              //  toggle?.isDrawerIndicatorEnabled = false
             //   supportActionBar?.setDisplayHomeAsUpEnabled(true)
              //  supportActionBar?.setDisplayShowHomeEnabled(true)
             //   toggle?.toolbarNavigationClickListener = toggle?.toolbarNavigationClickListener
            //    toggle?.toolbarNavigationClickListener = this
                when(supportFragmentManager.backStackEntryCount){
                    1->  supportFragmentManager.popBackStack()
                }
                navigateTo(TransferWalletFragment.newInstance("Drawer&"),true,false)
            }

            R.id.nav_logout -> {
                Utility.alertDialog(this,"Do you want to logout?")
            }
        }

         drawer_layout.closeDrawer(GravityCompat.START)
         return true
     }
     private inner class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
         override fun getItem(position: Int): Fragment {
             when(position){
                 0 ->return  HomeFragment.newInstance(0)
                 1->  return MyArtFragment.newInstance(0)
                 2 -> return HistoryFragment.newInstance(0)
                 3 -> return ProfileFragment.newInstance(0)
             }
             return null!!
         }

         override fun getCount(): Int {
             return 4
         }

         override fun getItemPosition(p : Any): Int {
             // POSITION_NONE makes it possible to reload the PagerAdapter
             return POSITION_NONE
         }

     }
     override fun onPageScrollStateChanged(p0: Int) {

     }

     override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

     }

     override fun onPageSelected(p0: Int) {
         when(supportFragmentManager.backStackEntryCount){
             1->  supportFragmentManager.popBackStack()
         }
         nav_view.menu.getItem(p0).isChecked = true
         navigation.menu.getItem(p0).isChecked = true
     }
     private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
         when (item.itemId) {
             R.id.navigation_home -> {
                 //   navigationViewClick!!.selected(0)
                 when(supportFragmentManager.backStackEntryCount){
                     1->  supportFragmentManager.popBackStack()
                 }


                 viewPager.currentItem = 0
                 nav_view.menu.getItem(0).isChecked = true

                 return@OnNavigationItemSelectedListener true
             }
             R.id.navigation_my_art -> {

                 when(supportFragmentManager.backStackEntryCount){
                     1->  supportFragmentManager.popBackStack()
                 }

                 viewPager.currentItem = 1
                 nav_view.menu.getItem(1).isChecked = true

                 return@OnNavigationItemSelectedListener true
             }

             R.id.navigation_history -> {
                 when(supportFragmentManager.backStackEntryCount){
                     1->  supportFragmentManager.popBackStack()
                 }

                 viewPager.currentItem = 2
                 nav_view.menu.getItem(2).isChecked = true


                 return@OnNavigationItemSelectedListener true
             }

             R.id.navigation_profile -> {
                 when(supportFragmentManager.backStackEntryCount){
                     1->  supportFragmentManager.popBackStack()
                 }
                 viewPager.currentItem = 3
                 return@OnNavigationItemSelectedListener true
             }

         }
         false
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
     override fun onStart() {
         super.onStart()
         //Allowing Strict mode policy for Nougat support
         val builder = StrictMode.VmPolicy.Builder()
         StrictMode.setVmPolicy(builder.build())
     }

  override fun currentItem(item: Int) {
      when(supportFragmentManager.backStackEntryCount){
          1->  {
              supportFragmentManager.popBackStack()
              viewPager.adapter!!.notifyDataSetChanged()
          }
      }
      viewPager.currentItem = item
      nav_view.menu.getItem(item).isChecked = true
      navigation.menu.getItem(item).isChecked = true
  }


 }



