package modules.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.support.v4.app.ActivityCompat
import android.util.Log
import base.ParentActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.mobitribe.app.ezzerecharge.network.RestClient
import extras.ApplicationConstants
import network.ApiEndPoints
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utils.HashUtility


open class MainBaseActivity : ParentActivity(){


    protected var ChildActivity: MainActivity? = null
    private val TAG = "Main Activity"
    private var locationManager: LocationManager? = null


    fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ApplicationConstants.REQUEST_LOCATION)

        } else {
            val location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {
                profile?.latitude = location.latitude
                profile?.longitude = location.longitude
                saveProfileInSharedPreference()
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ApplicationConstants.REQUEST_LOCATION -> {
                getLocation()
            }
        }
    }

}
