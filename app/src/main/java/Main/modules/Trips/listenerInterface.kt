package Main.modules.Trips

import Main.Model.response.HotelData
import Main.Model.response.RestaurantData

interface PlacesRecyclerViewClickListener {

    fun onPlaceCardSelected(position : Int, selection : String)
    fun onPlaceCardDeSelected(position : Int, selection : String)
}

interface HotelRecyclerViewClickListener {

    fun onHotelCardSelected(position : Int, selection : String)
    fun onHotelCardDeSelected(position : Int, selection : String)
}

interface CabRecyclerViewClickListener {

    fun onCabCardSelected(position : Int, selection : String)
    fun onCabCardDeSelected(position : Int, selection : String)
}

interface RestaurantRecyclerViewClickListener {

    fun onRestaurantCardSelected(position : Int, selection : String)
    fun onRestaurantCardDeSelected(position : Int, selection : String)
}

interface ClickListener {

    fun onCardSelected(position : Int, indicator : String, hotelObj : HotelData?, restaurantObj: RestaurantData?)
    fun onCardDeSelected(position : Int, indicator : String)
}