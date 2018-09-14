package Main.Model.response

import java.io.Serializable

class UserPlacesData  : Serializable{

    var iD : Int ? = null
    var placeName : String ? = null
    var placeCity : String ? = null
    var placeCountry : String ? = null
    var bgImage : String ? = null
    var rating : Float ? = null
    var description : String ? = null
    var attractions : String ? = null
    var location : Location? = null
    var districtName : String ? = null
    var startPlaceCheck : Boolean ? = null
    var hotel : HotelData ? = null
    var restaurantName : String ? = null
    var endPlaceCheck : Boolean ? = null


    inner class Location : Serializable {
        var address : String ? = null
        var latitude : Double ? = null
        var longitude : Double ? = null
    }
}
