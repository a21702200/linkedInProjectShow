package pt.ulusofona.deisi.cm2223.g21702200.algorithms

import kotlin.math.pow

// not used anymore

fun distancia(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {  // NOT USED
    // Convert latitude and longitude from degrees to radians
    val lat1Rad = Math.toRadians(lat1)
    val lon1Rad = Math.toRadians(lon1)
    val lat2Rad = Math.toRadians(lat2)
    val lon2Rad = Math.toRadians(lon2)

    // Radius of the Earth in meters
    val radius = 6371000.0

    // Haversine formula
    val dlon = lon2Rad - lon1Rad
    val dlat = lat2Rad - lat1Rad
    val a = kotlin.math.sin(dlat / 2)
        .pow(2) + kotlin.math.cos(lat1Rad) * kotlin.math.cos(lat2Rad) * kotlin.math.sin(
        dlon / 2
    ).pow(2)
    val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))

    return radius * c
}
