package modules.search

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sarwan.final_year_project.R
import kotlinx.android.synthetic.main.searchlistitem.view.*
import java.util.ArrayList

/*
class SearchListViewAdapter(private val mContext: Context,
                            private val dataList: ArrayList<SearchSuggestionResponse.Records?>)
    : RecyclerView.Adapter<SearchListViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.searchlistitem,parent,false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.loadData(dataList.get(position))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun loadData(data: SearchSuggestionResponse.Records?) {

            itemView.businessTitle.text = data?.title
            itemView.businessdescription.text = data?.type
            setOnClickListener(data!!)

        }

        private fun setOnClickListener(data: SearchSuggestionResponse.Records?) {
            itemView.listLayout.setOnClickListener {

                val intent = Intent(mContext, DetailsActivity::class.java)
                intent.putExtra("itemId",data?.iD)
                mContext.startActivity(intent)

            }
        }
    }


    fun swapData(records: ArrayList<SearchSuggestionResponse.Records?>){
        dataList.clear()
        dataList.addAll(records)
        notifyDataSetChanged()
    }
}
*/
