package com.example.dashboard.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.dashboard.R
import com.example.dashboard.databinding.FragmentDashboardBinding
import com.example.dashboard.view.action.DashboardAction
import com.example.utils.core.BaseFragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Random

private const val LOCATION_PERMISSION_REQUEST_CODE = 1234
private var countDownTimer: CountDownTimer? = null
private const val COUNTDOWN_TIME = 3600000L

class DashboardFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: DashboardViewModel by viewModel()
    private var googleMap: GoogleMap? = null

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
        observeActions()
        viewModel.fetchDashboard()
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
                addRandomMarkersNearby(currentLocation)
            }
        }
        googleMap?.setOnMarkerClickListener { marker ->
            showBottomDialog(marker.position)
            true
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(callback: (LatLng) -> Unit) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                callback(LatLng(location.latitude, location.longitude))
            }
        }
    }

    private fun addRandomMarkersNearby(center: LatLng) {
        val random = Random()

        for (i in 1..5) {
            val randomLat = center.latitude + (random.nextDouble() * 0.02) * if (random.nextBoolean()) 1 else -1
            val randomLng = center.longitude + (random.nextDouble() * 0.02) * if (random.nextBoolean()) 1 else -1
            googleMap?.addMarker(MarkerOptions().position(LatLng(randomLat, randomLng)))
        }
    }

    private fun hasLocationPermissions(): Boolean =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).all {
            ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
        }

    private fun requestLocationPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun observeActions() {
        viewModel.dashboardAction.observe(viewLifecycleOwner) { action ->
            when (action) {
                is DashboardAction.DashboardLoaded -> {}

                is DashboardAction.Error -> showError(action.msg ?: "")
            }
        }

        binding.iconStart.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_detalhes_fragment)
        }
    }

    private fun showBottomDialog(latLng: LatLng) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_bottom_check_in, null)

        val btnCheckIn: Button = view.findViewById(R.id.btnCheckIn)

        btnCheckIn.setOnClickListener {
            btnCheckIn.isEnabled = false
            startTimer(btnCheckIn)
            handleCheckIn(latLng)
        }

        bottomSheetDialog.setOnDismissListener {
            countDownTimer?.cancel()
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    private fun startTimer(btn: Button) {
        countDownTimer = object : CountDownTimer(COUNTDOWN_TIME, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                btn.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                btn.text = getString(R.string.check_in)
                btn.isEnabled = true
            }
        }.start()
    }

    private fun handleCheckIn(latLng: LatLng) {
        Toast.makeText(requireContext(), "Checked in at: ${latLng.latitude}, ${latLng.longitude}", Toast.LENGTH_SHORT)
            .show()
    }

    private fun showError(error: String) = Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()

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
        countDownTimer?.cancel()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}
