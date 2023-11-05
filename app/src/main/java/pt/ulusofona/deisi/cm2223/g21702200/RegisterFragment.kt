package pt.ulusofona.deisi.cm2223.g21702200


import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projectcm.R
import com.example.projectcm.databinding.FragmentRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21702200.algorithms.encodeImageToBase64
import pt.ulusofona.deisi.cm2223.g21702200.data.ProjectRepository
import pt.ulusofona.deisi.cm2223.g21702200.data.local.DatabaseApp
import pt.ulusofona.deisi.cm2223.g21702200.data.local.ProjectRoom
import pt.ulusofona.deisi.cm2223.g21702200.models.Cinema
import pt.ulusofona.deisi.cm2223.g21702200.models.Photo
import pt.ulusofona.deisi.cm2223.g21702200.models.RegistrationData
import java.sql.Timestamp
import java.util.Calendar
import java.util.UUID


private const val ARG_TITLE = "ARG_TITLE"
private const val ARG_ID = "ARG_ID"


class RegisterFragment : Fragment(), OnLocationChangedListener {

    private lateinit var room: ProjectRoom

    private lateinit var cinemas: MutableList<Cinema>

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var model: ProjectRepository

    private var onlychangeLocationOneTime: Boolean = true

    private var title: String? = null

    private var id: String? = null

    private var imageUris: MutableList<Uri> = mutableListOf()

    private var encodedPhotos: MutableList<Photo> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            id = it.getString(ARG_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_register, container, false)

        binding = FragmentRegisterBinding.bind(view)

