package Main.modules.Trips

import Main.Model.response.PlacesData
import Main.extras.ImageSetter
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sarwan.final_year_project.R
import kotlinx.android.synthetic.main.places_layout_empty.view.*
import kotlinx.android.synthetic.main.plan_trip_destinations_layout.view.*
import modules.destinations.DestinationsActivity
import kotlin.collections.ArrayList

class PlanTripPlacesAdapter(private val context : Context, private val dataList : ArrayList<PlacesData?>,
                            private val listener: PlacesRecyclerViewClickListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_EMPTY = 0
    private val VIEW_TYPE_ITEM = 1
    private var mParent : ViewGroup ? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mParent = parent
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
            val data = dataList.get(position)
            holder.loadData(data,position)

        } else if (holder is EmptyViewHolder) {
            holder.clickListener()
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun loadData(data: PlacesData?, position: Int) {
            itemView.priceTagLayout.visibility = View.GONE
            ImageSetter.set(context,data?.bgImage!!,itemView.mainImage,null)
            itemView.planTripPlaceName.text = data.placeName
            itemView.planTripRatingBar.rating = data.rating!!
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
            listener.onPlaceCardDeSelected(pos as Int,dataList.get(position)?.placeName!!)
            checkBoxEnabled = false
        }

        private fun enableCustomCheckBox(pos: Any?, position: Int) {
            itemView.checkId.setImageResource(R.drawable.ic_check_circle_accent_24dp)
            listener.onPlaceCardSelected(pos as Int,dataList.get(position)?.placeName!!)
            checkBoxEnabled = true
        }
    }

    private var checkBoxEnabled = false
    inner class EmptyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun clickListener(){
            itemView.explore.setOnClickListener {
                context.startActivity(Intent(context, DestinationsActivity::class.java).
                        putExtra("imageUri",dataList.get(0)?.bgImage))
            }
        }
    }

    fun swap(data: ArrayList<PlacesData>?) {
        dataList.clear()
        dataList.addAll(data!!)
        notifyDataSetChanged()
    }

}