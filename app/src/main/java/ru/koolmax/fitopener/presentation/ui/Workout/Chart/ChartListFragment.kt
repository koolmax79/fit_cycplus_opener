package ru.koolmax.fitopener.presentation.ui.Workout.Chart

import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.data.media.FileRepository

class ChartListFragment : Fragment(R.layout.fragment_chart_list) {
    private val viewModel by lazy { ViewModelProvider(this, ChartListViewModelFactory(requireContext(), FileRepository()))[ChartListViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString("fit")?.let {
            viewModel.GetFitRecords(it)
        }

        viewModel.FitRecords.observe(viewLifecycleOwner) {
            var listView = view.findViewById<ListView>(R.id.list_chart).apply {
                adapter = ChartDataAdapter(view.context, it)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(fit: String) =
            ChartListFragment().apply {
                arguments = Bundle().apply {
                    putString("fit", fit)
                }
            }
    }
}