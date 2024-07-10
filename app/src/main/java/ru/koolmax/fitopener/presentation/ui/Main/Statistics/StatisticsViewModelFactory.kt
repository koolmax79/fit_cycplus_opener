package ru.koolmax.fitopener.presentation.ui.Main.Statistics

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.koolmax.fitopener.data.db.FitRepository

class StatisticsViewModelFactory(private val context: Context, private val repository: FitRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatisticsViewModel(context, repository) as T
    }
}