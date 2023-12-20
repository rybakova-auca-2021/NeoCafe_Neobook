package com.example.neocafe.view.mainPage

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.neocafe.R
import com.example.neocafe.model.Branch
import com.example.neocafe.viewModel.GetBranchesViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastKnownLocation: LatLng? = null
    private var googleMap: GoogleMap? = null
    private val viewModel: GetBranchesViewModel by viewModels()

    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap

        googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

        // Check for location permission
        if (isLocationPermissionGranted()) {
            try {
                googleMap.isMyLocationEnabled = true

                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        lastKnownLocation = LatLng(it.latitude, it.longitude)
                        val cameraPosition = CameraPosition.Builder()
                            .target(lastKnownLocation!!)
                            .zoom(15.0f)
                            .build()
                        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    }
                }
            } catch (securityException: SecurityException) {
            }
        } else {
            requestLocationPermission()
        }
//        val customMarkerBitmap = BitmapFactory.decodeResource(resources, R.drawable.marker)
//        val customMarkerIcon = BitmapDescriptorFactory.fromBitmap(customMarkerBitmap)
        val bishkek = LatLng(42.875901469124514, 74.60371251208278)
        val cameraPosition = CameraPosition.Builder()
            .target(bishkek)
            .zoom(13.0f)
            .build()
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        //googleMap.addMarker(MarkerOptions().position(bishkek).title("Marker in Bishkek").icon(customMarkerIcon))

        // Call getBranches to add markers for branches
        getBranches()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun isLocationPermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_LOCATION_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.getMapAsync(callback)
            } else {
                // Handle the case where the user denied the permission
            }
        }
    }

    private fun getBranches() {
        viewModel.getAllBranches { branches ->
            branches.forEach { branch ->
                val branchLocation = LatLng(branch.latitude.toDouble(), branch.longitude.toDouble())
                val customMarkerBitmap = BitmapFactory.decodeResource(resources, R.drawable.marker)
                val customMarkerIcon = BitmapDescriptorFactory.fromBitmap(customMarkerBitmap)


                val marker = googleMap?.addMarker(MarkerOptions().position(branchLocation).title(branch.address).icon(customMarkerIcon))

                marker?.tag = branch

                googleMap?.setOnMarkerClickListener { clickedMarker ->
                    val clickedBranch = clickedMarker.tag as Branch
                    val bottomSheetFragment = BranchInfoBottomFragment.newInstance(clickedBranch.title)
                    bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
                    true
                }
            }
        }
    }

}
