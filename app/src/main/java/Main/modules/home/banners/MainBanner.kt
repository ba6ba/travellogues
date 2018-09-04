package Main.modules.home.banners

import Main.extras.ImageSetter
import Main.extras.RandomColors
import Main.extras.SlideChanger
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.sarwan.final_year_project.R
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.banner_fragment.*
import java.util.*

class MainBanner : Fragment(){

    private var dotscount: Int = 0
    private val initialBound : Int = 0
    private lateinit var dots: Array<ImageView?>
    private var imagePathArray : ArrayList<String>?=null
    private var timer : Timer ? = null
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.banner_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        setupViewPager()
    }

    private var adapter : MainBannerSliderAdapter?=null
    private var slideChanger = SlideChanger()

    fun setupViewPager() {
        adapter = MainBannerSliderAdapter(context!!,imagePathArray?.size)
        viewPager.adapter = adapter
        //pageSwitcher(4)
                //slideChanger.pageSwitcher(4,viewPager,imagePathArray?.size!!)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                changeDots(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        initializeDots()
    }


    private fun setupData(){

        imagePathArray = arrayListOf(
                ApplicationConstants.MAIN_BANNER_IMAGE_1,
                ApplicationConstants.MAIN_BANNER_IMAGE_2,
                ApplicationConstants.MAIN_BANNER_IMAGE_3,
                ApplicationConstants.MAIN_BANNER_IMAGE_4,
                ApplicationConstants.MAIN_BANNER_IMAGE_5,
                ApplicationConstants.MAIN_BANNER_IMAGE_6,
                ApplicationConstants.MAIN_BANNER_IMAGE_7,
                ApplicationConstants.MAIN_BANNER_IMAGE_8,
                ApplicationConstants.MAIN_BANNER_IMAGE_9,
                ApplicationConstants.MAIN_BANNER_IMAGE_10
        )
    }

    private fun setContents(position: Int){
        ImageSetter.set(context!!,imagePathArray!![((0  until imagePathArray!!.size-1).random())],bannerImage,null)
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt((endInclusive + 1) - start) +  start

    private fun changeDots(position: Int) {
        for (i in initialBound until dotscount) {

            if(i.equals(position)){
                activatingDots(i)
                setContents(i)
            }
            else
            {
                inActivatingDots(i)
            }

        }

    }

    private fun inActivatingDots(position: Int) {
        dots[position]?.setImageDrawable(ContextCompat.getDrawable(context!!.applicationContext,
                R.drawable.non_active_dot))
    }

    private fun activatingDots(position : Int) {
        dots[position]?.setImageDrawable(ContextCompat.getDrawable(context!!.applicationContext,
                R.drawable.active_dot))
    }

    private fun initializeDots() {
        dotscount = imagePathArray?.size as Int

        dots = arrayOfNulls<ImageView>(dotscount)


        for (i in initialBound until dotscount) {

            dots[i] = ImageView(context!!)
            inActivatingDots(i)

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)

            params.setMargins(12, 0, 12, 0)

            SliderDots?.addView(dots.get(i), params)
        }
        setContents(initialBound)
        activatingDots(initialBound)


    }

    fun pageSwitcher(seconds: Long) {
        timer = Timer() // At this line a new Thread will be created
        timer?.scheduleAtFixedRate(RemindTask(), 0, seconds * 1000) // delay
    }

    inner class RemindTask : TimerTask() {

        override fun run() {
            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            (Runnable {
                if (page > imagePathArray?.size!!) {
                    timer?.cancel()
                } else {
                    viewPager?.setCurrentItem(page++)
                }
            })

        }
    }
}
