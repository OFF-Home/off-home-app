package com.offhome.app.ui.createactivity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.offhome.app.R
import java.time.DayOfWeek
import java.time.Month
import java.util.*

class CreateActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    lateinit var viewModel: CreateActivityViewModel

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


    @RequiresApi(Build.VERSION_CODES.N)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        viewModel = ViewModelProvider(this).get(CreateActivityViewModel::class.java)

        this.title = "Create activity"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pickDate()

        /*val datePicker = findViewById<TextView>(R.id.date_pick_text)

        datePicker.setOnClickListener{
            DatePickerDialog(this, { _: DatePicker, i: Int, i1: Int, i2: Int ->
                "$day/$month/$year".also { datePicker.text = it }
            }, day, month, year).apply {
                show()
            }
        }*/

        val pick_availability = findViewById<NumberPicker>(R.id.pick_availability)
        val btn_activity_created = findViewById<Button>(R.id.btn_create)
        pick_availability.maxValue = 10
        pick_availability.minValue = 3

        val act_title = findViewById<EditText>(R.id.activity_title)
        btn_activity_created.setOnClickListener{
            Toast.makeText(this, "Activitat creada amb éxit!", Toast.LENGTH_SHORT).show()
        }

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



}
