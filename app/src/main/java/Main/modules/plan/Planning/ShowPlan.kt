package modules.plan.Planning

import Main.Model.TripPlanning
import Main.utils.DistanceUtility
import android.os.Bundle
import base.ParentActivity
import com.anychart.anychart.AnyChart
import com.anychart.anychart.Pert
import com.example.sarwan.final_year_project.R
import extras.ApplicationConstants
import com.anychart.anychart.AnyChart.pert
import com.anychart.anychart.Milestones
import java.io.File.separator
import com.anychart.anychart.TreeFillingMethod
import android.R.attr.data
import com.anychart.anychart.DataEntry
import kotlinx.android.synthetic.main.activity_show_planning.*
import android.R.attr.x
import android.widget.LinearLayout


class ShowPlan : ParentActivity() {

    private var trip : TripPlanning ? = null
    private var pertChart = AnyChart.pert()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_planning)
        trip = intent.extras!!.getSerializable(ApplicationConstants.TRIP_OBJECT_KEY) as TripPlanning
        getInitials()
        setChart()
    }

    private fun setChart() {
        val data = ArrayList<DataEntry>()
        data.add(CustomPertDataEntry("0", DistanceUtility.calculateTravelTimeInInt(
                DistanceUtility.calculateDistance(trip?.places?.get(0)?.location?.latitude!!,trip?.places?.get(0)?.location?.longitude!!,
                        trip?.startPlace?.location?.latitude!!,trip?.startPlace?.location?.longitude!!),true),
                trip?.places?.get(0)?.initials!!,trip?.places?.get(0)?.placeName!!))
        for(i in 1 until trip?.places?.size as Int) {
            data.add(CustomPertDataEntry(i.toString(), DistanceUtility.calculateTravelTimeInInt(
                    DistanceUtility.calculateDistance(trip?.places?.get(i)?.location?.latitude!!,trip?.places?.get(i)?.location?.longitude!!,
                            trip?.startPlace?.location?.latitude!!,trip?.startPlace?.location?.longitude!!),true),
                    trip?.places?.get(i)?.initials!!, arrayOf(i.toString()),trip?.places?.get(i)?.placeName!!))
        }

//        pertChart?.data(data)
//        anyCharView.setChart(pertChart)
    }

    private fun getInitials() {
        for(i in 0 until trip?.places?.size as Int) {
            trip?.places?.get(i)?.initials = prepareInitial(trip?.places?.get(i)?.placeName)
        }
    }

    private fun prepareInitial(toGet : String?) : String {
        val initial = String()
        val split = toGet?.split(" ")
        for (i in split?.indices!!) {
            (initial+split[0])
        }
        return initial.toUpperCase()
    }

    private fun addingLayout(item : LinearLayout){
        item.setLayoutParams(android.view.ViewGroup.LayoutParams(120, 80))
        layout.addView(item)
    }

    private fun makeLayout(item : LinearLayout){
        item.setLayoutParams(android.view.ViewGroup.LayoutParams(120, 80))
        layout.addView(item)
    }
}

