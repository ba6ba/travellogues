package modules.bookings

import Main.modules.bookings.HotelFragment
import android.os.Bundle
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.selection_layout.*

class BookingActivity : ParentActivity(){

    private var toolbarTitle : String ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selection_layout)

        toolbarTitle = intent.extras.getString(ApplicationConstants.TOOLBAR_TITLE)

        setToolbar()
        attachFragment()
    }
/*

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
*/

    private fun setToolbar() {
//        setSupportActionBar(fragmentToolbar)
//        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
//        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
//        supportActionBar?.setIcon(R.drawable.abc_ic_arrow_drop_right_black_24dp)
//        getSupportActionBar()?.setTitle(toolbarTitle)
    }

    private fun attachFragment() {

//        val fragment = HotelFragment.newInstance()
//        supportFragmentManager?.beginTransaction()?.
//                add(R.id.fragmentFrameLayout,fragment)
//                ?.commit()
    }

}