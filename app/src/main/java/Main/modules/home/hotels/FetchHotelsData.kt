/*
package modules.home.hotels

import Main.Model.response.HotelData
import Main.Model.response.HotelsResponse
import Main.modules.home.banners.HotelBanner
import android.util.Log
import base.ParentActivity
import com.mobitribe.app.ezzerecharge.network.RestClient
import retrofit2.Call
import retrofit2.Response

class FetchHotelsData : HotelsActivity() {

    private var dataList = ArrayList<HotelData>()
    private var childActivity = HotelsActivity()
    private var childBanner = HotelBanner()

    fun fetchHotels(activity : Boolean) :  ArrayList<HotelData>? {

        RestClient.getRestAdapter().
                getHotels().
                enqueue(object : retrofit2.Callback<HotelsResponse>{
                    override fun onFailure(call: Call<HotelsResponse>?, t: Throwable?) {
                        onFailureResponse(t!!)
                    }

                    override fun onResponse(call: Call<HotelsResponse>?, response: Response<HotelsResponse>?) {

                        if (response?.body() != null) {
                            Log.d("Successful",response.body().data?.get(0)?.hotelName)
                            dataList =  response.body().data!!
                            if(!activity){
                                //childBanner.setupViewPager()
                            }
                            else{
                                childActivity.setupViewPager()
                            }
                        }
                        else {
                            dataList = emptyList<HotelData>() as ArrayList<HotelData>
                        }
                    }

                })
        return dataList
    }
}*/
