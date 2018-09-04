package Main.extras

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Picasso
import android.graphics.drawable.Drawable
import com.example.sarwan.final_year_project.R.mipmap.ic_launcher
import android.graphics.Bitmap
import com.example.sarwan.final_year_project.R
import com.squareup.picasso.Target
import extras.ApplicationConstants
import android.graphics.drawable.BitmapDrawable
import android.widget.RelativeLayout


class ImageSetter {
    companion object {
        fun set(context : Context, imageUri : String?, imageView : ImageView? , target : RelativeLayout?, vararg scaleType: ImageView.ScaleType)
        {
//            Picasso.with(context).isLoggingEnabled = true
            /*val target = object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    imageView.setImageBitmap(BlurImage.fastblur(bitmap, 1f, ApplicationConstants.BLUR_PRECENTAGE))
                }

                override fun onBitmapFailed(errorDrawable: Drawable) {
                    imageView.setImageResource(R.mipmap.ic_launcher)
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable) {

                }
            }*/
            if(imageView==null){

                Picasso.with(context).load(imageUri).into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                        target?.setBackground(BitmapDrawable(bitmap))
                    }

                    override fun onBitmapFailed(errorDrawable: Drawable) {

                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable) {

                    }
                })
            }
            else {
            Picasso.with(context).load(imageUri).fit().centerCrop().into(imageView)
            }

        }
    }
}