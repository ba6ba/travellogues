package modules.home.hotels

import Main.Model.response.HotelData
import Main.base.NoInternetFragment
import Main.extras.ImageSetter
import Main.extras.RandomColors
import Main.modules.Trips.PlanTripPlacesAdapter
import Main.utils.DateTimeUtility
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import extras.ApplicationConstants
import extras.InternetAvailability
import kotlinx.android.synthetic.main.hotels_activity.*
import modules.user.UserInformationActivity
import java.util.*

open class HotelsActivity : ParentActivity()
{

    private var dotscount: Int = 0
    private val initialBound : Int = 0
    private var currentBound : Int ? = null
    private lateinit var dots: Array<ImageView?>

    private var adapter : HotelsActivitySlider?=null

    private var hotelDataObject : HotelData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hotels_activity)
        checkInternetConnection()
    }
    private val internetAvailability = InternetAvailability()

    private fun checkInternetConnection() {
        if(internetAvailability.isNetworkAvailable(this)){
            hotelDataObject = intent.extras.getSerializable(ApplicationConstants.HOTEL_OBJECT_KEY) as HotelData
            setupViewPager()
            clickListeners()
        }
        else {
            showNoInternetFrame()
        }
    }

    private fun clickListeners() {
        bookingButton.setOnClickListener {
            startActivity(Intent(this,UserInformationActivity::class.java)
                    .putExtra(ApplicationConstants.HOTEL_OBJECT_KEY,hotelDataObject))
            finish()
        }
    }

    fun showNoInternetFrame() {
        supportFragmentManager.beginTransaction().replace(R.id.noInternetLayout, NoInternetFragment()).addToBackStack(null).commit()
    }

    private fun inActivatingDots(position: Int) {
        dots[position]?.setImageDrawable(ContextCompat.getDrawable(this.applicationContext,
                R.drawable.non_active_dot_hotels))
    }

    private fun activatingDots(position : Int) {
        dots[position]?.setImageDrawable(ContextCompat.getDrawable(this.applicationContext,
                R.drawable.active_dot_hotels))
    }

    fun setupViewPager() {
        adapter = HotelsActivitySlider(this,hotelDataObject?.insideCardImage)
        hotelsActivityviewPager.adapter = adapter

        hotelsActivityviewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                changeSlides(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        initializeSlide()
    }


    private fun setContents(position: Int){
        ImageSetter.set(this,hotelDataObject?.bannerImage!!,insideImageView,null)
        dateMonth.text = DateTimeUtility.calculateDay()
        date.text = DateTimeUtility.calculateDate()
        time.text = DateTimeUtility.calculateTime()
        indicator.setBackgroundColor(resources.getColor
        (RandomColors.indicatorArray[((0  until RandomColors.indicatorArray.size-1).random())]))
        hotels.text = hotelDataObject?.hotelCountryName
        hotelsCityName.text = hotelDataObject?.hotelCityName
        try {
            if(position==initialBound || position==initialBound+2){
                rightLayout.visibility = View.VISIBLE
                rightLayoutwithIcons.visibility = View.GONE
                hotelName.text = hotelDataObject?.hotelName
                hotelDesc.text = hotelDataObject?.description
                hotelRatingBar.rating = hotelDataObject?.rating!!
            }
            else {
                rightLayoutwithIcons.visibility = View.VISIBLE
                rightLayout.visibility = View.GONE
                phoneText.text = hotelDataObject?.phoneNumber
                mailText.text = hotelDataObject?.emailAddress
                webText.text = hotelDataObject?.website
                hotelAddress.text = hotelDataObject?.location?.address
            }
        }
        catch (e : Exception) {
            rightLayout.visibility = View.GONE
            rightLayoutwithIcons.visibility = View.GONE
            e.printStackTrace()
        }

    }


    fun ClosedRange<Int>.random() =
            Random().nextInt((endInclusive + 1) - start) +  start

    private fun changeSlides(position: Int) {
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



    private fun initializeSlide() {
        dotscount = hotelDataObject?.insideCardImage?.size!!

        dots = arrayOfNulls<ImageView>(dotscount)


        for (i in initialBound until dotscount) {

            dots[i] = ImageView(this)
            inActivatingDots(i)

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)

            params.setMargins(18, 0, 18, 0)

            HotelSliderDots?.addView(dots.get(i), params)
        }
        setContents(initialBound)
        activatingDots(initialBound)
    }

}
