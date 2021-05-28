package com.offhome.app.ui.infoactivity



import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.profilejson.UserUsername
import com.offhome.app.data.model.ActivityDataForInvite
import com.offhome.app.data.model.ActivityFromList
import com.offhome.app.data.model.ReviewOfParticipant
import com.offhome.app.ui.chats.groupChat.GroupChatActivity
import com.offhome.app.ui.inviteChoosePerson.InviteActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class *InfoActivity*
 * This class is the one that displays the information about an Activity
 * @author Emma Pereira
 * @property mMap references the GoogleMap that appears in the screen with the activity's location
 * @property imageLike references the ImageView to add an activity to a user's liked activities
 * @property activity references the ActivityFromList that we get as a parameter from the previous screen
 * @property latitude references the latitude coordinate of the activity's location the map
 * @property longitude references the longitude coordinate of the activity's location on the map
 */
class   InfoActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var imageLike: ImageView
    private lateinit var activity: ActivityFromList
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var viewModel: InfoActivityViewModel
    private lateinit var participantsAdapter: ParticipantsRecyclerViewAdapter
    private lateinit var reviewsAdapter: ReviewsRecyclerViewAdapter
    private lateinit var layoutParticipants: RecyclerView
    private lateinit var layoutReviews: RecyclerView
    private var joined = false
    private lateinit var estrelles: RatingBar
    private lateinit var comment: EditText
    private lateinit var btnsubmit: Button
    private var reviewsList: MutableList<ReviewOfParticipant> = ArrayList()
    private lateinit var btnAddCalendar: Button

    private lateinit var userUID: String
    private lateinit var username: String

    private lateinit var groupChat: FloatingActionButton
    private var nRemainingParticipants: Int = 12

    private lateinit var btnJoin: Button
    private lateinit var creator: TextView
    private lateinit var layout: View

    /**
     * This is executed when the activity is launched for the first time or created again.
     * @param savedInstanceState is the instance of the saved State of the activity
     */
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        // recibir actividad seleccionada de la otra pantalla
        val arguments = intent.extras
        val activityString = arguments?.getString("activity")

        activity = GsonBuilder().create().fromJson(activityString, ActivityFromList::class.java)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(InfoActivityViewModel::class.java)
        participantsAdapter = ParticipantsRecyclerViewAdapter()

        btnJoin = findViewById(R.id.btn_join)
        creator = findViewById(R.id.textViewCreator)
        layoutParticipants = findViewById(R.id.listParticipants)

        with(layoutParticipants) {
            layoutManager = LinearLayoutManager(context)
            adapter = participantsAdapter
        }

        setInfoUsuariCreador()
        uploadParticipants()

        val datahora = findViewById<TextView>(R.id.textViewDataTimeActivity)
        datahora.text = activity.dataHoraIni

        val capacity = findViewById<TextView>(R.id.textViewCapacity)
        capacity.text = activity.maxParticipant.toString()

        val description = findViewById<TextView>(R.id.textViewDescription)
        description.text = activity.descripcio

        estrelles = findViewById(R.id.ratingStars)
        comment = findViewById(R.id.yourcomment)
        btnsubmit = findViewById(R.id.submitcomment)
        btnAddCalendar = findViewById(R.id.btnAddToCalendar)

        // get the current date
        val currentTime = Calendar.getInstance().time
        // change final date format
        var date = changeDateFormat()

        // si el usuario no es participante de la activity o si esta no se ha realizado, no se permite hacer rating y/o review
        if (date != null) {
            if (!joined or (date > currentTime)) cantreview()
        }

        valoracioUsuari()

        showReviews()

        btnJoin.setOnClickListener {
        joined = !joined
        if (joined) {
            btnJoin.text = "JOINED"
            if (date < currentTime) reviewpossible()
            else cantreview()
            viewModel.joinActivity(activity.usuariCreador, activity.dataHoraIni).observe(
                this,
                {
                    if (it != " ") {
                        if (it == "You have joined the activity!") {
                            val snackbar: Snackbar = Snackbar
                                .make(layout, "Successfully joined!", Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.go_chat)) {
                                    displayChatGroup()
                                }
                            snackbar.show()
                            val participants = ArrayList<UserUsername>()
                            val actualParticipants = viewModel.participants.value
                            for (item in actualParticipants!!) {
                                if (item.username != "emma") participants.add(item)
                            }
                            participants.add(UserUsername("emma"))
                            participantsAdapter.setData(participants)
                            btnAddCalendar.visibility = View.VISIBLE
                        } else {
                            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )
        } else {
            btnJoin.text = "JOIN"
            cantreview()
            viewModel.deleteUsuari(activity.usuariCreador, activity.dataHoraIni).observe(
                this,
                {
                    if (it != " ") {
                        if (it == "You have left the activity :(") {
                            val snackbar: Snackbar = Snackbar
                                .make(layout, "You left :( !", Snackbar.LENGTH_LONG)
                            snackbar.show()
                            val participants = ArrayList<UserUsername>()
                            val actualParticipants = viewModel.participants.value
                            for (item in actualParticipants!!) {
                                participants.add(item)
                            }
                            participants.remove(UserUsername("emma"))
                            participantsAdapter.setData(participants)
                            btnAddCalendar.visibility = View.GONE
                        } else {
                            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )
        }
        }
        addToCalendar()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // asignar título actividad como título de la pantalla
        title = activity.titol

        // canviar like al clicar
        var clicked = false

        imageLike = findViewById(R.id.imageViewIconLike)
        imageLike.setOnClickListener {
            clicked = !clicked
            if (clicked) {
                imageLike.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                imageLike.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        }

        groupChat = findViewById(R.id.joinGroupChat)
        groupChat.setOnClickListener {
            displayChatGroup()
        }
    }


                        /*********************************************************************************************************/

    /**
     * It displays the button where the user can go straight to the group chat of that activity, if he/she is a member of it
     */
    private fun displayChatGroup() {
        // go to GroupChatActivity only if the user has joined the activity
        val intent = Intent(this, GroupChatActivity::class.java)
        intent.putExtra("usuariCreador", userUID)
        intent.putExtra("dataHI", activity.dataHoraIni.split(".")[0])
        intent.putExtra("titleAct", activity.titol)
        startActivity(intent)
        finish()
    }

    @SuppressLint("SetTextI18n")
    private fun setInfoUsuariCreador(){
        viewModel.getProfileInfo(activity.usuariCreador)
        viewModel.profileInfo.observe(
            this, Observer@{
                val profileInfoVM = it ?: return@Observer
                userUID = profileInfoVM.uid
                username = profileInfoVM.username

                creator.text = getString(R.string.created_by) + " " + username
            }
        )
    }

    private fun uploadParticipants(){
        // cargar participantes activity y mirar si el usuario ya esta apuntado en esta
        viewModel.getParticipants(activity.usuariCreador, activity.dataHoraIni).observe(
            this,
            {
                if (it != null) {
                    participantsAdapter.setData(it)
                    for (item in it) {
                        // if (item.username == SharedPreferenceManager.getStringValue(Constants().PREF_USERNAME)) joined = true
                        if (item.username == "emma") joined = true
                    }
                    // mirar si el usario ya es participante de la actividad
                    if (joined) btnJoin.text = "JOINED"

                    // TODO crec que aquest observer no salta
                    Log.d(
                        "getParticipants",
                        "arribo al InfoActivity::getParticipants.observe i passo el setData. A més, it.size = " + it.size.toString()
                    )
                    nRemainingParticipants = activity.maxParticipant - it.size
                    Log.d(
                        "getParticipants",
                        "nRemainingParticipants = " + nRemainingParticipants.toString()
                    )
                }
            }
        )
    }

    private fun valoracioUsuari(){
        viewModel.getValoracioUsuari(
            activity.usuariCreador, activity.dataHoraIni,
            SharedPreferenceManager.getStringValue(
                Constants().PREF_EMAIL
            ).toString()
        ).observe(
            this,
            {
                // si tiene valoración, ponerla en las estrellas y ya no puede añadir rating, solo review
                if (it.valoracio != 0) {
                    estrelles.setRating(it.valoracio.toFloat())
                    estrelles.invalidate()
                    estrelles.isFocusable = false
                    estrelles.setIsIndicator(true)
                }
                // si tiene review, cambiar el texto del edittext, bloquearlo y bloquear boton submit
                if (it.review != " ") {
                    comment.setHint(R.string.reviewnotpossible)
                    comment.isFocusable = false
                    btnsubmit.setEnabled(false)
                }
            }
        )
        layout = findViewById(R.id.content)

        // al clicar a submit, envía valoracion a back
        btnsubmit.setOnClickListener {
            // si no hay estrellas, muestra mensaje pidiendolas
            if (estrelles.numStars == 0) {
                val snackbar: Snackbar = Snackbar
                    .make(layout, R.string.mustaddrating, Snackbar.LENGTH_LONG)
                snackbar.show()
            }
            // si las hay, enviar datos a back
            else {
                viewModel.putValoracio(
                    SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString(),
                    activity.usuariCreador,
                    activity.dataHoraIni,
                    estrelles.numStars,
                    comment.text.toString()
                ).observe(
                    this,
                    {
                        if (it != " ") {
                            if (it == "Your rating has been saved") {
                                val snackbar: Snackbar = Snackbar
                                    .make(layout, R.string.savedrating, Snackbar.LENGTH_LONG)
                                snackbar.show()

                                // cambiar estrellas y edit text a que ya no pueda añadir nada
                                estrelles.isFocusable = false
                                estrelles.setIsIndicator(true)
                                comment.setHint(R.string.reviewnotpossible)
                                comment.isFocusable = false
                                btnsubmit.setEnabled(false)
                            }
                        }
                    }
                )
            }
        }
    }

    private fun showReviews(){
        // mostrar todas las reviews de la activity
        reviewsAdapter = ReviewsRecyclerViewAdapter()
        layoutReviews = findViewById(R.id.listComments)
        with(layoutReviews) {
            layoutManager = LinearLayoutManager(context)
            adapter = reviewsAdapter
        }

        viewModel.getReviews(activity.usuariCreador, activity.dataHoraIni).observe(
            this,
            {
                if (it != null) {
                    for (item in it) {
                        if (item.review != null) reviewsList.add(item)
                    }
                }
                reviewsAdapter.setData(reviewsList)
            }
        )
    }

    private fun addToCalendar(){
        btnAddCalendar.setOnClickListener {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()) {
                                val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                                sdf.timeZone = TimeZone.getDefault()
                                val dataHoraIni = sdf.parse(activity.dataHoraIni)
                                val dataHoraFi = sdf.parse(activity.dataHoraFi)
                                val calendarIni = Calendar.getInstance()
                                calendarIni.time = dataHoraIni
                                val calendarFi = Calendar.getInstance()
                                calendarFi.time = dataHoraFi
                                val millisStart = calendarIni.timeInMillis
                                val millisEnd = calendarFi.timeInMillis
                                val cr: ContentResolver = applicationContext.getContentResolver()
                                val values = ContentValues()
                                values.put(CalendarContract.Events.DTSTART, millisStart)
                                values.put(CalendarContract.Events.DTEND, millisEnd)
                                values.put(CalendarContract.Events.TITLE, activity.titol)
                                values.put(CalendarContract.Events.DESCRIPTION, activity.descripcio)
                                values.put(CalendarContract.Events.CALENDAR_ID, getCalendarId())
                                values.put(
                                    CalendarContract.Events.ORGANIZER,
                                    activity.usuariCreador
                                )
                                values.put(
                                    CalendarContract.Events.EVENT_TIMEZONE,
                                    TimeZone.getDefault().id
                                )
                                val uri: Uri? =
                                    cr.insert(CalendarContract.Events.CONTENT_URI, values)
                                Toast.makeText(
                                    applicationContext,
                                    getString(R.string.saved_to_calendar),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                }).check()
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we add the marker and move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.setZoomControlsEnabled(true)

        // transform address to coordinates
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>
        addresses = geocoder.getFromLocationName(activity.nomCarrer + " " + activity.numCarrer, 1)
        if (addresses.size > 0) {
            latitude = addresses[0].latitude
            longitude = addresses[0].longitude
        }

        // Add a marker in Sydney and move the camera
        val place = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(place).title(activity.titol))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 16.0f))
    }

    /**
     * Function to specify the options menu for an activity
     * @param menu provided
     * @return true
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share_invite_menu, menu)
        return true
    }

    /**
     * Function called when the user selects an item from the options menu
     * @param item selected
     * @return true if the menu is successfully handled
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_outside_app_btn) {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT,
                getString(
                    R.string.share_activity_message,
                    "this is supposed to be some kind of URL"
                )
            ) // TODO el URL
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share To:"))
        } else if (item.itemId == R.id.share_in_app_btn) {
            changeToInviteActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeToInviteActivity() {
        val intentCanviAChat = Intent(this, InviteActivity::class.java)
        intentCanviAChat.putExtra(
            "activity",
            GsonBuilder().create().toJson(
                ActivityDataForInvite(
                    maxParticipant = activity.maxParticipant,
                    nRemainingParticipants = this.nRemainingParticipants,
                    usuariCreador = activity.usuariCreador,
                    dataHoraIni = activity.dataHoraIni,
                    categoria = activity.categoria,
                    titol = activity.titol,
                    descripcio = activity.descripcio
                )
            )
        )
        startActivity(intentCanviAChat)
    }

    /**
     * Function called when we want to disable the rating or review functionalities
     */
    fun cantreview() {
        estrelles.isFocusable = false
        estrelles.setIsIndicator(true)
        comment.setHint(R.string.reviewnotpossible)
        comment.isFocusable = false
        btnsubmit.setEnabled(false)
    }

    /**
     * Function called when we want to enable the rating or review functionalities
     */
    fun reviewpossible() {
        estrelles.isFocusable = true
        estrelles.setIsIndicator(false)
        comment.setHint(R.string.insert_text)
        comment.isFocusableInTouchMode = true
        btnsubmit.setEnabled(true)
    }

    fun changeDateFormat(): Date {
        // transform dataHoraIni into date format
        val mydate = activity.dataHoraFi
        var date: Date? = null
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        date = format.parse(mydate)
        return date

        /*try {
            date = format.parse(mydate)
            return date
        } catch (e: ParseException) {
            e.printStackTrace()
        }*/
    }

    private fun getCalendarId(): Long? {
        val projection = arrayOf(CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)

        var calCursor = applicationContext.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            CalendarContract.Calendars.VISIBLE + " = 1 AND " + CalendarContract.Calendars.IS_PRIMARY + "=1",
            null,
            CalendarContract.Calendars._ID + " ASC"
        )

        if (calCursor != null && calCursor.count <= 0) {
            calCursor = applicationContext.contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                CalendarContract.Calendars.VISIBLE + " = 1",
                null,
                CalendarContract.Calendars._ID + " ASC"
            )
        }

        if (calCursor != null) {
            if (calCursor.moveToFirst()) {
                val calName: String
                val calID: String
                val nameCol = calCursor.getColumnIndex(projection[1])
                val idCol = calCursor.getColumnIndex(projection[0])

                calName = calCursor.getString(nameCol)
                calID = calCursor.getString(idCol)

                calCursor.close()
                return calID.toLong()
            }
        }
        return null
    }

}
