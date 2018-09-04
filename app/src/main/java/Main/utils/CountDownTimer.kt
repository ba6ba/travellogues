package utils

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.util.concurrent.TimeUnit

class CountDownTimer(millisInFuture: Long, countDownInterval: Long
, button : Button ,counterVal : TextView , counterLabel : TextView) : CountDownTimer(millisInFuture, countDownInterval) {

    private var buttonSearch = button
    private var value = counterVal
    private var label = counterLabel


    override fun onTick(millisUntilFinished: Long) {
        if (buttonSearch != null && value != null) {
            buttonSearch.setBackgroundColor(Color.GRAY)
            buttonSearch.setEnabled(false)
            value.setText("" + String.format("%d min : %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))))
        }
    }

    override fun onFinish() {
        if (buttonSearch != null && value != null) {
            buttonSearch.setEnabled(true)
            value.setText("0:00")
            value.setVisibility(View.INVISIBLE)
            label.setVisibility(View.INVISIBLE)
        }
    }
}