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
import com.offhome.app.model.ActivityFromList
import com.offhome.app.ui.infoactivity.InfoActivity
import java.util.*


class MapsFragment : Fragment() {

    companion object {
        fun newInstance() =
            MapsFragment()
    }

    private lateinit var mMap: GoogleMap
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var activitiesList: List<ActivityFromList> = ArrayList()
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
        for (item in activitiesList) {
            // transform address to coordinates
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>
            addresses = geocoder.getFromLocationName(item.nomCarrer + " " + item.carrerNum, 1)
            if (addresses.size > 0) {
                latitude = addresses[0].latitude
                longitude = addresses[0].longitude
            }
            // set marker in place
            val place = LatLng(latitude, longitude)
            mMap.addMarker(MarkerOptions().position(place).title(item.titol))

            mMap.setOnInfoWindowClickListener {
                //al clicar al título, se abre la pantalla con la info de la activity
                val intent = Intent(context, InfoActivity::class.java)
                intent.putExtra("activity", GsonBuilder().create().toJson(item))
                context?.startActivity(intent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_activities_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        activitiesViewModel = ViewModelProvider(this).get(ActivitiesViewModel::class.java)

        activitiesViewModel.getActivitiesList((activity as Activities).categoryName).observe(
            viewLifecycleOwner,
            Observer {
                activitiesList = it
                mapFragment?.getMapAsync(callback)
            }
        )
    }
}
