package modules.destinations

import Main.Model.response.HotelData
import Main.Model.response.PlacesData
import Main.Model.response.PlacesResponse
import Main.base.NoInternetFragment
import Main.modules.home.adapters.DestinationsAdapter
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import com.mobitribe.app.ezzerecharge.network.RestClient
import extras.ApplicationConstants
import extras.InternetAvailability
import kotlinx.android.synthetic.main.destinations_fragment_layout.*
import retrofit2.Call
import retrofit2.Response

class DestinationsActivity : ParentActivity() {

    private lateinit var destinationsAdapter : DestinationsAdapter
    private lateinit var layoutManager : LinearLayoutManager
    private var dataList = ArrayList<PlacesData?>()
    private lateinit var adapter : DestinationsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.destinations_fragment_layout)
        checkInternetConnection()
    }
    private val internetAvailability = InternetAvailability()

    private fun checkInternetConnection() {
        if(internetAvailability.isNetworkAvailable(this)){
            makingUpCardView()

        }
        else {
            showNoInternetFrame()
        }
    }
    fun showNoInternetFrame() {
        supportFragmentManager.beginTransaction().add(R.id.noInternetLayout, NoInternetFragment()).addToBackStack(null).commit()
    }

    private fun makingUpCardView() {
        initializeLayout()
        fetchPlaces()
    }

    private fun initializeLayout() {
        layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        adapter = DestinationsAdapter(this,dataList)
        destinationsRecyclerView.layoutManager = layoutManager
        destinationsRecyclerView.adapter = adapter
    }


    private fun fetchPlaces() {
        RestClient.getRestAdapter().
                getPlaces().
                enqueue(object : retrofit2.Callback<PlacesResponse>{
                    override fun onFailure(call: Call<PlacesResponse>?, t: Throwable?) {
                        onFailureResponse(t!!)
                    }

                    override fun onResponse(call: Call<PlacesResponse>?, response: Response<PlacesResponse>?) {

                        if (response?.body() != null) {
                            Log.d("Successful Destination",response.body().data?.get(0)?.placeName)
                            loadPlacesData(response.body().data)
                        }
                    }

                })
    }

    private fun loadPlacesData(data: ArrayList<PlacesData>?) {
        adapter.swap(data)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        cardSortLayout.visibility = View.VISIBLE
    }
}