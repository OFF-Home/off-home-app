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
import androidx.lifecycle.Observer
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
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.Result
import com.offhome.app.data.model.ActivityDataForInvite
import com.offhome.app.data.model.ActivityFromList
import com.offhome.app.data.model.ReviewOfParticipant
import com.offhome.app.data.profilejson.UserUsername
import com.offhome.app.ui.chats.groupChat.GroupChatActivity
import com.offhome.app.ui.inviteChoosePerson.AuxGenerateDynamicLink
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
class InfoActivity : AppCompatActivity(), OnMapReadyCallback {

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
    private lateinit var datahora: TextView
    private lateinit var creator: TextView
    private lateinit var capacity: TextView
    private lateinit var description: TextView
    private lateinit var layout: View

    /**
     * This is executed when the activity is launched for the first time or created again.
     * @param savedInstanceState is the instance of the saved State of the activity
     */
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

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

        Log.w("iniMostrarActivitat", "entro a iniMostrarActivitat")
        btnJoin = findViewById<Button>(R.id.btn_join)
        datahora = findViewById<TextView>(R.id.textViewDataTimeActivity)
        creator = findViewById<TextView>(R.id.textViewCreator)
        capacity = findViewById<TextView>(R.id.textViewCapacity)
        description = findViewById<TextView>(R.id.textViewDescription)
        estrelles = findViewById<RatingBar>(R.id.ratingStars)
        comment = findViewById<EditText>(R.id.yourcomment)
        btnsubmit = findViewById<Button>(R.id.submitcomment)
        layout = findViewById<View>(R.id.content)

        estrelles = findViewById<RatingBar>(R.id.ratingStars)
        comment = findViewById<EditText>(R.id.yourcomment)
        btnsubmit = findViewById<Button>(R.id.submitcomment)
        btnAddCalendar = findViewById(R.id.btnAddToCalendar)

        // mostrar todas las reviews de la activity
        reviewsAdapter = ReviewsRecyclerViewAdapter()
        layoutReviews = findViewById(R.id.listComments)
        with(layoutReviews) {
            layoutManager = LinearLayoutManager(context)
            adapter = reviewsAdapter
        }

        // ara procedim a obtenir les dades de la activitat per a poder mostrar algo

        if (intent.extras != null && intent.extras!!.getString("activity") != null) { // si tenim intent.extras (és a dir, venim d'una altra activity de la app)
            Log.w("intent.extras", "is not null")
            // recibir actividad seleccionada de la otra pantalla
            val arguments = intent.extras
            val activityString = arguments?.getString("activity")

            activity = GsonBuilder().create().fromJson(activityString, ActivityFromList::class.java)

            // Ferran
            iniMostrarActivitat()
        } else { // si extras nulls, potser hem vingut a aquesta activity a través d'un dynamic link
            checkForDynamicLinks() // aquesta funció obté la PK d'una activitat a través del dynamic link, fa GET per obtenir-ne totes les dades, i LLAVORS crida a iniMostrarActivitat()
        }
    }

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
    private fun setInfoUsuariCreador() {
        viewModel.getProfileInfo(activity.usuariCreador)
        viewModel.profileInfo.observe(
            this,
            Observer@{
                if (it is Result.Success) {
                    userUID = it.data.uid
                    username = it.data.username

                    creator.text = getString(R.string.created_by) + " " + username
                }
            }
        )
    }

    // Ferran: he ficat en aquest mètode tot el que es feia a onCreate que podia requerir tenir les dades de la Activitat. (get dades, set listeners, ...)
    fun iniMostrarActivitat() {
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
                                    .make(layout, getString(R.string.successfully_joined), Snackbar.LENGTH_LONG)
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

    private fun uploadParticipants() {
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

                    // aquest observer salta?
                    Log.d("getParticipants", "arribo al InfoActivity::getParticipants.observe i passo el setData. A més, it.size = " + it.size.toString())

                    nRemainingParticipants = activity.maxParticipant - it.size
                    Log.d(
                        "getParticipants",
                        "nRemainingParticipants = " + nRemainingParticipants.toString()
                    )
                }
            }
        )
    }

    private fun valoracioUsuari() {
        datahora.text = activity.dataHoraIni
        creator.text = getString(R.string.created_by) + activity.usuariCreador
        capacity.text = activity.maxParticipant.toString()
        description.text = activity.descripcio

        // get the current date
        val currentTime = Calendar.getInstance().time
        // change final date format
        var date = changeDateFormat()

        // si el usuario no es participante de la activity o si esta no se ha realizado, no se permite hacer rating y/o review
        if (date != null) {
            if (!joined or (date > currentTime)) {
                cantreview()
            }
        }
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

    private fun showReviews() {
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

    private fun addToCalendar() {
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
            if (this::activity.isInitialized) {
                val linkGenerator = AuxGenerateDynamicLink()
                val dynamicLinkUri: Uri = linkGenerator.generateDynamicLink(
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

                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.share_activity_message, dynamicLinkUri)
                )
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share To:"))
            } else {
                Toast.makeText(applicationContext, R.string.error, Toast.LENGTH_SHORT).show()
            }
        } else if (item.itemId == R.id.share_in_app_btn) {
            changeToInviteActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeToInviteActivity() {
        if (this::activity.isInitialized) {
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
        } else {
            Toast.makeText(applicationContext, R.string.error, Toast.LENGTH_SHORT).show()
        }
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

    private fun checkForDynamicLinks() {
        Log.d("dynamic links", "we check for dynamic links")
        // al video (de fa 1any i mig) ho fa una mica diferent
        // el seu segur q habilita analytics
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                Log.w("dynamicLink", "getDynamicLink:onSuccess")
                // ara tenim el dynamic link. let's extract the deeplink url

                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    Log.w("dynamicLink-deeplink", "pendingDynamicLinkData != null")
                }
                // Handle the deep link. For example, open the linked
                // content, or apply promotional credit to the user's
                // account.
                // ...
                val activityCreator = deepLink?.getQueryParameter("creator") // params de query ("? = ")  que puc posar al deeplink
                val activityDateTime = deepLink?.getQueryParameter("dataHora")

                if (activityCreator != null && activityDateTime != null) {
                    // hem obtingut la PK de la activitat. fem GET de backend i la mostrarem
                    Log.w("dynamicLink", "getInfoActivitatIMostrar")
                    getInfoActivitatIMostrar(activityCreator, activityDateTime)
                } else {
                    // no hauria d'arribar aquí. ja faré tractat de l'error per si a cas I guess
                    Log.w("deep link query params", "getDynamicLink: deep link query params are null")
                }
            }
            .addOnFailureListener(this) { e -> Log.w("dynamicLink", "getDynamicLink:onFailure", e) }
    }

    private fun getInfoActivitatIMostrar(activityCreator: String, activityDateTime: String) {

        viewModel.getActivityResult(activityCreator, activityDateTime)
        viewModel.infoActivitatResult.observe( // aquesta not bad
            this@InfoActivity,
            Observer {
                Log.w("getInfoActivitatIMostr3", "salta l'observer1")
                if (it is Result.Success) {
                    Log.w("getInfoActivitatIMostr3", "we got an actual activity!!!!!")
                    // activity = it.data
                    activity = it.data
                    // i ja puc mostrar la info
                    iniMostrarActivitat()
                } else {
                    Toast.makeText(this, R.string.couldnt_retrieve_link_activity, Toast.LENGTH_LONG).show()
                }
            }
        )
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
