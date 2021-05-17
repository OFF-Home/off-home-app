@file:Suppress("DEPRECATION")

package com.offhome.app.ui.createactivity



import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.offhome.app.MainActivity
import com.offhome.app.R
import com.offhome.app.model.ActivityData as ActivityData
import java.util.*

/**
 * This class interacts with the User and let him/her create a new activity indicating its parameters on the corresponding screen
 * @author Maria Nievas Viñals
 * @property pick_availability references the NumberPicker to input the maximum number of participants allowed to go to the activity
 * @property datePicker references the TextView to input the data and hour of the activity
 * @property btn_invitefriends references the Button to share the activity with people through other apps
 * @property act_title references the EditText to input the title of the activity
 * @property btn_CREATED references the Button to create the activity
 * @property description references the EditText to input the description of the activity
 * @property finalDate references the TextView that indicates the date chosen for the activity on the screen
 * @property location references the EditText to input the location where the activity will take place
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
    private lateinit var dateFinishPicker: TextView
    private lateinit var btn_invitefriends: Button
    private lateinit var act_title: EditText
    private lateinit var btn_CREATED: Button
    private lateinit var description: EditText
    private lateinit var startDate: TextView
    private lateinit var endDate: TextView
    private lateinit var dataHoraIni: String
    private lateinit var dataHoraEnd: String
    private lateinit var maxParticipant: String
    private lateinit var nameStreet: EditText
    private lateinit var numberStreet: EditText
    private lateinit var category_selected: Spinner

    /**
     * This function represents the current time using current locale and timezone
     */
    private fun getDateTimeCalendar() {
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
     * @param savedInstanceState is the instance of the saved State of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        viewModel = ViewModelProviders.of(this).get(CreateActivityViewModel::class.java)

        this.title = "Create activity"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        act_title = findViewById(R.id.activity_title)
        description = findViewById(R.id.about_the_activity)
        startDate = findViewById(R.id.date_pick_text1)
        endDate = findViewById(R.id.date_pick_text2)
        datePicker = findViewById(R.id.btn_pickdate1)
        dateFinishPicker = findViewById(R.id.btn_pickdate2)
        nameStreet = findViewById(R.id.street)
        numberStreet = findViewById(R.id.streetNum)
        btn_invitefriends = findViewById(R.id.btn_invite_friends)
        act_title = findViewById(R.id.activity_title)
        category_selected = findViewById(R.id.sp_choose_category)

        pickDate()

        pickAvailability()

        inviteFriends()

        createTheActivity()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * This function let the user pick a date where the activity created will take place
     */
    private fun pickDate() {
        datePicker.setOnClickListener {
            getDateTimeCalendar()

            DatePickerDialog(this, this, this.year, this.month, this.day).show()
        }
        dateFinishPicker.setOnClickListener {
            getDateTimeCalendar()

            DatePickerDialog(this, this, this.year, this.month, this.day).show()
        }
    }
    /**
     * This function let the user pick the number maximum of participants allowed by the activity created
     */
    private fun pickAvailability() {
        pick_availability = findViewById(R.id.pick_availability)
        pick_availability.maxValue = 10
        pick_availability.minValue = 3
    }

    /**
     * This function let the user invite friends by sending the data of the new activity to other apps of the user's device
     */
    private fun inviteFriends() {
        btn_invitefriends.setOnClickListener {
            val message: String = act_title.text.toString()

            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent, "Invite friends from:"))
        }
    }

    /**
     * This functions let the user create the new activity by pressing the CREATED button
     */
    private fun createTheActivity() {
        btn_CREATED = findViewById(R.id.btn_create)
        btn_CREATED.setOnClickListener {
            if (validate()) {

                pick_availability.setOnValueChangedListener { _, oldVal, newVal ->
                    maxParticipant = if (oldVal != newVal) "$newVal"
                    else "$oldVal"
                }

                val activitydata = ActivityData(
                    nameStreet.text.toString(),
                    numberStreet.text.toString().toInt(),
                    dataHoraIni,
                    "Walking",
                    maxParticipant.toInt(),
                    act_title.text.toString(),
                    description.text.toString(),
                    dataHoraEnd
                )

                viewModel.addActivity(activitydata).observe(
                    this,
                    {
                        if (it != " ") {
                            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                            if (it == "Activity created") {
                                startActivity(Intent(this, MainActivity::class.java))
                            }
                        }
                    }
                )
            }
        }
    }

    /**
     * This function validates whereas the activity can be created depending on the information introduced by the user, and also shows the corresponding
     * error message on the screen
     * @return true if the activity can be created; otherwise return false
     */
    private fun validate(): Boolean {
        if (act_title.text.toString().isEmpty()) {
            act_title.error = "Name should not be blank"
            return false
        } else if (description.text.toString().isEmpty()) {
            description.error = "Name should not be blank"
            return false
        } else if (startDate.text.toString() == "") {
            startDate.error = "Start date should not be blank"
            return false
        } else if (endDate.text.toString() == "") {
            endDate.error = "End date should not be blank"
            return false
        } else if (nameStreet.text.toString() == "") {
            nameStreet.error = "Street number should not be blank"
            return false
        }
        //      else if (category_selected. etc)
        return true
    }

    /**
     * This function is called every time the user changes the date picked
     */
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()
        TimePickerDialog(this, this, hour, minute, true).show()
    }

    @SuppressLint("SetTextI18n")

    /**
     * This function is called when the user is done setting a new time and the dialog has closed
     */
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute

        startDate.text = "$savedDay-$savedMonth-$savedYear\n at $savedHour:$savedMinute h"
        dataHoraIni = "$savedYear-$savedMonth-$savedDay $savedHour:$savedMinute:00"

        // això s'ha d'arreglar
        endDate.text = "$savedDay-$savedMonth-$savedYear\n at $savedHour:$savedMinute h"
        dataHoraEnd = "$savedYear-$savedMonth-$savedDay $savedHour:$savedMinute:00"
    }
}
