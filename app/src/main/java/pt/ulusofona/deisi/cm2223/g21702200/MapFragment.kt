package pt.ulusofona.deisi.cm2223.g21702200

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projectcm.R
import com.example.projectcm.databinding.FragmentMapBinding
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21702200.data.local.DatabaseApp
import pt.ulusofona.deisi.cm2223.g21702200.data.local.ProjectRoom
import java.util.Locale


class MapFragment : Fragment(), OnLocationChangedListener {

    private lateinit var binding: FragmentMapBinding

    private var map: GoogleMap? = null

    private lateinit var room: ProjectRoom

    private val clickedMarkers = mutableMapOf<Marker, Boolean>()

    private lateinit var geocoder: Geocoder


    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        var permissionGranted = false

        permissionsBuilder(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        ).build().send { result ->
            if (!result.allGranted()) {

                Toast.makeText(
                    requireContext(), "Dont have location permissions", Toast.LENGTH_SHORT
                ).show()

            } else {
                permissionGranted = true
            }
        }


        //FusedLocation.registerListener(this)

        room = ProjectRoom(
            DatabaseApp.getInstance(requireContext()).cinemasDao(),
            DatabaseApp.getInstance(requireContext()).filmesDao(),
            DatabaseApp.getInstance(requireContext()).registoDao()
        )

        val view = inflater.inflate(R.layout.fragment_map, container, false)

        binding = FragmentMapBinding.bind(view)

        geocoder = Geocoder(context!!, Locale.getDefault())

        binding.map.onCreate(savedInstanceState)



        binding.map.getMapAsync { map ->
            this.map = map

            if (permissionGranted) {
                map.isMyLocationEnabled = true
            }

            FusedLocation.registerListener(this)
            //map.addMarker(MarkerOptions())

            addMarkers()

        }

        //binding.map.getMapAsync { googleMap ->
        //map = googleMap
        //}

        return binding.root
    }


    @SuppressLint("MissingPermission")
    private fun addMarkers() {

        //FusedLocation.registerListener(this)
        //map?.isMyLocationEnabled = true

        room.getAllRegistrationData { result ->

            val dataRegistos = result.getOrNull()

            for (registo in dataRegistos!!) {

                room.getCinemaObjectById(registo.cinemaId) { cinemaObject ->

                    val cinema = cinemaObject.getOrNull()!!

                    val latitude = cinema.latitude
                    val longitude = cinema.longitude

                    val movieName = registo.name
                    val rate = registo.rate

                    CoroutineScope(Dispatchers.Main).launch {
                        //val markerOptions = MarkerOptions()
                        //.position(LatLng(latitude, longitude))
                        //.title(movieName)
                        //.snippet(rate)

                        val marker = map!!.addMarker(
                            MarkerOptions().position(LatLng(latitude, longitude)).title(movieName)
                                .snippet(rate).icon(getMarkerIcon(rate))
                        )

                        //val markerIcon = getMarkerIcon(rate)
                        //markerOptions.icon(markerIcon)

                        //val marker = map?.addMarker(markerOptions)
                        marker!!.tag = registo.uuid

                        map!!.setOnMarkerClickListener { clickedMarker ->
                            val filmeId = clickedMarker.tag as String

                            if (clickedMarkers.containsKey(clickedMarker)) {
                                onClick(filmeId)
                            } else {
                                clickedMarker.showInfoWindow()

                                clickedMarkers[clickedMarker] = true
                            }
                            true
                        }
                    }
                }
            }
        }
    }

    private fun getMarkerIcon(rate: String): BitmapDescriptor {


        //val calendar2 = Calendar.getInstance()
        //calendar2.time = dateFilme

        //val calendar = Calendar.getInstance()

        //val dayOfMonthFilme = calendar.get(Calendar.DAY_OF_MONTH)
        //val MonthOfMonthFilme = calendar.get(Calendar.MONTH)
        //val yearOfMonthFilme = calendar.get(Calendar.YEAR)


        //if (dayOfMonthFilme == calendar2.get(Calendar.DAY_OF_MONTH) && MonthOfMonthFilme == calendar2.get(
        // Calendar.MONTH) && yearOfMonthFilme == calendar2.get(Calendar.YEAR)) {
        //println("filme do dia de hoje")
        //}

        // i dont do that way, what i learn to prove my defence whatn training


        /*

        val calendarToday = Calendar.getInstance()
        calendarToday.set(Calendar.HOUR, 0)
        calendarToday.set(Calendar.MINUTE, 0)
        calendarToday.set(Calendar.SECOND, 0)
        calendarToday.set(Calendar.MILLISECOND, 0)

        val calendarMinor2Days = Calendar.getInstance()
        calendarMinor2Days.set(Calendar.HOUR, 0)
        calendarMinor2Days.set(Calendar.MINUTE, 0)
        calendarMinor2Days.set(Calendar.SECOND, 0)
        calendarMinor2Days.set(Calendar.MILLISECOND, 0)

        calendarMinor2Days.add(Calendar.DAY_OF_YEAR, -2)

        val list = result.getOrNull()!!

        val listResult = mutableListOf<RegistrationData>()

        for (registo in list) {

            val calendarRegisto = Calendar.getInstance()
            calendarRegisto.time = registo.date

            calendarRegisto.set(Calendar.HOUR, 0)
            calendarRegisto.set(Calendar.MINUTE, 0)
            calendarRegisto.set(Calendar.SECOND, 0)
            calendarRegisto.set(Calendar.MILLISECOND, 0)



            if (calendarRegisto >= calendarMinor2Days && calendarRegisto <= calendarToday) {
                listResult.add(registo)

            }
        }
        */


        val color: BitmapDescriptor
        when (rate.toIntOrNull()) {
            in 1..2 -> color =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)

            in 3..4 -> color =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)

            in 5..6 -> color =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)

            in 7..8 -> color =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)

            in 9..10 -> color =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)

            else -> color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        }
        return color
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    private fun placeCityName(latitude: Double, longitude: Double) {

        if (Connectivity.isOnline(context!!)) { // para nao crashar no offline!

            val addresses = geocoder.getFromLocation(latitude, longitude, 5)
            val location = addresses?.first {
                it.locality != null && it.locality.isNotEmpty()
            }
            if (location != null) {
                binding.tvCityName.text = location.locality
            }
        }
    }

    private fun placeCamera(latitude: Double, longitude: Double) {
        val cameraPosition =
            CameraPosition.Builder().target(LatLng(latitude, longitude)).zoom(12f).build()

        map?.animateCamera(
            CameraUpdateFactory.newCameraPosition(cameraPosition)
        )
    }


    override fun onLocationChanged(latitude: Double, longitude: Double) {
        placeCamera(latitude, longitude)
        placeCityName(latitude, longitude)
    }


    override fun onDestroy() {
        super.onDestroy()
        FusedLocation.unregisterListener()
    }

    private fun onClick(filmeId: String) {
        NavigationManager.goToDetails(parentFragmentManager, filmeId)
    }
}
