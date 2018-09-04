package modules.home.hotels

import Main.extras.ImageSetter
import Main.extras.SettingData
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.example.sarwan.final_year_project.R

class HotelsActivitySlider(internal var context: Context,internal var insideCardImage: ArrayList<String>?) : PagerAdapter(){

    internal var layoutInflater: LayoutInflater? = null

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj as RelativeLayout
    }

    override fun getCount(): Int {
        return insideCardImage?.size!!
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater?.inflate(R.layout.hotels_slider, container, false)
        ImageSetter.set(context,insideCardImage?.get(position)!!,view?.findViewById(R.id.hotelBackground)!!,null)
        container.addView(view)
        return view!!

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}