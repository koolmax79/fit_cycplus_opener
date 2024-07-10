package ru.koolmax.fitopener.presentation.ui.Workout.Chart

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.koolmax.fitopener.data.media.FileRepository

class ChartListViewModelFactory(private val context: Context, private val repository: FileRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChartListViewModel(context, repository) as T
    }
}