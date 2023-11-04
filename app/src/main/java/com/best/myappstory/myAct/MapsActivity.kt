package com.best.myappstory.myAct

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.best.myappstory.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.best.myappstory.databinding.ActivityMapsBinding
import com.best.myappstory.myStories.Story
import com.best.myappstory.myViewModels.MapsViewModel
import com.best.myappstory.myViewModels.ViewModelFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapsViewModel by viewModels {
        ViewModelFactory(this)
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
        private var ORIGIN = "origin"
        private var NAME = "name"
        private var LAT = 1.0
        private var LON = 1.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ORIGIN = intent.getStringExtra("origin").toString()
        val story = getParceableData()
        if (story != null) {
            NAME = story.userName.toString()
            LAT = story.lat?.toDouble() ?: 0.0
            LON = story.lon?.toDouble() ?: 0.0
        }

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

        if (ORIGIN == "FromActionBar") {
            viewModel.listStories.observe(this) { listStories ->
                for (storyItem in listStories) {
                    setMarker(storyItem.lat.toDouble(), storyItem.lon.toDouble(), storyItem.name)
                }
            }
        } else if (ORIGIN == "FromListStory") {
            setMarker(LAT, LON, NAME)
        }


        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getParceableData(): Story? {
        if (Build.VERSION.SDK_INT >= 33) {
            return intent.getParcelableExtra(EXTRA_STORY, Story::class.java)
        } else {
            @Suppress("DEPRECATED")
            return intent.getParcelableExtra(EXTRA_STORY)
        }
    }

    private fun setMarker(lat: Double, lon: Double, title: String) {
        val loc = LatLng(lat, lon)
        mMap.addMarker(MarkerOptions().position(loc).title(title))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
    }
}