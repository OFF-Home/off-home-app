@file:Suppress("DEPRECATION")

package com.offhome.app.ui.createactivity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.offhome.app.MainActivity
import com.offhome.app.R
import java.util.*

/**
 * Create Activity class that let the user create a new activity indicating its parameters on the corresponding screen
 * @author Maria
 **/


class CreateActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    private lateinit var pick_availability: NumberPicker
    private lateinit var datePicker: TextView
    private lateinit var btn_invitefriends: Button
    private lateinit var act_title: EditText
    private lateinit var btn_CREATED: Button
    private lateinit var description: EditText
    private lateinit var date: TextView
    private lateinit var location: EditText
    private lateinit var pickText: TextView

    /**
     * This function represents the current time using current locale and timezone
     */
    private fun getDateTimeCalendar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.MINUTE)
        minute = cal.get(Calendar.MINUTE)
    }

    private lateinit var viewModel: CreateActivityViewModel
    @RequiresApi(Build.VERSION_CODES.N)

    /**
     * This function initializes the activity [CreateActivity]
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        viewModel = ViewModelProviders.of(this).get(CreateActivityViewModel::class.java)

        val activityObserver = Observer<List<com.offhome.app.model.ActivityFromList>>{
            Log.d("Activity", it.toString())
        }

        //viewModel.getActivitiesList().observe(this,activityObserver)

        this.title = "Create activity"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pickDate()

        pickAvailability()

        inviteFriends()

        createTheActivity()
    }

    /**
     * This function let the user pick a date where the activity created will take place
     */
    private fun pickDate(){
        datePicker = findViewById(R.id.btn_pickdate)
        datePicker.setOnClickListener{
            getDateTimeCalendar()

            DatePickerDialog(this,this, this.year,this.month, this.day).show()
        }
    }

    /**
     * This function let the user pick the number maximum of participants allowed by the activity created
     */
    private fun pickAvailability(){
        pick_availability = findViewById(R.id.pick_availability)
        pick_availability.maxValue = 10
        pick_availability.minValue = 3
    }

    /**
     * This function let the user invite friends by sending the data of the new activity to other apps of the user's device
     */
    private fun inviteFriends(){
        btn_invitefriends = findViewById(R.id.btn_invite_friends)
        act_title = findViewById(R.id.activity_title)
        btn_invitefriends.setOnClickListener{
            val message : String = act_title.text.toString()

            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,message)
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent, "Invite friends from:"))
        }
    }

    /**
     * This functions let the user create the new activity by pressing the CREATED button
     */
    private fun createTheActivity(){
        btn_CREATED = findViewById(R.id.btn_create)
        btn_CREATED.setOnClickListener{
            if (validate()) {
                Toast.makeText(this, "Activity created!", Toast.LENGTH_SHORT).show()
                //NewActivity = new instance of Activity
                //viewModel.addActivity(NewActivity)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /**
     * This function validates whereas the activity can be created depending on the information introduced by the user, and also shows the corresponding
     * error message on the screen
     * @return true if the activity can be created or otherwise; otherwise return false
     */
    private fun validate() :Boolean{
        act_title = findViewById(R.id.activity_title)
        description = findViewById(R.id.about_the_activity)
        date = findViewById(R.id.date_pick_text)
        location = findViewById(R.id.locationpck2)

        if (act_title.text.toString().isEmpty()){
            act_title.error = "Name should not be blank"
            return false
        }
        if (description.text.toString().isEmpty()){
            description.error = "Name should not be blank"
            return false
        }
        if (date.text.toString() == ""){
            date.error = "Date should not be blank"
            return false
        }
        if (location.text.toString().isEmpty()){
            location.error = "Location should not be blank"
            return false
        }
        return true
    }

    /**
     * This function is called every time the user changes the date picked
     */
    override fun onDateSet(view: DatePicker?,year: Int, month: Int, dayOfMonth: Int){
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()
        TimePickerDialog(this,this,hour,minute,true).show()
    }

    @SuppressLint("SetTextI18n")

    /**
     * This function is called when the user is done setting a new time and the dialog has closed
     */
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int){
        savedHour = hourOfDay
        savedMinute = minute
        pickText = findViewById(R.id.date_pick_text)
        pickText.text = "$savedDay-$savedMonth-$savedYear\n Hour: $savedHour Minute: $savedMinute"
    }
}

