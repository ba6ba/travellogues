package modules.Profile

import Main.Model.response.*
import Main.modules.Trips.*
import android.annotation.TargetApi
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import com.mobitribe.app.ezzerecharge.network.RestClient
import kotlinx.android.synthetic.main.trip_activity_layout.*
import retrofit2.Call
import retrofit2.Response
import extras.ApplicationConstants
import java.util.*
import kotlin.collections.ArrayList
import android.app.DatePickerDialog
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import extras.AlertDialog
import modules.profile.ProfileActivity
import modules.user.UserInformationActivity


@TargetApi(Build.VERSION_CODES.N)
class TripsActivity : Fragment(), PlacesRecyclerViewClickListener,SwipeRefreshLayout.OnRefreshListener,
        HotelRecyclerViewClickListener,RestaurantRecyclerViewClickListener,CabRecyclerViewClickListener{

    private lateinit var PlaceslayoutManager : LinearLayoutManager
    private lateinit var HotelslayoutManager : LinearLayoutManager
    private lateinit var RestaurantslayoutManager : LinearLayoutManager
    private lateinit var CabslayoutManager : LinearLayoutManager


    private var placesDataList = ArrayList<PlacesData?>()
    private var hotelsDataList = ArrayList<HotelData?>()
    private var restaurantsDataList = ArrayList<RestaurantData?>()
    private var cabsDataList = ArrayList<CabData?>()

    private lateinit var placesAdapter : PlanTripPlacesAdapter
    private lateinit var hotelsAdapter : PlanTripHotelsAdapter
    private lateinit var restaurantsAdapter : PlanTripRestaurantsAdpater
    private lateinit var cabsAdapter : PlanTripCabsAdapter

    private var pActivity : ParentActivity? = null
    private var alertDialog = AlertDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ParentActivity
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.trip_activity_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alertDialog.tripDialog(context!!)
        disableFab()
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.isRefreshing = true
        setToolbar()
        initializeLayoutView()
        fetchApiData()
        calendarBuilder()
        clickListeners()
    }

    private fun disableFab() {
        fab.hide()
        fab.isEnabled = false
    }

    private fun clickListeners() {
        fab.setOnClickListener {
            startActivity(Intent(context!!,UserInformationActivity::class.java).putExtra(ApplicationConstants.PRICE,getPrice()))
        }
        profileMenu.setOnClickListener {
            startActivity(Intent(context!!,ProfileActivity::class.java))
        }
    }

    private fun getPrice() : Int {
        if(cabPriceValueIndex!=null){
            return (hotelsDataList.get(hotelPriceValueIndex!!)?.hotelPrice!! +
                    restaurantsDataList.get(restaurantsPriceValueIndex!!)?.restaurantPrice!! +
                    discountedCabPrice())
        }
        return (hotelsDataList.get(hotelPriceValueIndex!!)?.hotelPrice!! + restaurantsDataList.get(restaurantsPriceValueIndex!!)?.restaurantPrice!!)
    }

    private fun discountedCabPrice(): Int {
        return ((cabsDataList.get(cabPriceValueIndex!!)?.cabPrice!!)).
                minus((cabsDataList.get(cabPriceValueIndex!!)?.cabPrice!! / cabsDataList.get(cabPriceValueIndex!!)?.cabDiscount!!))
    }

    private var simpleDateFormat = SimpleDateFormat(ApplicationConstants.DATE_FORMAT, Locale.US)
    private val myCalendar = Calendar.getInstance()
    private fun calendarBuilder() {

        from.setOnClickListener {
            setDatePickerDialog(getString(R.string.fromDate))
        }
        to.setOnClickListener {
            recommendationLayout.visibility = View.GONE
            setDatePickerDialog(getString(R.string.toDate))
        }
    }

    private fun setDatePickerDialog(whichOne: String) {
        val date = object : DatePickerDialog.OnDateSetListener {

            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel(whichOne)
            }

        }
        DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateLabel(whichOne : String) {
        when(whichOne){
            getString(R.string.fromDate) -> from.setText(simpleDateFormat.format(myCalendar.time),TextView.BufferType.EDITABLE)
            getString(R.string.toDate) -> {
                to.setText(simpleDateFormat.format(myCalendar.time),TextView.BufferType.EDITABLE)

                if(!(routeField.text.isNullOrEmpty()) || !routeField.text.equals("")){
                    showHotelLayout()
                }
                else {
                    Toast.makeText(context,getString(R.string.must_select_place),Toast.LENGTH_LONG).show()
                }
            }
        }

    }
    private fun showHotelLayout() {
        if(fabEnabled){
            fab.isEnabled = true
        }
        hotelsDataList.clear()
        hotelsAdapter.notifyDataSetChanged()
        fetchHotels(routeField.text.toString())
    }


    private fun fetchApiData() {
        fetchPlaces()
    }


    private fun setToolbar() {
        pActivity?.setSupportActionBar(tripToolbar)
//        pActivity?.supportActionBar?.setTitle(getString(R.string.plan_my_trip))
    }


    private fun initializeLayoutView() {

        PlaceslayoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        placesAdapter = PlanTripPlacesAdapter(context!!,placesDataList,this)
        selectPlaceRecyclerview.layoutManager = PlaceslayoutManager
        selectPlaceRecyclerview.adapter = placesAdapter

        HotelslayoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        hotelsAdapter = PlanTripHotelsAdapter(context!!,hotelsDataList,this)
        recHotelsRecyclerview.layoutManager = HotelslayoutManager
        recHotelsRecyclerview.adapter = hotelsAdapter

        RestaurantslayoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        restaurantsAdapter = PlanTripRestaurantsAdpater(context!!,restaurantsDataList,this)
        recRestaurantsRecyclerview.layoutManager = RestaurantslayoutManager
        recRestaurantsRecyclerview.adapter = restaurantsAdapter

        CabslayoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        cabsAdapter = PlanTripCabsAdapter(context!!,cabsDataList,this)
        recCabsRecyclerview.layoutManager = CabslayoutManager
        recCabsRecyclerview.adapter = cabsAdapter
    }


    private fun fetchHotels(sortBy: String){
        pActivity?.showProgress()
        RestClient.getRestAdapter().
                getHotels().
                enqueue(object : retrofit2.Callback<HotelsResponse>{
                    override fun onFailure(call: Call<HotelsResponse>?, t: Throwable?) {
                        pActivity?.onFailureResponse(t!!)
                    }

                    override fun onResponse(call: Call<HotelsResponse>?, response: Response<HotelsResponse>?) {

                        if (response?.body() != null) {
                            Log.d("Successful",response.body().data?.get(0)?.hotelName)
                            loadHotelsData(response.body().data!!,sortBy)
                        }
                    }

                })
        pActivity?.hideProgress()
    }

    private fun fetchPlaces() {
        RestClient.getRestAdapter().
                getPlaces().
                enqueue(object : retrofit2.Callback<PlacesResponse>{
                    override fun onFailure(call: Call<PlacesResponse>?, t: Throwable?) {
                        pActivity?.onFailureResponse(t!!)
                    }

                    override fun onResponse(call: Call<PlacesResponse>?, response: Response<PlacesResponse>?) {

                        if (response?.body() != null) {
                            Log.d("Successful",response.body().data?.get(0)?.placeName)
                            loadPlacesData(response.body().data)
                        }
                    }

                })
    }



    private fun loadHotelsData(data: ArrayList<HotelData>, sortBy: String) {
        val finalList = data.filter { it.hotelCityName == sortBy }
        hotelsAdapter.swap(finalList as ArrayList<HotelData>/*filterHotelData(data,sortBy)*/)
        recommendationLayout.visibility = View.VISIBLE
    }

    private fun loadPlacesData(data: ArrayList<PlacesData>?) {
        stopRefreshLoader()
        placesAdapter.swap(data)
    }
    private var hotelPriceValueIndex : Int ? = null

    override fun onPlaceCardSelected(position: Int, selection: String) {
        routeField.append(selection)
        fabEnabled = true
    }
    override fun onPlaceCardDeSelected(position: Int, selection: String)
    {
        routeField.text = ""
        fabEnabled = false
    }

    override fun onHotelCardSelected(position: Int, selection: String) {
        makeRestaurantLayout()
        hotelPriceValueIndex = position

    }
    override fun onHotelCardDeSelected(position: Int, selection: String) {
        recommendationRestaurantLayout.visibility = View.GONE
        clearRestaurantLayout()
    }

    private fun clearRestaurantLayout() {
        restaurantsDataList.clear()
        restaurantsAdapter.notifyDataSetChanged()
    }

    private fun restaurantsClickListeners() {
        economicalRestaurants.setOnClickListener {
            recRestaurantsRecyclerview.visibility = View.GONE
            fetchRestaurants(ApplicationConstants.ECONOMIC)
        }
        luxuryRestaurants.setOnClickListener {
            recRestaurantsRecyclerview.visibility = View.GONE
            fetchRestaurants(ApplicationConstants.LUXURY)
        }
    }

    private fun makeRestaurantLayout() {
        recommendationRestaurantLayout.visibility = View.VISIBLE
        restaurantsClickListeners()
    }

    private fun fetchRestaurants(sortBy: String) {
        pActivity?.showProgress()
        RestClient.getRestAdapter().
                getRestaurants().
                enqueue(object : retrofit2.Callback<RestaurantResponse>{
                    override fun onFailure(call: Call<RestaurantResponse>?, t: Throwable?) {
                        pActivity?.onFailureResponse(t!!)
                    }

                    override fun onResponse(call: Call<RestaurantResponse>?, response: Response<RestaurantResponse>?) {

                        if (response?.body() != null) {
                            loadRestaurantsData(response.body().data!!,sortBy)
                        }
                    }

                })
        pActivity?.hideProgress()
    }

    private fun loadRestaurantsData(data: ArrayList<RestaurantData>, sortBy: String) {
        recRestaurantsRecyclerview.visibility = View.VISIBLE
        val finalList = data.filter { it.restaurantCategory == sortBy }
        restaurantsAdapter.swap(finalList as ArrayList<RestaurantData>)
    }

    private var restaurantsPriceValueIndex : Int ? = null
    private var cabPriceValueIndex : Int ? = null

    override fun onRestaurantCardSelected(position: Int, selection: String) {
        makeCabLayout()
        fab.show()
        restaurantsPriceValueIndex = position
    }

    override fun onRestaurantCardDeSelected(position: Int, selection: String) {
        recommendationCabsLayout.visibility = View.GONE
        clearCabsLayout()
    }

    private fun clearCabsLayout() {
        cabsDataList.clear()
        cabsAdapter.notifyDataSetChanged()
    }

    private fun makeCabLayout() {
        recommendationCabsLayout.visibility = View.VISIBLE
        cabsClickListeners()
    }

    private fun cabsClickListeners() {
        economicalCabs.setOnClickListener {
            recCabsRecyclerview.visibility = View.GONE
            fetchCabs(ApplicationConstants.ECONOMIC)
        }
        luxuryCabs.setOnClickListener {
            recCabsRecyclerview.visibility = View.GONE
            fetchCabs(ApplicationConstants.LUXURY)
        }
    }

    private fun fetchCabs(sortBy : String) {
        pActivity?.showProgress()
        RestClient.getRestAdapter().
                getCabs().
                enqueue(object : retrofit2.Callback<CabResponse>{
                    override fun onFailure(call: Call<CabResponse>?, t: Throwable?) {
                        pActivity?.onFailureResponse(t!!)
                    }

                    override fun onResponse(call: Call<CabResponse>?, response: Response<CabResponse>?) {

                        if (response?.body() != null) {
                            loadCabsData(response.body().data!!,sortBy)
                        }
                    }

                })
        pActivity?.hideProgress()
    }

    private fun loadCabsData(data: ArrayList<CabData>, sortBy: String) {
        recCabsRecyclerview.visibility = View.VISIBLE
        val finalList = data.filter { it.cabCategory == sortBy }
        cabsAdapter.swap(finalList as ArrayList<CabData>)
    }

    override fun onCabCardSelected(position: Int, selection: String) {
        cabPriceValueIndex = position
    }

    override fun onCabCardDeSelected(position: Int, selection: String) {
    }
    private var fabEnabled  = false
    fun stopRefreshLoader(){
        swipeRefreshLayout.isRefreshing = false
    }
    override fun onRefresh() {
        fetchApiData()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CategoriesFragment.
         */
        @JvmStatic
        fun newInstance() = TripsActivity()
    }
}
