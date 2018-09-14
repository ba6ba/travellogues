package Main.modules.plan.Trips

import Main.Model.response.RestaurantData
import Main.extras.ImageSetter
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sarwan.final_year_project.R
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.places_layout_empty.view.*
import kotlinx.android.synthetic.main.plan_trip_destinations_layout.view.*
import kotlin.collections.ArrayList

class PlanTripRestaurantsAdpater(private val context : Context, private val dataList : ArrayList<RestaurantData?>,
                                 private val listener: RestaurantRecyclerViewClickListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_EMPTY = 0
    private val VIEW_TYPE_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(context).inflate(R.layout.plan_trip_destinations_layout, parent, false)
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
            holder.loadData(dataList.get(position)!!,position)

        } else if (holder is EmptyViewHolder) {
            holder.clickListener()
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun loadData(data: RestaurantData, position: Int) {
            ImageSetter.set(context,(data).bannerImage!!,itemView.mainImage,null)
            itemView.planTripPlaceName.text = data.restaurantName
            itemView.planTripRatingBar.rating = data.restaurantRating!!
            itemView.priceTag.text = data.restaurantPrice.toString()+ ApplicationConstants.PER_DAY
            setOnClickListener(position)
        }
        private fun setOnClickListener(position: Int) {
            itemView.checkId.setTag(position)
            itemView.checkId.setOnClickListener {
                val pos = it.getTag()
                if(!checkBoxEnabled){
                    enableCustomCheckBox(pos, position)
                }
                else {
                    disableCustomCheckBox(pos, position)
                }
            }
            itemView.favourite.setOnClickListener {
                if(!enable)
                {
                    itemView.favourite.setImageResource(R.drawable.ic_favorite_black_24dp)
                    enable = true
                }
                else {
                    itemView.favourite.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                    enable = false
                }
            }
        }

        private var enable = false

        private fun disableCustomCheckBox(pos: Any?, position: Int) {
            itemView.checkId.setImageResource(R.drawable.ic_check_circle_black_24dp)
            listener.onRestaurantCardDeSelected(pos as Int,dataList.get(position)?.restaurantName!!)
            checkBoxEnabled = false
        }

        private fun enableCustomCheckBox(pos: Any?, position: Int) {
            itemView.checkId.setImageResource(R.drawable.ic_check_circle_accent_24dp)
            listener.onRestaurantCardSelected(pos as Int,dataList.get(position)?.restaurantName!!)
            checkBoxEnabled = true
        }
    }

    private var checkBoxEnabled = false

    inner class EmptyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun clickListener(){
            itemView.explore.setOnClickListener {
            }
        }
    }

    fun swap(data: ArrayList<RestaurantData>?) {
        dataList.clear()
        dataList.addAll(data!!)
        notifyDataSetChanged()
    }

}