package ru.koolmax.fitopener.domain

abstract class ParsableUuid(val uuid: String) {

    abstract fun commands(param: Any? = null): Array<String>
    abstract fun getReadStringFromBytes(byteArray: ByteArray): String

}
