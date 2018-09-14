package Main.modules.home.banners

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.example.sarwan.final_year_project.R

class MainBannerSliderAdapter(internal var context: Context,internal var count : Int?) : PagerAdapter(){

    internal var layoutInflater: LayoutInflater? = null


    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj as RelativeLayout

    }

    override fun getCount(): Int {
        return count!!
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        val view = layoutInflater?.inflate(R.layout.banner_slider, container, false)
        container.addView(view)
        return view!!
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}