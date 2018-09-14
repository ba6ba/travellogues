package modules.plan

import Main.Model.response.HotelData
import Main.Model.response.RestaurantData
import Main.extras.ImageSetter
import Main.extras.Location
import Main.extras.VerticalViewPager
import Main.modules.home.banners.MainBannerSliderAdapter
import Main.utils.DistanceUtility
import android.location.Address
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import android.widget.LinearLayout
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import extras.AlertDialog
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.details_activity.*
import kotlinx.android.synthetic.main.trip_planning_new_activity.*
import java.util.*
import kotlin.collections.ArrayList

class HotelOrRestaurantDetailsActivity : ParentActivity() {

    private var hotelObject : HotelData? = null
    private var restObject : RestaurantData? = null
    private var mTitle : String ? = null
    private var mPrice : Int ? = null
    private var mCity : String ? = null
    private var mCategory : String = ""
    private var mRating : String ? = null
    private var mAddress : String ? = "N/A"
    private var mKms : String ? = null
    private var mPhNo : String ? = "N/A"
    private var mEmail : String ? = "N/A"
    private var mDescription : String = "N/A"
    var addressList : MutableList<Address>? =null
    private var dotscount: Int = 0
    private val initialBound : Int = 0
    private lateinit var dots: Array<ImageView?>
    private var adapter : MainBannerSliderAdapter?=null
    private var imageArray : ArrayList<String> ? = null
    private var guestsCount = HashMap<String,String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
        getIntentData()
        setupViewPager()
        loadUI()
        clickListeners()
    }

    private fun clickListeners() {
        selectRoom.setOnClickListener {
            room = alertDialog.roomBuilder(this,imageArray?.get(0)!!,guestsCount?.keys?.elementAt(0).toString(),guestsCount?.values?.elementAt(0).toString())
        }
    }

    private var room = "None"
    private var alertDialog = AlertDialog()
    private fun getIntentData() {

        guestsCount = intent.extras?.get(ApplicationConstants.GUESTS) as HashMap<String, String>
        hotelObject = intent.extras?.getSerializable(ApplicationConstants.HOTEL_OBJECT_KEY) as? HotelData
        restObject = intent.extras?.getSerializable(ApplicationConstants.RESTAURANT_OBJECT_KEY) as? RestaurantData

        if(hotelObject!=null){
            mTitle = hotelObject?.hotelName
            mPrice = hotelObject?.hotelPrice
            mCity = hotelObject?.hotelCityName
            mRating = hotelObject?.rating.toString()
            addressList = Location.getAddress(this,hotelObject?.location?.latitude!!,hotelObject?.location?.longitude!!)
            mAddress = Location.getCityName(this,hotelObject?.location?.latitude!!,hotelObject?.location?.longitude!!)
                    /*hotelObject?.location?.address//addressList?.get(0)?.getAddressLine(0)*/
            mKms = DistanceUtility.calculateDistance(profile?.latitude!!,profile?.longitude!!
                    ,hotelObject?.location?.latitude!!,hotelObject?.location?.longitude!!).toString() + ApplicationConstants.KMS
            mPhNo = hotelObject?.phoneNumber
            mEmail = hotelObject?.emailAddress
            mDescription = hotelObject?.description!!
            imageArray= hotelObject?.insideCardImage
        }
        else {
            mTitle = restObject?.restaurantName
            mPrice = restObject?.restaurantPrice
            mCity = restObject?.restaurantCityName
            mRating = restObject?.restaurantRating.toString()
            addressList = Location.getAddress(this,restObject?.location?.latitude!!,restObject?.location?.longitude!!)
            mAddress = Location.getCityName(this,restObject?.location?.latitude!!,restObject?.location?.longitude!!)
                    /*restObject?.location?.address//ddressList?.get(0)?.getAddressLine(0)*/
            mKms = DistanceUtility.calculateDistance(profile?.latitude!!,profile?.longitude!!
                    ,restObject?.location?.latitude!!,restObject?.location?.longitude!!).toString() + ApplicationConstants.KMS
            mPhNo = restObject?.phoneNumber
            mEmail = restObject?.emailAddress
            mDescription = restObject?.description!!
            mCategory= restObject?.restaurantCategory!! + getString(R.string.restaurant)
            imageArray= restObject?.restaurantScreenShots
        }
    }

    fun setupViewPager() {
        adapter = MainBannerSliderAdapter(this,imageArray?.size)
        viewPager.adapter = adapter
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

    private fun loadUI()
    {
        price.text = mPrice.toString()+ ApplicationConstants.PKR
        name.text = mTitle.toString()
        cityName.text = mCity
        rating.text = mRating
        address.text = mAddress
        kms.text = mKms
        phoneNumber.text = mPhNo
        emailAddress.text = mEmail
        description.text = mDescription
        category.text = mCategory
    }

    private fun setContents(position: Int){
        ImageSetter.set(this,imageArray!![position],bannerImage,null)
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
        dots[position]?.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.non_active_dot_black))
    }

    private fun activatingDots(position : Int) {
        dots[position]?.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.active_dot_black))
    }

    private fun initializeDots() {
        dotscount = imageArray?.size as Int

        dots = arrayOfNulls<ImageView>(dotscount)


        for (i in initialBound until dotscount) {

            dots[i] = ImageView(this)
            inActivatingDots(i)

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)

            params.setMargins(0, 12, 0, 12)

            SliderDots?.addView(dots.get(i), params)
        }
        setContents(initialBound)
        activatingDots(initialBound)
    }
}