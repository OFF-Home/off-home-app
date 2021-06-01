package com.offhome.app.ui.activitieslist



import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.offhome.app.R
import com.offhome.app.data.Result
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
    private lateinit var viewSeek: View
    private lateinit var seekBarDistance: SeekBar
    private lateinit var textViewDistance: TextView
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var activitiesList: MutableList<ActivityFromList> = ArrayList()
    private var currentActivities: MutableList<ActivityFromList> = ArrayList()
    private lateinit var activitiesViewModel: ActivitiesViewModel
    private var locationManager : LocationManager? = null
    private lateinit var locationUser: Location
    private lateinit var mapFragment: SupportMapFragment


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

        Dexter.withContext(context)
            .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(object: MultiplePermissionsListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(response: MultiplePermissionsReport?) {
                    if (response!!.areAllPermissionsGranted()) {
                        mMap.isMyLocationEnabled = true;
                        mMap.uiSettings.isMyLocationButtonEnabled = true;
                        var locationListener = object: LocationListener{
                            override fun onLocationChanged(location: Location) {
                                locationUser = location
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(locationUser.latitude, locationUser.longitude), 12.5f))
                            }
                            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                            }

                            override fun onProviderEnabled(provider: String) {
                            }

                            override fun onProviderDisabled(provider: String) {
                            }

                        }
                        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
                        initOptionRadi()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    TODO("Not yet implemented")
                }
            }).check()
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
     * Inits options of filtering if location enabled
     */
    private fun initOptionRadi() {
        seekBarDistance.visibility = View.VISIBLE
        viewSeek.visibility = View.VISIBLE
        textViewDistance.visibility = View.VISIBLE
        seekBarDistance.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress == 0) {
                    textViewDistance.text = getString(R.string.all_activities)
                    activitiesViewModel.getActivitiesList((activity as Activities).categoryName).observe(
                        viewLifecycleOwner,
                        Observer {
                            if (it != null) {
                                activitiesList.clear()
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
                                    val currentTime = Calendar.getInstance().time
                                    if (date != null) {
                                        if (date > currentTime) {
                                            activitiesList.add(item)
                                        }
                                    }
                                }
                            }
                            mMap.clear()
                            currentActivities = activitiesList
                            mapFragment.getMapAsync(callback)
                        }
                    )
                } else {
                    textViewDistance.text = progress.toString() + "km"
                    locationUser = mMap.myLocation
                    activitiesViewModel.getActivitiesByRadi((activity as Activities).categoryName, locationUser.altitude, locationUser.longitude, progress).observe(
                        viewLifecycleOwner,
                        Observer {
                            if (it is Result.Success) {
                                activitiesList.clear()
                                for (item in it.data) {
                                    // transform dataHoraIni into date format
                                    val mydate = item.dataHoraFi
                                    var date: Date? = null
                                    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                                    try {
                                        date = format.parse(mydate)
                                    } catch (e: ParseException) {
                                        e.printStackTrace()
                                    }
                                    val currentTime = Calendar.getInstance().time
                                    if (date != null) {
                                        if (date!! > currentTime) {
                                            activitiesList.add(item)
                                        }
                                    }
                                }
                            }
                            mMap.clear()
                            currentActivities = activitiesList
                            mapFragment.getMapAsync(callback)
                        }
                    )
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
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
        mapFragment = (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!

        activitiesViewModel = ViewModelProvider(this).get(ActivitiesViewModel::class.java)
        locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager?

        viewSeek = view.findViewById(R.id.viewSeekBar)
        seekBarDistance = view.findViewById(R.id.seekBarDistance)
        textViewDistance = view.findViewById(R.id.textViewDistance)

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
