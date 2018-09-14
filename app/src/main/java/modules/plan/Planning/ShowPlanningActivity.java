/*
package modules.plan.Planning;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sarwan.final_year_project.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Main.Model.TripPlanning;
import Main.Model.response.PlacesData;
import base.ParentActivity;
import extras.ApplicationConstants;

import static java.lang.String.join;

public class ShowPlanningActivity extends ParentActivity {

    private TableLayout mTableLayout;
    ProgressDialog mProgressBar;
    EditText editText;
    TripPlanning Data = new TripPlanning();
    //Total Budged
    int Budged = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Intent i = new Intent(this, Kotlin.class);
        //startActivity(i);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_planning);

        Data = (TripPlanning) getIntent().getExtras().getSerializable(ApplicationConstants.TRIP_OBJECT_KEY);
        //mProgressBar = new ProgressDialog(this);
        // setup the table
mTableLayout = (TableLayout) findViewById(R.id.tableInvoices);

        mTableLayout.setStretchAllColumns(true);
        mTableLayout.setPadding(10, 10, 10, 10);


        startLoadData();
    }

    public void startLoadData()
    {
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
        loadData(Data);
//        mProgressBar.hide();
    }

    //Load Data
    public void loadData(TripPlanning data) {

        //Initalize DateFormat and Calender Instance
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat localTimeFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();

        int rows = data.getPlaces().size();

        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(data.getStartDate());
        //mTableLayout.removeAllViews();

        SimpleDateFormat InitialdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date Currentdate = null;
        try {
            Currentdate = InitialdateFormat.parse(data.getStartDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PlacesData previousPlace = new PlacesData();
        previousPlace = data.getStartPlace();
        int noOfDays = data.getNoOfDays();
        int TotalNoOfCab = 0;
        //String CurrenDistrict = "";
        //String PreviousDistrict = "Karachi";

        //Date currentTime = new Date(2018,9,22,9,00);
        float DayHour = (float) 9.0;


        //String[] districtsOfSelectedPlaces = getUniqueDistricts(data.placeAndHotels);
        //String PreviousPlace = "Karachi";

        ArrayList<PlacesData> districtPlaces = data.getPlaces();

        //Initialize Column Headers
        InsertRow(null,true,0);
        int TravelTime_InHour = 1;
        int i = 0;

        PlacesData.Location fLocation = data.getPlaces().get(data.getPlaces().size()-1).getLocation();
        PlacesData.Location sLocation = data.getEndPlace().getLocation();
        int ReturnTimeToEndPlaceInDay = (int) calculateTravelTime(fLocation.getLatitude(), fLocation.getLongitude(), sLocation.getLatitude(), sLocation.getLongitude())/24;
        if(calculateTravelTime(fLocation.getLatitude(), fLocation.getLongitude(), sLocation.getLatitude(), sLocation.getLongitude())%24 > 0)
            ReturnTimeToEndPlaceInDay++;

        //Loop Start
        if(data.getStartPlace().getPlaceName().equals(data.getPlaces().get(0).getPlaceName()))
                i=1;
        else
            i=0;
        for ( i=i ; i < districtPlaces.size() && (data.getNoOfDays()== -1 || noOfDays > ReturnTimeToEndPlaceInDay); i++) {
            GridDataModel gridDataModel;
            PlacesData row = null;

            if (i > -1)
                row = districtPlaces.get(i);

            if((data.getNoOfDays() == -1 || noOfDays > ReturnTimeToEndPlaceInDay)
                    && row.getPlaceName().equals("Skardu") || row.getPlaceName().equals("Diamer") || row.getPlaceName().equals("Hunza-Nagar")
                    || row.getPlaceName().equals("Astore") || row.getPlaceName().equals("Ghanche") || row.getPlaceName().equals("Ghizer"))
            {
               // InsertBudgedRow(row.place.placeName, 0, i,true);
                gridDataModel = new GridDataModel();

                gridDataModel.setDate(dateFormat.format(Currentdate));

                calendar.setTime(Currentdate);
                String Time = localTimeFormat.format(calendar.getTime());

                fLocation = previousPlace.getLocation();
                sLocation = row.getLocation();
                TravelTime_InHour = (int) calculateTravelTime(fLocation.getLatitude(), fLocation.getLongitude(), sLocation.getLatitude(), sLocation.getLongitude());
                calendar.add(Calendar.HOUR, TravelTime_InHour);
                if(calculateTravelTime(fLocation.getLatitude(), fLocation.getLongitude(), sLocation.getLatitude(), sLocation.getLongitude()) > TravelTime_InHour)
                    calendar.add(Calendar.MINUTE, 30);


                Time = Time + "-" + localTimeFormat.format(calendar.getTime());
                //currentTime = calendar.getTime();
                Currentdate = calendar.getTime();

                if(TravelTime_InHour >= 15) {
                    noOfDays--;
                    TotalNoOfCab++;
                }
                gridDataModel.setStay_Time(Time);
                gridDataModel.setFrom(previousPlace.getPlaceName());
                gridDataModel.setTo(row.getPlaceName());

                InsertRow(gridDataModel,false,i);
                InsertBudgedRow(row.getPlaceName(), 0, i,true);
                //PreviousDistrict = CurrenDistrict;
                previousPlace = row;

            }
            else {
                gridDataModel = new GridDataModel();
                SimpleDateFormat sdf = new SimpleDateFormat("H");
                int travelHour = Integer.valueOf(sdf.format(Currentdate));

                fLocation = previousPlace.getLocation();
                sLocation = row.getLocation();
                //DayHour > row.place.StayAndTravelTime &&
                if (i != -1
                  && (travelHour + row.getStayTime() + calculateTravelTime(fLocation.getLatitude(), fLocation.getLongitude(), sLocation.getLatitude(), sLocation.getLongitude()) + calculateTravelTime(sLocation.getLatitude(), sLocation.getLongitude(), row.getHotel().getLocation().getLatitude(), row.getHotel().getLocation().getLongitude()) >= 9
                  && travelHour + row.getStayTime() + calculateTravelTime(fLocation.getLatitude(), fLocation.getLongitude(), sLocation.getLatitude(), sLocation.getLongitude()) + calculateTravelTime(sLocation.getLatitude(), sLocation.getLongitude(), row.getHotel().getLocation().getLatitude(), row.getHotel().getLocation().getLongitude()) <= 18)
                  && (data.getNoOfDays()==-1 || noOfDays > ReturnTimeToEndPlaceInDay)) {
                    //Previous and current (row) loaction
                    TravelTime_InHour = (int) calculateTravelTime(fLocation.getLatitude(), fLocation.getLongitude(), sLocation.getLatitude(), sLocation.getLongitude());

                    //Assign First column Value
                    gridDataModel.setDate(dateFormat.format(Currentdate));

                    //Assign Second column Value
                    gridDataModel.setFrom(previousPlace.getPlaceName());

                    //Assign Third column Value
                    gridDataModel.setTo(row.getPlaceName());
                    DayHour = DayHour - row.getStayTime();

                    //Assign Forth column Value
                    calendar.setTime(Currentdate);
                    calendar.add(Calendar.HOUR, TravelTime_InHour);
                    if(calculateTravelTime(fLocation.getLatitude(), fLocation.getLongitude(), sLocation.getLatitude(), sLocation.getLongitude()) > TravelTime_InHour)
                        calendar.add(Calendar.MINUTE, 30);

                    String Time = localTimeFormat.format(calendar.getTime());

                    int sTime = (row.getStayTime()).intValue();
                    calendar.add(Calendar.HOUR, sTime);
                    if (row.getStayTime() > sTime)
                        calendar.add(Calendar.MINUTE, 30);

                    Time = Time + "-" + localTimeFormat.format(calendar.getTime());
                    Currentdate = calendar.getTime();

                    gridDataModel.setStay_Time(Time);

                    InsertRow(gridDataModel, false, i);
                    previousPlace = row;

                } else {
                    if (i > -1)
                        row = districtPlaces.get(i - 1);

                    fLocation = previousPlace.getLocation();
                    sLocation = row.getLocation();

                    //Initialize First Column
                    gridDataModel.setDate(dateFormat.format(Currentdate));

                    //Initialize Second Column
                    gridDataModel.setFrom(previousPlace.getPlaceName());

                    //Initialize Third Column
                    gridDataModel.setTo(row.getHotel().getHotelName());
                    gridDataModel.setHotelPrize("" + GetHotelPrize(row.getHotel().getHotelPrice(), data.getNoOfGuests(), false));

                    previousPlace.setPlaceName(row.getHotel().getHotelName());
                    previousPlace.getLocation().setLatitude(row.getHotel().getLocation().getLatitude());
                    previousPlace.getLocation().setLongitude(row.getHotel().getLocation().getLongitude());

                    //Initialize Forth Column
                    calendar.setTime(Currentdate);
                    //Hotel and Place (Row)
                    TravelTime_InHour = (int) calculateTravelTime(fLocation.getLatitude(), fLocation.getLongitude(), sLocation.getLatitude(), sLocation.getLongitude());
                    calendar.add(Calendar.HOUR, TravelTime_InHour);
                    if(calculateTravelTime(fLocation.getLatitude(), fLocation.getLongitude(), sLocation.getLatitude(), sLocation.getLongitude()) > TravelTime_InHour)
                        calendar.add(Calendar.MINUTE, 30);
                    Currentdate = calendar.getTime();

                    gridDataModel.setStay_Time(localTimeFormat.format(calendar.getTime()));

                    InsertRow(gridDataModel, false, i);

                    calendar.setTime(Currentdate);

                    SimpleDateFormat sdfforMinutes =new SimpleDateFormat("mm");
                    int minutes = Integer.valueOf(sdfforMinutes.format(calendar.getTime()));

                    int a = Integer.valueOf(sdf.format(calendar.getTime()));
                    int b = 9 - a;
                    calendar.add(Calendar.HOUR, b);
                    if(minutes > 0)
                        calendar.add(Calendar.MINUTE, -30);
                    if(b < 0) {
                        calendar.add(Calendar.DATE, 1);
                        noOfDays--;
                        TotalNoOfCab++;
                    }

                    Currentdate = calendar.getTime();
                    i--;
                }
            }
        }
        //End Loop

        if(ReturnTimeToEndPlaceInDay > 0)
        {
            fLocation = previousPlace.getLocation();
            sLocation = data.getEndPlace().getLocation();

            GridDataModel gridDataModel =new GridDataModel();
            //Previous and Curren Place (Row)
            TravelTime_InHour = (int) calculateTravelTime(fLocation.getLatitude(), fLocation.getLongitude(), sLocation.getLatitude(), sLocation.getLongitude());

            gridDataModel.setDate(dateFormat.format(Currentdate));
            //initialize new Day

            //Initialize Second Column
            gridDataModel.setFrom(previousPlace.getPlaceName());

            //Initialize Third Column
            gridDataModel.setTo(data.getEndPlace().getPlaceName());

            //Initialize Forth Column
            calendar.setTime(Currentdate);
            String Time = localTimeFormat.format(calendar.getTime());

            calendar.add(Calendar.HOUR, TravelTime_InHour);
            if(calculateTravelTime(fLocation.getLatitude(), fLocation.getLongitude(), sLocation.getLatitude(), sLocation.getLongitude()) > TravelTime_InHour)
                calendar.add(Calendar.MINUTE, 30);


            Time = Time + "-" + localTimeFormat.format(calendar.getTime());

            gridDataModel.setStay_Time(Time);

            InsertRow(gridDataModel, false, i);
        }
        //editText.setText(""+(GetHotelPrize(1,data.NoOfGuest, true) + GetTotalCabPrize(TotalNoOfCab,data.CabPrize)) );
        InsertBudgedRow("Budget", (GetHotelPrize(1,data.getNoOfGuests(), true) + GetTotalCabPrize(TotalNoOfCab, data.getCab().getCabPrice())) , i,false);
    }

    //Distance Function
    private Long calculateDistance(Double userLat ,Double  userLon,Double  lat ,Double  lon){

        Location firstLocation = new Location("");
        firstLocation.setLatitude(userLat);
        firstLocation.setLongitude(userLon);

        Location secondLocation = new Location("");
        secondLocation.setLatitude(lat);
        secondLocation.setLongitude(lon);

        Float distanceInKiloMeters = (firstLocation.distanceTo(secondLocation))/1000;
        return distanceInKiloMeters.longValue();

    }
    // Double userLat ,Double  userLon,Double  lat ,Double  lon
    private float calculateTravelTime(Double userLat ,Double  userLon,Double  lat ,Double  lon){
           Long d = calculateDistance(userLat,userLon,lat,lon);
        Float hours = Float.valueOf(String.valueOf(((d*1000)/11.111)/3600));
        Float minutes = Float.valueOf(String.valueOf(((((d*1000)/11.111)/60)%60)));

        float r =hours;
        if(minutes > 30)
            r++;
        else
            r = (float) (r + 0.5);
        return r;
    }




    // Get Hotel Budged
    private int GetHotelPrize(int HotelPrize, int NoOfGuest, boolean IsTotalBuged)
    {
        if(IsTotalBuged == false) {
            int NoOfRooms = (int) NoOfGuest / 5;
            if (NoOfGuest % 5 > 0)
                NoOfRooms++;
            Budged = Budged + (NoOfRooms * HotelPrize);
            if (IsTotalBuged == false) {
                return (NoOfRooms * HotelPrize);
            }
        }
        return Budged;
    }
    //Get total Transport Prize
    private int GetTotalCabPrize(int NoOfDays, int CabPrize)
    {
        return (CabPrize * NoOfDays);
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
    private void InsertBudgedRow(String Budged, int BudgedValue,int RowNumber, boolean IsOneColumn)
    {
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;

        int leftColumnMargin = 0;
        int topColumnMargin = 0;
        int rightColumnMargin = 0;
        int bottomColumnMargin = 0;

        int smallTextSize = 0, mediumTextSize = 0;

        //textSize = (int) getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);
        mediumTextSize = (int) getResources().getDimension(R.dimen.font_size_medium);

        int columnHeaderColor = Color.RED;
        //Declare First Column
        final TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.LEFT);
        tv.setPadding(leftColumnMargin, topColumnMargin, rightColumnMargin, bottomColumnMargin);

        //Declare First Column
            tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setText(Budged);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);
            tv.setTextColor(columnHeaderColor);


                //Declare Second Column
                final TextView tv2 = new TextView(this);
        if(!IsOneColumn) {
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv2.setGravity(Gravity.LEFT);
                tv2.setPadding(leftColumnMargin, topColumnMargin, rightColumnMargin, bottomColumnMargin);

                tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                tv2.setTextColor(columnHeaderColor);
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);
                tv2.setText("" + BudgedValue);
            }
        //Append Row
        final TableRow tr = new TableRow(this);
        tr.setId(RowNumber + 1);
        TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
        tr.setPadding(0, 0, 0, 0);
        tr.setLayoutParams(trParams);


        tr.addView(tv);
        tr.addView(tv2);

            tr.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    TableRow tr = (TableRow) v;
                    //do whatever action is needed
                }
            });
       // mTableLayout.addView(tr, trParams);

        //Line Seprator
            // add separator row
            final TableRow trSep = new TableRow(this);
            TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);

            trSep.setLayoutParams(trParamsSep);
            TextView tvSep = new TextView(this);
            TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            tvSepLay.span = 4;
            tvSep.setLayoutParams(tvSepLay);
            tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
            tvSep.setHeight(1);

            trSep.addView(tvSep);
           // mTableLayout.addView(trSep, trParamsSep);
        }

    //Insert Row
    public void InsertRow(GridDataModel rowData, boolean isColumnHeader, int rowNumber)
    {
        //Table Styling
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;

        int leftColumnMargin = 5;
        int topColumnMargin = 15;
        int rightColumnMargin = 0;
        int bottomColumnMargin = 15;

        int columnHeaderColor = Color.BLUE;
        int smallTextSize = 0, mediumTextSize = 0;

        //textSize = (int) getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);
        mediumTextSize = (int) getResources().getDimension(R.dimen.font_size_medium);

        //Declare First Column
        final TextView tv = new TextView(this);
        if (isColumnHeader == true) {
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
        } else {
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT));
        }
        tv.setGravity(Gravity.LEFT);
        tv.setPadding(leftColumnMargin, topColumnMargin, rightColumnMargin, bottomColumnMargin);

        //Declare First Column
        if (isColumnHeader == true)
        {
            tv.setText("Date");
            tv.setBackgroundColor(Color.parseColor("#f0f0f0"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);
            tv.setTextColor(columnHeaderColor);
        }
        else
        {
            tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setText(rowData.getDate());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
        }

        //Declare Second Column
        final TextView tv2 = new TextView(this);
        if (isColumnHeader == true) {
            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
        } else {
            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT));
        }
        tv2.setGravity(Gravity.LEFT);
        tv2.setPadding(leftColumnMargin, topColumnMargin, rightColumnMargin, bottomColumnMargin);

        if (isColumnHeader == true) {
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);
            tv2.setBackgroundColor(Color.parseColor("#f7f7f7"));
            tv2.setTextColor(columnHeaderColor);
            tv2.setText("From");
        } else {
            tv2.setBackgroundColor(Color.parseColor("#ffffff"));
            tv2.setTextColor(Color.parseColor("#000000"));
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            tv2.setText(rowData.getFrom());
        }

        //Declare Third Column
        final TextView tv3 = new TextView(this);
        if (isColumnHeader == true) {
            tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
        } else {
            tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT));
        }
        tv3.setGravity(Gravity.TOP);
        tv3.setPadding(leftColumnMargin, topColumnMargin, rightColumnMargin, bottomColumnMargin);

        //Initialize Third Column
        if (isColumnHeader == true) {
            tv3.setBackgroundColor(Color.parseColor("#f0f0f0"));
            tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);
            tv3.setTextColor(columnHeaderColor);
            tv3.setText("To");

        } else {
            tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
            tv3.setTextColor(Color.parseColor("#000000"));
            tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            tv3.setText(rowData.getTo());
        }

        final TextView tv4 = new TextView(this);
        if (isColumnHeader == true) {
            tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
        } else {
            tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT));
        }
        tv4.setPadding(leftColumnMargin, topColumnMargin, rightColumnMargin, bottomColumnMargin);
        tv4.setGravity(Gravity.LEFT);

        if (isColumnHeader == true) {
            tv4.setBackgroundColor(Color.parseColor("#f7f7f7"));
            tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);
            tv4.setTextColor(columnHeaderColor);
            tv4.setText("Time");
        } else {
            tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            tv4.setBackgroundColor(Color.parseColor("#ffffff"));
            tv4.setTextColor(Color.parseColor("#000000"));
            tv4.setText(rowData.getStay_Time());
        }

        //Append Row
        final TableRow tr = new TableRow(this);
        tr.setId(rowNumber + 1);
        TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
        tr.setPadding(0, 0, 0, 0);
        tr.setLayoutParams(trParams);


        tr.addView(tv);
        tr.addView(tv2);
        tr.addView(tv3);
        tr.addView(tv4);

        if (isColumnHeader == false)
        {

            tr.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    TableRow tr = (TableRow) v;
                    //do whatever action is needed
                }
            });
        }
       // mTableLayout.addView(tr, trParams);

        //Line Seprator
        if (isColumnHeader == false) {

            // add separator row
            final TableRow trSep = new TableRow(this);
            TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);

            trSep.setLayoutParams(trParamsSep);
            TextView tvSep = new TextView(this);
            TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            tvSepLay.span = 4;
            tvSep.setLayoutParams(tvSepLay);
            tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
            tvSep.setHeight(1);

            trSep.addView(tvSep);
          //  mTableLayout.addView(trSep, trParamsSep);
        }
    }

    //////////////////////////////////////////////////////////////////////////////

    //
    // The params are dummy and not used
    //
class LoadDataTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {


            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressBar.hide();
            loadData(Data);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

}
*/
