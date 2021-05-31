package com.offhome.app.ui.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.offhome.app.R

class OnboardingActivity : AppCompatActivity() {

    private lateinit var nextButton:Button
    private lateinit var backButton:Button
    private lateinit var pagenumText:TextView

    private var page:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, OnboardingFragmentPlanAct.newInstance())
                .commitNow()
        }

        nextButton=findViewById(R.id.button_next_onboarding)
        backButton=findViewById(R.id.button_back_onboarding)
        pagenumText=findViewById(R.id.page_num_text_onboarding)
        pagenumText.text = "1/3"

        nextButton.setOnClickListener {
            nextOnClickListener()
        }
        backButton.setOnClickListener{
            backOnClickListener()
        }
    }

    private fun nextOnClickListener(){
        //Log.d("next", "next bttn pressed")
        if (page == 1) {
            supportFragmentManager.beginTransaction().replace(R.id.container, OnBoardingFragmentMeetPpl.newInstance()).commitNow()
            backButton.visibility = View.VISIBLE
        }
        else if (page == 2) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, OnBoardingFragmentCovid.newInstance()).commitNow()
            nextButton.text = getString(R.string.done_button)
        }
        else if (page == 3) {
            //intent a login
        }

        //porsiaca
        if (page <3) {
            ++page
            pagenumText.text = page.toString()+"/3"
        }
    }
    private fun backOnClickListener() {
        if (page == 1) {
            //no deberia
        }
        else if (page == 2) {
            supportFragmentManager.beginTransaction().replace(R.id.container, OnboardingFragmentPlanAct.newInstance()).commitNow()
            backButton.visibility = View.GONE
        }
        else if (page == 3) {
            supportFragmentManager.beginTransaction().replace(R.id.container, OnBoardingFragmentMeetPpl.newInstance()).commitNow()
            nextButton.text = getString(R.string.next_button)
        }

        //porsiaca
        if (page > 1) {
            --page
            pagenumText.text = page.toString() + "/3"
        }
    }
}
