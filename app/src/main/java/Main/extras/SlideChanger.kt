package Main.extras

import android.support.v4.view.ViewPager
import android.widget.Toast
import java.util.*

class SlideChanger{

    companion object {
        var timer: Timer ? = null
        var page = 1
        var mViewPager : ViewPager ? = null
        var mCount : Int ? = null
    }
    fun pageSwitcher(seconds: Long,viewPager: ViewPager,count : Int) {
        timer = Timer() // At this line a new Thread will be created
        mViewPager = viewPager
        mCount = count
        timer?.scheduleAtFixedRate(RemindTask(), 0, seconds * 1000) // delay
        // in
        // milliseconds
    }

    internal  class RemindTask : TimerTask() {

        override fun run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            (Runnable {
                if (page > mCount!!) {
                    timer?.cancel()
                } else {
                    mViewPager?.setCurrentItem(page++)
                }
            })

        }
    }
}