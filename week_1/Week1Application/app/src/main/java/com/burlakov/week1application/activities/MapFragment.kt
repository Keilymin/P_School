package com.burlakov.week1application.activities


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.burlakov.week1application.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), OnMapReadyCallback {

    private val INITIAL_ZOOM = 12f
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_LOCATION_PERMISSION = 1
    private val mapType = GoogleMap.MAP_TYPE_TERRAIN
    private lateinit var map: GoogleMap
    private lateinit var search: Button
    private var currMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_map, container, false)

        search = root.findViewById(R.id.search)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapFragment = SupportMapFragment.newInstance()
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(R.id.map, mapFragment)?.commit()
        mapFragment.getMapAsync(this)

        search.setOnClickListener {
            if (currMarker != null) {
                val intent = Intent(context, MapSearchResultActivity::class.java)
                intent.putExtra(
                    MapSearchResultActivity.LAT,
                    currMarker!!.position.latitude.toString()
                )
                intent.putExtra(
                    MapSearchResultActivity.LON,
                    currMarker!!.position.longitude.toString()
                )
                startActivity(intent)
            } else Toast.makeText(context, getString(R.string.no_map_pos), Toast.LENGTH_SHORT).show()
        }

        return root
    }


    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map.mapType = mapType
        map.setOnMapClickListener {
            map.clear()
            currMarker = map.addMarker(
                MarkerOptions()
                    .position(it)
            )
        }
        enableMyLocation()
    }

    @Suppress("DEPRECATION")
    private fun enableMyLocation() {
        if (addMarkOnMyLocation()) {
            map.setOnMyLocationButtonClickListener { addMarkOnMyLocation() }
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED) && addMarkOnMyLocation()
                ) {
                    map.setOnMyLocationButtonClickListener { addMarkOnMyLocation() }
                }
            }
            else -> {
            }
        }
    }

    private fun addMarkOnMyLocation(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        map.clear()
                        val home = LatLng(location.latitude, location.longitude)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(home, INITIAL_ZOOM))
                        currMarker = map.addMarker(
                            MarkerOptions()
                                .position(home)
                        )
                    }
                }
            return true
        } else return false
    }

}