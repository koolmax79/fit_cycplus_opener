package ru.koolmax.fitopener.domain

import android.content.Context
import android.net.Uri

interface IFitLibrary {
    fun init(context: Context)
    //fun GetFitList(): List<IFitFile>
    //fun GetFit(name: String): IFitFile
    fun AddFit(uri: Uri): IFitFile?
    //fun DelFit()
}