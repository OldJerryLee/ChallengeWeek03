package com.fabriciopujol.challengethirdweek

import android.graphics.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fabriciopujol.challengethirdweek.models.UserMap

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class DisplayMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var userMap: UserMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_map)
        userMap = intent.getSerializableExtra(EXTRA_USER_MAP) as UserMap

        supportActionBar?.title=userMap.title
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val bounds = LatLngBounds.Builder()

        for (place in userMap.places) {
            val latLng = LatLng(place.latitude, place.longitude)
            bounds.include(latLng)
            mMap.addMarker(MarkerOptions().position(latLng).title(place.title).snippet(place.descrition))
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),1000,1000,0))
    }
}