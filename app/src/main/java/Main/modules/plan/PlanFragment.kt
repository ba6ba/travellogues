package Main.modules.plan

import Main.modules.plan.Planning.TripTabsPagerAdapter
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import extras.AlertDialog
import kotlinx.android.synthetic.main.plan_fragment.*
import modules.profile.ProfileActivity

class PlanFragment : Fragment() {

    private var pActivity : ParentActivity ? = null
    private var alertDialog = AlertDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ParentActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.plan_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alertDialog.tripDialog(context!!)
        setToolbar()
        clickListeners()
        setTabLayout()
    }

    private fun setToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(tripToolbar)
    }

    private fun clickListeners() {
        profileMenu.setOnClickListener {
            startActivity(Intent(context!!, ProfileActivity::class.java))
        }
    }

    private var planAdapter : TripTabsPagerAdapter?=null

    private fun setTabLayout() {
        planAdapter = TripTabsPagerAdapter(childFragmentManager)
        tripViewPager.setAdapter(planAdapter)
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL)
        tabLayout.setOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(tripViewPager))
        tripViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tripViewPager.setOffscreenPageLimit(2)

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CategoriesFragment.
         */
        @JvmStatic
        fun newInstance() = PlanFragment()
    }
}