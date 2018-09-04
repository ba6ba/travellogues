package modules.splash

import Main.extras.ImageSetter
import Main.extras.RandomColors
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_splash_screen.*
import modules.authentication.LoginActivity
import modules.main.MainActivity
import java.util.*

class SplashScreen : ParentActivity() {

    val SPLASH_DELAY:Long = 3000
    private var fromTop : Animation?=null
    private var imageArray : ArrayList<String> ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_splash_screen)

        /*fromTop = AnimationUtils.loadAnimation(this,R.anim.from_top)
        splashLogo.animation = fromTop*/
        setImages()
        ImageSetter.set(this,imageArray!![((0  until imageArray!!.size-1).random())],backGround,null)
        startTimer()
    }

    private fun setImages() {
        imageArray = arrayListOf("https://image.ibb.co/jbGjhp/logo_03.png",
                "https://image.ibb.co/dAnJ8U/logo_01.png",
                "https://image.ibb.co/m7dQoU/logo_02.png")
    }

    private fun startTimer(){
        Handler().postDelayed({ checkForRegisteredUser() },SPLASH_DELAY)
    }


    fun ClosedRange<Int>.random() =
            Random().nextInt((endInclusive + 1) - start) +  start

    private fun checkForRegisteredUser() {
        if(profile?.isLoggedIn == true){
            openActivityWithFinish(MainActivity::class.java)

        }
         else {
            openActivityWithFinish(LoginActivity::class.java)
        }
    }

}