package ru.koolmax.fitopener.presentation.ui.Main.Profile

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.preference.Preference
import java.text.SimpleDateFormat
import java.util.Calendar

class DatePreference(context: Context, attrs: AttributeSet?) : Preference(context, attrs) {
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy")

    override fun onClick() {
        val c = Calendar.getInstance()
        c.timeInMillis = getPersistedLong(System.currentTimeMillis())
        val datePickerDialog = DatePickerDialog(context,
            { view, year, month, dayOfMonth ->
                val selectedDate: Calendar = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val ms = selectedDate.getTimeInMillis()
                persistLong(ms)
                setSummary(dateFormat.format(ms))
                notifyChanged()
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
        super.onClick()
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any? {
        return dateFormat.format(System.currentTimeMillis())
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        val date = getPersistedLong(System.currentTimeMillis())
        setSummary(dateFormat.format(date))
    }
}