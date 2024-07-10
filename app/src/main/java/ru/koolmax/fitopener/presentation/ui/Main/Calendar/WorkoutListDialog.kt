package ru.koolmax.fitopener.presentation.ui.Main.Calendar

import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.domain.IFitFile


class WorkoutListDialog(val list: List<IFitFile>): DialogFragment(R.layout.dialog_workout_list) {
    interface OnDialogResultListener {
        fun onDialogResult(fit: String)
    }

    var resultListener: OnDialogResultListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.session_list)

        //val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, list.map {
        //    it.GetSession()?.let {
        //        "${MeasureUtil.getDateTime(it.startTime)} ${MeasureUtil.getDistance(it.totalDistance).toList().joinToString(" ")}" } } )
        //listView.adapter = adapter
        //listView.setOnItemClickListener(object: AdapterView.OnItemClickListener {
        //    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        //        //resultListener?.onDialogResult(list[p2].GetFile().name)
        //        dialog?.dismiss()
        //    }

        //})
    }
}