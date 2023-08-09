package com.example.dashboard.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dashboard.R
import com.example.dashboard.databinding.FragmentDashboardBinding
import com.example.dashboard.view.action.DashboardAction
import com.example.utils.core.BaseFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val LOCATION_PERMISSION_REQUEST_CODE = 1234

class DashboardFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: DashboardViewModel by viewModel()
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
        }
    }

    private fun hasLocationPermissions(): Boolean =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).all {
            ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
        }

    private fun requestLocationPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun observeActions() {
        viewModel.dashboardAction.observe(viewLifecycleOwner) { action ->
            when (action) {
                is DashboardAction.DashboardLoaded -> { /* Handle dashboard loaded */ }
                is DashboardAction.Error -> showError(action.msg ?: "")
            }
        }
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
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}
