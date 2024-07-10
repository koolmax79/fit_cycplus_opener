package ru.koolmax.fitopener.presentation.ui.Main.Calendar

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.koolmax.fitopener.data.db.FitRepository

class CalendarViewModelFactory(private val application: Application, private val repository: FitRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CalendarViewModel(application, repository) as T
    }
}