package Main.Model.response

import java.io.Serializable

class RestaurantData : Serializable {
    var iD : Int ? = null
    var bannerImage : String ? = null
    var restaurantScreenShots : ArrayList<String> ? = null
    var restaurantName : String ? = null
    var restaurantCityName : String ? = null
    var restaurantRating : Float ? = null
    var phoneNumber : String ? = null
    var emailAddress : String ? = null
    var website : String ? = null
    var description : String ? = null
    var location : Location? = null
    var restaurantCategory : String ? = null
    var restaurantPrice : Int ? = null
    var checkEnabled : Boolean = false

    inner class Location : Serializable {
        var address : String ? = null
        var latitude : Double ? = null
        var longitude : Double ? = null
    }
}
