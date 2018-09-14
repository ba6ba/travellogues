package network


import Main.Model.body.TravellerBody
import Main.Model.response.*
import retrofit2.Call
import retrofit2.http.*



interface WebServices {

    @GET(NetworkConstants.API_PUBLIC+NetworkConstants.API_VERSION+ApiEndPoints.PLACES)
    fun getPlaces(): Call<PlacesResponse>

    @GET(NetworkConstants.API_PUBLIC+NetworkConstants.API_VERSION+ApiEndPoints.HOTELS)
    fun getHotels(): Call<HotelsResponse>

    @GET(NetworkConstants.API_PUBLIC+NetworkConstants.API_VERSION+ApiEndPoints.RESTAURANTS)
    fun getRestaurants(): Call<RestaurantResponse>

    @GET(NetworkConstants.API_PUBLIC+NetworkConstants.API_VERSION+ApiEndPoints.CABS)
    fun getCabs(): Call<CabResponse>

    @POST(NetworkConstants.API_PUBLIC+NetworkConstants.API_VERSION+ApiEndPoints.TRAVELLER_INFO)
    fun postTravellerInfo(@Body travellerBody : TravellerBody): Call<TravellerInfoResponse>

    @GET(NetworkConstants.API_PUBLIC+NetworkConstants.API_VERSION+ApiEndPoints.ALL_DISTRICTS)
    fun getAllDistricts(): Call<AllDistrictsResponse>
}

