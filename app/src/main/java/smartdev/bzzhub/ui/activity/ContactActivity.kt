package smartdev.bzzhub.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_chat.view.*
import kotlinx.android.synthetic.main.toolbar_utils.toolbar
import smartdev.bzzhub.R

class ContactActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        toolbar.iv_back.setOnClickListener { finish() }
    }
}