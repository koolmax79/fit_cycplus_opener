package ru.koolmax.fitopener.domain

import ru.koolmax.fitopener.data.db.FitSessionItem

interface IFitFile {
    fun GetSession(): FitSessionItem?
    //fun GetRecords(): FitRecords?
    //fun GetHeartZone(iHeartZone: IHeartZone): List<Pair<HeartZoneInfo, UInt>>
    //fun GetFile(): File

    //fun getListTypes(): Set<FitDataImpl.FitListType>
    //fun getAvg(fitListType: FitDataImpl.FitListType?): Float
    //fun getMax(fitListType: FitDataImpl.FitListType?): Float
    //fun getMeasure(fitListType: FitDataImpl.FitListType?): String?
    //fun getMin(fitListType: FitDataImpl.FitListType?): Float

}