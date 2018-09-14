package Main.modules.plan.Planning

import Main.Model.response.PlacesData
import Main.extras.ImageSetter
import Main.modules.plan.Trips.FragmentInteraction
import Main.modules.plan.Trips.StartAndEndListener
import Main.utils.DistanceUtility
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.places_layout_empty.view.*
import kotlinx.android.synthetic.main.user_selected_card.view.*
import modules.destinations.DestinationsActivity
import kotlin.collections.ArrayList

class UserPlanTripPlacesAdapter(private val context : Context, private val dataList : ArrayList<PlacesData?>,
                            private val listener: FragmentInteraction, private val pActivity : ParentActivity
    , private val dataListener : StartAndEndListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_EMPTY = 0
    private val VIEW_TYPE_ITEM = 1
    private var mParent : ViewGroup ? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mParent = parent
        if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(context).inflate(R.layout.user_selected_card, parent, false)
            return ViewHolder(view)
        } else if (viewType == VIEW_TYPE_EMPTY) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.plan_trip_destinations_layout_empty, parent, false)
            return EmptyViewHolder(v)
        }
        return null as RecyclerView.ViewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        /* It is for the recognition of
         * which view would be embedded */
        return if (dataList[position]==null) VIEW_TYPE_EMPTY else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val data = dataList.get(position)
            holder.loadData(data,position)

        } else if (holder is EmptyViewHolder) {
            holder.clickListener()
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun loadData(data: PlacesData?, position: Int) {
            itemView.selectedPlace.text = data?.placeName
            itemView.selectedDistrict.text = data?.districtName!!
            ImageSetter.set(context,data?.bgImage!!,itemView.bottomImage,null)

            hotelAndRestaurantsUpdated(data)

            if (data.startPlaceCheck==true)
            {
                itemView.startTick.setBackgroundResource(R.drawable.done_black)
                itemView.start.text = "Start Place"
                itemView.travelTime.text = ""
                itemView.fromLocation.text = ""
                itemView.fromKM.text = ""
            }
            else {
                itemView.startTick.setBackgroundResource(R.drawable.done_grey)
                itemView.start.text = "SP"
            }

            if (data.endPlaceCheck==true){
                endPlace = data.placeName!!
                itemView.endTick.setBackgroundResource(R.drawable.done_black)
                itemView.end.text = "End Place"
            }
            else {
                itemView.endTick.setBackgroundResource(R.drawable.done_grey)
                itemView.end.text = "EP"
            }

            if(startPlace==pActivity.profile?.cityName){

                val distance = DistanceUtility.calculateDistance(pActivity.profile?.latitude!!,pActivity.profile?.longitude!!
                        ,data?.location?.latitude!!,data.location?.longitude!!)
                itemView.fromKM.text = distance.toString() + ApplicationConstants.KMS
                itemView.fromLocation.text = "from " + pActivity.profile?.cityName
                itemView.travelTime.text = DistanceUtility.calculateTravelTime(distance,false)

            }
            else {
                if(!data.startPlaceCheck!!){
                    val distance = DistanceUtility.calculateDistance(startPlaceLat!!,startPlaceLon!!
                            ,data?.location?.latitude!!,data.location?.longitude!!)
                    itemView.fromKM.text = distance.toString() + ApplicationConstants.KMS
                    itemView.fromLocation.text = "from " + startPlace
                    itemView.travelTime.text = DistanceUtility.calculateTravelTime(distance,false)
                }
            }
            setOnClickListener(position)
        }

        private fun setOnClickListener(position: Int) {
            itemView.card.setTag(position)
            itemView.card.setOnClickListener {
                val pos = it.getTag()
                listener.onAttach(dataList.get(pos as Int),null,null,position)
            }

            itemView.endPlace.setTag(position)
            itemView.endPlace.setOnClickListener {
                val pos = it.getTag()
                check(pos as Int,false)
            }

            itemView.startPlace.setTag(position)
            itemView.startPlace.setOnClickListener {
                val pos = it.getTag()
                check(pos as Int,true)
            }
        }

        private fun check(value: Int,start : Boolean) {
            for(i in 0 until dataList.size)
            {
                if(i==value)
                {
                    if(start){
                        dataList.get(i)?.startPlaceCheck = true
                        dataListener.onStart(dataList.get(i)?.placeName)
                        startPlace = dataList.get(i)?.placeName!!
                        startPlaceLat=dataList.get(i)?.location?.latitude!!
                        startPlaceLon=dataList.get(i)?.location?.longitude!!
                    }
                    else {
                        dataList.get(i)?.endPlaceCheck = true
                        dataListener.onEnd(dataList.get(i)?.placeName)
                    }
                }
                else {
                    if(start){
                        dataList.get(i)?.startPlaceCheck = false
                    }
                    else {
                        dataList.get(i)?.endPlaceCheck = false
                    }
                }
            }
            notifyDataSetChanged()
        }

        private fun hotelAndRestaurantsUpdated(data: PlacesData?) {
            if(data?.hotel!=null){
                itemView.hotelName.text = data.hotel?.hotelName
            }
            else {
                itemView.hotelName.visibility = View.GONE
            }
            if(data?.restaurantName!=null){
                itemView.restlName.text = data.restaurantName
            }
            else {
                itemView.restlName.visibility = View.GONE
            }
        }
    }


    inner class EmptyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun clickListener(){
            itemView.explore.setOnClickListener {
                context.startActivity(Intent(context, DestinationsActivity::class.java))
            }
        }
    }

    fun clear(){
        dataList.clear()
        notifyDataSetChanged()
    }

    fun addMore(data: ArrayList<PlacesData>?) {
        dataList.addAll(data!!)
        notifyDataSetChanged()
    }

    fun swap(data: ArrayList<PlacesData>?) {
        dataList.clear()
        dataList.addAll(data!!)
        notifyDataSetChanged()
    }

    fun update(position: Int,data: PlacesData?){
        dataList.set(position,data)
        notifyItemChanged(position)
    }

    fun remove(data: PlacesData?){
        dataList.remove(data)
        notifyDataSetChanged()
    }

    private var startPlaceLat = pActivity?.profile?.latitude!!
    private var startPlaceLon = pActivity?.profile?.longitude!!
    private var startPlace  = pActivity.profile?.cityName
    private var endPlace = pActivity.profile?.cityName

    companion object {
    }
}