package com.example.dashboard.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.dashboard.R
import com.example.dashboard.data.models.PlacesResponse
import com.example.dashboard.databinding.FragmentDashboardBinding
import com.example.dashboard.view.action.DashboardAction
import com.example.utils.core.BaseFragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

private const val LOCATION_PERMISSION_REQUEST_CODE = 1234

class DashboardFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: DashboardViewModel by viewModel()
    private var googleMap: GoogleMap? = null
    private lateinit var placesClient: PlacesClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = getDataBinding(inflater, container, R.layout.fragment_dashboard)
        with(binding.mapView) {
            onCreate(savedInstanceState)
            getMapAsync(this@DashboardFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the Places API client
        Places.initialize(requireContext(), getString(R.string.google_maps_key))
        placesClient = Places.createClient(requireContext())
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        googleMap?.apply {
            if (!hasLocationPermissions()) {
                requestLocationPermissions()
                return
            }
            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            getCurrentLocation { currentLocation ->
                moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
                observeActions(currentLocation)
                viewModel.fetchDashboard(currentLocation)
            }
        }
        googleMap?.setOnMarkerClickListener { marker ->
            showBottomDialog(marker)
            true
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(callback: (LatLng) -> Unit) {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                callback(LatLng(location.latitude, location.longitude))
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun searchNearbyParkingLots(places: PlacesResponse) {
        places.results.forEach { place ->
            val latLng = LatLng(place.geometry.location.lat, place.geometry.location.lng)
            googleMap?.addMarker(MarkerOptions().position(latLng).title(place.name))
        }
    }

    private fun hasLocationPermissions(): Boolean =
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }

    private fun addRandomMarkersNearby(center: LatLng) {
        val random = Random()

        for (i in 1..5) {
            val randomLat = center.latitude + (random.nextDouble() * 0.02) * if (random.nextBoolean()) 1 else -1
            val randomLng = center.longitude + (random.nextDouble() * 0.02) * if (random.nextBoolean()) 1 else -1
            googleMap?.addMarker(MarkerOptions().position(LatLng(randomLat, randomLng)).title("Estacionamento ParkCar"))
        }
    }

    private fun requestLocationPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun observeActions(center: LatLng) {
        viewModel.dashboardAction.observe(viewLifecycleOwner) { action ->
            when (action) {
                is DashboardAction.DashboardLoaded -> searchNearbyParkingLots(action.placesResponse)
                is DashboardAction.Error -> addRandomMarkersNearby(center)
            }
        }

        binding.iconStart.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_detalhes_fragment)
        }
    }

    private fun showBottomDialog(marker: Marker) {
        binding.bottomCardView.visibility = View.VISIBLE
        binding.locationInfoName.text = "Location: ${marker.title}"

        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>? =
            geocoder.getFromLocation(marker.position.latitude, marker.position.longitude, 1)
        val address: Address? = addresses?.firstOrNull()

        binding.locationInfoAddress.text = address?.getAddressLine(0) ?: "No address found"

        binding.btnReservation.setOnClickListener {
            handleReservation(marker)
        }
    }

    private fun handleReservation(marker: Marker) {
        val title = marker.title ?: "No title"
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(marker.position.latitude, marker.position.longitude, 1)
        val address: Address? = addresses?.firstOrNull()
        val addressLine = address?.getAddressLine(0) ?: "No address found"

        saveLastReservation(title, addressLine)

        Toast.makeText(
            requireContext(),
            "Reserved at: ${marker.position.latitude}, ${marker.position.longitude}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun saveLastReservation(title: String, address: String) {
        val sharedPreferences = requireContext().getSharedPreferences("reservations", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("last_reservation_title", title)
        editor.putString("last_reservation_address", address)
        editor.apply()
    }

    private fun showError(error: String) =
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()

    // Map Lifecycle Management
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        super.onStop()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}
