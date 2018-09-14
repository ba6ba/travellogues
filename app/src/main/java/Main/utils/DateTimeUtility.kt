package Main.utils

import android.text.format.DateFormat
import extras.ApplicationConstants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import javax.xml.datatype.DatatypeConstants.DAYS
import java.util.concurrent.TimeUnit


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

        fun calculateNoOfDays(startDateValue : String,endDateValue : String): String {
            val sdf = SimpleDateFormat(ApplicationConstants.DATE_FORMAT)
            val startDate = sdf.parse(startDateValue)
            val endDate = sdf.parse(endDateValue)
            val diff = endDate.getTime() - startDate.getTime()
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toString()
        }
    }
}