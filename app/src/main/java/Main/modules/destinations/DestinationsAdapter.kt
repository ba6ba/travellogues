package Main.modules.home.adapters

import Main.Model.response.PlacesData
import Main.extras.ImageSetter
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sarwan.final_year_project.R
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.destinations_layout.view.*
import modules.destinations.DestinationFragment
import java.io.Serializable
import java.util.*

class DestinationsAdapter(private val activity : FragmentActivity, private val dataList : ArrayList<PlacesData?>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_EMPTY = 0
    private val VIEW_TYPE_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(activity).inflate(R.layout.destinations_layout, parent, false)
            return ViewHolder(view)
        } else if (viewType == VIEW_TYPE_EMPTY) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.destinations_layout_empty, parent, false)
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
            holder.loadData(position)

        } else if (holder is EmptyViewHolder) {
            holder.clickListener()
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun loadData(position: Int) {
            ImageSetter.set(activity.baseContext,dataList.get(position)!!.bgImage,itemView.backgroundImageDestinations,null)
            itemView.destinationName.text = dataList.get(position)!!.placeName
            itemView.destinationDesc.text = dataList.get(position)!!.placeCity
            itemView.destinationAttractions.append(dataList.get(position)!!.attractions)
            itemView.destinationRatingBar.rating = dataList.get(position)!!.rating!!
            itemView.destinationExplore.setOnClickListener {
                setOnClickListener(position)
            }
        }


        private fun setOnClickListener(position: Int) {
            val bundle = Bundle()
            val fragment = DestinationFragment()
            bundle.putSerializable(ApplicationConstants.PLACES_OBJECT_KEY,dataList.get(position) as Serializable)
            fragment.setArguments(bundle)
            activity.supportFragmentManager.beginTransaction().
                    add(R.id.destinationGroundedLayout, fragment)
                    .commit()
            activity.findViewById<View>(R.id.cardSortLayout).visibility = View.GONE
        }


    }

    inner class EmptyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun clickListener(){
        }
    }

    fun swap(data: ArrayList<PlacesData>?) {
        dataList.clear()
        dataList.addAll(data!!)
        notifyDataSetChanged()
    }

}