package Main.extras

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.util.*

class Location {
    companion object {
        fun getCityName(context : Context, lat : Double, lon : Double) : String{
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (addresses != null && addresses.size > 0) {
                return  addresses.get(0).getLocality()
            }
            return ""
        }

        fun getAddress(context : Context, lat : Double, lon : Double) : MutableList<Address>{
            val geocoder = Geocoder(context, Locale.getDefault())
            return geocoder.getFromLocation(lat, lon, 1)
        }
    }
}