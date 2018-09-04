/*
package modules.home.hotels

import Main.Model.response.HotelData
import Main.extras.ImageSetter
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sarwan.final_year_project.R
import kotlin.collections.ArrayList

class HotelDetailsAdapter(private val context : Context, private val dataList : ArrayList<HotelData?>,
                            private val listener: HotelRecyclerViewClickListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_EMPTY = 0
    private val VIEW_TYPE_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(context).inflate(R.layout.hotel_slider_card_view, parent, false)
            return ViewHolder(view)
        } else if (viewType == VIEW_TYPE_EMPTY) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.hotel_slider_card_view_empty, parent, false)
            return EmptyViewHolder(v)
        }
        return null as RecyclerView.ViewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        */
/* It is for the recognition of
         * which view would be embedded *//*

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
        fun loadData(data: HotelData, position: Int) {
            ImageSetter.set(context,(data).bannerImage!!,itemView.mainImage,null)
            itemView.planTripPlaceName.text = data.hotelName
            itemView.planTripRatingBar.rating = data.rating!!
            itemView.priceTag.text = data.hotelPrice.toString()
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
        }

        private fun disableCustomCheckBox(pos: Any?, position: Int) {
            itemView.checkId.setImageResource(R.drawable.ic_check_circle_black_24dp)
            listener.onHotelCardDeSelected(pos as Int,dataList.get(position)?.hotelName!!)
            checkBoxEnabled = false
        }

        private fun enableCustomCheckBox(pos: Any?, position: Int) {
            itemView.checkId.setImageResource(R.drawable.ic_check_circle_accent_24dp)
            listener.onHotelCardSelected(pos as Int,dataList.get(position)?.hotelName!!)
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

    fun swap(data: ArrayList<HotelData>?) {
        dataList.clear()
        dataList.addAll(data!!)
        notifyDataSetChanged()
    }

}*/
