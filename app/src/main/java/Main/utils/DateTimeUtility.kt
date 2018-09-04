package Main.utils

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap



class DateTimeUtility {
    companion object {
        fun calculateDay(): String {
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("EEEE", Locale.ENGLISH)
            return df.format(c)
        }

        fun calculateDate() : String {
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("MMM d", Locale.ENGLISH)
            return df.format(c)
        }

        fun calculateTime(): String {
            val calendar = Calendar.getInstance()
            val timeformat = SimpleDateFormat("hh:mm a")
            val strtime = timeformat.format(calendar.time).toString().toLowerCase()
            return strtime
        }
    }
}