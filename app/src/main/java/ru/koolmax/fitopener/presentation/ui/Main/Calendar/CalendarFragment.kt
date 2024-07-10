package ru.koolmax.fitopener.presentation.ui.Main.Calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener
import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.data.db.FitDatabase
import ru.koolmax.fitopener.data.db.FitRepository
import ru.koolmax.fitopener.presentation.MeasureUtil
import ru.koolmax.fitopener.presentation.setText


class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    private val viewModel by lazy { ViewModelProvider(this, CalendarViewModelFactory(requireActivity().application, FitRepository(FitDatabase.invoke(this.requireContext()))))[CalendarViewModel::class.java] }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarView = view.findViewById<CalendarView>(R.id.view_calendar)
        viewModel.CalendarDayList.observe(viewLifecycleOwner) {
            calendarView.setCalendarDays(it)
        }
        viewModel.GetCalendarDayList()

        val monthListener = object: OnCalendarPageChangeListener {
            override fun onChange() {
                calendarView.currentPageDate.time.let {
                    viewModel.GetStatistics(it.year+1900, it.month+1)
                }
            }
        }

        monthListener.onChange()

        viewModel.GetStatistics(calendarView.currentPageDate.time.year+1900, calendarView.currentPageDate.time.month+1)

        calendarView.setOnPreviousPageChangeListener(monthListener)
        calendarView.setOnForwardPageChangeListener(monthListener)
        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                val list = viewModel.GetFit(calendarDay.calendar.toLocalDate())
                if(list.count()==1) {
                    OpenWorkout(view, list.first().fileName)
                }
                else {
                    //WorkoutListDialog(list).apply { resultListener = object : WorkoutListDialog.OnDialogResultListener {
                    //    override fun onDialogResult(fit: String) {
                    //        OpenWorkout(view, fit)
                    //   }
                    //} }.show(requireActivity().supportFragmentManager, "")
                }
            }
        })

        viewModel.SessionStatistic.observe(viewLifecycleOwner) {
            setText(R.id.view_training_count, it.count.toString())
            setText(R.id.view_total_distance, R.id.view_total_distance_measurement, MeasureUtil.getDistance(it.totalDistance))
            setText(R.id.view_total_ascent, R.id.view_total_ascent_measurement, MeasureUtil.getDistance(it.totalAscent))
            setText(R.id.view_total_elapsed_time, MeasureUtil.getDuration(it.totalElapsedTime))
            setText(R.id.view_total_moving_time, MeasureUtil.getDuration(it.totalMovingTime))
        }
    }

    private fun OpenWorkout(view: View, fit: String) {
        val bundle = Bundle()
        bundle.putString("fit", fit)
        Navigation.findNavController(view).navigate(R.id.navigation_workout, bundle)
    }
}