package  modules.plan

import Main.Model.TripPlanning
import Main.Model.response.CabData
import Main.Model.response.CabResponse
import Main.modules.plan.Trips.CabRecyclerViewClickListener
import Main.modules.plan.Trips.PlanTripCabsAdapter
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import com.mobitribe.app.ezzerecharge.network.RestClient
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.show_plan_layout.*
import retrofit2.Call
import retrofit2.Response

class ShowPlanActivity : ParentActivity() , CabRecyclerViewClickListener {

    private var index = 0
    private var budget = 50000
    private var sortBy = ""

    private var tripObject : TripPlanning ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_plan_layout)
        initializeLayout()
        if(budget>50000)
        {
            sortBy = "Luxury"
        }
        else {
            sortBy = "Economical"
        }
        fetchCabs(sortBy)
        tripObject = intent.extras.getSerializable(ApplicationConstants.TRIP_OBJECT_KEY) as TripPlanning

        plan.text = "guests" + tripObject?.adults + "districts "+ tripObject?.districts?.get(0) +
                "endDate " + tripObject?.endDate + " startDate " + tripObject?.startDate +
                "endPlace " + tripObject?.endPlace + " startPlace " + tripObject?.startPlace
        "nofDay"+ tripObject?.noOfDays!! + "place "+ tripObject?.places?.get(0)?.placeName
        "hotel "+ tripObject?.places?.get(0)?.hotel?.hotelName + "restaurant "+ tripObject?.places?.get(0)?.restaurantName
    }

    private fun fetchCabs(sortBy : String) {
        RestClient.getRestAdapter().
                getCabs().
                enqueue(object : retrofit2.Callback<CabResponse>{
                    override fun onFailure(call: Call<CabResponse>?, t: Throwable?) {
                        onFailureResponse(t!!)
                    }

                    override fun onResponse(call: Call<CabResponse>?, response: Response<CabResponse>?) {

                        if (response?.body() != null) {
                            loadCabsData(response.body().data!!,sortBy)
                        }
                    }

                })
    }

    private fun loadCabsData(data: ArrayList<CabData>, sortBy: String) {
        val finalList = data.filter { it.cabCategory == sortBy }
        cabsAdapter.swap(finalList as ArrayList<CabData>)
    }

    private fun initializeLayout(){
        CabslayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        cabsAdapter = PlanTripCabsAdapter(this,cabsDataList,this)
        recCabsRecyclerview.layoutManager = CabslayoutManager
        recCabsRecyclerview.adapter = cabsAdapter
    }

    private lateinit var CabslayoutManager : LinearLayoutManager
    private var cabsDataList = ArrayList<CabData?>()
    private lateinit var cabsAdapter : PlanTripCabsAdapter

    override fun onCabCardSelected(position: Int, selection: String) {
        index = position
        plan.append(cabsDataList.get(index)?.cabPrice.toString())
    }
}