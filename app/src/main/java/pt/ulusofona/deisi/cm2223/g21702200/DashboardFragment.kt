package pt.ulusofona.deisi.cm2223.g21702200

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.projectcm.R
import com.example.projectcm.databinding.FragmentDashboardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21702200.algorithms.calculateMeanRates
import pt.ulusofona.deisi.cm2223.g21702200.algorithms.deleteAllReg
import pt.ulusofona.deisi.cm2223.g21702200.algorithms.getKeyWithMostValues
import pt.ulusofona.deisi.cm2223.g21702200.data.local.DatabaseApp
import pt.ulusofona.deisi.cm2223.g21702200.data.local.ProjectRoom

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var room: ProjectRoom

    private lateinit var deleteALl: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        binding = FragmentDashboardBinding.bind(view)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()

        room = ProjectRoom(
            DatabaseApp.getInstance(requireContext()).cinemasDao(),
            DatabaseApp.getInstance(requireContext()).filmesDao(),
            DatabaseApp.getInstance(requireContext()).registoDao()
        )

        room.getAllRegistrationData { result ->
            if (result.isSuccess) {
                //println("result -> " + result)

                val lista = result.getOrElse { emptyList() }

                //println("lista -> " + lista)

                if (lista.isNotEmpty()) {
                    //println("lista antes -> " + lista)

                    val cinemasMoreUsed: HashMap<String, Int> = HashMap()

                    for (cinemaKey in lista) {
                        if (cinemasMoreUsed.containsKey(cinemaKey.cinema)) {
                            val currentCount = cinemasMoreUsed[cinemaKey.cinema]!!

                            cinemasMoreUsed[cinemaKey.cinema] = currentCount + 1
                        } else {

                            cinemasMoreUsed[cinemaKey.cinema] = 1
                        }
                    }

                    val cinemaMaisFrequentado = getKeyWithMostValues(cinemasMoreUsed)

                    //println("cinema mais frequentado -> " + cinemaMaisFrequentado)

                    val meanRates = calculateMeanRates(lista)

                    CoroutineScope(Dispatchers.Main).launch {


                        binding.registrationsNumberTextview.text = lista.size.toString()

                        binding.numberRegistrationsRateMediaTextview.text =
                            String.format("%.2f", meanRates)

                        binding.cinemaTextView.text = cinemaMaisFrequentado

                    }
                } else {

                    CoroutineScope(Dispatchers.Main).launch {

                        binding.registrationsNumberTextview.text = "N/A"

                        binding.numberRegistrationsRateMediaTextview.text = "N/A"

                        binding.cinemaTextView.text = "N/A"
                    }
                }
            }
        }

        binding.featuredMovieTitle.text = "Black Panther"
        binding.featuredMoviePoster.setImageResource(R.drawable.peakpx)


        deleteALl = binding.deleteAllReg


        deleteALl.setOnClickListener {
            deleteAllReg(parentFragmentManager, room, context!!)
        }
    }
}


