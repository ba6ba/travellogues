package Main.modules.plan.Planning

import Main.modules.chat.ChatFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import extras.ApplicationConstants
import modules.Profile.TripsActivity
import modules.home.HomeFragment

class TripTabsPagerAdapter(fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return ApplicationConstants.TOTAL_NUMBER_OF_TRIP_TABS
    }


    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return TripsActivity.newInstance()

            1 -> return TripPlanningActivity.newInstance()

            else -> return null
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "SINGLE DESTINATION"

            1 -> return "MULTIPLE DESTINATIONS"

            else -> return null
        }
    }
}