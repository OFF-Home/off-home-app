package com.offhome.app.ui.infopolitiques

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.offhome.app.R

/**
 * Class *InfoOffHome*
 * This class is the one that displays the info of OFF Home
 * @author Pau Cuesta
 */
class InfoOFFHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_offhome)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
