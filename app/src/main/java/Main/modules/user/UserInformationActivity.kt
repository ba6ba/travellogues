package modules.user

import Main.Model.body.TravellerBody
import Main.Model.response.HotelData
import Main.Model.response.RestaurantData
import Main.Model.response.TravellerInfoResponse
import Main.base.NoInternetFragment
import Main.modules.user.SwipeStackAdapter
import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import com.mobitribe.app.ezzerecharge.network.RestClient
import extras.ApplicationConstants
import extras.InternetAvailability
import kotlinx.android.synthetic.main.user_information_layout.*
import link.fls.swipestack.SwipeStack
import modules.main.MainActivity
import retrofit2.Call
import retrofit2.Response
import utils.ValidationUtility
import java.util.*

@TargetApi(Build.VERSION_CODES.N)
class UserInformationActivity : ParentActivity() {

    private var mCardData : HashMap<String,String> ? = null
    private var PRICE = 0

    private var hotelData : HotelData? = null
    private var restaurantData : RestaurantData? = null
    private val internetAvailability = InternetAvailability()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_information_layout)
        checkInternetConnection()
    }

    private fun checkInternetConnection() {
        if(internetAvailability.isNetworkAvailable(this)){
            setCardData()
            getIntentData()
            ValidationUtility.appendAtRunTime(ContactLayout,"-",3)
            bringButtonBack()
            clickListeners()
        }
        else {
            showNoInternetFrame()
        }
    }

    private fun getIntentData() {
        if(intent.extras.get(ApplicationConstants.PRICE)!=null){
            PRICE = intent.extras.getInt(ApplicationConstants.PRICE)
        }
        else if(intent.extras.get(ApplicationConstants.HOTEL_OBJECT_KEY)!=null){
            hotelData = intent.extras.getSerializable(ApplicationConstants.HOTEL_OBJECT_KEY) as HotelData
            PRICE = hotelData?.hotelPrice!!
        }
        else {
            hotelData = intent.extras.getSerializable(ApplicationConstants.HOTELS) as HotelData
            restaurantData = intent.extras.getSerializable(ApplicationConstants.RESTAURANTS) as RestaurantData
            PRICE = (hotelData?.hotelPrice!! + restaurantData?.restaurantPrice!!)
        }
    }

    fun showNoInternetFrame() {
        supportFragmentManager.beginTransaction().add(R.id.noInternetLayout, NoInternetFragment()).addToBackStack(null).commit()
    }

    private fun bringButtonBack(){
        var validated = false
        cardNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                cardNumber.error = null
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().length >=12) {
                    validated = true
                    payButton.visibility = View.VISIBLE
                }
                else{
                    payButton.visibility = View.GONE
                }
            }
        })

        cardHolderName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                cardHolderName.error = null
            }

            override fun afterTextChanged(s: Editable) {
                /*if (s.toString().length >=5) {
                    if(validated){
                        payButton.visibility = View.VISIBLE
                    }
                }
                else{
                    payButton.visibility = View.GONE
                }*/
            }
        })

        if(!expiryDate.text.isNullOrEmpty())
        {
            if(validated){
                payButton.visibility = View.VISIBLE
            }
        }
    }

    private fun setCardData() {
        mCardData = HashMap()
        mCardData?.put("Payoneer","https://image.ibb.co/mZb5DK/payoneer.jpg")
        mCardData?.put("BAHL","https://preview.ibb.co/eVzQDK/vis.jpg")
        mCardData?.put("HBL","https://image.ibb.co/b2Ri0z/hbl.jpg")
        mCardData?.put("Bank Alfalah","https://image.ibb.co/fnywLz/alfalah.jpg")
    }

    private val SUCCESSFULL_DELAY: Long = 3000

    private fun clickListeners() {
        cancelButton.setOnClickListener {
            onBackPressed()
        }
        addPaymentButton.setOnClickListener {
            if(validateInputFields()){
                addPaymentLayout.visibility = View.GONE
                cardsLayout.visibility = View.VISIBLE
                setCardSwipeStackLayout()
            }
            else {
                setError()
                removeError()
            }
        }
        expiryDate.setOnClickListener {
            setDatePickerDialog()
        }

        payButton.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.confirm_payment_dialog)

            // set the custom dialog components - text, image and button
            val payingButton = dialog.findViewById<View>(R.id.payingButton)
            (dialog.findViewById<View>(R.id.itemPrice) as TextView).text = PRICE.toString() + getString(R.string.pkr)
            (dialog.findViewById<View>(R.id.tax) as TextView).text = (PRICE.div(2)).toString() + getString(R.string.pkr)
            (dialog.findViewById<View>(R.id.total) as TextView).text = ((PRICE.div(2)) + PRICE).toString() + getString(R.string.pkr)
            // if button is clicked, close the custom dialog
            payingButton.setOnClickListener {
                dialog.setContentView(R.layout.payment_request_successful_layout)
                Handler().postDelayed({
                    postData()
                    openActivityWithFinish(Intent(this,MainActivity::class.java))
                },SUCCESSFULL_DELAY)
            }
            dialog.show()
        }
    }

    private fun postData() {
        RestClient.getRestAdapter().
                postTravellerInfo(makeBody()).
                enqueue(object : retrofit2.Callback<TravellerInfoResponse>{
                    override fun onFailure(call: Call<TravellerInfoResponse>?, t: Throwable?) {
                        onFailureResponse(t!!)
                    }

                    override fun onResponse(call: Call<TravellerInfoResponse>?, response: Response<TravellerInfoResponse>?) {

                        if (response?.body() != null && response.body().data?.canSend!!) {
                            sendSMS()
                        }
                    }

                })
    }

    private fun sendSMS()
    {

    }

    private fun makeBody(): TravellerBody {

        val body = TravellerBody()
        body.Name = NameLayout.editText?.text.toString()
        body.Address = AddressLayout.editText?.text.toString()
        body.Contact = ContactLayout.editText?.text.toString()
        body.Email = EmailLayout.editText?.text.toString()
        body.cardExpiry = expiryDate.text.toString()
        body.cardNumber = cardNumberLayout.editText?.text.toString()
        body.cardVendor = cardName.text.toString()

        return body
    }

    private fun setError() {
        if(TextUtils.isEmpty(NameLayout.editText?.text))
        {
           ValidationUtility.setError(NameLayout,getString(R.string.must_not_be_empty))
        }
        if(TextUtils.isEmpty(AddressLayout.editText?.text))
        {
            ValidationUtility.setError(AddressLayout,getString(R.string.must_not_be_empty))
        }
        if(TextUtils.isEmpty(ContactLayout.editText?.text))
        {
            ValidationUtility.setError(ContactLayout,getString(R.string.must_not_be_empty))
        }
        if(TextUtils.isEmpty(EmailLayout.editText?.text))
        {
            ValidationUtility.setError(EmailLayout,getString(R.string.must_not_be_empty))
        }
        if(!ValidationUtility.isValidEmail(EmailLayout.editText!!))
        {
            ValidationUtility.setError(EmailLayout,getString(R.string.enter_valid_email))
        }
        if(ContactLayout.editText?.text?.length!=10)
        {
            ValidationUtility.setError(ContactLayout,getString(R.string.number_length))
        }
    }


    private fun removeError() {
        ValidationUtility.removeErrors(NameLayout,AddressLayout,ContactLayout,EmailLayout)
    }

    private fun validateInputFields(): Boolean {
        var validated = true
        if(TextUtils.isEmpty(NameLayout.editText?.text))
        {
            validated = false
        }
        if(TextUtils.isEmpty(AddressLayout.editText?.text))
        {
            validated = false
        }
        if(TextUtils.isEmpty(ContactLayout.editText?.text))
        {
            validated = false
        }
        if(TextUtils.isEmpty(EmailLayout.editText?.text))
        {
            validated = false
        }
        if(!ValidationUtility.isValidEmail(EmailLayout.editText!!))
        {
            validated = false
        }
        if(ContactLayout.editText?.text?.length!=10)
        {
            validated = false
        }
        return validated
    }

    private fun setCardSwipeStackLayout() {
        swipeStack.adapter = SwipeStackAdapter(mCardData?.values!!,this)
        cardName.text = mCardData?.keys?.elementAt(swipeStack.currentPosition)
        swipeStack.setListener(object : SwipeStack.SwipeStackListener{
            override fun onViewSwipedToLeft(position: Int) {
                if(position+1==mCardData?.size){
                    cardName.text = mCardData?.keys?.elementAt(0)
                }
                else {
                    cardName.text = mCardData?.keys?.elementAt(position+1)
                }
            }

            override fun onViewSwipedToRight(position: Int) {
                if(position+1==mCardData?.size){
                    cardName.text = mCardData?.keys?.elementAt(0)
                }
                else {
                    cardName.text = mCardData?.keys?.elementAt(position+1)
                }
            }

            override fun onStackEmpty() {
                swipeStack.resetStack()
            }

        })
        cardHolderName.text = NameLayout.editText?.text
    }

    private fun setDatePickerDialog() {
        val date = object : DatePickerDialog.OnDateSetListener {

            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel()
            }

        }
        DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateLabel() {
            expiryDate.setText(simpleDateFormat.format(myCalendar.time), TextView.BufferType.EDITABLE)
    }
    private var simpleDateFormat = SimpleDateFormat(ApplicationConstants.DATE_FORMAT, Locale.US)
    private val myCalendar = Calendar.getInstance()
}