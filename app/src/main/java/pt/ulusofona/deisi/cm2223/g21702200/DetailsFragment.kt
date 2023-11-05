package pt.ulusofona.deisi.cm2223.g21702200

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectcm.R
import com.example.projectcm.databinding.FragmentDetailsBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21702200.data.local.DatabaseApp
import pt.ulusofona.deisi.cm2223.g21702200.data.local.ProjectRoom

private const val ARG_REGISTRATION_UUID = "ARG_REGISTRATION_UUID"

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var room: ProjectRoom

    private lateinit var imdbButton: Button

    private lateinit var cinemaButton: Button

    private var filmeUuid: String? = null


    private lateinit var adapter: PhotoRegistrationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            filmeUuid = it.getString(ARG_REGISTRATION_UUID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        binding = FragmentDetailsBinding.bind(view)

        return binding.root
    }


    override fun onStart() {
        super.onStart()

        room = ProjectRoom(
            DatabaseApp.getInstance(requireContext()).cinemasDao(),
            DatabaseApp.getInstance(requireContext()).filmesDao(),
            DatabaseApp.getInstance(requireContext()).registoDao()
        )

        imdbButton = binding.imdbLinkButton

        cinemaButton = binding.cinemaButton

        adapter = PhotoRegistrationAdapter()

        binding.photosList.layoutManager = LinearLayoutManager(requireContext())

        binding.photosList.adapter = adapter


        //println("filmeuuid no detail ->" + filmeUuid)

        filmeUuid?.let { uuid ->

            room.getRegistrationById(uuid) { result ->
                if (result.isSuccess) {
                    val registo = result.getOrNull()

                    //println("filme data a ser imprimido: " + registo)
                    CoroutineScope(Dispatchers.Main).launch {
                        if (registo != null) {
                            binding.movieNameTextview.text = registo.name
                            binding.dateSeenTextview.text = formatDate.format(registo.date)
                            binding.rating.text = registo.rate

                            if (registo.observacoes != "") {
                                binding.observationsTextview.text = registo.observacoes
                            } else {
                                binding.observationsTextview.text = "N/A"
                            }

                            cinemaButton.setOnClickListener {
                                NavigationManager.goToCinemaDetails(
                                    parentFragmentManager, registo.cinemaId
                                )
                            }

                            cinemaButton.text = registo.cinema

                            if (registo.imageUris!!.isNotEmpty()) {
                                binding.photosCount.text = registo.imageUris.size.toString()

                                adapter.updateItems(registo.imageUris)
                            } else {
                                binding.photosCount.text = "N/A"
                            }
                        }
                    }

                    room.getFilmeById(registo!!.filmeId) { resultado ->
                        if (resultado.isSuccess) {

                            val filme = resultado.getOrNull()

                            CoroutineScope(Dispatchers.Main).launch {
                                if (filme != null) {
                                    binding.imdbRatingTextview.text = filme.avaliacao
                                    binding.movieSynopsisTextview.text = filme.sinopse
                                    binding.movieGenreTextview.text = filme.genero
                                    binding.movieLaunchDateTextview.text = filme.lancamento
                                    binding.languagesTextview.text = filme.languages
                                    binding.actorsTextview.text = filme.actors
                                    binding.directorTextview.text = filme.director
                                    binding.awardsTextview.text = filme.awards
                                    binding.imdbVotesTextview.text = filme.imdbVotes

                                    val url = siteImdb + filme.id + "/"

                                    imdbButton.setOnClickListener {
                                        openLink(url)
                                    }
                                }


                                Picasso.get().load(filme!!.cartaz).into(binding.movieImageImageview)

                                //println(registo.imageUris)
                            }
                        }
                    }
                }
            }
        }

        Toast.makeText(requireContext(), "Page info loaded successfully!", Toast.LENGTH_SHORT)
            .show()

    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(uuid: String) = DetailsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_REGISTRATION_UUID, uuid)
            }
        }
    }
}
