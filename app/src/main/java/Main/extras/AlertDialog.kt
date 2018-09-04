package extras

import Main.Model.response.OffersData
import Main.Model.response.PropertiesData
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.sarwan.final_year_project.R
import com.squareup.picasso.Picasso
import modules.chat.ChatActivity

open class AlertDialog{

    fun builder(context: Context, response : String?){


        val dialog = Dialog(context)
        dialog.setContentView(R.layout.no_service_available)
        // set the custom dialog components - text, image and button
        val textView = dialog.findViewById<TextView>(R.id.textView)
        val okButton = dialog.findViewById<Button>(R.id.okButton)
        // if button is clicked, close the custom dialog
        textView.text = response
        okButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

    }

    fun offerDialog(context: Context, offer: OffersData?, cardBackgroundArrayIndex: Int){


        val dialog = Dialog(context)
        dialog.setContentView(R.layout.offer_dialog)
        // set the custom dialog components - text, image and button
        (dialog.findViewById<View>(R.id.headerImage)).background = context.resources.
                getDrawable(cardBackgroundArrayIndex)
        (dialog.findViewById<TextView>(R.id.offerCardTitle)).text = offer?.title
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

    }

    fun propertiesDialog(context: Activity, data: PropertiesData?){


        val dialog = Dialog(context)
        dialog.setContentView(R.layout.properties_dialog)
        // set the custom dialog components - text, image and button
        (dialog.findViewById<TextView>(R.id.title)).text = data?.title
        (dialog.findViewById<TextView>(R.id.desc)).text = data?.description
        (dialog.findViewById<RelativeLayout>(R.id.chatLayout)).setOnClickListener {
            context.startActivity(Intent(context,ChatActivity::class.java))
            context.finish()
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

    }


    fun tripDialog(context: Context){


        val dialog = Dialog(context)
        dialog.setContentView(R.layout.trip_dialog)
        // set the custom dialog components - text, image and button
        val image = dialog.findViewById<ImageView>(R.id.dialogImage)
        Picasso.with(context).load(ApplicationConstants.TRIP_DIALOG_BANNER).fit().into(image)
        // if button is clicked, close the custom dialog
        image.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

    }

    @TargetApi(Build.VERSION_CODES.M)
    fun serviceNotAvailable(context: Context){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.no_service_available)
        // set the custom dialog components - text, image and button
        val okButton = dialog.findViewById<Button>(R.id.okButton)
        (dialog.findViewById<RelativeLayout>(R.id.layout)).setBackgroundResource(R.color.progressLoaderScreen)
        (dialog.findViewById<TextView>(R.id.textView)).setTextColor(context.getColor(R.color.white_color))
        (dialog.findViewById<TextView>(R.id.textView)).text = context.getString(R.string.this_service_not_available)
        // if button is clicked, close the custom dialog
        okButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

    }


}