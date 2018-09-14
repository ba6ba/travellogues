package modules.destinations

import Main.Model.response.*
import Main.extras.ImageSetter
import Main.modules.plan.Trips.ClickListener
import Main.modules.plan.Trips.FragmentInteraction
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import com.mobitribe.app.ezzerecharge.network.RestClient
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.destination_fragment.*
import modules.plan.HotelOrRestaurantDetailsActivity
import retrofit2.Call
import retrofit2.Response

class DestinationFragment : Fragment(), ClickListener,FragmentInteraction {
    override fun onAttach(placesBundle: PlacesData?, hotelBundle: HotelData?, restBundle: RestaurantData?, position: Int) {
        val intent = Intent()
        if(hotelBundle!=null){
            intent.putExtra(ApplicationConstants.HOTEL_OBJECT_KEY,hotelBundle)
            intent.setClass(context, HotelOrRestaurantDetailsActivity::class.java)
            this.startActivity(intent)
        }
        else if(restBundle!=null){
            intent.putExtra(ApplicationConstants.RESTAURANT_OBJECT_KEY,restBundle)
            intent.setClass(context, HotelOrRestaurantDetailsActivity::class.java)
            this.startActivity(intent)
        }
    }

    private var pActivity : ParentActivity? = null
    private var placesObject : PlacesData? = null

    private lateinit var HotelslayoutManager : LinearLayoutManager
    private lateinit var RestaurantlayoutManager : LinearLayoutManager

    private var hotelsDataList = ArrayList<HotelData?>()
    private var restaurantDataList = ArrayList<RestaurantData?>()

    private lateinit var hotelsAdapter : SuggestionHotelsAdapter
    private lateinit var restaurantsAdapter : SuggestionRestaurantsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ParentActivity
        val arguments = arguments
        placesObject = arguments?.getSerializable(ApplicationConstants.PLACES_OBJECT_KEY) as PlacesData
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.destination_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeLayoutView()
        makingUpLayout()
        clickListener()
        setRecyclerViews()
    }

    private fun clickListener() {
        backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        doneButton.setOnClickListener {
            /*activity?.startActivity(Intent(context,UserInformationActivity::class.java).
                    putExtra(ApplicationConstants.RESTAURANTS,restaurantData).
                    putExtra(ApplicationConstants.HOTELS,hotelData))
            activity?.finish()*/
            activity?.supportFragmentManager?.beginTransaction()?.
                    remove(this)?.commit()
        }
    }

    private fun makingUpLayout() {
        ImageSetter.set(context!!,placesObject?.bgImage!!,groundImage,null)
        destinationNameGround.text = placesObject?.placeName
        destinationAddressGround.text = placesObject?.location?.address
    }

    private fun initializeLayoutView() {
        doneButton.isEnabled = false

        HotelslayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
        hotelsAdapter = SuggestionHotelsAdapter(context!!,hotelsDataList,this,this)
        nearHotelRecyclerview.layoutManager = HotelslayoutManager
        nearHotelRecyclerview.adapter = hotelsAdapter

        RestaurantlayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
        restaurantsAdapter = SuggestionRestaurantsAdapter(context!!,restaurantDataList,this,this)
        nearRestaurantRecyclerview.layoutManager = RestaurantlayoutManager
        nearRestaurantRecyclerview.adapter = restaurantsAdapter
    }

    private fun setRecyclerViews() {
        pActivity?.showProgress()
        fetchHotels()
        fetchRestaurants()
        pActivity?.hideProgress()
    }

    private fun fetchHotels(){
        RestClient.getRestAdapter().
                getHotels().
                enqueue(object : retrofit2.Callback<HotelsResponse>{
                    override fun onFailure(call: Call<HotelsResponse>?, t: Throwable?) {
                        pActivity?.onFailureResponse(t!!)
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
                        pActivity?.onFailureResponse(t!!)
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
        val finalList = data?.filter { it.restaurantCityName == placesObject?.placeName }
        restaurantsAdapter.swap(finalList as ArrayList<RestaurantData>)
    }

    private fun loadHotelsData(data: ArrayList<HotelData>?) {
        val finalList = data?.filter { it.hotelCityName == placesObject?.placeName }
        hotelsAdapter.swap(finalList as ArrayList<HotelData>)
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

        }
        if(restaurantData!=null && hotelData !=null){
            doneButton.isEnabled = true
        }
    }

    override fun onCardDeSelected(position: Int,indicator : String) {
        doneButton.isEnabled = false
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CategoriesFragment.
         */
        @JvmStatic
        fun newInstance() = DestinationFragment()
    }
}