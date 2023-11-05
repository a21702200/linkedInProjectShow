package pt.ulusofona.deisi.cm2223.g21702200

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectcm.R
import com.example.projectcm.databinding.FragmentCinemasBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21702200.data.local.DatabaseApp
import pt.ulusofona.deisi.cm2223.g21702200.data.local.ProjectRoom


class CinemasInfos : Fragment() {

    private lateinit var adapter: CinemasAdapter
    private lateinit var binding: FragmentCinemasBinding
    private lateinit var room: ProjectRoom


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_cinemas, container, false)

        adapter = CinemasAdapter(::onClick)

        binding = FragmentCinemasBinding.bind(view)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        room = ProjectRoom(
            DatabaseApp.getInstance(requireContext()).cinemasDao(),
            DatabaseApp.getInstance(requireContext()).filmesDao(),
            DatabaseApp.getInstance(requireContext()).registoDao()
        )

        binding.rvCinemas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCinemas.adapter = adapter





        room.getAllCinemas { result ->
            if (result.isSuccess) {


                CoroutineScope(Dispatchers.Main).launch {

                    val cinemas = result.getOrNull()!!

                    binding.cinemasCount.text = cinemas.size.toString()

                    adapter.updateItems(cinemas)
                }
            }
        }
    }

    private fun onClick(cinemaId: Int) {
        NavigationManager.goToCinemaDetails(parentFragmentManager, cinemaId)
    }

}
