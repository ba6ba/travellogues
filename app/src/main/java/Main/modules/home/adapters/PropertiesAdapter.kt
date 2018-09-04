package modules.home.adapters

import Main.Model.response.PropertiesData
import Main.extras.ImageSetter
import Main.extras.SettingData
import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.sarwan.final_year_project.R
import extras.AlertDialog
import kotlinx.android.synthetic.main.properties_layout.view.*


class PropertiesAdapter(private val context : Activity, private val dataList : ArrayList<PropertiesData>) :
        RecyclerView.Adapter<PropertiesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertiesAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.properties_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: PropertiesAdapter.ViewHolder, position: Int) {
        holder.loadData(dataList.get(position),position)
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun loadData(data: PropertiesData, position: Int) {
            itemView.propertyCardTitle.text = data.title
            itemView.propertyCardDesc.text = data.description
            for(i in 0 until data.iconArray.size)
            {
                val image = ImageView(context)
                image.setLayoutParams(android.view.ViewGroup.LayoutParams(80, 80))
                image.setMaxHeight(80)
                image.setMaxWidth(80)
                image.scaleType = ImageView.ScaleType.CENTER_CROP
                itemView.multipleIconsLayout.addView(image)
                ImageSetter.set(context,data.iconArray[i],image,null)
            }
            setOnClickListener(position)
        }
        private fun setOnClickListener(position: Int) {
            itemView.propertiesCardView.setTag(position)
            itemView.propertiesCardView.setOnClickListener {
                val pos = it.getTag()
                alertDialog.propertiesDialog(context,dataList.get(pos as Int))
            }

        }
    }

    private var alertDialog = AlertDialog()
    fun swap() {
        dataList.clear()
        dataList.addAll(SettingData.getPropertyData())
        notifyDataSetChanged()
    }
}