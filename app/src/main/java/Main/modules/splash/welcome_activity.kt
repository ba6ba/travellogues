/*
package modules.splash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.LinearLayout
import android.support.v4.view.ViewPager
import com.example.sarwan.final_year_project.R


class welcome_activity : AppCompatActivity() {

    private var slideAdapter: SlideAdapter? = null
    private var dotscount: Int = 0
    private lateinit var dots: Array<ImageView?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_layout)
        setupSlider()
        onClickListener()
    }

    private fun onClickListener() {
    }


    fun setupSlider() {

        slideAdapter = SlideAdapter(this)
        viewPager.setAdapter(slideAdapter)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                changeDots(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        initializeDots()
    }

    private fun setTextFields(position: Int){

        welcome_heading.setText(heading[position])
        welcome_sub_heading.setText(sub_heading[position])
    }


    private fun changeDots(position: Int) {
        for (i in 0 until dotscount) {

            if(i.equals(position)){
                activatingDots(i)
                setTextFields(i)
            }
            else
            {
                inActivatingDots(i)
            }
        }

    }

    private fun inActivatingDots(position: Int) {
        dots[position]?.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                R.drawable.non_active_dot))
    }

    private fun activatingDots(position : Int) {
        dots[position]?.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                R.drawable.active_dot))
    }

    private fun initializeDots() {
        dotscount = slideAdapter?.count as Int

        dots = arrayOfNulls<ImageView>(dotscount)


        for (i in 0..2) {

            dots[i] = ImageView(this)
            inActivatingDots(i)

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)

            params.setMargins(14, 0, 14, 0)

            SliderDots?.addView(dots.get(i), params)
        }
        setTextFields(0)
        activatingDots(0)


    }
}*/
