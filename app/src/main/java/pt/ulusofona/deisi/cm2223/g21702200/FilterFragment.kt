package pt.ulusofona.deisi.cm2223.g21702200


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projectcm.R
import com.example.projectcm.databinding.FragmentFilterBinding

class FilterFragment : Fragment() {

    private lateinit var binding: FragmentFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_filter, container, false)
        binding = FragmentFilterBinding.bind(view)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.filterButton.setOnClickListener {

            val filterText = binding.editTextBox.editText?.text.toString()

            val checkbox1Checked = binding.checkbox1.isChecked
            val checkbox2Checked = binding.checkbox2.isChecked

            NavigationManager.goToList(
                parentFragmentManager, filterText, checkbox1Checked, checkbox2Checked, true
            )
        }
    }
}