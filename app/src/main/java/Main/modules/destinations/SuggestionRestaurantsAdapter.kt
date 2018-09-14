package modules.destinations

import Main.Model.response.RestaurantData
import Main.extras.ImageSetter
import Main.modules.plan.Trips.ClickListener
import Main.modules.plan.Trips.FragmentInteraction
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.sarwan.final_year_project.R
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.places_layout_empty.view.*
import kotlinx.android.synthetic.main.trip_suggestions_card_view.view.*


class SuggestionRestaurantsAdapter (private val context: Context,
                                    private val dataList: ArrayList<RestaurantData?>,
                                    private val listener: ClickListener,
                                    private val attachListener: FragmentInteraction
                                    ) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_EMPTY = 0
    private val VIEW_TYPE_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(context).inflate(R.layout.trip_suggestions_card_view, parent, false)
            return ViewHolder(view)
        } else if (viewType == VIEW_TYPE_EMPTY) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.trip_suggestions_card_view_empty, parent, false)
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
            ImageSetter.set(context,(data).bannerImage!!,itemView.mainImageSuggest,null)
            itemView.planTripPlaceNameSuggest.text = data.restaurantName
            itemView.planTripRatingBarSuggest.rating = data.restaurantRating!!
            itemView.priceTag.text = data.restaurantPrice.toString()+ ApplicationConstants.PER_DAY
            if (data.checkEnabled!=true){
                itemView.checkedLayout.visibility = View.GONE
            }
            else {
                itemView.checkedLayout.visibility = View.VISIBLE
            }
            setOnClickListener(position)
        }
        private fun setOnClickListener(position: Int) {
            itemView.cardSuggest.setTag(position)
            itemView.cardSuggest.setOnClickListener {
                val pos = it.getTag()
                check(pos as Int)
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
            itemView.view.setOnClickListener {
                itemView.view.visibility = View.GONE
                itemView.viewText.visibility = View.VISIBLE
            }

            itemView.viewText.setOnDragListener(object :View.OnDragListener{
                override fun onDrag(p0: View?, p1: DragEvent?): Boolean {
                    itemView.viewText.visibility = View.GONE
                    itemView.view.visibility = View.VISIBLE
                    return true
                }

            })
            itemView.viewText.setTag(position)
            itemView.viewText.setOnClickListener {
                val pos = it.getTag()
                attachListener.onAttach(null,null,dataList.get(pos as Int),pos)
            }
        }

        private fun check(pos : Int) {
            for(i in 0 until dataList.size){
                if(i==pos){
                    dataList.get(i)?.checkEnabled = true
                    listener.onCardSelected(pos,ApplicationConstants.RESTAURANTS,null,dataList[pos])
                }
                else {
                    dataList.get(i)?.checkEnabled = false
                }
            }
            notifyDataSetChanged()
        }
        private var enable = false

    }

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