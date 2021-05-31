package com.offhome.app.ui.infopolitiques

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.offhome.app.R

/**
 * Class *CovidPolicy*
 * This class is the one that displays the policy of COVID situation
 * @author Pau Cuesta
 */
class CovidPolicyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_covid_policy)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
