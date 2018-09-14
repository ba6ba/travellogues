package modules.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.support.v4.app.ActivityCompat
import base.ParentActivity
import extras.ApplicationConstants
import android.location.Geocoder
import java.util.*


open class MainBaseActivity : ParentActivity() {

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
                val addresses = getAddress(location.latitude,location.longitude)
                profile?.latitude = location.latitude
                profile?.longitude = location.longitude
                profile?.address = addresses[0].getAddressLine(0)
                profile?.cityName= addresses.get(0).getLocality()
                profile?.countryName = addresses[0].getAddressLine(2)
                saveProfileInSharedPreference()
            }
        }
    }

    private fun getAddress(lat : Double, lon : Double) : MutableList<Address>{
        val geocoder = Geocoder(this, Locale.getDefault())
        return geocoder.getFromLocation(lat, lon, 1)
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
