package pt.ulusofona.deisi.cm2223.g21702200

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projectcm.R
import com.example.projectcm.databinding.FragmentCinemaDetailsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21702200.data.local.DatabaseApp
import pt.ulusofona.deisi.cm2223.g21702200.data.local.ProjectRoom
import java.sql.Date

private const val ARG_CINEMA_ID = "ARG_CINEMA_ID"

class CinemaDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCinemaDetailsBinding
    private lateinit var room: ProjectRoom

    private var cinemaId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cinemaId = it.getInt(ARG_CINEMA_ID)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_cinema_details, container, false)

        binding = FragmentCinemaDetailsBinding.bind(view)

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

        room.getCinemaObjectById(cinemaId!!) { result ->
            if (result.isSuccess) {
                val cinemaData = result.getOrNull()!!

                //println("Cinema object ->  " + cinemaData)

                CoroutineScope(Dispatchers.Main).launch {

                    binding.limpezaSala.text = cinemaData.ratings[0].score.toString() + "/10"
                    binding.qualidadeSom.text = cinemaData.ratings[1].score.toString() + "/10"
                    binding.conforto.text = cinemaData.ratings[2].score.toString() + "/10"
                    binding.qualidadeImagem.text = cinemaData.ratings[3].score.toString() + "/10"

                    binding.cinemaProvider.text = cinemaData.cinema_provider

                    binding.cinemaName.text = cinemaData.cinema_name

                    binding.adressCinema.text =
                        cinemaData.address + "\n" + cinemaData.postcode + " " + cinemaData.county

                    binding.mondayOpen.text =
                        formatHours.format(Date(cinemaData.opening_hours.Monday.open))
                    binding.mondayClose.text =
                        formatHours.format(Date(cinemaData.opening_hours.Monday.close))
                    binding.tuesdayOpen.text =
                        formatHours.format(Date(cinemaData.opening_hours.Tuesday.open))
                    binding.tuesdayClose.text =
                        formatHours.format(Date(cinemaData.opening_hours.Tuesday.close))
                    binding.wednesdayOpen.text =
                        formatHours.format(Date(cinemaData.opening_hours.Wednesday.open))
                    binding.wednesdayClose.text =
                        formatHours.format(Date(cinemaData.opening_hours.Wednesday.close))
                    binding.thursdayOpen.text =
                        formatHours.format(Date(cinemaData.opening_hours.Thursday.open))
                    binding.thursdayClose.text =
                        formatHours.format(Date(cinemaData.opening_hours.Thursday.close))
                    binding.fridayOpen.text =
                        formatHours.format(Date(cinemaData.opening_hours.Friday.open))
                    binding.fridayClose.text =
                        formatHours.format(Date(cinemaData.opening_hours.Friday.close))
                    binding.saturdayOpen.text =
                        formatHours.format(Date(cinemaData.opening_hours.Saturday.open))
                    binding.saturdayClose.text =
                        formatHours.format(Date(cinemaData.opening_hours.Saturday.close))
                    binding.sundayOpen.text =
                        formatHours.format(Date(cinemaData.opening_hours.Sunday.open))
                    binding.sundayClose.text =
                        formatHours.format(Date(cinemaData.opening_hours.Sunday.close))
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(cinemaId: Int) = CinemaDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_CINEMA_ID, cinemaId)
            }
        }
    }
}


