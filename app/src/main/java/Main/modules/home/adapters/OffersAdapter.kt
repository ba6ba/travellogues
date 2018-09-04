package modules.home.adapters

import Main.Model.response.OffersData
import Main.extras.SettingData
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sarwan.final_year_project.R
import extras.AlertDialog
import kotlinx.android.synthetic.main.offer_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

class OffersAdapter(private val context : Context, private val dataList : ArrayList<OffersData?>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val VIEW_TYPE_EMPTY = 0
    private val VIEW_TYPE_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(context).inflate(R.layout.offer_layout, parent, false)
            return ViewHolder(view)
        } else if (viewType == VIEW_TYPE_EMPTY) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.offer_layout_empty, parent, false)
            return EmptyViewHolder(v)
        }
        return null as RecyclerView.ViewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.loadData(dataList.get(position)!!,position)


        } else if (holder is EmptyViewHolder) {
            holder.clickListener()
        }
    }

    override fun getItemViewType(position: Int): Int {
        /* It is for the recognition of
         * which view would be embedded */
        return if (dataList[position]==null) VIEW_TYPE_EMPTY else VIEW_TYPE_ITEM
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun loadData(data: OffersData, position: Int) {
            itemView.headerImage.background = context.resources.
                    getDrawable(cardBackgroundArray[(0 until cardBackgroundArray.size).random()])
            itemView.offerCardTitle.text = data.title
            itemView.offerCardDesc.text = data.description
            setOnClickListener(position)
        }
        private fun setOnClickListener(position: Int) {
            itemView.offersCardView.setTag(position)
            itemView.offersCardView.setOnClickListener {
                val pos = it.getTag()
                alertDialog.offerDialog(context,dataList.get(pos as Int),cardBackgroundArray.get(pos))
            }

        }
    private var alertDialog = AlertDialog()
    }
    inner class EmptyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun clickListener() {

        }

    }

    fun ClosedRange<Int>.random() =
            Random().nextInt((endInclusive + 1) - start) +  start

    fun swap() {
        dataList.clear()
        dataList.addAll(SettingData.getOfferData())
        populateArray()
        notifyDataSetChanged()
    }

    private var cardBackgroundArray = ArrayList<Int>()

    fun populateArray(){
        for(i in 0 until dataList.size){
        cardBackgroundArray.add(dataList.get(i)?.cardBackground!!)
        }
    }
}