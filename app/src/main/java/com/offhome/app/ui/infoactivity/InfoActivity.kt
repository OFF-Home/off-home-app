package com.offhome.app.ui.infoactivity

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.offhome.app.R
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

    /**
     * This is executed when the activity is launched for first time or created again.
     * @param savedInstanceState is the instance of the saved State of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        //recibir actividad seleccionada de la otra pantalla
        val arguments = intent.extras
        val activityString = arguments?.getString("activity")
        //val activityString = "{\n" +
//                "        \"usuariCreador\": \"victorfer@gmai.com\",\n" +
//                "        \"nomCarrer\": \"Balmes2\",\n" +
//                "        \"numCarrer\": 11,\n" +
//                "        \"dataHoraIni\": \"13h\",\n" +
//                "        \"categoria\": \"Walking\",\n" +
//                "        \"maxParticipant\": 7,\n" +
//                "        \"titol\": \"Running in La Barce\",\n" +
//                "        \"descripcio\": \"so much fun!!!\",\n" +
//                "        \"dataHoraFi\": \" 13/5/2021\"\n" +
//                "    }"
        activity = GsonBuilder().create().fromJson(activityString, ActivityFromList::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(InfoActivityViewModel::class.java)

        val datahora = findViewById<TextView>(R.id.textViewDataTimeActivity)
        datahora.text = activity.dataHoraIni

        val capacity = findViewById<TextView>(R.id.textViewCapacity)
        capacity.text = activity.maxParticipant.toString()

        val creator = findViewById<TextView>(R.id.textViewCreator)
        creator.text = getString(R.string.created_by) + activity.usuariCreador

        val description = findViewById<TextView>(R.id.textViewDescription)
        description.text = activity.descripcio

        val layout = findViewById<View>(R.id.content)

        val btnJoin = findViewById<Button>(R.id.btn_join)
        btnJoin.setOnClickListener {
            viewModel.joinActivity(activity.usuariCreador, activity.dataHoraIni, "Pau").observe(this, {
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
            })
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        //asignar título actividad como título de la pantalla
        title = activity.titol



        //canviar like al clicar
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

        //transform address to coordinates
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
}