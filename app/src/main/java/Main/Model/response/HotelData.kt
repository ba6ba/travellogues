package Main.Model.response

import java.io.Serializable

class HotelData : Serializable{

    var iD : Int ? = null
    var bannerImage : String ? = null
    var insideCardImage : ArrayList<String> ? = null
    var hotelName : String ? = null
    var hotelCityName : String ? = null
    var hotelCountryName : String ? = null
    var rating : Float ? = null
    var phoneNumber : String ? = null
    var emailAddress : String ? = null
    var website : String ? = null
    var description : String ? = null
    var location : Location? = null
    var hotelPrice : Int ? = null

    inner class Location : Serializable {
        var address : String ? = null
        var latitude : String ? = null
        var longitude : String ? = null
    }
}