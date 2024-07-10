package ru.koolmax.fitopener.data.db

class FitRepository(private val db: FitDatabase) {
    suspend fun add(fit: FitSessionItem) = db.sessions().add(fit)
    suspend fun delete(fileName: String) = db.sessions().delete(fileName)
    suspend fun all() = db.sessions().all()
    suspend fun allByInterval(start: Int, end: Int) = db.sessions().allByInterval(start, end)
    suspend fun getSession(fileName: String) = db.sessions().getSession(fileName)

    suspend fun minStartTime() = db.sessions().minStartTime()?.let { return@let FitConverter().fromLocalDateTime(it) }
    suspend fun maxStartTime() = db.sessions().maxStartTime()?.let { return@let FitConverter().fromLocalDateTime(it) }

    suspend fun getStatistic(start: Int, end: Int) = db.sessions().getStatistic(start, end)

    suspend fun update(session: FitSessionItem) = db.sessions().update(session)

    //fun allByIntervalSync(start: Int, end: Int) = db.sessions().allByIntervalSync(start, end)

    //fun add(uri: Uri, context: Context) {

    //}
}
