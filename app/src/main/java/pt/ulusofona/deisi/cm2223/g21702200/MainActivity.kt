package pt.ulusofona.deisi.cm2223.g21702200


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.projectcm.R
import com.example.projectcm.databinding.ActivityMainBinding
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import pt.ulusofona.deisi.cm2223.g21702200.algorithms.populateDetailedBySearch
import pt.ulusofona.deisi.cm2223.g21702200.algorithms.readCinemasJson3
import pt.ulusofona.deisi.cm2223.g21702200.data.local.DatabaseApp
import pt.ulusofona.deisi.cm2223.g21702200.data.local.ProjectRoom

class MainActivity : AppCompatActivity(), ShakeDetector.OnShakeListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var shakeDetector: ShakeDetector

    private val SPEECH_REQUEST_CODE = 0

    private lateinit var room: ProjectRoom


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        room = ProjectRoom(
            DatabaseApp.getInstance(applicationContext).cinemasDao(),
            DatabaseApp.getInstance(applicationContext).filmesDao(),
            DatabaseApp.getInstance(applicationContext).registoDao()
        )

        readCinemasJson3(applicationContext) { result ->
            if (result.isSuccess) {
                val cines = result.getOrNull()

                room.insertCinemas(cines!!)
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        permissionsBuilder(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO
        ).build().send {}

        if (!screenRotated(savedInstanceState)) {
            NavigationManager.goToDashboard(
                supportFragmentManager
            )
        }
    }

    private fun screenRotated(savedInstanceState: Bundle?): Boolean {
        return savedInstanceState != null
    }


    override fun onStart() {
        super.onStart()

        shakeDetector = ShakeDetector(this)
        shakeDetector.setOnShakeListener(this)

        setSupportActionBar(binding.toolbar)
        setupDrawerMenu()
    }

    private fun onClickNavigationItem(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_dashboard -> NavigationManager.goToDashboard(supportFragmentManager)
            R.id.navigation_pickmovie -> NavigationManager.goToPickMovieDefault(
                supportFragmentManager
            )

            R.id.navigation_list -> NavigationManager.goToListDefault(supportFragmentManager)
            R.id.navigation_map -> NavigationManager.goToNavigationMap(supportFragmentManager)
            R.id.navigation_cinemas -> NavigationManager.goToCinemasInfos(supportFragmentManager)
            R.id.navigation_speech -> displaySpeechRecognizer()
        }
        binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupDrawerMenu() {
        val toggle = ActionBarDrawerToggle(
            this, binding.drawer, binding.toolbar, R.string.drawer_open, R.string.drawer_close
        )

        binding.navDrawer.setNavigationItemSelectedListener {
            onClickNavigationItem(it)
        }
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onResume() {
        super.onResume()
        shakeDetector.start()

    }

    override fun onPause() {
        super.onPause()
        shakeDetector.stop()
    }

    override fun onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawer(GravityCompat.START)
        } else if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("ResourceType")
    override fun onShake() {
        NavigationManager.goToPickMovieDefault(supportFragmentManager)
    }


    private fun displaySpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
        }

        // This starts the activity and populates the intent with the speech text.
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                    results!![0]
                }

            startGameDialogFragment(spokenText!!)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun startGameDialogFragment(spokenText: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Search?").setMessage(spokenText).setPositiveButton("Yes") { dialog, id ->

            populateDetailedBySearch(
                applicationContext, spokenText, room, supportFragmentManager
            )

            dialog.dismiss()
        }.setNegativeButton("No") { dialog, id ->


            dialog.dismiss()
        }.show()
    }
}







