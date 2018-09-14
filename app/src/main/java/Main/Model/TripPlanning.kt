package Main.Model

import Main.Model.response.*
import java.io.Serializable

class TripPlanning : Serializable{

    var iD : Int ? = null
    var tripName : String ? = null
    var startDate : String ? =null
    var endDate : String ? =null
    var adults : String ? =null
    var childrens : String ? =null
    var places : ArrayList<PlacesData> ? =null
    var hotel : HotelData ? =null
    var restaurant : RestaurantData ? =null
    var startPlace : PlacesData ? = null
    var endPlace : PlacesData ? = null
    var cab : CabData ? =null
    var districts : ArrayList<String> ? =null
    var noOfDays : Int ? = null
    var preference : String ? = null
    var noOfGuests : Int ? = null
}