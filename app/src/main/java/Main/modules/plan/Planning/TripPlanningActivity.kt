package Main.modules.plan.Planning

import Main.Model.TripPlanning
import Main.Model.response.*
import Main.modules.plan.Trips.*
import Main.utils.DateTimeUtility
import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import com.mobitribe.app.ezzerecharge.network.RestClient
import kotlinx.android.synthetic.main.trip_planning_new_activity.*
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.widget.DatePicker
import android.widget.TextView
import co.lujun.androidtagview.TagView
import extras.AlertDialog
import extras.ApplicationConstants
import modules.Planning.DetailsActivity
import modules.plan.Planning.ShowPlan
import modules.plan.Planning.ShowPlanningActivity
import utils.ValidationUtility
import kotlin.collections.HashMap


class TripPlanningActivity : Fragment(), SwipeRefreshLayout.OnRefreshListener,
        PlacesListener,FragmentInteraction,PlacesRecyclerViewClickListener,StartAndEndListener ,CabRecyclerViewClickListener{

    private var pActivity : ParentActivity? =null
    private var font : Typeface ? = null

    private var selectedRegion : String ? = null
    private var alertDialog = AlertDialog()
    private var tripObject = TripPlanning()

    private lateinit var PlaceslayoutManager : LinearLayoutManager
    private var placesDataList = ArrayList<PlacesData?>()
    private lateinit var placesAdapter : PlanTripPlacesAdapter

    private lateinit var UserPlaceslayoutManager : LinearLayoutManager
    private lateinit var userPlacesAdapter : UserPlanTripPlacesAdapter
    private var userPlacesDataList = ArrayList<PlacesData?>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = (activity as ParentActivity)
        fetchTempPlaces()
        fetchTempHotels()
        fetchCabs()

    }

    private var cabs = ArrayList<CabData>()
    private fun fetchCabs() {
        RestClient.getRestAdapter().
                getCabs().
                enqueue(object : retrofit2.Callback<CabResponse>{
                    override fun onFailure(call: Call<CabResponse>?, t: Throwable?) {
                        pActivity?.onFailureResponse(t!!)
                    }

                    override fun onResponse(call: Call<CabResponse>?, response: Response<CabResponse>?) {

                        if (response?.body() != null) {
                            cabs = response.body().data!!
                        }
                    }

                })
    }

    private fun loadCabsData(data: ArrayList<CabData>) {
        val finalList = data.filter { it.cabCategory == tripObject.preference } as ArrayList<CabData>
        cabsAdapter.swap(finalList)
    }
    private var hotels: ArrayList<HotelData> ? = null

    private fun initializeLayout(){
        CabslayoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL,false)
        cabsAdapter = PlanTripCabsAdapter(context!!,cabsDataList,this)
        recCabsRecyclerview.layoutManager = CabslayoutManager
        recCabsRecyclerview.adapter = cabsAdapter
    }

    private lateinit var CabslayoutManager : LinearLayoutManager
    private var cabsDataList = ArrayList<CabData?>()
    private lateinit var cabsAdapter : PlanTripCabsAdapter

    override fun onCabCardSelected(position: Int, selection: String) {
        tripObject.cab = cabsDataList.get(position)
    }
    private fun fetchTempHotels() {
        RestClient.getRestAdapter().
                getHotels().
                enqueue(object : retrofit2.Callback<HotelsResponse>{
                    override fun onFailure(call: Call<HotelsResponse>?, t: Throwable?) {
                        pActivity?.onFailureResponse(t!!)
                    }

                    override fun onResponse(call: Call<HotelsResponse>?, response: Response<HotelsResponse>?) {

                        if (response?.body() != null) {
                            hotels = response.body().data!!
                        }
                    }

                })
    }

    private var places: ArrayList<PlacesData>? = null

    private fun fetchTempPlaces() {
        RestClient.getRestAdapter().
                getPlaces().
                enqueue(object : retrofit2.Callback<PlacesResponse>{
                    override fun onFailure(call: Call<PlacesResponse>?, t: Throwable?) {
                        pActivity?.onFailureResponse(t!!)
                    }

                    override fun onResponse(call: Call<PlacesResponse>?, response: Response<PlacesResponse>?) {

                        if (response?.body() != null) {
                            places = response.body().data
                        }
                    }

                })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.trip_planning_new_activity,container,false)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        font = Typeface.createFromAsset(context?.assets, "fonts/titilliumwebregular.ttf")
        initializeLayout()
        initializeLayoutView()
        settingupTags()
        setupSpinner()
        calendarBuilder()
        clickListeners()
        setPreferenceLayout()
    }

    private fun setPreferenceLayout() {
        luxury.setOnClickListener {
            changeColor(luxury, economic)
            tripObject.preference = ApplicationConstants.LUXURY
        }
        economic.setOnClickListener {
            changeColor(economic,luxury)
            tripObject.preference = ApplicationConstants.ECONOMIC
        }
    }

    private fun changeColor(textView: TextView, update: TextView){
        textView.background = context?.resources?.getDrawable(R.drawable.text_view_background_dark_red)
        update.background = context?.resources?.getDrawable(R.drawable.text_view_background_red)
    }

    private var counter = 1
    private fun collectData() {
        if(selectedDist.size>0)
        {
            tripObject.adults = adults.text.toString()
            tripObject.childrens = childs.text.toString()
            tripObject.noOfGuests = Integer.parseInt(tripObject.adults).plus(Integer.parseInt(tripObject.childrens))
            if(validEndDate()){
                tripObject.endDate = to.text.toString()
                tripObject.noOfDays = Integer.parseInt(DateTimeUtility.calculateNoOfDays(from.text.toString(),to.text.toString()))
            }
            else {
                tripObject.endDate = ""
                tripObject.noOfDays = -1
            }
            if(!startAndEndPlace()){
                tripObject.startPlace = startP as PlacesData
                tripObject.endPlace = endP as PlacesData
            }
            else {
                tripObject.startPlace = startP as PlacesData
                tripObject.endPlace = endP as PlacesData
            }
            tripObject.districts = selectedDist
            tripObject.restaurant = selectedRestaurant
            //addDistrictPlaces()
            selectedPlaces.sortBy { it.sortOrder }
            tripObject.places = selectedPlaces
            checkHotel(tripObject.places)
            tripObject.iD = counter
            tripObject.tripName = "TRIP " + counter.toString() + " " + from.text.toString() + " to " + to.text.toString()
            counter++

            activity?.startActivity(Intent(context!!, ShowPlan::class.java).putExtra(ApplicationConstants.TRIP_OBJECT_KEY,tripObject))
        }

        else {
            tripObject.adults = adults.text.toString()
            tripObject.childrens = childs.text.toString()
            tripObject.noOfGuests = Integer.parseInt(tripObject.adults).plus(Integer.parseInt(tripObject.childrens))
            if(validEndDate()){
                tripObject.endDate = to.text.toString()
                tripObject.noOfDays = Integer.parseInt(DateTimeUtility.calculateNoOfDays(from.text.toString(),to.text.toString()))
            }
            else {
                tripObject.endDate = ""
                tripObject.noOfDays = -1
            }
            if(startAndEndPlace()){
                tripObject.startPlace = startP as PlacesData
                tripObject.endPlace = endP as PlacesData
            }
            tripObject.cab = getCab()
            tripObject.districts = selectedDist
            tripObject.iD = counter
            getRegionPlaces(districtList)
            tripObject.tripName = "TRIP " + counter.toString() + " " + from.text.toString() + " to " + to.text.toString()
            counter++
            if(goToNext){
                if(!tripObject.places?.isEmpty()!!)
                {
                    activity?.startActivity(Intent(context!!,ShowPlanningActivity::class.java).putExtra(ApplicationConstants.TRIP_OBJECT_KEY,tripObject))
                }
            }
        }
    }

    private fun getRegionPlaces(selectedRegionDistricts: ArrayList<AllDistrictsData>?) {
        for(i in 0 until selectedRegionDistricts?.get(0)?.districts?.size!!)
        {
            getDistrict = selectedRegionDistricts[0].districts?.get(i)
            for(j in 0 until places?.size as Int){
                val filter = places?.filter { it.districtName == getDistrict }
                if(filter?.size as Int>0)
                {
                    for(k in 0 until filter.size)
                    {
                        selectedPlaces.add(filter?.get(k)!!)
                    }
                    break
                }
            }
        }


        if(selectedPlaces.size>0){
            selectedPlaces.sortBy { it.sortOrder }
            tripObject.places = selectedPlaces
            getRegionHotel()
        }
    }

    private fun getRegionHotel() {
        checkHotel(selectedPlaces)
    }

    private fun checkHotel(places: ArrayList<PlacesData>?) {
        for(i in 0 until places?.size!!)
        {
            if(places[i].hotel==null){
                tripObject.places!![i].hotel = filterHotel(hotels,places[i].placeName!!,places[i].nearBy!!)
                goToNext = true
            }
            else {
                goToNext = true
            }
        }
    }

    private var getDistrict : String ? = null


    private var goToNext : Boolean = false
    private fun validEndDate(): Boolean {
        if(!TextUtils.isEmpty(to.text.toString()))
        {
            return true
        }
        return false
    }

    private fun validStartDate(): Boolean {
        if(!TextUtils.isEmpty(from.text.toString()))
        {
            return true
        }
        return false
    }

    private fun startAndEndPlace(): Boolean {
        if(startP==null && endP==null){
            startPlace()
            endPlace()
            return true
        }
        else if(startP==null){
            startPlace()
            return true
        }
        else if(endP==null){
            endPlace()
            return true
        }
        return false
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private var sdf = SimpleDateFormat(ApplicationConstants.DATE_FORMAT,Locale.US)
    @RequiresApi(Build.VERSION_CODES.N)
    private fun clickListeners() {
        planButton.setOnClickListener {
            if(validStartDate()){
                tripObject.startDate = from.text.toString()/*.plus(" 09:00")*/
                collectData()
            }
            else {
                from.error = "Required"
                ValidationUtility.removeErrors(from)
            }
        }
    }

    private val myCalendar = Calendar.getInstance()
    private fun calendarBuilder() {

        startDate.setOnClickListener {
            setDatePickerDialog(getString(R.string.fromDate))
        }
        endDate.setOnClickListener {
            setDatePickerDialog(getString(R.string.toDate))

        }
    }
    private fun setDatePickerDialog(whichOne: String) {
        val date = object : DatePickerDialog.OnDateSetListener {

            @TargetApi(Build.VERSION_CODES.N)
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {

                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel(whichOne)
            }

        }
        val datePickerDialog = DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH))
        if(sDate!=null){
            datePickerDialog.datePicker.minDate = sDate as Long
        }
        else {
            datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
        }
        datePickerDialog.show()
    }

    private var sDate : Long ? = null
    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateLabel(whichOne : String) {
        when(whichOne){
            getString(R.string.fromDate) ->
            {
                sDate = myCalendar.timeInMillis
                from.setText(sdf.format(myCalendar.time), TextView.BufferType.EDITABLE)
            }
            getString(R.string.toDate) -> {
                to.setText(sdf.format(myCalendar.time), TextView.BufferType.EDITABLE)
                totalDays.text = (ApplicationConstants.TOTAL_DAYS + DateTimeUtility.calculateNoOfDays(from.text.toString(),to.text.toString()))
                totalDays.visibility = View.VISIBLE
            }
        }
    }


    private fun requestForRegions() {
        pActivity?.showProgress()
        RestClient.getRestAdapter().
                getAllDistricts().
                enqueue(object : retrofit2.Callback<AllDistrictsResponse>{
                    override fun onFailure(call: Call<AllDistrictsResponse>?, t: Throwable?) {
                        pActivity?.hideProgress()
                        pActivity?.onFailureResponse(t!!)
                    }

                    override fun onResponse(call: Call<AllDistrictsResponse>?, response: Response<AllDistrictsResponse>?) {
                        pActivity?.hideProgress()
                        if (response?.body() != null) {
                            showRegions(filterData(response.body().data))
                            belowSpinnerLayout.visibility = View.VISIBLE
                        }
                    }

                })
    }

    private fun showRegions(filterData: ArrayList<AllDistrictsData>?) {
        tagContainerLayout?.setTags(filterData!![0].districts!!,tagBackgrounds)
        tagContainerLayout?.setOnTagClickListener(object : TagView.OnTagClickListener{
            override fun onTagLongClick(position: Int, text: String?) {

            }

            override fun onTagClick(position: Int, text: String?) {
                tagContainerLayout?.getTagView(position)?.setTagBackgroundColor(R.color.colorPrimary)
                selectedDist.add(filterData!![0].districts?.get(position)!!)
                if(selectedDist!=null){
                    filterPlaceData()
                }
                else alertDialog.builder(context!!,"Please select at least one District")
            }

            override fun onTagCrossClick(position: Int) {
            }
        })
    }


    private var tagBackgrounds : MutableList<IntArray> ? = null
    private var tagBackgroundArray : ArrayList<Int> ? = null
    private var selectedDist = ArrayList<String>()

    private var districtList : ArrayList<AllDistrictsData>?=null
    private fun filterData(data: ArrayList<AllDistrictsData>?) : ArrayList<AllDistrictsData>{
        districtList = data?.filter { it.regionName == selectedRegion } as ArrayList<AllDistrictsData>
        return districtList!!
    }


    private fun setupSpinner() {
        val dataset = LinkedList(Arrays.asList("Punjab","KPK","Gilgit Baltistan"))
        spinner.attachDataSource(dataset)
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if((!(adults.text.isNullOrEmpty() || TextUtils.isEmpty(adults.text)) || !(childs.text.isNullOrEmpty() || TextUtils.isEmpty(childs.text)) ||
                        !(adults.text.isNullOrEmpty() || TextUtils.isEmpty(adults.text)) && !(childs.text.isNullOrEmpty() || TextUtils.isEmpty(childs.text)))
                )
                {
                    guestsCount.put(adults.text.toString(),childs.text.toString())
                    selectedDist.clear()
                    selectedRegion = dataset[p2]
                    if(selectedRegion.equals("Gilgit Baltistan")) {
                        requestForRegions()
                    }
                }

                else {
                    ValidationUtility.setEditTextError("Required",adults,childs)
                    ValidationUtility.removeErrors(adults,childs)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        spinner.setTextInternal("e.g : KPK / Balochistan")

        spinner.setOnClickListener {
            belowSpinnerLayout.visibility = View.GONE
            placesDataList.clear()
            userPlacesDataList.clear()
            selectedPlaces.clear()
            placesAdapter.clear()
            userPlacesAdapter.clear()
        }
    }

    private fun initializeLayoutView() {
        PlaceslayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
        placesAdapter = PlanTripPlacesAdapter(context!!,placesDataList,this)
        placeRecyclerView.layoutManager = PlaceslayoutManager
        placeRecyclerView.adapter = placesAdapter

        UserPlaceslayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
        userPlacesAdapter = UserPlanTripPlacesAdapter(context!!,userPlacesDataList,this,pActivity!!,this)
        usersPlaceRecyclerView.layoutManager = UserPlaceslayoutManager
        usersPlaceRecyclerView.adapter = userPlacesAdapter
    }

    private fun settingupTags() {
        tagBackgroundArray = arrayListOf(R.color.progressLoader,R.color.iconColor,R.color.darkGrey,R.color.facebookColor
                ,R.color.parrotGreen,R.color.bronzeBlueScreen)
        tagContainerLayout?.tagTypeface = font
        theme = intArrayOf(tagBackgroundArray!![(0 until tagBackgroundArray!!.size).random()],R.color.white,R.color.white)
        tagBackgrounds?.add(theme!!)
    }
    private var theme : IntArray ? = null

    private fun getCab() : CabData{
        var packageCategory = ""
        if (!luxury.text.toString().isEmpty())
        {
           packageCategory = luxury.text.toString()
        }
        else if (!economic.text.toString().isEmpty())
        {
            packageCategory = economic.text.toString()
        }

        return cabs.filter { it.cabCategory == packageCategory }.get(0)
    }

    private fun filterPlaceData() {
        selectedDist.sorted()
        filteredPlaces = ArrayList()
        placesAdapter.clear()

        for(i in 0 until selectedDist.size)
        {
            filteredPlaces?.add(places?.filter { it.districtName==selectedDist.get(i) } as ArrayList<PlacesData>)
            loadPlaceAdapter(filteredPlaces?.get(i),selectedDist.size,i)
        }
        recCabsRecyclerview.visibility = View.VISIBLE
        loadCabsData(cabs)
    }

    private fun loadPlaceAdapter(selectedPlaces: ArrayList<PlacesData>?, size: Int, lastIndex: Int) {
        placesAdapter.addMore(selectedPlaces)
    }

    private var filteredPlaces = ArrayList<ArrayList<PlacesData>>()
    private var selectedPlaces = ArrayList<PlacesData>()


    override fun onRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun onAttach(placesBundle: PlacesData?, hotelBundle: HotelData?,restBundle: RestaurantData?,position: Int) {
        startActivityPosition = position
        val intent = Intent()
        intent.putExtra(ApplicationConstants.PLACES_OBJECT_KEY,placesBundle)
        intent.putExtra(ApplicationConstants.GUESTS,guestsCount)
        intent.setClass(activity?.baseContext, DetailsActivity::class.java)
        this.startActivityForResult(intent,ApplicationConstants.DETAILS_ACTIVITY_REQUEST_CODE)
    }

    private var guestsCount = HashMap<String,String>()
    private var startActivityPosition : Int ? = null

    private var selectedRestaurant : RestaurantData ? = null
    private var selectedHotel : HotelData ? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode){
            ApplicationConstants.DETAILS_ACTIVITY_REQUEST_CODE->
            {
                showSelectedCards(data)
            }
        }
    }

    private fun showSelectedCards(data: Intent?) {
        val dataObj = data?.extras?.getBundle(ApplicationConstants.DETAILS_OBJECT_KEY)
        selectedHotel = (dataObj?.getSerializable(ApplicationConstants.HOTEL_OBJECT_KEY) as? HotelData)
        selectedRestaurant = (dataObj?.getSerializable(ApplicationConstants.RESTAURANT_OBJECT_KEY) as? RestaurantData)
        if(selectedRestaurant!=null){
            userPlacesDataList.get(startActivityPosition!!)?.restaurantName = (dataObj?.getSerializable(ApplicationConstants.RESTAURANT_OBJECT_KEY) as RestaurantData).restaurantName
        }
        selectedPlaces?.get(startActivityPosition!!)?.hotel = (dataObj?.getSerializable(ApplicationConstants.HOTEL_OBJECT_KEY) as HotelData)
        userPlacesDataList.get(startActivityPosition!!)?.hotel = (dataObj?.getSerializable(ApplicationConstants.HOTEL_OBJECT_KEY) as HotelData)
        userPlacesAdapter.update(startActivityPosition!!,userPlacesDataList.get(startActivityPosition!!))
    }


    override fun onSelected(iD: Int, vararg selection : String) : Boolean{
        selectedPlacesHeading.visibility = View.VISIBLE
        val requestToAdd = placesDataList.filter { it?.iD==iD }
        if(selectedPlaces.size>0){
            val toAdd = selectedPlaces.filter { it.iD == requestToAdd.get(0)?.iD }
            if(toAdd==null || toAdd.isEmpty())
            {
                selectedPlaces.add(requestToAdd.get(0) as PlacesData)
                userPlacesAdapter.swap(selectedPlaces)
                return true
            }
            else {
                return false
            }
        }
        else {
            selectedPlaces.add(requestToAdd.get(0)!!)
            userPlacesAdapter.addMore(selectedPlaces)
            return true
        }
    }

    override fun onDeSelected(iD: Int) : Boolean{
        val requestToRemove = placesDataList.filter { it?.iD==iD }
        if(selectedPlaces.size==0)
        {
            return false
        }
        else {
            val toRemove = (selectedPlaces.filter { it.iD== requestToRemove.get(0)?.iD })
            try {
                val index = selectedPlaces.indexOf(toRemove.get(0))
                if(toRemove!=null || !toRemove.isEmpty())
                {
                    selectedPlaces.removeAt(index)
                    userPlacesAdapter.swap(selectedPlaces)
                    return true
                }
                else
                {
                    return false
                }
            }catch (e : IndexOutOfBoundsException){
                return false
            }
        }
    }

    private fun startPlace(){
        startP = PlacesData()
        startP?.placeName = pActivity?.profile?.cityName!!
        startP?.location?.latitude = pActivity?.profile?.latitude
        startP?.location?.longitude = pActivity?.profile?.longitude
    }

    private fun endPlace(){
        endP = PlacesData()
        endP?.placeName = pActivity?.profile?.cityName!!
        endP?.location?.latitude = pActivity?.profile?.latitude
        endP?.location?.longitude = pActivity?.profile?.longitude
    }
    var startP  : PlacesData ? = null
    var endP : PlacesData  ? = null
    override fun onPlaceCardSelected(startPlace : String?, endPlace : String?) {
       /* startP = startPlace
        endP = endPlace*/
    }

    override fun onPlaceCardDeSelected(startPlace : String?, endPlace : String?) {
       /* startP = startPlace
        endP = endPlace*/
    }
    override fun onStart(data: PlacesData?) {
        startP = data!!
    }

    override fun onEnd(data: PlacesData?) {
        endP = data!!
    }

    override fun onRemove(position: Int) {
        placesAdapter.remove(position)
        placesDataList.removeAt(position)
    }

    private var availableHotels: HotelData? = null

    private fun filterHotel(data: java.util.ArrayList<HotelData>?, filterBy: String, nearBy: String): HotelData? {
        val mPreference = tripObject.preference
        doFilter(data,filterBy,nearBy,mPreference)
        if(availableHotels!=null)
        {
            return availableHotels
        }
        else {
            return doFilter(data,filterBy,nearBy,updatePreference(mPreference))
        }
    }

    private fun updatePreference(mPreference: String?) : String{
        if(mPreference?.equals(preferences[0]) as Boolean)
        {
            return preferences[1]
        }
        else {
            return  preferences[0]
        }
    }

    private fun doFilter(data: java.util.ArrayList<HotelData>?, filterBy: String, nearBy: String, mPreference: String?) : HotelData? {
        try {
            var tempList = (data?.filter { it.hotelCityName == filterBy  && it.category == mPreference })
            if(tempList==null || tempList?.size == 0)
            {
                tempList = (data?.filter { it.hotelCityName == nearBy  && it.category == mPreference })
                if(tempList!=null || tempList?.size != 0){
                    availableHotels = tempList!![0]
                }
                else availableHotels = null
            }
            else { availableHotels = tempList[0] }
            return availableHotels
        }
        catch (e : Exception){
            e.printStackTrace()
            availableHotels = null
            return availableHotels
        }
    }

    var preferences = arrayListOf<String>("Luxury","Economical")

    fun ClosedRange<Int>.random() =
            Random().nextInt((endInclusive + 1) - start) +  start

    private fun addDistrictPlaces()
    {
        for (i in 0 until selectedDist.size)
        {
            val districtName = selectedDist.get(i)
            if(!districtName.isEmpty())
            {
                when(districtName){
                    "Ghizer"->{selectedPlaces.add(places?.filter { it.iD==67 }?.get(0) as PlacesData)}
                    "Diamer"->{selectedPlaces.add(places?.filter { it.iD==68 }?.get(0) as PlacesData)}
                    "Hunza-Nagar"->{selectedPlaces.add(places?.filter { it.iD==66 }?.get(0) as PlacesData)}
                    "Skardu"->{selectedPlaces.add(places?.filter { it.iD== 39}?.get(0) as PlacesData)}
                    "Ghanche"->{selectedPlaces.add(places?.filter { it.iD==65 }?.get(0) as PlacesData)}
                    "Astore"->{selectedPlaces.add(places?.filter { it.iD==11 }?.get(0) as PlacesData)}
                }
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CategoriesFragment.
         */
        @JvmStatic
        fun newInstance() = TripPlanningActivity()
    }
}