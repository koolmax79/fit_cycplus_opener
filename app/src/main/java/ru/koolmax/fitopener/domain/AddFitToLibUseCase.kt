package ru.koolmax.fitopener.domain

import android.net.Uri

class AddFitToLibUseCase(private val fitLibrary : IFitLibrary) {
    fun execute(uri: Uri): IFitFile? {
        return fitLibrary.AddFit(uri)
    }
}