package modules.home.adapters

import Main.Model.response.IconsData
import Main.extras.ImageSetter
import Main.extras.SettingData
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sarwan.final_year_project.R
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.icons_layout.view.*
import modules.bookings.BookingActivity


class IconsAdapter(private val context : Context, private val dataList : ArrayList<IconsData>) : RecyclerView.Adapter<IconsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconsAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.icons_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: IconsAdapter.ViewHolder, position: Int) {
        holder.loadData(dataList.get(position),position)
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun loadData(data: IconsData, position: Int) {
            itemView.iconTitle.text = data.title
            ImageSetter.set(context,data.imageUri!!,itemView.iconImage,null)
            setOnClickListener(position)
        }

        private fun setOnClickListener(position: Int) {
            itemView.iconLayout.setTag(position)
            itemView.iconLayout.setOnClickListener {
                val pos = it.getTag()
                val intent = Intent()
                intent.putExtra(ApplicationConstants.TOOLBAR_TITLE,dataList.get(pos as Int).title)
                intent.setClass(context, BookingActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    fun swap() {
        dataList.clear()
        dataList.addAll(SettingData.getIconsData())
        notifyDataSetChanged()
    }

}