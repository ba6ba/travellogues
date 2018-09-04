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
    var location : Location? = null

    inner class Location : Serializable {
        var address : String ? = null
        var latitude : String ? = null
        var longitude : String ? = null
    }
}