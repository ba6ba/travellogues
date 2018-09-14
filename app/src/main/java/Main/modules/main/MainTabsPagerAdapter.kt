package modules.main

import Main.modules.plan.Planning.TripPlanningActivity
import Main.modules.chat.ChatFragment
import Main.modules.plan.PlanFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import extras.ApplicationConstants
import modules.home.HomeFragment


class MainTabsPagerAdapter(fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return ApplicationConstants.TOTAL_NUMBER_OF_MAIN_TABS
    }


    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return HomeFragment.newInstance()

//            1 -> return TripsActivity.newInstance()
            1 -> return PlanFragment.newInstance()

            2 -> return ChatFragment.newInstance()

            else -> return null
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }
}

