package ru.koolmax.fitopener.presentation.ui.Workout.Chart

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.koolmax.fitopener.data.media.FileRepository
import ru.koolmax.fitopener.data.media.FitItem
import com.github.mikephil.charting.data.Entry

class ChartListViewModel(private val context: Context, private val repository: FileRepository): ViewModel() {
    //private val getFitRecordsUseCase = GetFitRecordsUseCase(FitLibraryImpl)

    val FitRecords = MutableLiveData<List<Pair<FitItem.FitListType, List<Entry>>>>()

    fun GetFitRecords(fit: String) {
        val result = mutableListOf<Pair<FitItem.FitListType, List<Entry>>>()
        var records = repository.getRecords(fit, context)!!.records
        for(type in  FitItem.FitListType.values()) {
            records[type]?.let {
                if(!it.isNullOrEmpty()) {
                    result.add(Pair(type, it))
                }
            }
        }
        FitRecords.value = result
    }
}