        return binding.root
    }


    override fun onStart() {
        super.onStart()

        model = ProjectRepository.getInstance()

        FusedLocation.registerListener(this)

        room = ProjectRoom(
            DatabaseApp.getInstance(requireContext()).cinemasDao(),
            DatabaseApp.getInstance(requireContext()).filmesDao(),
            DatabaseApp.getInstance(requireContext()).registoDao()
        )

        binding.movieTitle.text = title

        if (onlychangeLocationOneTime) {
            room.getAllCinemas { result ->

                if (result.isSuccess) {

                    val allCinemas = result.getOrNull()!!.toMutableList()

                    //println("cinemas inicialmente -> " + allCinemas)

                    cinemas = allCinemas

                    initializeSpinner()
                }
            }
        }

        val datePicker = binding.datePicker1

        val calendarDate = Calendar.getInstance()

        datePicker.init(
            calendarDate.get(Calendar.YEAR),
            calendarDate.get(Calendar.MONTH),
            calendarDate.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            //val msg = "You Selected: $day/$month/$year"
            //Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }


        val ratesForSpinner = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")

        val spinnerRate = binding.spinner2

        // if (spinner2 != null) {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ratesForSpinner)

        spinnerRate.adapter = adapter

        spinnerRate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                //Toast.makeText(
                // requireContext(),
                // getString(R.string.selected_item) + " " + "" + rate[position],
                // Toast.LENGTH_SHORT
                // ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        //}


        //val btnGallery = binding.btnGallery
        //btnGallery.setOnClickListener {
        //dispatchPickImageIntent()
        //}


        model.getFilmeById(id!!) {}


        val btnSave = binding.registerButton


        btnSave.setOnClickListener {

            val cinema = binding.spinnerCinemaLocation.selectedItem.toString()

            room.getCinemaObjectByName(cinema) { result ->
                if (result.isSuccess) {

                    val cinemaObject = result.getOrNull()

                    if (cinemaObject != null) {

                        val idCinema = cinemaObject.cinema_id

                        val rate = binding.spinner2.selectedItem.toString()

                        val datePickerFromBinding = binding.datePicker1

                        calendarDate.set(Calendar.DAY_OF_MONTH, datePickerFromBinding.dayOfMonth)
                        calendarDate.set(Calendar.MONTH, datePickerFromBinding.month)
                        calendarDate.set(Calendar.YEAR, datePickerFromBinding.year)

                        val dataFormatada = Timestamp(calendarDate.timeInMillis)

                        //val observacoes = binding.movieObservations.text.toString()

                        val observacoes = binding.movieObservations.editText?.text.toString()

                        val registo = RegistrationData(
                            UUID.randomUUID().toString(),
                            id!!,
                            idCinema,
                            title!!,
                            cinemaObject.cinema_name,
                            rate,
                            dataFormatada.time,
                            encodedPhotos,
                            observacoes,
                            cinemaObject.postcode
                        )

                        //println("IMAGE URIS -> " + imageUris)
                        //println("ENCODED PHOTOS  -> " + encodedPhotos)


                        room.insertRegistrationData(registo) {
                            //println("Registo Salvo")

                            CoroutineScope(Dispatchers.Main).launch {

                                Toast.makeText(
                                    requireContext(), "Data saved successfully", Toast.LENGTH_SHORT
                                ).show()

                                NavigationManager.goToSearch(
                                    parentFragmentManager
                                )
                            }
                        }

                    } else {
                        println("Erro cinema nao encontrado")
                    }
                }
            }
        }


        println("Voltei a refazer o start")

        //val photo = binding.Photo

        val pickImageButton: Button = binding.registerPhotos

        pickImageButton.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }

        if (imageUris.isNotEmpty()) {

            //println("image Uris -> " + imageUris)

            for (uri in imageUris) {

                val inputStream = context!!.contentResolver.openInputStream(uri)

                val bitmap = BitmapFactory.decodeStream(inputStream)

                val encodedImage = encodeImageToBase64(bitmap)

                //println("encoded image -> " + encodedImage)

                //val photo = ContactsContract.Contacts.Photo(
                //imageUri = uri.toString(),
                //timestamp = timestamp
                //)

                val timestamp = System.currentTimeMillis()

                encodedPhotos.add(Photo(encodedImage, timestamp))

                //println("encoded photo adding -> " + Photo(encodedImage,timestamp))


                //val bitmapDecoded = decodeBase64ToBitmap(encodedImage)

                //photo.setImageBitmap(bitmapDecoded)
            }
        } else {
            println("No uris")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {

                if (data.clipData != null) {

                    val clipData = data.clipData
                    val itemCount = data.clipData!!.itemCount

                    for (i in 0 until itemCount) {
                        val uri = clipData!!.getItemAt(i).uri

                        imageUris.add(uri)

                        //println("Adding image uris -> " + imageUris)

                    }
                } else if (data.data != null) {

                    // Single image selected

                    val uri = data.data!!
                    imageUris.add(uri)
                }
            }
        }
    }


    private fun initializeSpinner() {
        //println("vim ao spinner initializer")

        val spinner = binding.spinnerCinemaLocation

        val cinemaNames = cinemas.map { it.cinema_name }.toMutableList()

        //println("cinemas ->" + cinemaNames)

        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, cinemaNames
        )

        CoroutineScope(Dispatchers.Main).launch {

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinner.adapter = adapter
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        FusedLocation.unregisterListener()
    }


    override fun onLocationChanged(latitude: Double, longitude: Double) {
        changespinner(latitude, longitude)
    }


    private fun changespinner(latitude: Double, longitude: Double) {
        if (onlychangeLocationOneTime) {

            var menorDistancia: Double = Double.MAX_VALUE

            var cinemaMenorDist: Cinema? = null

            for (cinema in cinemas) {

                val latitudeCinema = cinema.latitude
                val longitudeCinema = cinema.longitude

                val mylocation = Location("Actual")

                val dest_location = Location("Cinema")

                mylocation.latitude = latitude
                mylocation.longitude = longitude

                dest_location.latitude = latitudeCinema
                dest_location.longitude = longitudeCinema

                val distance: Double = mylocation.distanceTo(dest_location).toDouble()

                //println("cinema latitude: " + cinema.latitude + "cinema longitude: " + cinema.longitude)
                //println("cinema latitude atual: " + latitude + "cinema longitude atual: " + longitude)

                //println("cinema: " + cinema.cinema_name + " Distancia: " + distance)

                if (distance < menorDistancia) {

                    cinemaMenorDist = cinema

                    menorDistancia = distance
                }
            }

            //println("cinema de menor distancia" + cinemaMenorDist)

            //println("cinemas a ordenar -> " + cinemas)

            cinemas.remove(cinemaMenorDist) // Remove the object from the list

            if (cinemaMenorDist != null) {
                cinemas.add(0, cinemaMenorDist)
            }

            //println("cinemas depois de ordenado -> " + cinemas)

            CoroutineScope(Dispatchers.Main).launch {
                initializeSpinner()
            }

            onlychangeLocationOneTime = false
        }
    }

    companion object {

        private const val PICK_IMAGES_REQUEST = 1

        @JvmStatic
        fun newInstance(title: String, id: String) = RegisterFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_ID, id)
            }
        }
    }
}

