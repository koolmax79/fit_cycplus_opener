package ru.koolmax.fitopener.data

import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.data.media.FitItem

object FitTypeImpl {
    val info = mapOf(
        FitItem.FitListType.ALTITUDE to FitTypeInfo("Высота", R.color.ALTITUDE),
        FitItem.FitListType.GRADE to FitTypeInfo("Градиент", R.color.SPEED),
        FitItem.FitListType.SPEED to FitTypeInfo("Скорость", R.color.SPEED),
        FitItem.FitListType.HEART to FitTypeInfo("ЧСС", R.color.HEART),
        FitItem.FitListType.CADENCE to FitTypeInfo("Каденс", R.color.CADENCE),
        FitItem.FitListType.TEMPERATURE to FitTypeInfo("Температура", R.color.TEMPERATURE),
        FitItem.FitListType.HEART_TIME to FitTypeInfo("ЧСС по времени", R.color.HEART_TIME)
    )
}