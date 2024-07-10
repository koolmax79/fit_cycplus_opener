package ru.koolmax.fitopener.presentation.ui.Main.Calendar

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applandeo.materialcalendarview.CalendarDay
import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.data.db.FitConverter
import ru.koolmax.fitopener.data.db.FitRepository
import ru.koolmax.fitopener.data.db.FitSessionItem
import ru.koolmax.fitopener.data.db.FitStatisticItem
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar

fun LocalDateTime.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    with(this) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthValue - 1)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)
        calendar.set(Calendar.MILLISECOND, nano / 1000000)
    }
    return calendar
}

fun Calendar.toLocalDate(): LocalDate {
    return LocalDate.of(this.time.year+1900, this.time.month+1, this.time.date)
}

class CalendarViewModel(private val context: Context, private val repository: FitRepository): ViewModel() {
    val CalendarDayList = MutableLiveData<ArrayList<CalendarDay>>()
    val SessionStatistic = MutableLiveData<FitStatisticItem>()
    private var monthSessionList = listOf<FitSessionItem>()

    fun GetCalendarDayList() {
        viewModelScope.launch {
            val list = ArrayList<CalendarDay>()
            repository.all().forEach {
                val day = CalendarDay(it.startTime!!.toCalendar())
                day.backgroundDrawable = context.resources.getDrawable(R.drawable.calendar_training_day, null)
                list.add(day)
            }
            CalendarDayList.value = list
        }
    }

    fun GetStatistics(year: Int, month: Int) {
        viewModelScope.launch {
            val begin = LocalDateTime.of(year, month, 1, 0, 0)
            val end = begin.plusMonths(1)
            monthSessionList =
                repository.allByInterval(FitConverter().toLocalDateTime(begin), FitConverter().toLocalDateTime(end))
            SessionStatistic.value =
                repository.getStatistic(FitConverter().toLocalDateTime(begin), FitConverter().toLocalDateTime(end))
        }
    }

    fun GetFit(date: LocalDate) = monthSessionList.filter { it.startTime?.dayOfMonth==date.dayOfMonth }
}