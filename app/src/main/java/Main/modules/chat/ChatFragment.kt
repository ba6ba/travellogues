package Main.modules.chat

import Main.extras.ImageSetter
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import extras.ApplicationConstants
import kotlinx.android.synthetic.main.chat_layout.*
import modules.chat.ChatActivity

class ChatFragment : Fragment(){

    private var pActivity : ParentActivity? = null
    private lateinit var layoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ParentActivity
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chat_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setIconImage()
        setOnClickListener()
    }

    private fun setIconImage() {
        ImageSetter.set(context!!,ApplicationConstants.CHAT_SCREEN_ICON,chatBgImage,null)
    }

    private fun setOnClickListener() {
        startChatButton.setOnClickListener {
            pActivity?.openActivity(ChatActivity::class.java)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CategoriesFragment.
         */
        @JvmStatic
        fun newInstance() = ChatFragment()
    }
}