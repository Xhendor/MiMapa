package com.uabc.edu.mapa

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


class MapsActivity : AppCompatActivity(),
        OnMapReadyCallback, LocationListener {


    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (ContextCompat.checkSelfPermission(applicationContext,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(applicationContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(applicationContext,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(applicationContext,
                                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(applicationContext,
                                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            if (ContextCompat.checkSelfPermission(applicationContext,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(this,
                                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                        1)


                            }
                        }

                    }

                }


            }


            database.use {
                select("Puntos")
                        .exec {

                           while( moveToNext()){

                            var latitude =  getDouble(1)
                            var longitud=getDouble(2)
                            var fecha=getString(3)

                            println("Latitud"+latitude.toString())
                               println("Longitud"+longitud.toString())
                               println("Fecha"+fecha)


                           }
                        }

            }


        }





    }

    override fun onStart() {
        super.onStart()

        if (ContextCompat.checkSelfPermission(applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            locationManager =
                    getSystemService(Context.LOCATION_SERVICE) as LocationManager

            locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0L, 5f, this);
            locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0L, 1f, this);
        }
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
        //Add a marker in Sydney and move the camera
        val mexicali = LatLng(32.6519, -115.4683)
        val df = LatLng(19.4326, -99.1332)
        val guadalajara = LatLng(20.5, -103.0583)
        mMap.addMarker(MarkerOptions().position(mexicali).title("Chicali").snippet("Mexicali"))
        mMap.addMarker(MarkerOptions().position(df).title("El DF"))
        mMap.addMarker(MarkerOptions().position(guadalajara).title("Guadalajara"))

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexicali))



        if (ContextCompat.checkSelfPermission(applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            var pos = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            mMap.isMyLocationEnabled = true
            location(pos)
        }


    }

    override fun onLocationChanged(location: Location?) {

        location(location)

        Toast.makeText(applicationContext,
                "Lat:" + location?.latitude.toString()
                        + ",Longitud:" + location?.longitude.toString(), Toast.LENGTH_LONG).show()
    }

    private fun location(location: Location?) {
        var soyYo = LatLng(location!!.latitude, location.longitude)
        mMap.addMarker(MarkerOptions().position(soyYo).title("Soy yo").snippet("Aqui toy"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(soyYo))
        val current = LocalDateTime.now()

        database.use {
            insert("Puntos",

                    "latitud" to location!!.latitude,
                    "longitud" to location!!.longitude,
                    "fecha" to  current.toString())
        }


    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

}
