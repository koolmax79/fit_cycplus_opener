package ru.koolmax.fitopener.presentation.ui.Workout

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.data.ble.DeviceRepository
import ru.koolmax.fitopener.data.db.FitDatabase
import ru.koolmax.fitopener.data.db.FitRepository
import ru.koolmax.fitopener.data.media.FileRepository
import ru.koolmax.fitopener.presentation.ui.Main.FitViewModel
import ru.koolmax.fitopener.presentation.ui.Main.FitViewModelFactory
import ru.koolmax.fitopener.presentation.ui.Workout.Chart.ChartListFragment
import ru.koolmax.fitopener.presentation.ui.Workout.TotalInfo.TotalInfoFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator

class WorkoutActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProvider(this, FitViewModelFactory(application,
        FileRepository(),
        FitRepository(FitDatabase.invoke(this)),
        DeviceRepository()
    )
    )[FitViewModel::class.java] }
    lateinit private var fit: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_workout)

        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.setIcon(R.drawable.directions_bike)
        supportActionBar?.title = ""

        intent.getStringExtra("fit")?.let { fit = it }

        val adapter = WorkoutPageAdapter(2, supportFragmentManager, lifecycle)
        adapter.onGetFragment = {
            pos ->
            when (pos) {
                0 -> TotalInfoFragment.newInstance(fit)
                1 -> ChartListFragment.newInstance(fit)
                else -> throw Exception("not exists fragment")
            }
        }

        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        viewPager.adapter = adapter
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        TabLayoutMediator(tabLayout, viewPager) {
            tab, position -> tab.text = resources.getStringArray(R.array.workout_tabs)[position]
        }.attach()
        viewModel.setDisplayed(fit)
        //val fab: FloatingActionButton = binding.fab

        //fab.setOnClickListener { view ->
        //    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //        .setAction("Action", null)
        //        .setAnchorView(R.id.fab).show()
        //}
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.workout_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.delete -> {
                Log.i("FitOpener", "delete")
                MaterialAlertDialogBuilder(this,
                    R.style.MaterialAlertDialog_App)
                    .setMessage("Удалить fit")
                    .setNegativeButton("Нет") { dialog, which ->
                        // Respond to negative button press
                    }
                    .setPositiveButton("Да") { dialog, which ->
                        viewModel.delFitSession(fit)
                        finish()
                    }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}