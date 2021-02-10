package com.fabriciopujol.challengethirdweek

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.fabriciopujol.challengethirdweek.models.Place
import com.fabriciopujol.challengethirdweek.models.UserMap

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class CreateMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var markers: MutableList<Marker> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        supportActionBar?.title = intent.getStringExtra(EXTRA_MAP_TITLE)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapFragment.view?.let {
            Snackbar.make(it, "Pressione para adicionar um marcador !", Snackbar.LENGTH_INDEFINITE)
                .setAction("OK",{})
                .setActionTextColor(ContextCompat.getColor(this, android.R.color.white))
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.Save){
            if (markers.isEmpty()){
                Toast.makeText(this, "É preciso ter pelo menos um marcador",Toast.LENGTH_LONG).show()
                return true
            }

            val places = markers.map {
                Place(it.title, it.snippet, it.position.latitude, it.position.longitude)
            }
            val userMap = intent.getStringExtra(EXTRA_MAP_TITLE)?.let { UserMap(it,places) }
            val data = Intent()
            data.putExtra(EXTRA_USER_MAP,userMap)
            setResult(Activity.RESULT_OK, data)
            finish()

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnInfoWindowClickListener {
            markers.remove(it)
            it.remove()
        }
        mMap.setOnMapLongClickListener {
            showAlertDialog(it)
        }

        val manaus = LatLng(-3.10719 , -60.0261)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(manaus,10f))

    }

    private fun showAlertDialog(latLng: LatLng){
        val placeFormView = LayoutInflater.from(this).inflate(R.layout.dialog_create_place,null)
        val dialog = AlertDialog.Builder(this).setTitle("Criar um marcador").setView(placeFormView)
            .setNegativeButton("Cancelar",null).setPositiveButton("OK",null).show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener{
            val title = placeFormView.findViewById<EditText>(R.id.editTitulo).text.toString()
            val des = placeFormView.findViewById<EditText>(R.id.editDes).text.toString()
            if(title.trim().isEmpty() || des.trim().isEmpty()){
                Toast.makeText(this, "Por favor insira um titulo e descrição",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val marker = mMap.addMarker(MarkerOptions().position(latLng).title(title).snippet(des))
            markers.add(marker)
            dialog.dismiss()
        }

    }

}