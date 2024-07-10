package ru.koolmax.fitopener.data

interface OnProgressListener {
    fun onBegin(max: Int)
    fun onStep(count: Int)
    fun onFinish()
}