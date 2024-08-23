package smartdev.bzzhub.ui.activity

import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.item_activity.view.*
import smartdev.bzzhub.R
import smartdev.bzzhub.ui.base.BaseActivity

class EventDetailActivity : BaseActivity() {
    var image: String = ""
    var title: String  = ""
    var eventName: String = ""
    var aboutUs: String = ""
    var venue: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        extractVariables()

        iv_back.setOnClickListener { finish() }
        tv_about.text = aboutUs
        Glide.with(this).load(image).centerCrop().into( iv_banner)
        tv_title.text = title
        tv_event_title.text = eventName
        tv_venue_title.text = venue
    }

    fun extractVariables(){
        image = intent.getStringExtra("image")
        title = intent.getStringExtra("title")
        eventName = intent.getStringExtra("event_name")
        aboutUs = intent.getStringExtra("about_us")
        venue = intent.getStringExtra("venue")
    }
}