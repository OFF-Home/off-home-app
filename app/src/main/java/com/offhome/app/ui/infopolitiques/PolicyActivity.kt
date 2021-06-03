package com.offhome.app.ui.infopolitiques

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import com.offhome.app.R

/**
 * Class *CovidPolicy*
 * This class is the one that displays the policy of our APP
 * @author Pau Cuesta
 */
class PolicyActivity : AppCompatActivity() {

    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textView = findViewById(R.id.textViewPolicy)
        textView.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(getString(R.string.textView_privacy_policy), Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(getString(R.string.textView_privacy_policy))
        }
    }
}
