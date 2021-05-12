 package com.offhome.app.ui.infoactivity

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnTouchListener
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
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.model.ActivityFromList
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
    private var participantsList: List<String> = ArrayList()


    /**
     * This is executed when the activity is launched for the first time or created again.
     * @param savedInstanceState is the instance of the saved State of the activity
     */
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
        layoutParticipants = findViewById(R.id.listParticipants)
        with(layoutParticipants) {
            layoutManager = LinearLayoutManager(context)
            adapter = participantsAdapter
        }


        viewModel.getParticipants(activity.usuariCreador, activity.dataHoraIni).observe(
            this,
             {
                participantsAdapter.setData(it)
            }
        )

        val datahora = findViewById<TextView>(R.id.textViewDataTimeActivity)
        datahora.text = activity.dataHoraIni

        val capacity = findViewById<TextView>(R.id.textViewCapacity)
        capacity.text = activity.maxParticipant.toString()

        val creator = findViewById<TextView>(R.id.textViewCreator)
        creator.text = getString(R.string.created_by) + activity.usuariCreador

        val description = findViewById<TextView>(R.id.textViewDescription)
        description.text = activity.descripcio

        val estrelles = findViewById<RatingBar>(R.id.ratingStars)

        val comment = findViewById<EditText>(R.id.yourcomment)

        val btnsubmit = findViewById<Button>(R.id.submitcomment)

        viewModel.getValoracioUsuari(activity.usuariCreador, activity.dataHoraIni, SharedPreferenceManager.getStringValue(
            Constants().PREF_EMAIL).toString()).observe(
            this,
            {
                //si tiene valoración, ponerla en las estrellas y ya no puede añadir rating, solo review
                if (it.valoracio != 0) {
                    estrelles.numStars = it.valoracio
                    estrelles.isFocusable = false
                    estrelles.setIsIndicator(true)
                }
                //si tiene review, cambiar el texto del edittext, bloquearlo y bloquear boton submit
                if (it.review != " ") {
                    comment.setHint(R.string.cantreview)
                    comment.isFocusable = false
                    btnsubmit.setEnabled(false)
                }
            }
        )

        val layout = findViewById<View>(R.id.content)

        //al clicar a submit, envía valoracion a back
        btnsubmit.setOnClickListener {
            //si no hay estrellas, muestra mensaje pidiendolas
            if (estrelles.numStars == 0) {
                val snackbar: Snackbar = Snackbar
                    .make(layout, R.string.mustaddrating, Snackbar.LENGTH_LONG)
                snackbar.show()
            }
            //si las hay, enviar datos a back
            else {
                viewModel.putValoracio(SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString(),
                    activity.usuariCreador, activity.dataHoraIni, estrelles.numStars, comment.text.toString()).observe(
                    this,
                    {
                        if (it != " ") {
                            if (it == "Your rating has been saved") {
                                val snackbar: Snackbar = Snackbar
                                    .make(layout, R.string.savedrating, Snackbar.LENGTH_LONG)
                                snackbar.show()

                                //cambiar estrellas y edit text a que ya no pueda añadir nada
                                estrelles.isFocusable = false
                                estrelles.setIsIndicator(true)
                                comment.setHint(R.string.cantreview)
                                comment.isFocusable = false
                                btnsubmit.setEnabled(false)
                            }
                        }
                    }
                )
            }
        }

        //mostrar todas las reviews de la activity, repasar esto
        reviewsAdapter = ReviewsRecyclerViewAdapter()
        layoutReviews = findViewById(R.id.listComments)
        with(layoutReviews) {
            layoutManager = LinearLayoutManager(context)
            adapter = reviewsAdapter
        }

        viewModel.getReviews(activity.usuariCreador, activity.dataHoraIni).observe(
            this,
            {
                reviewsAdapter.setData(it)
            }
        )


        val btnJoin = findViewById<Button>(R.id.btn_join)
        var joined = false

        btnJoin.setOnClickListener {
            joined = !joined
            if (joined) {
                btnJoin.text = "JOINED"
                viewModel.joinActivity(activity.usuariCreador, activity.dataHoraIni).observe(
                    this,
                    {
                        if (it != " ") {
                            if (it == "You have joined the activity!") {
                                val snackbar: Snackbar = Snackbar
                                    .make(layout, "Successfully joined!", Snackbar.LENGTH_LONG)
                                    .setAction(getString(R.string.go_chat)) {
                                        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
                                    }
                                snackbar.show()
                            } else {
                                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                )
            }
            else {
                btnJoin.text = "JOIN"

                viewModel.deleteUsuari(activity.usuariCreador, activity.dataHoraIni).observe(
                    this,
                    {
                        if (it != " ") {
                            if (it == "You have left the activity :(") {
                                val snackbar: Snackbar = Snackbar
                                    .make(layout, "You left :( !", Snackbar.LENGTH_LONG)
                                    .setAction(getString(R.string.go_chat)) {
                                        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
                                    }
                                snackbar.show()
                            } else {
                                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                )
            }

        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // asignar título actividad como título de la pantalla
        title = activity.titol

        // canviar like al clicar
        var clicked = false

        imageLike = findViewById<ImageView>(R.id.imageViewIconLike)
        imageLike.setOnClickListener {
            clicked = !clicked
            if (clicked) {
                imageLike.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                imageLike.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
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
        addresses = geocoder.getFromLocationName(activity.nomCarrer + " " + activity.carrerNum, 1)
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
        menuInflater.inflate(R.menu.share_menu,menu)
        return true
    }

    /**
     * Function called when the user selects an item from the options menu
     * @param item selected
     * @return true if the menu is successfully handled
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_btn){
            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"Hey! Check out this great activity:")
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"Share To:"))
        }
        return super.onOptionsItemSelected(item)
    }


}
