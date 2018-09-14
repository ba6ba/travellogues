package Main.modules.plan.Trips

import Main.Model.response.HotelData
import Main.Model.response.PlacesData
import Main.Model.response.RestaurantData

interface PlacesRecyclerViewClickListener {

    fun onPlaceCardSelected(startPlace : String?, endPlace : String?)
    fun onPlaceCardDeSelected(startPlace : String?, endPlace : String?)
}

interface StartAndEndListener {

    fun onStart(name : String?)
    fun onEnd(name : String?)

}

interface PlacesListener {

    fun onSelected(iD : Int, vararg selection: String) : Boolean
    fun onDeSelected(iD : Int) : Boolean
    fun onRemove(position : Int)
}

interface FragmentInteraction{
    fun onAttach(placesBundle: PlacesData?, hotelBundle: HotelData?,restBundle: RestaurantData?,position: Int)
}

interface HotelRecyclerViewClickListener {

    fun onHotelCardSelected(position : Int, selection : String)
    fun onHotelCardDeSelected(position : Int, selection : String)
}

interface CabRecyclerViewClickListener {
    fun onCabCardSelected(position : Int, selection : String)
}

interface RestaurantRecyclerViewClickListener {

    fun onRestaurantCardSelected(position : Int, selection : String)
    fun onRestaurantCardDeSelected(position : Int, selection : String)
}

interface ClickListener {

    fun onCardSelected(position : Int, indicator : String, hotelObj : HotelData?, restaurantObj: RestaurantData?)
    fun onCardDeSelected(position : Int, indicator : String)
}

interface AdapterListener {

    fun callBackOnSelect()
    fun callBackOnDeSelect()
}