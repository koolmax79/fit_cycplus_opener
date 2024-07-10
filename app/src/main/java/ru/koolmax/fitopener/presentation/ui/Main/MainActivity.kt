package ru.koolmax.fitopener.presentation.ui.Main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.data.HeartZoneImpl
import ru.koolmax.fitopener.data.ble.DeviceRepository
import ru.koolmax.fitopener.data.db.FitDatabase
import ru.koolmax.fitopener.data.db.FitRepository
import ru.koolmax.fitopener.data.media.FileRepository
import ru.koolmax.fitopener.presentation.PermissionUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProvider(this, FitViewModelFactory(application,
        FileRepository(),
        FitRepository(FitDatabase.invoke(this)),
        DeviceRepository()
    )
    )[FitViewModel::class.java] }
    private var newUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //HeartZoneImpl.SetBirthDay(LocalDate.of(1979, 10, 5))

        with(PreferenceManager.getDefaultSharedPreferences(this)) {
            val c = Calendar.getInstance()
            val ms = getLong("dateBirth", 0)
            if(ms!=0L)
                c.timeInMillis = ms
            HeartZoneImpl.SetBirthDay(LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)))
        }

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        val navController = (supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment).navController
        findViewById<BottomNavigationView>(R.id.view_bottom_navigation).setupWithNavController(navController)

        PermissionUtil.checkAndRequest(this, permissionsList)

        if (intent?.action == Intent.ACTION_VIEW) {
            (intent.data as Uri)?.let {
                readFile(intent.data!!)
            }
        }
    }

    fun readFile(uri: Uri) {
        viewModel.addFitToLib(newUri!!)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private val permissionsList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}
