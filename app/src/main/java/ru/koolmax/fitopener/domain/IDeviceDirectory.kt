package ru.koolmax.fitopener.domain

import android.net.Uri

interface IDeviceDirectory {
    fun getListUri(): List<Uri>
    fun setDir(path: String)
}