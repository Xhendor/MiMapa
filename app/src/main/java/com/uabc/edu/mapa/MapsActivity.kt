package com.uabc.edu.mapa

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
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
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.alert
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


class MapsActivity : AppCompatActivity(),
        OnMapReadyCallback, LocationListener,GoogleMap.OnMarkerClickListener {


    private lateinit var mMap: GoogleMap
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var mFirebaseDatabase:FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    val RC_SIGN_IN = 1

    private lateinit var locationManager: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase.reference.child("mensajes")

        //Obteniendo la instancia del FirebaseAPP
        mFirebaseAuth = FirebaseAuth.getInstance()
        //Crear el listener
        mFirebaseAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            //No implementado
            var usuario: FirebaseUser? = firebaseAuth.currentUser

            if (usuario != null) {
                Toast.makeText(applicationContext, usuario.displayName,
                        Toast.LENGTH_SHORT).show()
            } else {


                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(Arrays.asList(

                                        AuthUI.IdpConfig.EmailBuilder().build(),
                                        AuthUI.IdpConfig.GoogleBuilder().build()
                                ))
                                .build(),
                        RC_SIGN_IN)

            }

        }
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //<editor-fold desc="PERMISOS">

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
            //<editor-fold>

            //<editor-fold desc="Codigo SQLITE Select">
            /*  database.use {
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

            }            */
            //<editor-fold>


        }


        //</editor-fold>


    }

    override fun onStart() {
        super.onStart()

        if (ContextCompat.checkSelfPermission(applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager =
                    getSystemService(Context.LOCATION_SERVICE) as LocationManager

            locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1000L, 10f, this);
            locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 1000L, 10f, this);
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
        mMap.setOnMarkerClickListener(this)

        //<editor-fold desc="Codigo comentado Marcadores">
        //Add a marker in Sydney and move the camera
        /*val mexicali = LatLng(32.6519, -115.4683)
        val df = LatLng(19.4326, -99.1332)
        val guadalajara = LatLng(20.5, -103.0583)
        mMap.addMarker(MarkerOptions().position(mexicali).title("Chicali").snippet("Mexicali"))
        mMap.addMarker(MarkerOptions().position(df).title("El DF"))
        mMap.addMarker(MarkerOptions().position(guadalajara).title("Guadalajara"))

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexicali))
        */
        //</editor-fold>


        if (ContextCompat.checkSelfPermission(applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            var pos = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            mMap.isMyLocationEnabled = true
            location(pos)
        }


    }


    override fun onLocationChanged(location: Location?) {

        location(location)
        //<editor-fold desc="Toast de location">
        /* Toast.makeText(applicationContext,
                "Lat:" + location?.latitude.toString()
                     + ",Longitud:" + location?.longitude.toString(), Toast.LENGTH_LONG).show()*/
        //</editor-fold>
    }

    private fun location(location: Location?) {
        var soyYo = LatLng(location!!.latitude, location.longitude)
        var marker=MarkerOptions().position(soyYo).title("Soy yo").snippet("Aqui toy")
        mMap.addMarker(marker)


        mMap.moveCamera(CameraUpdateFactory.newLatLng(soyYo))
        val current:Calendar = Calendar.getInstance()

        database.use {
            insert("Puntos",

                    "latitud" to location!!.latitude,
                    "longitud" to location!!.longitude,
                    "fecha" to current.time.toString())
        }


    }



    //Agregando menu a la vista
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    //Agregando funcionalidad al seleccinar un item del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out_menu -> {
                AuthUI.getInstance().signOut(this)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RC_SIGN_IN) run {
            if (resultCode == Activity.RESULT_OK) {
                // Sign-in succeeded, set up the UI
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show()

            } else if (resultCode == Activity.RESULT_CANCELED) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {

        alert("Enviar notificación") {
            title = "Mensajes"
            yesButton {
                var punto:Puntos=Puntos(marker!!.title,
                        marker!!.position.latitude.toString(),
                        marker!!.position.longitude.toString())
                mDatabaseReference.push().setValue(punto)
            }
            noButton { }
        }.show()

        return true;
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

}
