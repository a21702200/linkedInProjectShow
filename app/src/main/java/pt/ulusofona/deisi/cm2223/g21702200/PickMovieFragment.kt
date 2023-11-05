package pt.ulusofona.deisi.cm2223.g21702200

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectcm.R
import com.example.projectcm.databinding.FragmentPickMovieBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21702200.data.ProjectRepository

class PickMovieFragment : Fragment() {

    private lateinit var binding: FragmentPickMovieBinding

    private lateinit var model: ProjectRepository

    private lateinit var adapter: PickMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_pick_movie, container, false)

        adapter = PickMovieAdapter(::onClick)

        binding = FragmentPickMovieBinding.bind(view)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        if (!Connectivity.isOnline(context!!)) {
            Toast.makeText(context, "Device is Offline!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Device is Online!", Toast.LENGTH_SHORT).show()
        }

        model = ProjectRepository.getInstance()

        binding.submitButton.setOnClickListener {
            saveInput()
        }

        binding.rvPick.layoutManager = LinearLayoutManager(requireContext())

        binding.rvPick.adapter = adapter
    }


    private fun showPopup() {

        CoroutineScope(Dispatchers.Main).launch {

            val alertDialog = AlertDialog.Builder(requireContext()).setTitle("Movie not found")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }.create()

            alertDialog.show()
        }
    }

    private fun saveInput() {

        val input = binding.search.editText?.text.toString()

        model.getFilmesByName(input) { result ->

            if (result.isSuccess) {
                val filmes = result.getOrNull()

                //println("resposta do filmes by name -> " + filmes)

                CoroutineScope(Dispatchers.Main).launch {

                    adapter.updateItems(filmes!!)
                }

            } else {

                showPopup()

                CoroutineScope(Dispatchers.Main).launch {

                    adapter.updateItems(emptyList())
                }
            }
        }
    }

    private fun onClick(titulo: String, filmeId: String) {

        NavigationManager.goToRegister(parentFragmentManager, titulo, filmeId)
    }
}