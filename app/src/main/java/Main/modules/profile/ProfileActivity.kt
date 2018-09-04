package modules.profile

import Main.base.NoInternetFragment
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import com.google.firebase.auth.FirebaseAuth
import extras.AlertDialog
import extras.InternetAvailability
import kotlinx.android.synthetic.main.profile_activity_layout.*
import modules.authentication.LoginActivity

class ProfileActivity : ParentActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity_layout)
        auth = FirebaseAuth.getInstance()
        alertDialog = AlertDialog()
        checkInternetConnection()
    }

    private val internetAvailability = InternetAvailability()

    private fun checkInternetConnection() {
        if(internetAvailability.isNetworkAvailable(this)){
            setToolbar()
            setFields()
            clickListeners()
        }
        else {
            showNoInternetFrame()
        }
    }

    private fun setFields() {
        profileName?.text = profile?.firstName + " "+profile?.lastName
        profileCategory?.text = profile?.travellingCategory
    }

    private var alertDialog : AlertDialog ? = null

    private fun clickListeners() {
        myTrips.setOnClickListener {
            alertDialog?.serviceNotAvailable(this)
        }

        myWallet.setOnClickListener {
            alertDialog?.serviceNotAvailable(this)
        }

        myCards.setOnClickListener {
            alertDialog?.serviceNotAvailable(this)
        }

        scanQR.setOnClickListener {
            alertDialog?.serviceNotAvailable(this)
        }

        changePassword.setOnClickListener {
            alertDialog?.serviceNotAvailable(this)
        }

        logout.setOnClickListener {
            auth?.signOut()
            profile?.isLoggedIn = false
            saveProfileInSharedPreference()
            openActivityWithFinish(Intent(this,LoginActivity::class.java))
            finish()
        }
    }

    private var auth : FirebaseAuth?=null

    private fun setToolbar() {
        setSupportActionBar(profileToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    fun showNoInternetFrame() {
        supportFragmentManager.beginTransaction().add(R.id.noInternetLayout, NoInternetFragment()).addToBackStack(null).commit()
    }
}