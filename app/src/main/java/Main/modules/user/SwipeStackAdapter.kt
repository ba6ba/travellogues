package Main.modules.user

import Main.extras.ImageSetter
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import com.squareup.picasso.Picasso


class SwipeStackAdapter(private val mData: MutableCollection<String>,private var activity : Activity?) : BaseAdapter() {

    private var pActivity = ParentActivity()

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): String {
        return mData.elementAt(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertview: View?, parent: ViewGroup): View {
        val cV = activity?.getLayoutInflater()?.inflate(R.layout.card, parent, false)
        Picasso.with(activity).load(mData.elementAt(position)).into(cV?.findViewById<ImageView>(R.id.cardBanner))
        return cV!!
    }
}