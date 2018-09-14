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

class ShowPlanActivity : ParentActivity() {

    private var index = 0
    private var budget = 50000
    private var sortBy = ""

    private var tripObject : TripPlanning ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_plan_layout)
        if(budget>50000)
        {
            sortBy = "Luxury"
        }
        else {
            sortBy = "Economical"
        }
        tripObject = intent.extras.getSerializable(ApplicationConstants.TRIP_OBJECT_KEY) as TripPlanning
    }

}