package Main.modules.home.banners

import Main.Model.response.HotelData
import Main.Model.response.HotelsResponse
import Main.extras.ImageSetter
import Main.extras.RandomColors
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import com.mobitribe.app.ezzerecharge.network.RestClient
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.hotel_fragment.*
import modules.home.hotels.HotelsActivity
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class
HotelBanner : Fragment(){

    private var dotscount: Int = 0
    private val initialBound : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hotel_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
    }

    private var adapter : HotelBannerSliderAdapter?=null

    fun setupViewPager() {
        adapter = HotelBannerSliderAdapter(context!!,hotelsDataList?.size!!)
        hotelViewPager.adapter = adapter

        hotelViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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

    private var hotelsDataList : ArrayList<HotelData>?=null

    private fun setupData(){
        RestClient.getRestAdapter().
                getHotels().
                enqueue(object : retrofit2.Callback<HotelsResponse>{
                    override fun onFailure(call: Call<HotelsResponse>?, t: Throwable?) {
                        //TODO
                    }

                    override fun onResponse(call: Call<HotelsResponse>?, response: Response<HotelsResponse>?) {

                        if (response?.body() != null) {
                            Log.d("Successful",response.body().data?.get(0)?.hotelName)
                            hotelsDataList =  response.body().data!!
                            setupViewPager()
                        }
                        else {
                            hotelsDataList = emptyList<HotelData>() as ArrayList<HotelData>
                        }
                    }

                })
    }

    private fun setContents(position: Int){
        hotelRatingBar.isActivated = false
        ImageSetter.set(context!!,hotelsDataList?.get(position)?.bannerImage!!,hotelBannerImage,null)
        ImageSetter.set(context!!,hotelsDataList?.get(position)?.insideCardImage?.get(0)!!,insideImageView,null)
        hotelCityName.text = hotelsDataList?.get(position)?.hotelCityName
        hotelName.text = hotelsDataList?.get(position)?.hotelName
        hotelRatingBar.rating = hotelsDataList?.get(position)?.rating!!
        indicator.setBackgroundColor(resources.getColor
        (RandomColors.indicatorArray[((0  until RandomColors.indicatorArray.size-1).random())]))

        exploreLayout.setOnClickListener {
            startActivity(Intent(context,HotelsActivity::class.java)
                    /*.putExtra(ApplicationConstants.SLIDE_ITEM_ID,position)*/
                    .putExtra(ApplicationConstants.HOTEL_OBJECT_KEY,hotelsDataList?.get(position))
            )
        }
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt((endInclusive + 1) - start) +  start

    private fun changeSlides(position: Int) {
        for (i in initialBound until dotscount) {

            if(i.equals(position)){
                setContents(i)
            }
        }

    }


    private fun initializeSlide() {

        dotscount = hotelsDataList?.size!!
        setContents(initialBound)

    }

}
