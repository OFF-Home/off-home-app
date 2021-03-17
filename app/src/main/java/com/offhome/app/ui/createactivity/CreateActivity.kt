package com.offhome.app.ui.createactivity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.offhome.app.R
import java.util.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CreateActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val buttonPickDate = findViewById<FloatingActionButton>(R.id.btnPickDate)

        val datepick = findViewById<TextView>(R.id.datepck)

        buttonPickDate.setOnClickListener{
            val bpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
            datepick.text = "$day/$month/$year"
        }, day, month, year)
            bpd.show()
        }

        datepick.setOnClickListener{
            val bpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
                datepick.text = "$day/$month/$year"
            }, day, month, year)
            bpd.show()
        }

        val pick_availability = findViewById<NumberPicker>(R.id.pick_availability)
        val btn_activity_created = findViewById<Button>(R.id.btn_create)
        pick_availability.maxValue = 10
        pick_availability.minValue = 3

        val act_title = findViewById<EditText>(R.id.activity_title)
        btn_activity_created.setOnClickListener{
            //Toast.makeText(this, pick_availability.value.toString(),Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "Activitat creada amb éxit!", Toast.LENGTH_SHORT).show()
        }
    }

}