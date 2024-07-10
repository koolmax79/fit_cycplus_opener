package ru.koolmax.fitopener.presentation.ui.Workout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class WorkoutPageAdapter(private val count: Int, fm: FragmentManager, livecycle: Lifecycle): FragmentStateAdapter(fm, livecycle) {
    var onGetFragment: ((Int) -> Fragment)? = null

    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        return onGetFragment?.invoke(position)!!
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}