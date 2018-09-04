package Main.base

import Main.extras.ImageSetter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sarwan.final_year_project.R
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.no_internet_layout.*

class NoInternetFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.no_internet_layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noInternetImage.setImageResource(R.drawable.no_internet)
        //ImageSetter.set(context!!,ApplicationConstants.NO_INTERNET_CONNECTION,noInternetImage,null)
        cancel.setOnClickListener {
            fragmentManager?.popBackStack()
        }
    }
}