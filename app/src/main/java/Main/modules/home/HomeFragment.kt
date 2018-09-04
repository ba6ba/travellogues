package modules.home

import Main.Model.response.*
import Main.extras.EndlessScroll
import Main.modules.home.adapters.PlacesAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import com.mobitribe.app.ezzerecharge.network.RestClient
import kotlinx.android.synthetic.main.home_layout.*
import modules.home.adapters.IconsAdapter
import modules.home.adapters.OffersAdapter
import modules.home.adapters.PropertiesAdapter
import retrofit2.Call
import retrofit2.Response

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    override fun onRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var pActivity : ParentActivity? = null

    private lateinit var IconslayoutManager : LinearLayoutManager
    private lateinit var PropertieslayoutManager : LinearLayoutManager
    private lateinit var OfferslayoutManager : LinearLayoutManager
    private lateinit var PlaceslayoutManager : LinearLayoutManager

    private  lateinit var iconsAdapter : IconsAdapter
    private lateinit var propertiesAdapter : PropertiesAdapter

    private var iconsDataList = ArrayList<IconsData>()
    private var propertiesDataList = ArrayList<PropertiesData>()

    private var offersDataList = ArrayList<OffersData?>()
    private lateinit var offersAdapter : OffersAdapter

    private var placesDataList = ArrayList<PlacesData?>()
    private lateinit var placesAdapter : PlacesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ParentActivity
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeLayoutView()
        fetchData()
        attachingEndlessScrollers()
    }

    private fun attachingEndlessScrollers() {
        EndlessScroll.applyEndlessScroll(placesRecyclerview,PlaceslayoutManager,placesAdapter,placesDataList as ArrayList<Any?>)
        EndlessScroll.applyEndlessScroll(offersRecyclerview,OfferslayoutManager,offersAdapter,offersDataList as ArrayList<Any?>)
    }

    private fun fetchData() {
        iconsAdapter.swap()
        propertiesAdapter.swap()
        offersAdapter.swap()
        fetchPlaces()
    }

    private fun fetchPlaces() {
            RestClient.getRestAdapter().
                    getPlaces().
                    enqueue(object : retrofit2.Callback<PlacesResponse>{
                        override fun onFailure(call: Call<PlacesResponse>?, t: Throwable?) {
                            pActivity?.onFailureResponse(t!!)
                        }

                        override fun onResponse(call: Call<PlacesResponse>?, response: Response<PlacesResponse>?) {

                            if (response?.body() != null) {
                                generateTempList(response.body().data)
                                loadPlacesData(tempList)
                            }
                        }

                    })
        }

    private fun generateTempList(data: ArrayList<PlacesData>?) {
        for(i in 0 until 5)
        {
            tempList.add(data!![i])
        }
    }

    private var tempList = ArrayList<PlacesData>()
    private fun loadPlacesData(data: ArrayList<PlacesData>?) {
        placesAdapter.swap(data)
    }

    private fun initializeLayoutView() {

        IconslayoutManager  = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        iconsAdapter = IconsAdapter(context!!,iconsDataList)
        topIconsRecyclerView.layoutManager = IconslayoutManager
        topIconsRecyclerView.adapter = iconsAdapter

        PropertieslayoutManager  = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        propertiesAdapter = PropertiesAdapter(activity!!,propertiesDataList)
        propertiesRecyclerview.layoutManager = PropertieslayoutManager
        propertiesRecyclerview.adapter = propertiesAdapter

        OfferslayoutManager  = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        offersAdapter = OffersAdapter(context!!,offersDataList)
        offersRecyclerview.layoutManager = OfferslayoutManager
        offersRecyclerview.adapter = offersAdapter

        PlaceslayoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        placesAdapter = PlacesAdapter(context!!,placesDataList)
        placesRecyclerview.layoutManager = PlaceslayoutManager
        placesRecyclerview.adapter = placesAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CategoriesFragment.
         */
        @JvmStatic
        fun newInstance() = HomeFragment()
    }


}