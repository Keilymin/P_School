package com.burlakov.week1application.activities


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private val INITIAL_ZOOM = 12f
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_LOCATION_PERMISSION = 1
    private val mapType = GoogleMap.MAP_TYPE_TERRAIN
    private lateinit var map: GoogleMap
    private lateinit var search: Button
    private var currMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.map)

        search = findViewById(R.id.search)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.map, mapFragment).commit()
        mapFragment.getMapAsync(this)

        search.setOnClickListener {
            if (currMarker != null) {
                val intent = Intent(this, MapSearchResultActivity::class.java)
                intent.putExtra(
                    MapSearchResultActivity.LAT,
                    currMarker!!.position.latitude.toString()
                )
                intent.putExtra(
                    MapSearchResultActivity.LON,
                    currMarker!!.position.longitude.toString()
                )
                startActivity(intent)
            } else Toast.makeText(this, getString(R.string.no_map_pos), Toast.LENGTH_SHORT).show()
        }

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

    private fun enableMyLocation() {
        if (addMarkOnMyLocation()) {
            map.setOnMyLocationButtonClickListener { addMarkOnMyLocation() }
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            fusedLocationClient.lastLocation
                .addOnSuccessListener(this) { location ->
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}