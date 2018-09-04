package Main.UI

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.util.AttributeSet
import android.view.View
import com.example.sarwan.final_year_project.R
import com.squareup.picasso.Picasso


object RoundedImageView {
    fun getRoundedCornerBitmap(bitmap: Bitmap, leftPixels: Int,rightPixels: Int,topPixels: Int,bottomPixels: Int): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap
                .height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val leftPx = leftPixels.toFloat()
        val rightPx = rightPixels.toFloat()
        val topPx = topPixels.toFloat()
        val bottomPx = bottomPixels.toFloat()


        paint.setAntiAlias(true)
        canvas.drawARGB(0, 0, 0, 0)
        paint.setColor(color)
        canvas.drawRect(leftPx,topPx,rightPx,bottomPx,paint)
//        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }
}