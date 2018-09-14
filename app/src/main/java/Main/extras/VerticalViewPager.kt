package Main.extras

import android.content.Context
import android.text.method.Touch.onTouchEvent
import android.view.MotionEvent
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View


class VerticalViewPager(context: Context, vararg attrs: AttributeSet) : ViewPager(context) {

   init {
       // The majority of the magic happens here
       setPageTransformer(true, VerticalPageTransformer())
       // The easiest way to get rid of the overscroll drawing that happens on the left and right
       overScrollMode = View.OVER_SCROLL_NEVER
   }

     inner class VerticalPageTransformer : ViewPager.PageTransformer {

        override fun transformPage(view: View, position: Float) {

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f)

            } else if (position <= 1) { // [-1,1]
                view.setAlpha(1f)

                // Counteract the default slide transition
                view.setTranslationX(view.getWidth() * -position)

                //set Y position to swipe in from top
                val yPosition = position * view.getHeight()
                view.setTranslationY(yPosition)

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f)
            }
        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private fun swapXY(ev: MotionEvent): MotionEvent {
        val width = width.toFloat()
        val height = height.toFloat()

        val newX = ev.y / height * width
        val newY = ev.x / width * height

        ev.setLocation(newX, newY)

        return ev
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val intercepted = super.onInterceptTouchEvent(swapXY(ev))
        swapXY(ev) // return touch coordinates to original reference frame for any child views
        return intercepted
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return super.onTouchEvent(swapXY(ev))
    }

}