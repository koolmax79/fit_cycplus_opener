package ru.koolmax.fitopener.presentation.ui.Main.Statistics

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.koolmax.fitopener.data.db.FitConverter
import ru.koolmax.fitopener.data.db.FitRepository
import ru.koolmax.fitopener.data.db.FitSessionItem
import ru.koolmax.fitopener.data.db.FitStatisticItem
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

enum class ChartType {
    NONE, DISTANCE, ASCENT, MOVING_TIME, HEART_RATE, SPEED;
}

class StatisticsViewModel(private val context: Context, private val repository: FitRepository) : ViewModel() {
    val RangeYear = MutableLiveData<Pair<Int, Int>>()
    val EntryList = MutableLiveData<List<BarEntry>>()
    val SessionStatistic = MutableLiveData<FitStatisticItem>()
    val FitSessionList = MutableLiveData<List<FitSessionItem>>()

    init {
        val year = LocalDate.now().year
        RangeYear.value = Pair(year, year)
    }

    //fun chartTypeToFitType() {
    //    FitListType
    //}

    fun getRangeYear() {
        viewModelScope.launch {
            val begin = repository.minStartTime()?.year
            val end = repository.maxStartTime()?.year
            if(begin !=null && end!=null)
                RangeYear.value = Pair(begin, end)
        }
    }

    fun getChartData(type: ChartType) {
        val list = mutableListOf<BarEntry>()
        when(type) {
            ChartType.DISTANCE -> {
                FitSessionList.value?.let {
                    it.forEachIndexed { index, it ->
                        list.add(BarEntry(
                            index.toFloat(),
                            (it.totalDistance ?: 0).toFloat() ).apply { data = it.fileName })
                    }
                }
            }

            ChartType.ASCENT -> {
                FitSessionList.value?.let {
                    it.forEachIndexed { index, it ->
                        list.add(BarEntry(
                            index.toFloat(),
                            (it.totalAscent ?: 0).toFloat() ).apply { data = it.fileName })
                    }
                }
            }

            ChartType.MOVING_TIME -> {
                FitSessionList.value?.let {
                    it.forEachIndexed { index, it ->
                        list.add(BarEntry(
                            index.toFloat(),
                            (it.totalMovingTime ?: 0).toFloat() ).apply { data = it.fileName })
                    }
                }
            }

            ChartType.HEART_RATE -> {
                FitSessionList.value?.let {
                    it.forEachIndexed { index, it ->
                        list.add(BarEntry(
                            index.toFloat(),
                            (it.avgHeartRate ?: 0).toFloat() ).apply { data = it.fileName })
                    }
                }
            }

            ChartType.SPEED -> {
                FitSessionList.value?.let {
                    it.forEachIndexed { index, it ->
                        list.add(BarEntry(
                            index.toFloat(),
                            (it.avgSpeed ?: 0).toFloat() ).apply { data = it.fileName })
                    }
                }
            }
            ChartType.NONE -> {
            }
        }
        EntryList.value = list
    }

    fun getStatistic(year: Int) {
        viewModelScope.launch {
            val begin = LocalDateTime.of(year, 1, 1, 0, 0)
            val end = begin.plusYears (1)
            FitSessionList.value = repository.allByInterval(FitConverter().toLocalDateTime(begin), FitConverter().toLocalDateTime(end))
            SessionStatistic.value = repository.getStatistic(FitConverter().toLocalDateTime(begin), FitConverter().toLocalDateTime(end))
        }
    }
}