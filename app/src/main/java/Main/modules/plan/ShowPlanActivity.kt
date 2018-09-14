package modules.plan.Planning

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

import com.example.sarwan.final_year_project.R

import java.text.ParseException
import java.util.ArrayList
import java.util.Arrays
import java.util.HashSet
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

import java.math.BigDecimal
import java.sql.Time
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

import Main.Model.TripPlanning
import Main.Model.response.PlacesData
import base.ParentActivity
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.show_plan_layout.*

import java.lang.String.join

class ShowPlanningActivity : ParentActivity() {

    internal var mProgressBar: ProgressDialog? = null
    internal var Data = TripPlanning()
    //Total Budged
    internal var Budged = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        //Intent i = new Intent(this, Kotlin.class);
        //startActivity(i);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_plan_layout)

        Data = intent.extras!!.getSerializable(ApplicationConstants.TRIP_OBJECT_KEY) as TripPlanning
        //mProgressBar = new ProgressDialog(this);
        // setup the table

        tableInvoices.isStretchAllColumns = true
        tableInvoices.setPadding(10, 10, 10, 10)

        startLoadData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    fun startLoadData() {
        //        Places place = new Places();
        //        TripPlanning tripData = place.GetTripData();

        // editText = findViewById(R.id.editText);
        // editText.setText(tripData.StartDate.toString());

        //Invoices invoices = new Invoices();
        //Data = tripData;

        //        mProgressBar.setCancelable(false);
        //        mProgressBar.setMessage("Creating your Plan..");
        //        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //        mProgressBar.show();
        loadData(Data)
        //        mProgressBar.hide();
    }

