package ru.koolmax.fitopener.presentation.ui.Main.FitList

import androidx.recyclerview.widget.DiffUtil
import ru.koolmax.fitopener.data.db.FitSessionItem

class FitListDiffUtil(): DiffUtil.ItemCallback<FitSessionItem>() {
    override fun areItemsTheSame(oldItem: FitSessionItem, newItem: FitSessionItem): Boolean {
        return oldItem.fileName == newItem.fileName
    }

    override fun areContentsTheSame(oldItem: FitSessionItem, newItem: FitSessionItem): Boolean {
        return oldItem == newItem
    }
}