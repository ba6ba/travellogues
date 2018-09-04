package base

import Main.Model.User
import Main.base.NoInternetFragment
import Main.extras.ProgressLoader
import Main.extras.SharedPreference
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.sarwan.final_year_project.R
import extras.InternetAvailability
import java.io.IOException



open class ParentActivity : AppCompatActivity() {

    var profile: User? = User()
        get() = field
        set(value) {
            field = value
        }

    private val TAG = "ParentActivity"

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profile = SharedPreference(this).userFromPref
    }

    fun saveProfileInSharedPreference(){
        try {
            SharedPreference(this).setUserInPref(profile)
        } catch (e:Exception){
            Log.e(TAG, e.message)
        }
    }

    /**
     * @usage It use to show any message provided by the caller
     * @param view
     * @param message
     */
    fun showMessage( message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    /**
     * @usage it opens the activity receives in parameter
     * @param activity
     */
    fun openActivity(activity: Class<*>) {
        startActivity(Intent(this, activity))
    }

    /**
     * @usage it opens the activity receives in parameter and finish  the current activity running
     * @param activity
     */
    fun openActivityWithFinish(activity: Class<*>) {
        startActivity(Intent(this, activity))
        finish()
    }

    /**
     * @usage it opens the activity with provide intent and finish  the current activity running
     * @param intent
     */
    fun openActivityWithFinish(intent: Intent) {
        startActivity(intent)
        finish()
    }

    /**
     * @usage It opens the activity with the provided bundle as a parameter
     * @param activity
     * @param bundle
     */
    fun openActivity(activity: Class<*>, bundle: Bundle) {
        startActivity(Intent(this, activity).putExtras(bundle))
    }

    /**
     * @usage It opens the activity for result with the provided bundle as a parameter
     * @param activity
     * @param bundle
     */
    fun openActivityForResults(activity: Class<*>, bundle: Bundle, requestCode: Int) {
        startActivityForResult(Intent(this, activity).putExtras(bundle), requestCode)
    }


    /**
     * @usage It opens the activity for result
     * @param activity
     */
    fun openActivityForResults(activity: Class<*>, requestCode: Int) {
        startActivityForResult(Intent(this, activity), requestCode)
    }

    /**
     * @usage it handles onFailure Response of retrofit
     * @param throwable
     */

    fun onFailureResponse(throwable: Throwable) {
        if (throwable is IOException) {
            showMessage("No Internet Connection")
        } else {
//            showMessage(getString(R.string.some_went_wrong))
            showMessage("There\\'s Something Wrong.")
        }
    }


    private var progressLoader: ProgressLoader? = null

    fun showProgress() {
        try {
            if (progressLoader == null) {
                progressLoader = ProgressLoader()
            }

            progressLoader!!.show(supportFragmentManager, TAG)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "showProgress:" + e.message)
        }

    }

    fun hideProgress() {
        if (progressLoader != null) {
            try {
                progressLoader!!.dismissAllowingStateLoss()
            } catch (e: Exception) {
                Log.e(TAG, "hideProgress:" + e.message)
            }

        }
    }

    protected var forwardTransition: Boolean = true

    override fun onPause() {
        if (forwardTransition)
            overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity)
        else
            overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
        super.onPause()
    }

    fun resetSharedPreferences() {
        profile = User()
        saveProfileInSharedPreference()
    }

}