    //Load Data
    fun loadData(data: TripPlanning) {

        //Initalize DateFormat and Calender Instance
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val localTimeFormat = SimpleDateFormat("HH:mm")
        val calendar = Calendar.getInstance()

        val rows = data.places!!.size

        tableInvoices?.removeAllViews();

        val InitialdateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        var Currentdate: Date? = null
        try {
            Currentdate = InitialdateFormat.parse(data.startDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        var previousPlace: PlacesData? = PlacesData()
        previousPlace = data.startPlace
        var noOfDays = data.noOfDays!!
        var TotalNoOfCab = 0
        //String CurrenDistrict = "";
        //String PreviousDistrict = "Karachi";

        //Date currentTime = new Date(2018,9,22,9,00);
        var DayHour = 9.0.toFloat()


        //String[] districtsOfSelectedPlaces = getUniqueDistricts(data.placeAndHotels);
        //String PreviousPlace = "Karachi";

        val districtPlaces = data.places

        //Initialize Column Headers
        InsertRow(null, true, 0)
        var TravelTime_InHour = 1
        var i = 0

        var fLocation: PlacesData.Location = data.places!![data.places!!.size - 1].location
        var sLocation: PlacesData.Location = data.endPlace!!.location
        var ReturnTimeToEndPlaceInDay = calculateTravelTime(fLocation.latitude, fLocation.longitude, sLocation.latitude, sLocation.longitude).toInt() / 24

        if (calculateTravelTime(fLocation.latitude, fLocation.longitude, sLocation.latitude, sLocation.longitude) % 24 > 0)
            ReturnTimeToEndPlaceInDay++

        //Loop Start
        if (data.startPlace!!.placeName == data.places!![0].placeName)
            i = 1
        else
            i = 0
        i = i
        while (i < districtPlaces!!.size && (data.noOfDays == -1 || noOfDays > ReturnTimeToEndPlaceInDay)) {
            val gridDataModel: GridDataModel
            var row: PlacesData? = null

            if (i > -1)
                row = districtPlaces[i]

            if ((data.noOfDays == -1 || noOfDays > ReturnTimeToEndPlaceInDay) && row!!.placeName == "Skardu" || row!!.placeName == "Diamer" || row.placeName == "Hunza-Nagar"
                    || row.placeName == "Astore" || row.placeName == "Ghanche" || row.placeName == "Ghizer") {
                // InsertBudgedRow(row.place.placeName, 0, i,true);
                gridDataModel = GridDataModel()

                gridDataModel.Date=(dateFormat.format(Currentdate))

                calendar.time = Currentdate
                var Time = localTimeFormat.format(calendar.time)

                fLocation = previousPlace!!.location
                sLocation = row.location
                TravelTime_InHour = calculateTravelTime(fLocation.latitude, fLocation.longitude, sLocation.latitude, sLocation.longitude).toInt()
                calendar.add(Calendar.HOUR, TravelTime_InHour)
                if (calculateTravelTime(fLocation.latitude, fLocation.longitude, sLocation.latitude, sLocation.longitude) > TravelTime_InHour)
                    calendar.add(Calendar.MINUTE, 30)


                Time = Time + "-" + localTimeFormat.format(calendar.time)
                //currentTime = calendar.getTime();
                Currentdate = calendar.time

                if (TravelTime_InHour >= 15) {
                    noOfDays--
                    TotalNoOfCab++
                }
                gridDataModel.Stay_Time = (Time)
                gridDataModel.From = (previousPlace.placeName)
                gridDataModel.To = (row.placeName)

                InsertRow(gridDataModel, false, i)
                InsertBudgedRow(row.placeName, 0, i, true)
                //PreviousDistrict = CurrenDistrict;
                previousPlace = row

            } else {
                gridDataModel = GridDataModel()
                val sdf = SimpleDateFormat("H")
                val travelHour = Integer.valueOf(sdf.format(Currentdate))

                fLocation = previousPlace!!.location
                sLocation = row.location
                //DayHour > row.place.StayAndTravelTime &&
                if (i != -1
                        && travelHour.toFloat() + row.stayTime!! + calculateTravelTime(fLocation.latitude, fLocation.longitude, sLocation.latitude, sLocation.longitude) + calculateTravelTime(sLocation.latitude, sLocation.longitude, row.hotel!!.location.latitude, row.hotel!!.location.longitude) >= 9 && travelHour.toFloat() + row.stayTime!! + calculateTravelTime(fLocation.latitude, fLocation.longitude, sLocation.latitude, sLocation.longitude) + calculateTravelTime(sLocation.latitude, sLocation.longitude, row.hotel!!.location.latitude, row.hotel!!.location.longitude) <= 18
                        && (data.noOfDays == -1 || noOfDays > ReturnTimeToEndPlaceInDay)) {
                    //Previous and current (row) loaction
                    TravelTime_InHour = calculateTravelTime(fLocation.latitude, fLocation.longitude, sLocation.latitude, sLocation.longitude).toInt()

                    //Assign First column Value
                    gridDataModel.Date = (dateFormat.format(Currentdate))

                    //Assign Second column Value
                    gridDataModel.From =(previousPlace.placeName)

                    //Assign Third column Value
                    gridDataModel.To = (row.placeName)
                    DayHour = DayHour - row.stayTime!!

                    //Assign Forth column Value
                    calendar.time = Currentdate
                    calendar.add(Calendar.HOUR, TravelTime_InHour)
                    if (calculateTravelTime(fLocation.latitude, fLocation.longitude, sLocation.latitude, sLocation.longitude) > TravelTime_InHour)
                        calendar.add(Calendar.MINUTE, 30)

                    var Time = localTimeFormat.format(calendar.time)

                    val sTime = row.stayTime!!.toInt()
                    calendar.add(Calendar.HOUR, sTime)
                    if (row.stayTime as Float > sTime)
                        calendar.add(Calendar.MINUTE, 30)

                    Time = Time + "-" + localTimeFormat.format(calendar.time)
                    Currentdate = calendar.time

                    gridDataModel.Stay_Time = (Time)

                    InsertRow(gridDataModel, false, i)
                    previousPlace = row

                }
                else {
                    if (i > -1)
                        row = districtPlaces[i - 1]

                    fLocation = previousPlace.location
                    sLocation = row.location

                    //Initialize First Column
                    gridDataModel.Date = (dateFormat.format(Currentdate))

                    //Initialize Second Column
                    gridDataModel.From = (previousPlace.placeName)

                    //Initialize Third Column
                    gridDataModel.To = (row.hotel!!.hotelName)
                    gridDataModel.HotelPrize = ("" + GetHotelPrize(row.hotel!!.hotelPrice!!, data.noOfGuests!!, false))

                    previousPlace.placeName = row.hotel!!.hotelName
                    previousPlace.location.latitude = row.hotel!!.location.latitude
                    previousPlace.location.longitude = row.hotel!!.location.longitude

                    //Initialize Forth Column
                    calendar.time = Currentdate
                    //Hotel and Place (Row)
                    TravelTime_InHour = calculateTravelTime(fLocation.latitude, fLocation.longitude, sLocation.latitude, sLocation.longitude).toInt()
                    calendar.add(Calendar.HOUR, TravelTime_InHour)
                    if (calculateTravelTime(fLocation.latitude, fLocation.longitude, sLocation.latitude, sLocation.longitude) > TravelTime_InHour)
                        calendar.add(Calendar.MINUTE, 30)
                    Currentdate = calendar.time

                    gridDataModel.Stay_Time = (localTimeFormat.format(calendar.time))

                    InsertRow(gridDataModel, false, i)

                    calendar.time = Currentdate

                    val sdfforMinutes = SimpleDateFormat("mm")
                    val minutes = Integer.valueOf(sdfforMinutes.format(calendar.time))

                    val a = Integer.valueOf(sdf.format(calendar.time))
                    val b = 9 - a
                    calendar.add(Calendar.HOUR, b)
                    if (minutes > 0)
                        calendar.add(Calendar.MINUTE, -30)
                    if (b < 0) {
                        calendar.add(Calendar.DATE, 1)
                        noOfDays--
                        TotalNoOfCab++
                    }

                    Currentdate = calendar.time
                    i--
                }
            }
            i++
        }
        //End Loop

        if (ReturnTimeToEndPlaceInDay > 0) {
            fLocation = previousPlace!!.location
            sLocation = data.endPlace!!.location

            val gridDataModel = GridDataModel()
            //Previous and Curren Place (Row)
            TravelTime_InHour = calculateTravelTime(fLocation.latitude, fLocation.longitude, sLocation.latitude, sLocation.longitude).toInt()

            gridDataModel.Date = (dateFormat.format(Currentdate))
            //initialize new Day

            //Initialize Second Column
            gridDataModel.From = (previousPlace.placeName)

            //Initialize Third Column
            gridDataModel.To = (data.endPlace!!.placeName)

            //Initialize Forth Column
            calendar.time = Currentdate
            var Time = localTimeFormat.format(calendar.time)

            calendar.add(Calendar.HOUR, TravelTime_InHour)
            if (calculateTravelTime(fLocation.latitude, fLocation.longitude, sLocation.latitude, sLocation.longitude) > TravelTime_InHour)
                calendar.add(Calendar.MINUTE, 30)


            Time = Time + "-" + localTimeFormat.format(calendar.time)

            gridDataModel.Stay_Time = (Time)

            InsertRow(gridDataModel, false, i)
        }
        //editText.setText(""+(GetHotelPrize(1,data.NoOfGuest, true) + GetTotalCabPrize(TotalNoOfCab,data.CabPrize)) );
        InsertBudgedRow("Budget", GetHotelPrize(1, data.noOfGuests!!, true) + GetTotalCabPrize(TotalNoOfCab, data.cab!!.cabPrice!!), i, false)
    }

    //Distance Function
    private fun calculateDistance(userLat: Double?, userLon: Double?, lat: Double?, lon: Double?): Long {

        val firstLocation = Location("")
        firstLocation.latitude = userLat!!
        firstLocation.longitude = userLon!!

        val secondLocation = Location("")
        secondLocation.latitude = lat!!
        secondLocation.longitude = lon!!

        val distanceInKiloMeters = firstLocation.distanceTo(secondLocation) / 1000
        return distanceInKiloMeters.toLong()

    }

    // Double userLat ,Double  userLon,Double  lat ,Double  lon
    private fun calculateTravelTime(userLat: Double?, userLon: Double?, lat: Double?, lon: Double?): Float {
        val d = calculateDistance(userLat, userLon, lat, lon)
        val hours = java.lang.Float.valueOf((d * 1000 / 11.111 / 3600).toString())
        val minutes = java.lang.Float.valueOf((d * 1000 / 11.111 / 60 % 60).toString())

        var r = hours!!
        if (minutes > 30)
            r++
        else
            r = (r + 0.5).toFloat()
        return r
    }


    // Get Hotel Budged
    private fun GetHotelPrize(HotelPrize: Int, NoOfGuest: Int, IsTotalBuged: Boolean): Int {
        if (IsTotalBuged == false) {
            var NoOfRooms = NoOfGuest / 5
            if (NoOfGuest % 5 > 0)
                NoOfRooms++
            Budged = Budged + NoOfRooms * HotelPrize
            if (IsTotalBuged == false) {
                return NoOfRooms * HotelPrize
            }
        }
        return Budged
    }

    //Get total Transport Prize
    private fun GetTotalCabPrize(NoOfDays: Int, CabPrize: Int): Int {
        return CabPrize * NoOfDays
    }

    //    private PlaceAndHotel[] getPlacesAndHotelAccordingToDistrict(PlaceAndHotel[] placeAndHotels, String DistrictName) {
    //        ArrayList<PlaceAndHotel> placesAndHotel = new ArrayList<>();
    //        PlaceAndHotel addDistrictAsaPlace = new PlaceAndHotel();
    //
    //        PlacesData placesData = new PlacesData();
    //        placesData.placeName = DistrictName;
    //        placesData.StayAndTravelTime = 2;
    //
    //        addDistrictAsaPlace.place = placesData;
    //        addDistrictAsaPlace.HotelName = "Moon Restaurant";
    //
    //        placesAndHotel.add(addDistrictAsaPlace);
    //
    //        for(PlaceAndHotel pAndH : placeAndHotels){
    //            if(DistrictName.equals(pAndH.place.placeCity))
    //            {
    //                placesAndHotel.add(pAndH);
    //            }
    //        }
    //
    //        PlaceAndHotel[] arr = new PlaceAndHotel[placesAndHotel.size()];
    //        placesAndHotel.toArray(arr);
    //
    //        return arr;
    //    }

    // Get Unique Districts
    //    private String[] getUniqueDistricts(PlaceAndHotel[] placeAndHotels)
    //    {
    //    ArrayList<String> Districts = new ArrayList<>();
    //    //String S = "";
    //    for(PlaceAndHotel placeAndHotel : placeAndHotels)
    //    {
    //        Districts.add(placeAndHotel.place.placeCity);
    //      //  S = S + placeAndHotel.place.placeCity;
    //    }
    //    Set<String> unique = new HashSet<>(Districts);
    //    String[] sas ={};
    //    return unique.toArray(sas);
    //    }

    //Insert Buged Row
    private fun InsertBudgedRow(Budged: String?, BudgedValue: Int, RowNumber: Int, IsOneColumn: Boolean) {
        val leftRowMargin = 0
        val topRowMargin = 0
        val rightRowMargin = 0
        val bottomRowMargin = 0

        val leftColumnMargin = 0
        val topColumnMargin = 0
        val rightColumnMargin = 0
        val bottomColumnMargin = 0

        var smallTextSize = 0
        var mediumTextSize = 0

        //textSize = (int) getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = resources.getDimension(R.dimen.font_size_small).toInt()
        mediumTextSize = resources.getDimension(R.dimen.font_size_medium).toInt()

        val columnHeaderColor = Color.RED
        //Declare First Column
        val tv = TextView(this)
        tv.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT)
        tv.gravity = Gravity.LEFT
        tv.setPadding(leftColumnMargin, topColumnMargin, rightColumnMargin, bottomColumnMargin)

        //Declare First Column
        tv.setBackgroundColor(Color.parseColor("#f8f8f8"))
        tv.setTextColor(Color.parseColor("#000000"))
        tv.text = Budged
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
        tv.setTextColor(columnHeaderColor)


        //Declare Second Column
        val tv2 = TextView(this)
        if (!IsOneColumn) {
            tv2.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT)
            tv2.gravity = Gravity.LEFT
            tv2.setPadding(leftColumnMargin, topColumnMargin, rightColumnMargin, bottomColumnMargin)

            tv2.setBackgroundColor(Color.parseColor("#ffffff"))
            tv2.setTextColor(columnHeaderColor)
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
            tv2.text = "" + BudgedValue
        }
        //Append Row
        val tr = TableRow(this)
        tr.id = RowNumber + 1
        val trParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT)
        trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin)
        tr.setPadding(0, 0, 0, 0)
        tr.layoutParams = trParams


        tr.addView(tv)
        tr.addView(tv2)

        tr.setOnClickListener { v ->
            val tr = v as TableRow
            //do whatever action is needed
        }
        tableInvoices?.addView(tr, trParams);

        //Line Seprator
        // add separator row
        val trSep = TableRow(this)
        val trParamsSep = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT)
        trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin)

        trSep.layoutParams = trParamsSep
        val tvSep = TextView(this)
        val tvSepLay = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT)
        tvSepLay.span = 4
        tvSep.layoutParams = tvSepLay
        tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"))
        tvSep.height = 1

        trSep.addView(tvSep)
        tableInvoices?.addView(trSep, trParamsSep);
    }

    //Insert Row
    fun InsertRow(rowData: GridDataModel?, isColumnHeader: Boolean, rowNumber: Int) {
        //Table Styling
        val leftRowMargin = 0
        val topRowMargin = 0
        val rightRowMargin = 0
        val bottomRowMargin = 0

        val leftColumnMargin = 5
        val topColumnMargin = 15
        val rightColumnMargin = 0
        val bottomColumnMargin = 15

        val columnHeaderColor = Color.BLUE
        var smallTextSize = 0
        var mediumTextSize = 0

        //textSize = (int) getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = resources.getDimension(R.dimen.font_size_small).toInt()
        mediumTextSize = resources.getDimension(R.dimen.font_size_medium).toInt()

        //Declare First Column
        val tv = TextView(this)
        if (isColumnHeader == true) {
            tv.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT)
        } else {
            tv.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT)
        }
        tv.gravity = Gravity.LEFT
        tv.setPadding(leftColumnMargin, topColumnMargin, rightColumnMargin, bottomColumnMargin)

        //Declare First Column
        if (isColumnHeader == true) {
            tv.text = "Date"
            tv.setBackgroundColor(Color.parseColor("#f0f0f0"))
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
            tv.setTextColor(columnHeaderColor)
        } else {
            tv.setBackgroundColor(Color.parseColor("#f8f8f8"))
            tv.setTextColor(Color.parseColor("#000000"))
            tv.setText(rowData!!.Date)
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
        }

        //Declare Second Column
        val tv2 = TextView(this)
        if (isColumnHeader == true) {
            tv2.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT)
        } else {
            tv2.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT)
        }
        tv2.gravity = Gravity.LEFT
        tv2.setPadding(leftColumnMargin, topColumnMargin, rightColumnMargin, bottomColumnMargin)

        if (isColumnHeader == true) {
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
            tv2.setBackgroundColor(Color.parseColor("#f7f7f7"))
            tv2.setTextColor(columnHeaderColor)
            tv2.text = "From"
        } else {
            tv2.setBackgroundColor(Color.parseColor("#ffffff"))
            tv2.setTextColor(Color.parseColor("#000000"))
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
            tv2.setText(rowData!!.From)
        }

        //Declare Third Column
        val tv3 = TextView(this)
        if (isColumnHeader == true) {
            tv3.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT)
        } else {
            tv3.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT)
        }
        tv3.gravity = Gravity.TOP
        tv3.setPadding(leftColumnMargin, topColumnMargin, rightColumnMargin, bottomColumnMargin)

        //Initialize Third Column
        if (isColumnHeader == true) {
            tv3.setBackgroundColor(Color.parseColor("#f0f0f0"))
            tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
            tv3.setTextColor(columnHeaderColor)
            tv3.text = "To"

        } else {
            tv3.setBackgroundColor(Color.parseColor("#f8f8f8"))
            tv3.setTextColor(Color.parseColor("#000000"))
            tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
            tv3.setText(rowData!!.To)
        }

        val tv4 = TextView(this)
        if (isColumnHeader == true) {
            tv4.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT)
        } else {
            tv4.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT)
        }
        tv4.setPadding(leftColumnMargin, topColumnMargin, rightColumnMargin, bottomColumnMargin)
        tv4.gravity = Gravity.LEFT

        if (isColumnHeader == true) {
            tv4.setBackgroundColor(Color.parseColor("#f7f7f7"))
            tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
            tv4.setTextColor(columnHeaderColor)
            tv4.text = "Time"
        } else {
            tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
            tv4.setBackgroundColor(Color.parseColor("#ffffff"))
            tv4.setTextColor(Color.parseColor("#000000"))
            tv4.setText(rowData?.Stay_Time)
        }

        //Append Row
        val tr = TableRow(this)
        tr.id = rowNumber + 1
        val trParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT)
        trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin)
        tr.setPadding(0, 0, 0, 0)
        tr.layoutParams = trParams


        tr.addView(tv)
        tr.addView(tv2)
        tr.addView(tv3)
        tr.addView(tv4)

        if (isColumnHeader == false) {

            tr.setOnClickListener { v ->
                val tr = v as TableRow
                //do whatever action is needed
            }
        }
        tableInvoices?.addView(tr, trParams);

        //Line Seprator
        if (isColumnHeader == false) {

            // add separator row
            val trSep = TableRow(this)
            val trParamsSep = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT)
            trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin)

            trSep.layoutParams = trParamsSep
            val tvSep = TextView(this)
            val tvSepLay = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT)
            tvSepLay.span = 4
            tvSep.layoutParams = tvSepLay
            tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"))
            tvSep.height = 1

            trSep.addView(tvSep)
            tableInvoices?.addView(trSep, trParamsSep);
        }
    }

}
