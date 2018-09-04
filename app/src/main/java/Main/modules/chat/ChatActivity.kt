package modules.chat

import Main.base.NoInternetFragment
import android.os.Bundle
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import extras.InternetAvailability

class ChatActivity : ParentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity_layout)
        checkInternetConnection()
    }
    private val internetAvailability = InternetAvailability()

    private fun checkInternetConnection() {
        if(internetAvailability.isNetworkAvailable(this)){

        }
        else {
            showNoInternetFrame()
        }
    }
    fun showNoInternetFrame() {
        supportFragmentManager.beginTransaction().add(R.id.noInternetLayout, NoInternetFragment()).addToBackStack(null).commit()
    }
}