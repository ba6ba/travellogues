package Main.extras

import Main.Model.User
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson

class SharedPreference(mContext: Context) {
    private val mPrefs: SharedPreferences
    private val USER_PROFILE = "USER_PROFILE"

    val userFromPref: User?
        get() {
            val gson = Gson()
            val json = mPrefs.getString(USER_PROFILE, "")
            return gson.fromJson<User>(json, User::class.java!!)
        }

    init {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext)
    }

    fun setUserInPref(user: User?) {
        val prefsEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        prefsEditor.putString(USER_PROFILE, json)
        prefsEditor.apply()
    }

}