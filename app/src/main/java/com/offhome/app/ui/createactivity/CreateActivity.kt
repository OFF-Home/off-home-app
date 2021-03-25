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
import com.offhome.app.R
import java.util.*

/**
 * Create Activity class
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        viewModel = ViewModelProviders.of(this).get(CreateActivityViewModel::class.java)

        val activityObserver = Observer<List<com.offhome.app.model.ActivityFromList>>{
            Log.d("Activity", it.toString())
        }

        viewModel.getActivitiesList().observe(this,activityObserver)

        this.title = "Create activity"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pickDate()

        val pick_availability = findViewById<NumberPicker>(R.id.pick_availability)
        pick_availability.maxValue = 10
        pick_availability.minValue = 3


        val btn_CREATED = findViewById<Button>(R.id.btn_create)
        btn_CREATED.setOnClickListener{
            if (validate()) {
                Toast.makeText(this, "Activity created!", Toast.LENGTH_SHORT).show()
                //viewModel.addActivity()
            }
        }

        val btn_invitefriends = findViewById<Button>(R.id.btn_invite_friends)
        val act_title = findViewById<EditText>(R.id.activity_title)
        btn_invitefriends.setOnClickListener{
            //val message: String = etUserMessage.text.toString()
            val message : String = act_title.text.toString();

            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,message)
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent, "Invite friends from:"))
        }

        //val ch_category = findViewById<Spinner>(R.id.sp_choose_category)
    }

    private fun pickDate(){
        val datePicker = findViewById<TextView>(R.id.btn_pickdate)
        datePicker.setOnClickListener{
            getDateTimeCalendar()

            DatePickerDialog(this,this, this.year,this.month, this.day).show()
        }
    }

    override fun onDateSet(view: DatePicker?,year: Int, month: Int, dayOfMonth: Int){
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()
        TimePickerDialog(this,this,hour,minute,true).show()
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int){
        savedHour = hourOfDay
        savedMinute = minute
        val pickText = findViewById<TextView>(R.id.date_pick_text)
        pickText.text = "$savedDay-$savedMonth-$savedYear\n Hour: $savedHour Minute: $savedMinute"
    }


    private fun validate() :Boolean{
        val title = findViewById<EditText>(R.id.activity_title)
        val description = findViewById<EditText>(R.id.about_the_activity)
        val date = findViewById<TextView>(R.id.date_pick_text)
        val location = findViewById<EditText>(R.id.locationpck2)

        if (title.text.toString().isEmpty()){
            title.error = "Name should not be blank"
            return false
        }
        else if (description.text.toString().isEmpty()){
            description.error = "Name should not be blank"
            return false
        }
        //no funciona
        else if (date == null){
            Toast.makeText(this, "Date should not be blank", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (location.text.toString().isEmpty()){
            location.error = "Location should not be blank"
            return false
        }
        return true
    }

}

