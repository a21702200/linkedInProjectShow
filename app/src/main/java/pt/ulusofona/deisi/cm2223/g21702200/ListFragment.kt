package pt.ulusofona.deisi.cm2223.g21702200

import android.Manifest
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectcm.R
import com.example.projectcm.databinding.FragmentListBinding
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21702200.algorithms.filterList
import pt.ulusofona.deisi.cm2223.g21702200.data.local.DatabaseApp
import pt.ulusofona.deisi.cm2223.g21702200.data.local.ProjectRoom
import pt.ulusofona.deisi.cm2223.g21702200.models.RegistrationData


private const val ARG_FILTER_TEXT = "ARG_FILTER_TEXT"
private const val ARG_CHECK_BOX_500 = "ARG_CHECK_BOX_500"
private const val ARG_CHECK_BOX_1000 = "ARG_CHECK_BOX_1000"
private const val ARG_VOLTEI = "ARG_VOLTEI"

class ListFragment : Fragment(), OnLocationChangedListener {

    private lateinit var adapter: MoviesAdapter
    private lateinit var binding: FragmentListBinding
    private lateinit var room: ProjectRoom
    private lateinit var sortButtonDescending: ImageButton
    private lateinit var sortButtonAscending: ImageButton
    private lateinit var filterButton: Button


    private var checkboxChecked500Filter: Boolean = false
    private var checkboxChecked1000Filter: Boolean = false

    private var filterTextFromFilter: String? = null
    private var voltei: Boolean = false


    private lateinit var stateList: MutableList<RegistrationData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            filterTextFromFilter = it.getString(ARG_FILTER_TEXT)
            checkboxChecked500Filter = it.getBoolean(ARG_CHECK_BOX_500)
            checkboxChecked1000Filter = it.getBoolean(ARG_CHECK_BOX_1000)
            voltei = it.getBoolean(ARG_VOLTEI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        room = ProjectRoom(
            DatabaseApp.getInstance(requireContext()).cinemasDao(),
            DatabaseApp.getInstance(requireContext()).filmesDao(),
            DatabaseApp.getInstance(requireContext()).registoDao()
        )

        val view = inflater.inflate(R.layout.fragment_list, container, false)

        //adapter = MoviesAdapter(::onClick,context!!)
        adapter = MoviesAdapter(::onClick, room)

        binding = FragmentListBinding.bind(view)

        return binding.root
    }


    override fun onStart() {
        super.onStart()

        permissionsBuilder(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        ).build().send { result ->
            if (!result.allGranted()) {

                Toast.makeText(
                    requireContext(), "Dont have location permissions", Toast.LENGTH_SHORT
                ).show()

            }
        }

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR

        FusedLocation.registerListener(this)

        binding.moviesList.layoutManager = LinearLayoutManager(requireContext())

        binding.moviesList.adapter = adapter

        stateList = mutableListOf()

        sortButtonDescending = binding.sortDescending
        sortButtonAscending = binding.sortAscending
        filterButton = binding.filterButton


        if (voltei) {

            Toast.makeText(requireContext(), "Filter Applied", Toast.LENGTH_SHORT).show()

            room.getAllRegistrationData { result ->
                if (result.isSuccess) {

                    stateList = result.getOrNull()!!.toMutableList()


                    if (!filterTextFromFilter.equals("")) {

                        val lista11 = mutableListOf<RegistrationData>()

                        for (elementoLista in stateList) {
                            if (elementoLista.name.lowercase()
                                    .equals(filterTextFromFilter!!.lowercase())
                            ) {

                                lista11.add(elementoLista)
                            }
                        }

                        stateList =
                            lista11.sortedByDescending { it.rate.toIntOrNull() }.toMutableList()

                        CoroutineScope(Dispatchers.Main).launch {
                            adapter.updateItems(stateList.toList())
                        }
                    }
                }
            }
        } else {

            room.getAllRegistrationData { result ->
                if (result.isSuccess) {

                    val list = result.getOrNull()

                    if (list != null) {

                        stateList =
                            list.sortedByDescending { it.rate.toIntOrNull() }.toMutableList()
                    }

                    CoroutineScope(Dispatchers.Main).launch {

                        adapter.updateItems(stateList)

                    }
                }
            }
        }


        sortButtonDescending.setOnClickListener {

            stateList = stateList.sortedBy { it.rate.toIntOrNull() }.toMutableList()


            adapter.updateItems(stateList)

            Toast.makeText(
                requireContext(), "Descending order", Toast.LENGTH_SHORT
            ).show()
        }

        sortButtonAscending.setOnClickListener {

            stateList = stateList.sortedByDescending { it.rate.toIntOrNull() }.toMutableList()

            adapter.updateItems(stateList)

            Toast.makeText(requireContext(), "Ascending order!", Toast.LENGTH_SHORT).show()
        }

        filterButton.setOnClickListener {

            NavigationManager.goTofilter(parentFragmentManager)

        }
    }


    private fun onClick(registoUuid: String) {
        NavigationManager.goToDetails(parentFragmentManager, registoUuid)
    }


    override fun onDestroy() {
        super.onDestroy()
        FusedLocation.unregisterListener()

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onLocationChanged(latitude: Double, longitude: Double) {
        filterListIfNeed(latitude, longitude)
    }

    private fun filterListIfNeed(latitude: Double, longitude: Double) {

        if (checkboxChecked500Filter && !checkboxChecked1000Filter) {

            filterList(stateList, room, latitude, longitude, 500) { result ->

                stateList = result.getOrNull()!!

                CoroutineScope(Dispatchers.Main).launch {
                    adapter.updateItems(stateList)
                }

                checkboxChecked500Filter = false
            }
        }

        if (checkboxChecked1000Filter) {

            filterList(stateList, room, latitude, longitude, 1000) { result ->

                stateList = result.getOrNull()!!

                CoroutineScope(Dispatchers.Main).launch {
                    adapter.updateItems(stateList)
                }

                checkboxChecked1000Filter = false
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(
            filterText: String,
            checkboxChecked500: Boolean,
            checkboxChecked1000: Boolean,
            voltei: Boolean
        ) = ListFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_FILTER_TEXT, filterText)
                putBoolean(ARG_CHECK_BOX_500, checkboxChecked500)
                putBoolean(ARG_CHECK_BOX_1000, checkboxChecked1000)
                putBoolean(ARG_VOLTEI, voltei)
            }
        }
    }
}