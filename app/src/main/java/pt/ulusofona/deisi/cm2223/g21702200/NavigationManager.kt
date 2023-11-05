package pt.ulusofona.deisi.cm2223.g21702200

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.projectcm.R


object NavigationManager {

    private fun placeFragment(fm: FragmentManager, fragment: Fragment) {
        val transition = fm.beginTransaction()
        transition.replace(R.id.fragment_container, fragment)
        //transition.addToBackStack(null)
        transition.commit()
    }

    private fun placeFragmentHorizontal(fm: FragmentManager, fragment: Fragment) { // if need
        val transition = fm.beginTransaction()
        transition.replace(R.id.fragment_container, fragment)
        transition.addToBackStack(null)
        transition.commit()
    }

    fun goToListDefault(fm: FragmentManager) {
        placeFragment(fm, ListFragment())
    }

    fun goToDashboard(fm: FragmentManager) {
        placeFragment(fm, DashboardFragment())
    }

    fun goToNavigationMap(fm: FragmentManager) {
        placeFragment(fm, MapFragment())
    }

    fun goToCinemasInfos(fm: FragmentManager) {
        placeFragment(fm, CinemasInfos())
    }

    fun goToPickMovieDefault(fm: FragmentManager) {
        placeFragment(fm, PickMovieFragment())
    }

    fun goToCinemaDetails(fm: FragmentManager, cinemaId: Int) {

        placeFragment(fm, CinemaDetailsFragment.newInstance(cinemaId))
    }

    fun goToSearch(fm: FragmentManager) {
        placeFragment(fm, PickMovieFragment())
    }

    fun goToDetails(fm: FragmentManager, registoUuid: String) {
        placeFragment(fm, DetailsFragment.newInstance(registoUuid))
    }

    fun goTofilter(fm: FragmentManager) {
        placeFragmentHorizontal(fm, FilterFragment())
    }

    fun goToList(
        fm: FragmentManager,
        filterText: String,
        checkboxChecked500: Boolean,
        checkboxChecked1000: Boolean,
        voltei: Boolean
    ) {
        placeFragment(
            fm,
            ListFragment.newInstance(filterText, checkboxChecked500, checkboxChecked1000, voltei)
        )
    }

    fun goToRegister(fm: FragmentManager, titulo: String, filmeId: String) {
        placeFragment(fm, RegisterFragment.newInstance(titulo, filmeId))
    }
}