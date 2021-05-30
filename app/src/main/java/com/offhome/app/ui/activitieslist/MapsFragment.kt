package com.offhome.app.ui.activitieslist



import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.data.model.ActivityFromList
import com.offhome.app.ui.infoactivity.InfoActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Class that defines the fragment to show the Map with the Activities
 * @author Emma Pereira
 * @property mMap references the GoogleMap that appears in the screen with the activities locations
 * @property latitude references the latitude coordinate of the activity's location the map
 * @property longitude references the longitude coordinate of the activity's location on the map
 * @property activitiesList references the list of activities that will be displayed on the map
 * @property activitiesViewModel references the viewmodel of the activities
 */
class MapsFragment : Fragment() {

    companion object {
        fun newInstance() =
            MapsFragment()
    }

    private lateinit var mMap: GoogleMap
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var activitiesList: MutableList<ActivityFromList> = ArrayList()
    private var currentActivities: MutableList<ActivityFromList> = ArrayList()
    private lateinit var activitiesViewModel: ActivitiesViewModel

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we add the markers for the activities and move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private val callback = OnMapReadyCallback { googleMap ->

        mMap = googleMap
        mMap.uiSettings.setZoomControlsEnabled(true)

        // Add a marker in Barcelona and move the camera
        val barcelona = LatLng(41.3879, 2.16992)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(barcelona, 12.5f))

        // set markers in map with all the activities
        for (item in currentActivities) {
            // transform address to coordinates
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>
            addresses = geocoder.getFromLocationName(item.nomCarrer + " " + item.numCarrer, 1)
            if (addresses.size > 0) {
                latitude = addresses[0].latitude
                longitude = addresses[0].longitude
            }
            // set marker in place
            val place = LatLng(latitude, longitude)
            mMap.addMarker(MarkerOptions().position(place).title(item.titol))

            mMap.setOnInfoWindowClickListener {
                // al clicar al título, se abre la pantalla con la info de la activity
                val intent = Intent(context, InfoActivity::class.java)
                intent.putExtra("activity", GsonBuilder().create().toJson(item))
                context?.startActivity(intent)
            }
        }
    }

    /**
     * Called to initialize the fragment and has the observers, returns the view inflated
     * @param inflater is the Layout inflater to inflate the view
     * @param container is the part which contains the view
     * @param savedInstanceState is the last saved instance of the view
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_activities_fragment, container, false)
    }

    /**
     * Called once the view is inflated and here is where we display the information and we initizalize other views
     * @param view is the view initialized by the onCreateView function
     * @param savedInstanceState is the last saved instance of the view
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        activitiesViewModel = ViewModelProvider(this).get(ActivitiesViewModel::class.java)

        // get the current date
        val currentTime = Calendar.getInstance().time

        activitiesViewModel.getActivitiesList((activity as Activities).categoryName).observe(
            viewLifecycleOwner,
            Observer {
                if (it != null) {
                    for (item in it) {
                        // transform dataHoraIni into date format
                        val mydate = item.dataHoraFi
                        var date: Date? = null
                        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                        try {
                            date = format.parse(mydate)
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }

                        if (date != null) {
                            if (date > currentTime) {
                                activitiesList.add(item)
                            }
                        }
                    }
                }
                currentActivities = activitiesList
                mapFragment?.getMapAsync(callback)
            }
        )
    }
}
