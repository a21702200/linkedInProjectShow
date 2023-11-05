package pt.ulusofona.deisi.cm2223.g21702200

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

const val APP_API_BASE_URL = "https://www.omdbapi.com"
const val APP_API_TOKEN = "bd1f7421"

@SuppressLint("SimpleDateFormat")
val formatDate = SimpleDateFormat("dd/MM/yyyy")

@SuppressLint("SimpleDateFormat")
val formatHours = SimpleDateFormat("HH:mm")

const val siteImdb = "https://www.imdb.com/title/"