package Main.utils

import android.location.Location
import extras.ApplicationConstants
import org.androidannotations.annotations.App
import kotlin.math.roundToLong

class DistanceUtility {
    companion object {
        fun calculateDistance(userLat : Double, userLon : Double , lat : Double,lon : Double): Long {
            val firstLocation = Location("")
            firstLocation.setLatitude(userLat)
            firstLocation.setLongitude(userLon)

            val secondLocation = Location("")
            secondLocation.setLatitude(lat)
            secondLocation.setLongitude(lon)
            val distanceInKiloMeters = (firstLocation.distanceTo(secondLocation))/1000
            return distanceInKiloMeters.roundToLong()
        }



        fun calculateTravelTime(distance : Long, localTravellingSpeed : Boolean) : String
        {   var travellingSpeed = 0.0
            if(localTravellingSpeed){
                travellingSpeed = ApplicationConstants.LOCAL_TRAVELLING_SPEED
            }
            else travellingSpeed = ApplicationConstants.CITY_TRAVELLING_SPEED

            val hours = (((distance*1000)/travellingSpeed)/3600).toLong().toString()
            val minutes = ((((distance*1000)/travellingSpeed)/60)%60).toLong().toString()
            return hours + ApplicationConstants.HOURS + minutes + ApplicationConstants.MINUTES
        }

        private fun deg2rad(deg: Double): Double {
            return deg * Math.PI / 180.0
        }

        private fun rad2deg(rad: Double): Double {
            return rad * 180.0 / Math.PI
        }
    }
}

/* /*val theta = userLon - lon
            var dist = Math.sin(deg2rad(userLat)) * Math.sin(deg2rad(lat)) + (Math.cos(deg2rad(userLat))
                    * Math.cos(deg2rad(lat))
                    * Math.cos(deg2rad(theta)))
            dist = Math.acos(dist)
            dist = rad2deg(dist)
            dist = dist * 60.0 * 1.1515
            return dist.roundToLong()*/*/