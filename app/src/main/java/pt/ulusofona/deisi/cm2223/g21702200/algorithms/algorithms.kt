package pt.ulusofona.deisi.cm2223.g21702200.algorithms

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.util.Base64
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pt.ulusofona.deisi.cm2223.g21702200.NavigationManager
import pt.ulusofona.deisi.cm2223.g21702200.data.local.ProjectRoom
import pt.ulusofona.deisi.cm2223.g21702200.models.Cinema
import pt.ulusofona.deisi.cm2223.g21702200.models.CinemaDays
import pt.ulusofona.deisi.cm2223.g21702200.models.CinemaOpeningHours
import pt.ulusofona.deisi.cm2223.g21702200.models.CinemaRating
import pt.ulusofona.deisi.cm2223.g21702200.models.RegistrationData
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat


fun calculateMeanRates(ratesList: List<RegistrationData>): Double {
    val n = ratesList.size

    if (n == 0) {
        return 0.0
    }

    var sumOfRates = 0

    for (rates in ratesList) {
        sumOfRates += rates.rate.toIntOrNull()!!
    }


    return sumOfRates / n.toDouble()
}

fun getKeyWithMostValues(map: HashMap<String, Int>): String? {
    var maxKey: String? = null
    var maxValue = Int.MIN_VALUE

    for ((key, value) in map) {
        if (value > maxValue) {
            maxKey = key
            maxValue = value
        }
    }

    return maxKey
}


