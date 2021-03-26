package com.offhome.app.ui.infoactivity

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.model.ActivityFromList
import java.util.*


/**
 * Class *InfoActicity*
 *
 * This class is the one that displays the information about an Activity
 * @author Emma Pereira
 * @property InfoActivityViewModel references the ViewModel class for this Activity
 * @property mMap references the GoogleMap that appears in the screen with the activity's location
 * @property imageLike references the ImageView to add an activity to a user's liked activities
 */
class InfoActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var imageLike: ImageView
    private lateinit var activity: ActivityFromList
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var infoActivityViewModel: InfoActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        //recibir actividad seleccionada de la otra pantalla
        val arguments = intent.extras
        //val activityString = arguments?.getString("activity")
        val activityString = "{\n" +
                "        \"usuariCreador\": \"victorfer@gmai.com\",\n" +
                "        \"nomCarrer\": \"Balmes2\",\n" +
                "        \"numCarrer\": 11,\n" +
                "        \"dataHoraIni\": \"13h\",\n" +
                "        \"categoria\": \"Walking\",\n" +
                "        \"maxParticipant\": 7,\n" +
                "        \"titol\": \"Running in La Barce\",\n" +
                "        \"descripcio\": \"so much fun!!!\",\n" +
                "        \"dataHoraFi\": \" 13/5/2021\"\n" +
                "    }"
        activity = GsonBuilder().create().fromJson(activityString, ActivityFromList::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
     * This is where we can add markers or lines, add listeners or move the camera.
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