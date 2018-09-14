package modules.main

import Main.base.NoInternetFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.sarwan.final_year_project.R
import extras.AlertDialog
import extras.ApplicationConstants
import extras.InternetAvailability
import kotlinx.android.synthetic.main.activity_main.*
import modules.profile.ProfileActivity


class MainActivity : MainBaseActivity()
{
    private var mainActivityTabsPagerAdapter : MainTabsPagerAdapter?=null
    private var imm: InputMethodManager?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ChildActivity = this
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        checkInternetConnection()

    }

    private fun setTabLayout() {
        mainActivityTabsPagerAdapter = MainTabsPagerAdapter(supportFragmentManager)
        customViewPager.setAdapter(mainActivityTabsPagerAdapter)
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL)
        tabLayout.setOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(customViewPager))
        customViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        customViewPager.setOffscreenPageLimit(3)

    }

        override fun onCreateOptionsMenu(menu: Menu):Boolean {
            super.onCreateOptionsMenu(menu)
            return true

        }


    private val internetAvailability = InternetAvailability()


    private fun checkInternetConnection() {
        if(internetAvailability.isNetworkAvailable(this)){
            getLocation()
            setTabLayout()
        }
        else {
            showNoInternetFrame()
        }
    }
     fun showNoInternetFrame() {
        supportFragmentManager.beginTransaction().add(R.id.noInternetLayout, NoInternetFragment()).addToBackStack(null).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}