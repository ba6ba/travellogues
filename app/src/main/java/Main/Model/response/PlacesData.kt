package Main.Model.response

import java.io.Serializable

class PlacesData  : Serializable{

    var iD : Int ? = null
    var placeName : String ? = null
    var placeCity : String ? = null
    var placeCountry : String ? = null
    var bgImage : String ? = null
    var rating : Float ? = null
    var description : String ? = null
    var attractions : String ? = null
    var location = Location()
    var districtName : String ? = null
    var startPlaceCheck : Boolean = false
    var hotel :HotelData?= null
    var restaurantName : String ? = null
    var endPlaceCheck : Boolean = false
    var checkEnabled : Boolean ? = null
    var nearBy : String ? = null
    var stayTime : Float ? = null
    var sortOrder : Int ? = null

    class Location : Serializable {
        var address : String ? = null
        var latitude : Double ? = null
        var longitude : Double ? = null
    }
}

