package Main.modules.Trips

import Main.Model.response.CabData
import Main.extras.ImageSetter
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sarwan.final_year_project.R
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.places_layout_empty.view.*
import kotlinx.android.synthetic.main.plan_trip_cabs_layout.view.*
import kotlin.collections.ArrayList

class PlanTripCabsAdapter(private val context : Context, private val dataList : ArrayList<CabData?>,
                          private val listener: CabRecyclerViewClickListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_EMPTY = 0
    private val VIEW_TYPE_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(context).inflate(R.layout.plan_trip_cabs_layout, parent, false)
            return ViewHolder(view)
        } else if (viewType == VIEW_TYPE_EMPTY) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.plan_trip_cabs_layout_empty, parent, false)
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
        fun loadData(data: CabData, position: Int) {
            ImageSetter.set(context,(data).cabBannerImage!!,itemView.cabsMainImage,null)
            itemView.planTripCabName.text = data.cabName
            itemView.planTripCabRatingBar.rating = data.cabrating!!
            itemView.planTripCabPrice.text = (ApplicationConstants.PRICE).capitalize() +" "+data.cabPrice.toString() + ApplicationConstants.PER_DAY
            itemView.planTripCabDiscount.text = (ApplicationConstants.DISCOUNT).capitalize() +" "+ data.cabDiscount.toString() + context.getString(R.string.percent_symbol)
            setOnClickListener(position)
        }
        private fun setOnClickListener(position: Int) {
            itemView.cabsCheckId.setTag(position)
            itemView.cabsCheckId.setOnClickListener {
                val pos = it.getTag()
                if(!checkBoxEnabled){
                    enableCustomCheckBox(pos, position)
                }
                else {
                    disableCustomCheckBox(pos, position)
                }
            }
        }

        private fun disableCustomCheckBox(pos: Any?, position: Int) {
            itemView.cabsCheckId.setImageResource(R.drawable.ic_check_circle_black_24dp)
            listener.onCabCardDeSelected(pos as Int,dataList.get(position)?.cabName!!)
            checkBoxEnabled = false
        }

        private fun enableCustomCheckBox(pos: Any?, position: Int) {
            itemView.cabsCheckId.setImageResource(R.drawable.ic_check_circle_accent_24dp)
            listener.onCabCardSelected(pos as Int,dataList.get(position)?.cabName!!)
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

    fun swap(data: ArrayList<CabData>?) {
        dataList.clear()
        dataList.addAll(data!!)
        notifyDataSetChanged()
    }

}