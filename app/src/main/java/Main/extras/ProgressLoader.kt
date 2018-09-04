package Main.extras

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.Window
import com.example.sarwan.final_year_project.R


class ProgressLoader : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(activity!!)

        // request a window without the title
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        val inflater = activity!!.layoutInflater
        val parent = inflater.inflate(R.layout.fragment_progress_loader, null)
        dialog.setContentView(parent)
        dialog.setCanceledOnTouchOutside(false)
        //Set the dialog to immersive
        dialog.window!!.decorView.systemUiVisibility = activity!!.window.decorView.systemUiVisibility

        return dialog

    }

    override fun show(manager: FragmentManager, tag: String) {

        try {
            if (this.isAdded) {
                return  //or return false/true, based on where you are calling from
            }
            val ft = manager.beginTransaction()
            try {
                ft.add(this, TAG)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            /*try catch is used to tackle the crash
         when user close the app and some thing
         running in background of app*/

            ft.commitAllowingStateLoss()


        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.message)
        }

    }

    override fun dismiss() {
        try {
            super.dismiss()
        } catch (e: IllegalStateException) {
            super.dismissAllowingStateLoss()
        }

    }

    companion object {

        private val TAG = "ProgressLoader"
        private var loader: ProgressLoader? = null

        val progressLoader: ProgressLoader
            get() {
                if (loader == null) {
                    loader = ProgressLoader()

                }
                return loader!!

            }

        fun hideProgress() {

            if (loader != null) {

                loader!!.dismissAllowingStateLoss()
            }
        }
    }
}
