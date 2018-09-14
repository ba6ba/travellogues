package modules.Planning


import Main.Model.response.*
import Main.extras.ImageSetter
import Main.modules.plan.Trips.ClickListener
import Main.modules.plan.Trips.FragmentInteraction
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import com.mobitribe.app.ezzerecharge.network.RestClient
import extras.AlertDialog
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.destination_fragment.*
import modules.destinations.SuggestionHotelsAdapter
import modules.destinations.SuggestionRestaurantsAdapter
import modules.plan.HotelOrRestaurantDetailsActivity
import retrofit2.Call
import retrofit2.Response
import utils.ValidationUtility
import java.util.*

class DetailsActivity : ParentActivity(), ClickListener , FragmentInteraction {

    private var placesObject : PlacesData? = null

    private lateinit var HotelslayoutManager : LinearLayoutManager
    private lateinit var RestaurantlayoutManager : LinearLayoutManager

    private var hotelsDataList = ArrayList<HotelData?>()
    private var restaurantDataList = ArrayList<RestaurantData?>()

    private lateinit var hotelsAdapter : SuggestionHotelsAdapter
    private lateinit var restaurantsAdapter : SuggestionRestaurantsAdapter

    private var guestsCount = HashMap<String,String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.destination_fragment)
        placesObject = intent.extras?.getSerializable(ApplicationConstants.PLACES_OBJECT_KEY) as PlacesData
        guestsCount = intent.extras?.get(ApplicationConstants.GUESTS) as HashMap<String, String>
        initializeLayoutView()
        setupSpinners()
        makingUpLayout()
        clickListener()
        setRecyclerViews()
    }

    private var hotelSortBy : String ? = null
    private var restSortBy : String ? = null
    private var alertDialog = AlertDialog()
    private fun setupSpinners() {
        filterRes.setOnClickListener {
            restSortBy = alertDialog.filterBuilder(this)
        }
        filterHotel.setOnClickListener {
            hotelSortBy = alertDialog.filterBuilder(this)
        }
    }

    private fun clickListener() {
        backButton.setOnClickListener {
            onBackPressed()
        }
        doneButton.setOnClickListener {
            val intent = Intent()
            bundle.putSerializable(ApplicationConstants.HOTEL_OBJECT_KEY,hotelData)
            bundle.putSerializable(ApplicationConstants.RESTAURANT_OBJECT_KEY,restaurantData)
            intent.putExtra(ApplicationConstants.DETAILS_OBJECT_KEY,bundle)
            setResult(ApplicationConstants.DETAILS_ACTIVITY_REQUEST_CODE, intent)
            finish()
        }
    }

    private val bundle = Bundle()

    private fun makingUpLayout() {
        ImageSetter.set(this,placesObject?.bgImage!!,groundImage,null)
        destinationNameGround.text = placesObject?.placeName
        destinationAddressGround.text = placesObject?.location?.address
    }

    private fun initializeLayoutView() {
        doneButton.isEnabled = false

        HotelslayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        hotelsAdapter = SuggestionHotelsAdapter(this,hotelsDataList,this,this)
        nearHotelRecyclerview.layoutManager = HotelslayoutManager
        nearHotelRecyclerview.adapter = hotelsAdapter

        RestaurantlayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        restaurantsAdapter = SuggestionRestaurantsAdapter(this,restaurantDataList,this,this)
        nearRestaurantRecyclerview.layoutManager = RestaurantlayoutManager
        nearRestaurantRecyclerview.adapter = restaurantsAdapter
    }

    private fun setRecyclerViews() {
        showProgress()
        fetchHotels()
        fetchRestaurants()
        hideProgress()
    }

    private fun fetchHotels(){
        RestClient.getRestAdapter().
                getHotels().
                enqueue(object : retrofit2.Callback<HotelsResponse>{
                    override fun onFailure(call: Call<HotelsResponse>?, t: Throwable?) {
                        onFailureResponse(t!!)
                    }

                    override fun onResponse(call: Call<HotelsResponse>?, response: Response<HotelsResponse>?) {

                        if (response?.body() != null) {
                            loadHotelsData(response.body().data!!)
                        }
                        else {
                            loadHotelsData(null)
                        }
                    }

                })
    }

    private fun fetchRestaurants(){
        RestClient.getRestAdapter().
                getRestaurants().
                enqueue(object : retrofit2.Callback<RestaurantResponse>{
                    override fun onFailure(call: Call<RestaurantResponse>?, t: Throwable?) {
                        onFailureResponse(t!!)
                    }

                    override fun onResponse(call: Call<RestaurantResponse>?, response: Response<RestaurantResponse>?) {

                        if (response?.body() != null) {
                            loadRestaurantsData(response.body().data!!)
                        }
                        else{
                            loadRestaurantsData(null)
                        }
                    }

                })
    }

    private fun loadRestaurantsData(data: ArrayList<RestaurantData>?) {
        if(restSortBy==null){
            var finalList = data?.filter { it.restaurantCityName == placesObject?.placeName }
            if(finalList?.isEmpty() as Boolean || finalList.size==0){
                finalList = data?.filter { it.restaurantCityName == placesObject?.nearBy }
            }
            restaurantsAdapter.swap(finalList as ArrayList<RestaurantData>)
        }
        else {
            var finalList = data?.filter { it.restaurantCityName == placesObject?.placeName }
            if(finalList?.isEmpty() as Boolean || finalList.size==0){
                finalList = data?.filter { it.restaurantCityName == placesObject?.nearBy }
            }
            finalList = finalList?.filter { it.restaurantCategory == restSortBy }
            restaurantsAdapter.swap(finalList as ArrayList<RestaurantData>)
        }

    }

    private fun loadHotelsData(data: ArrayList<HotelData>?) {
        if(hotelSortBy==null){
            var finalList = data?.filter { it.hotelCityName == placesObject?.placeName }
            if(finalList?.isEmpty() as Boolean || finalList.size==0){
                finalList = data?.filter { it.hotelCityName == placesObject?.nearBy }
            }
            hotelsAdapter.swap(finalList as ArrayList<HotelData>)
        }
        else {
            var finalList = data?.filter { it.hotelCityName == placesObject?.placeName }
            if(finalList?.isEmpty() as Boolean || finalList.size==0){
                finalList = data?.filter { it.hotelCityName == placesObject?.nearBy }
            }
            finalList = finalList?.filter { it.category == hotelSortBy }
            hotelsAdapter.swap(finalList as ArrayList<HotelData>)
        }

    }

    private var hotelData : HotelData? = null
    private var restaurantData : RestaurantData? = null

    override fun onCardSelected(position : Int, indicator : String, hotelObj : HotelData?, restaurantObj: RestaurantData?)
    {
        if(indicator.equals(ApplicationConstants.RESTAURANTS))
        {
            restaurantData = restaurantObj!!

        }
        else if(indicator.equals(ApplicationConstants.HOTELS))
        {
            hotelData = hotelObj
            doneButton.isEnabled = true
        }
        if(restaurantData!=null && hotelData !=null){
            doneButton.isEnabled = true
        }
    }

    override fun onCardDeSelected(position: Int,indicator : String) {
        doneButton.isEnabled = false
    }

    override fun onAttach(placesBundle: PlacesData?, hotelBundle: HotelData?,restBundle: RestaurantData?,position: Int) {
        val intent = Intent()
        if(hotelBundle!=null){
            intent.putExtra(ApplicationConstants.HOTEL_OBJECT_KEY,hotelBundle)
            intent.putExtra(ApplicationConstants.GUESTS,guestsCount)
            intent.setClass(this, HotelOrRestaurantDetailsActivity::class.java)
            startActivity(intent)
        }
        else if(restBundle!=null){
            intent.putExtra(ApplicationConstants.RESTAURANT_OBJECT_KEY,restBundle)
            intent.putExtra(ApplicationConstants.GUESTS,guestsCount)
            intent.setClass(this, HotelOrRestaurantDetailsActivity::class.java)
            startActivity(intent)
        }

    }

}