fun readCinemasJson3(context: Context, onFinished: (Result<List<Cinema>>) -> Unit) {

    CoroutineScope(Dispatchers.IO).launch {

        try {

            val listaCinemas = mutableListOf<Cinema>()

            val inputStream = context.assets.open("cinemas.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val jsonObject = JSONObject(jsonString)

            val cinemasArray: JSONArray = jsonObject.getJSONArray("cinemas")

            for (cinema in 0 until cinemasArray.length()) {
                val cinemaObject = cinemasArray.getJSONObject(cinema)


                //Log.d("JSON_DEBUG", cinemaObject.toString())


                val cinemaId = cinemaObject.getInt("cinema_id")
                val cinemaName = cinemaObject.getString("cinema_name")
                val cinemaProvider = cinemaObject.getString("cinema_provider")


                //var logoUrl: String? = null

                //if (cinemaObject.has("logo_url")) {
                //logoUrl = jsonObject.getString("logo_url")

                //}

                val logoUrl: String? = cinemaObject.optString("logo_url")


                val latitude = cinemaObject.getDouble("latitude")
                val longitude = cinemaObject.getDouble("longitude")
                val address = cinemaObject.getString("address")
                val postcode = cinemaObject.getString("postcode")
                val county = cinemaObject.getString("county")


                val photos = mutableListOf<String>()

                val photosUrls = cinemaObject.optJSONArray("photos")

                if (photosUrls != null) {

                    val photosArray: JSONArray = cinemaObject.getJSONArray("photos")

                    for (photo in 0 until photosArray.length()) {

                        val photoUrl = photosArray.getString(photo)
                        photos.add(photoUrl)


                        //println("uma foto de cada vez -> " + photo)


                    }

                    //println("photos -> " + photos)
                }


                val ratings = mutableListOf<CinemaRating>()

                val ratingsArray: JSONArray = cinemaObject.getJSONArray("ratings")

                for (rating in 0 until ratingsArray.length()) {
                    val ratingObject: JSONObject = ratingsArray.getJSONObject(rating)
                    val category = ratingObject.getString("category")
                    val score = ratingObject.getInt("score")

                    ratings.add(CinemaRating(category, score))
                }

                //println("ratingsarray -> " + ratingsArray)


                val openingHoursObject = cinemaObject.getJSONObject("opening_hours")

                val format = SimpleDateFormat("HH:mm")

                val monday = openingHoursObject.getJSONObject("Monday")
                val mondayOpen = format.parse(monday.getString("open"))
                val mondayClose = format.parse(monday.getString("close"))

                val tuesday = openingHoursObject.getJSONObject("Tuesday")
                val tuesdayOpen = format.parse(tuesday.getString("open"))
                val tuesdayClose = format.parse(tuesday.getString("close"))

                val wednesday = openingHoursObject.getJSONObject("Wednesday")
                val wednesdayOpen = format.parse(wednesday.getString("open"))
                val wednesdayClose = format.parse(wednesday.getString("close"))

                val thursday = openingHoursObject.getJSONObject("Thursday")
                val thursdayOpen = format.parse(thursday.getString("open"))
                val thursdayClose = format.parse(thursday.getString("close"))

                val friday = openingHoursObject.getJSONObject("Friday")
                val fridayOpen = format.parse(friday.getString("open"))
                val fridayClose = format.parse(friday.getString("close"))

                val saturday = openingHoursObject.getJSONObject("Saturday")
                val saturdayOpen = format.parse(saturday.getString("open"))
                val saturdayClose = format.parse(saturday.getString("close"))

                val sunday = openingHoursObject.getJSONObject("Sunday")
                val sundayOpen = format.parse(sunday.getString("open"))
                val sundayClose = format.parse(sunday.getString("close"))


                val cinemaDays = CinemaDays(
                    CinemaOpeningHours(
                        mondayOpen!!.time, mondayClose!!.time
                    ), CinemaOpeningHours(
                        tuesdayOpen!!.time, tuesdayClose!!.time
                    ), CinemaOpeningHours(
                        wednesdayOpen!!.time, wednesdayClose!!.time
                    ), CinemaOpeningHours(
                        thursdayOpen!!.time, thursdayClose!!.time
                    ), CinemaOpeningHours(
                        fridayOpen!!.time, fridayClose!!.time
                    ), CinemaOpeningHours(
                        saturdayOpen!!.time, saturdayClose!!.time
                    ), CinemaOpeningHours(
                        sundayOpen!!.time, sundayClose!!.time
                    )
                )

                val cinemaToInsert = Cinema(
                    cinemaId,
                    cinemaName,
                    cinemaProvider,
                    logoUrl,
                    latitude,
                    longitude,
                    address,
                    postcode,
                    county,
                    photos,
                    ratings,
                    cinemaDays
                )

                listaCinemas.add(cinemaToInsert)

            }

            onFinished(Result.success(listaCinemas))

        } catch (e: JSONException) {
            e.printStackTrace()
            onFinished(Result.failure(Exception("Cinema file not parsed")))
        }
    }
}

fun populateDetailedBySearch(
    context: Context, spokenText: String, room: ProjectRoom, fragmentManager: FragmentManager
) {

    room.getAllRegistrationData { result ->

        val registrations = result.getOrNull()!!

        var registoEncontrado: RegistrationData? = null

        for (registo in registrations) {
            if (registo.name.lowercase() == spokenText.lowercase()) {
                registoEncontrado = registo
            }
        }


        if (registoEncontrado != null) {
            NavigationManager.goToDetails(fragmentManager, registoEncontrado.uuid)
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "Not find", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun encodeImageToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun decodeBase64ToBitmap(encodedImage: String): Bitmap {
    val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)

    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

fun filterList(
    stateList: MutableList<RegistrationData>,
    room: ProjectRoom,
    latitude: Double,
    longitude: Double,
    distanceGiven: Int,
    onFinished: (Result<MutableList<RegistrationData>>) -> Unit
) {

    val listaToUpdate: MutableList<RegistrationData> = mutableListOf()

    room.getAllCinemas { cinemas ->

        if (cinemas.isSuccess) {

            for (registo in stateList) {

                for (cinema in cinemas.getOrNull()!!) {
                    if (cinema.cinema_id == registo.cinemaId) {

                        val latitudeCinema = cinema.latitude
                        val longitudeCinema = cinema.longitude

                        val mylocation = Location("Actual")

                        val dest_location = Location("Cinema")

                        mylocation.latitude = latitude
                        mylocation.longitude = longitude

                        dest_location.latitude = latitudeCinema
                        dest_location.longitude = longitudeCinema

                        val distance: Double =
                            mylocation.distanceTo(dest_location).toDouble() // in meters

                        if (distance <= distanceGiven) {
                            listaToUpdate.add(registo)
                        }

                        //println("A passar por cada registo -> " + registo.name)

                        listaToUpdate.sortedByDescending { it.rate.toIntOrNull() }.toMutableList()

                    }
                }
            }

            onFinished(Result.success(listaToUpdate))
        }
    }
}

fun deleteAllReg(fragment: FragmentManager, room: ProjectRoom, context: Context) {

    val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {

            DialogInterface.BUTTON_POSITIVE -> {

                room.deleteAllReg()

                Toast.makeText(context, "All registry deleted!", Toast.LENGTH_SHORT).show()

                NavigationManager.goToDashboard(fragment)
            }

            DialogInterface.BUTTON_NEGATIVE -> {
                dialog.dismiss()
            }
        }
    }

    val builder: AlertDialog.Builder = AlertDialog.Builder(context)

    builder.setMessage("Do you want to delete all Data ?")
        .setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener)
        .show()
}
